package br.edu.infnet.ecommerce.pagamento.application;

import br.edu.infnet.ecommerce.pagamento.application.port.AutorizacaoCartao;
import br.edu.infnet.ecommerce.pagamento.application.port.PedidoIntegracao;
import br.edu.infnet.ecommerce.pagamento.application.port.ProcessadorCartaoPort;
import br.edu.infnet.ecommerce.pagamento.domain.PagamentoRepository;
import br.edu.infnet.ecommerce.pagamento.domain.event.PagamentoAprovado;
import br.edu.infnet.ecommerce.pagamento.domain.event.PagamentoRecusado;
import br.edu.infnet.ecommerce.shared.domain.DomainEvent;
import br.edu.infnet.ecommerce.shared.domain.DomainEventPublisher;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.math.BigDecimal;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PagamentoApplicationServiceTest {

    private final PedidoIntegracao pedidoIntegracao = mock(PedidoIntegracao.class);
    private final ProcessadorCartaoPort processadorCartaoPort = mock(ProcessadorCartaoPort.class);
    private final PagamentoRepository pagamentoRepository = mock(PagamentoRepository.class);
    private final DomainEventPublisher domainEventPublisher = mock(DomainEventPublisher.class);

    private final PagamentoApplicationService service = new PagamentoApplicationService(
            pedidoIntegracao, processadorCartaoPort, pagamentoRepository, domainEventPublisher
    );

    @Test
    void devePublicarPagamentoAprovadoQuandoCartaoForAutorizado() {
        when(processadorCartaoPort.autorizar(any(), any()))
                .thenReturn(AutorizacaoCartao.aprovada("AUTH-123"));
        when(pagamentoRepository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        IniciarPagamentoCommand comando = new IniciarPagamentoCommand(
                10L, 1L, new BigDecimal("150.00"), "CARTAO", "4111111111111111"
        );

        PagamentoResultado resultado = service.processar(comando);

        assertThat(resultado.aprovado()).isTrue();

        ArgumentCaptor<Collection<DomainEvent>> eventosCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(domainEventPublisher).publicar(eventosCaptor.capture());

        Collection<DomainEvent> eventosPublicados = eventosCaptor.getValue();
        assertThat(eventosPublicados).hasSize(1);
        assertThat(eventosPublicados.iterator().next()).isInstanceOfSatisfying(PagamentoAprovado.class, evento -> {
            assertThat(evento.pedidoId()).isEqualTo(10L);
            assertThat(evento.usuarioId()).isEqualTo(1L);
            assertThat(evento.codigoAutorizacao()).isEqualTo("AUTH-123");
        });
    }

    @Test
    void devePublicarPagamentoRecusadoQuandoRegraDeNegocioRecusarSemChamarProcessador() {
        IniciarPagamentoCommand comando = new IniciarPagamentoCommand(
                20L, 2L, new BigDecimal("-1.00"), "CARTAO", "4111111111111111"
        );
        when(pagamentoRepository.salvar(any())).thenAnswer(invocation -> invocation.getArgument(0));

        PagamentoResultado resultado = service.processar(comando);

        assertThat(resultado.aprovado()).isFalse();
        assertThat(resultado.motivoRecusa()).isEqualTo("VALOR_INVALIDO");

        verify(processadorCartaoPort, never()).autorizar(any(), any());

        ArgumentCaptor<Collection<DomainEvent>> eventosCaptor = ArgumentCaptor.forClass(Collection.class);
        verify(domainEventPublisher).publicar(eventosCaptor.capture());

        Collection<DomainEvent> eventosPublicados = eventosCaptor.getValue();
        assertThat(eventosPublicados).hasSize(1);
        assertThat(eventosPublicados.iterator().next()).isInstanceOfSatisfying(PagamentoRecusado.class, evento -> {
            assertThat(evento.pedidoId()).isEqualTo(20L);
            assertThat(evento.usuarioId()).isEqualTo(2L);
            assertThat(evento.motivoRecusa()).isEqualTo("VALOR_INVALIDO");
        });
    }
}

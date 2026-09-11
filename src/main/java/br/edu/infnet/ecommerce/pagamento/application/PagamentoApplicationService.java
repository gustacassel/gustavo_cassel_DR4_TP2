package br.edu.infnet.ecommerce.pagamento.application;

import br.edu.infnet.ecommerce.pagamento.application.port.AutorizacaoCartao;
import br.edu.infnet.ecommerce.pagamento.application.port.PedidoIntegracao;
import br.edu.infnet.ecommerce.pagamento.application.port.ProcessadorCartaoPort;
import br.edu.infnet.ecommerce.pagamento.domain.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.FormaPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.NumeroCartao;
import br.edu.infnet.ecommerce.pagamento.domain.Pagamento;
import br.edu.infnet.ecommerce.pagamento.domain.PagamentoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PagamentoApplicationService {

    private final PedidoIntegracao pedidoIntegracao;
    private final ProcessadorCartaoPort processadorCartaoPort;
    private final PagamentoRepository pagamentoRepository;

    public PagamentoApplicationService(
            PedidoIntegracao pedidoIntegracao,
            ProcessadorCartaoPort processadorCartaoPort,
            PagamentoRepository pagamentoRepository
    ) {
        this.pedidoIntegracao = pedidoIntegracao;
        this.processadorCartaoPort = processadorCartaoPort;
        this.pagamentoRepository = pagamentoRepository;
    }

    @Transactional
    public PagamentoResultado processar(IniciarPagamentoCommand comando) {
        pedidoIntegracao.validarPedidoDoUsuario(comando.pedidoId(), comando.usuarioId());

        Dinheiro valor = new Dinheiro(comando.valor());
        FormaPagamento formaPagamento = FormaPagamento.deTexto(comando.formaPagamento());
        NumeroCartao numeroCartao = comando.numeroCartao() != null
                ? new NumeroCartao(comando.numeroCartao())
                : null;

        Pagamento pagamento = Pagamento.avaliar(
                comando.pedidoId(), comando.usuarioId(), valor, formaPagamento, numeroCartao
        );

        if (!pagamento.foiRecusado()) {
            AutorizacaoCartao autorizacao = processadorCartaoPort.autorizar(valor, numeroCartao);

            if (autorizacao.aprovado()) {
                pagamento.aprovar(autorizacao.codigoAutorizacao());
            } else {
                pagamento.recusar(autorizacao.motivoRecusa());
            }
        }

        Pagamento pagamentoSalvo = pagamentoRepository.salvar(pagamento);
        return PagamentoResultado.de(pagamentoSalvo);
    }
}

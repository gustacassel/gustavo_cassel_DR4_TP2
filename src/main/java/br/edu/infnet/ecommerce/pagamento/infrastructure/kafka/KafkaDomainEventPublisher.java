package br.edu.infnet.ecommerce.pagamento.infrastructure.kafka;

import br.edu.infnet.ecommerce.pagamento.domain.event.PagamentoAprovado;
import br.edu.infnet.ecommerce.pagamento.domain.event.PagamentoRecusado;
import br.edu.infnet.ecommerce.shared.domain.DomainEvent;
import br.edu.infnet.ecommerce.shared.domain.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.util.Collection;

/**
 * Publica os eventos de domínio como mensagens em tópicos Kafka. O evento
 * de domínio nunca é enviado "cru" — cada um é convertido para um DTO
 * próprio do tópico, para não acoplar quem consome ao modelo interno.
 */
@Primary
@Component
public class KafkaDomainEventPublisher implements DomainEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${topics.pagamento-aprovado}")
    private String topicoPagamentoAprovado;

    @Value("${topics.pagamento-recusado}")
    private String topicoPagamentoRecusado;

    public KafkaDomainEventPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public void publicar(Collection<DomainEvent> eventos) {
        eventos.forEach(this::enviar);
    }

    private void enviar(DomainEvent evento) {
        if (evento instanceof PagamentoAprovado aprovado) {
            var mensagem = new PagamentoAprovadoKafkaEvent(
                    aprovado.pedidoId(), aprovado.usuarioId(), aprovado.codigoAutorizacao(), aprovado.ocorridoEm()
            );
            enviar(topicoPagamentoAprovado, aprovado.pedidoId(), mensagem);
            return;
        }

        if (evento instanceof PagamentoRecusado recusado) {
            var mensagem = new PagamentoRecusadoKafkaEvent(
                    recusado.pedidoId(), recusado.usuarioId(), recusado.motivoRecusa(), recusado.ocorridoEm()
            );
            enviar(topicoPagamentoRecusado, recusado.pedidoId(), mensagem);
        }
    }

    private void enviar(String topico, Long chave, Object mensagem) {
        // chave = pedidoId: eventos do mesmo pedido caem sempre na mesma partição (ordem garantida).
        kafkaTemplate.send(topico, chave.toString(), objectMapper.writeValueAsString(mensagem));
    }
}

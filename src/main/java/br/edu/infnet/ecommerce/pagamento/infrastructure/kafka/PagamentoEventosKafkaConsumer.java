package br.edu.infnet.ecommerce.pagamento.infrastructure.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

/**
 * Consome as mensagens dos tópicos de Pagamento. Recebe o payload como
 * String bruta e converte manualmente com o ObjectMapper — evita erro de
 * deserialização automática quando o produtor e o consumidor não
 * compartilham a mesma classe/classpath (ex.: se o consumidor estivesse
 * em outro serviço).
 */
@Component
public class PagamentoEventosKafkaConsumer {

    private static final Logger log = LoggerFactory.getLogger(PagamentoEventosKafkaConsumer.class);

    private final ObjectMapper objectMapper;

    public PagamentoEventosKafkaConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${topics.pagamento-aprovado}")
    public void aoReceberPagamentoAprovado(String payload) {
        PagamentoAprovadoKafkaEvent evento = objectMapper.readValue(payload, PagamentoAprovadoKafkaEvent.class);
        log.info(
                "[Kafka] pagamento aprovado consumido: pedido {} (usuário {}, autorização {})",
                evento.pedidoId(), evento.usuarioId(), evento.codigoAutorizacao()
        );
    }

    @KafkaListener(topics = "${topics.pagamento-recusado}")
    public void aoReceberPagamentoRecusado(String payload) {
        PagamentoRecusadoKafkaEvent evento = objectMapper.readValue(payload, PagamentoRecusadoKafkaEvent.class);
        log.info(
                "[Kafka] pagamento recusado consumido: pedido {} (usuário {}, motivo {})",
                evento.pedidoId(), evento.usuarioId(), evento.motivoRecusa()
        );
    }
}

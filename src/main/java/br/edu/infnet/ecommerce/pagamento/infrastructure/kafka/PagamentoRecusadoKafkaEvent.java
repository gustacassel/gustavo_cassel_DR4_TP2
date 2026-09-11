package br.edu.infnet.ecommerce.pagamento.infrastructure.kafka;

import java.time.LocalDateTime;

/** Contrato público do tópico — independente do evento de domínio interno. */
public record PagamentoRecusadoKafkaEvent(
        Long pedidoId,
        Long usuarioId,
        String motivoRecusa,
        LocalDateTime ocorridoEm
) {
}

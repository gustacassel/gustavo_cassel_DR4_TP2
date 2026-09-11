package br.edu.infnet.ecommerce.pagamento.domain.event;

import br.edu.infnet.ecommerce.shared.domain.DomainEvent;

import java.time.LocalDateTime;

public record PagamentoRecusado(
        Long pedidoId,
        Long usuarioId,
        String motivoRecusa,
        LocalDateTime ocorridoEm
) implements DomainEvent {
}

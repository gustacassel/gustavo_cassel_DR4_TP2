package br.edu.infnet.ecommerce.pagamento.domain.event;

import br.edu.infnet.ecommerce.shared.domain.DomainEvent;

import java.time.LocalDateTime;

public record PagamentoAprovado(
        Long pedidoId,
        Long usuarioId,
        String codigoAutorizacao,
        LocalDateTime ocorridoEm
) implements DomainEvent {
}

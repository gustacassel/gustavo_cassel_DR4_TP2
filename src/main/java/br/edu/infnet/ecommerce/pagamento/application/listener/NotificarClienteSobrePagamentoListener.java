package br.edu.infnet.ecommerce.pagamento.application.listener;

import br.edu.infnet.ecommerce.pagamento.domain.event.PagamentoAprovado;
import br.edu.infnet.ecommerce.pagamento.domain.event.PagamentoRecusado;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Reage a eventos de Pagamento sem que o serviço de pagamento precise
 * conhecer esta classe — demonstra o desacoplamento que o Domain Event traz.
 */
@Component
public class NotificarClienteSobrePagamentoListener {

    private static final Logger log = LoggerFactory.getLogger(NotificarClienteSobrePagamentoListener.class);

    @EventListener
    public void aoAprovar(PagamentoAprovado evento) {
        log.info(
                "Notificando usuário {}: pagamento do pedido {} aprovado (autorização {})",
                evento.usuarioId(), evento.pedidoId(), evento.codigoAutorizacao()
        );
    }

    @EventListener
    public void aoRecusar(PagamentoRecusado evento) {
        log.info(
                "Notificando usuário {}: pagamento do pedido {} recusado ({})",
                evento.usuarioId(), evento.pedidoId(), evento.motivoRecusa()
        );
    }
}

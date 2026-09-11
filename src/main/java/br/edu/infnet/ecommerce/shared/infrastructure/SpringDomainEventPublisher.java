package br.edu.infnet.ecommerce.shared.infrastructure;

import br.edu.infnet.ecommerce.shared.domain.DomainEvent;
import br.edu.infnet.ecommerce.shared.domain.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * Publica eventos de domínio dentro da própria JVM usando o mecanismo nativo
 * do Spring. Só funciona localmente (monólito); em um cenário distribuído
 * essa implementação seria trocada por uma baseada em fila/tópico (ex.: Kafka),
 * sem alterar o domínio nem os Application Services.
 */
@Component
public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @Override
    public void publicar(Collection<DomainEvent> eventos) {
        eventos.forEach(applicationEventPublisher::publishEvent);
    }
}

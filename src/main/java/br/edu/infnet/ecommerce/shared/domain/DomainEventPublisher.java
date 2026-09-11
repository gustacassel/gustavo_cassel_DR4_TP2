package br.edu.infnet.ecommerce.shared.domain;

import java.util.Collection;

/**
 * Porta de publicação de eventos de domínio. O domínio depende só desta
 * abstração — a tecnologia de transporte (Spring events, Kafka, etc.) é um
 * detalhe de infraestrutura plugável por trás dela.
 */
public interface DomainEventPublisher {

    void publicar(Collection<DomainEvent> eventos);
}

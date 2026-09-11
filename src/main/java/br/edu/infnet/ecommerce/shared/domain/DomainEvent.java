package br.edu.infnet.ecommerce.shared.domain;

import java.time.LocalDateTime;

/** Um fato de negócio já ocorrido e imutável. */
public interface DomainEvent {

    LocalDateTime ocorridoEm();
}

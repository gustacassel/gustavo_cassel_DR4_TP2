package br.edu.infnet.ecommerce.shared.domain;

import java.util.ArrayList;
import java.util.List;

/** Base para todo Aggregate Root que precisa registrar eventos de domínio. */
public abstract class AggregateRoot {

    private final List<DomainEvent> eventos = new ArrayList<>();

    protected void registrarEvento(DomainEvent evento) {
        eventos.add(evento);
    }

    /** Cópia defensiva: sobrevive a uma chamada seguinte de {@link #limparEventos()}. */
    public List<DomainEvent> obterEventos() {
        return List.copyOf(eventos);
    }

    public void limparEventos() {
        eventos.clear();
    }
}

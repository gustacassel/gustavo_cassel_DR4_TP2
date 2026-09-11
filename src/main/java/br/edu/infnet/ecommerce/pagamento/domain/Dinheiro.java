package br.edu.infnet.ecommerce.pagamento.domain;

import java.math.BigDecimal;
import java.util.Objects;

public record Dinheiro(BigDecimal valor) {

    public static final BigDecimal LIMITE_MAXIMO = new BigDecimal("10000.00");

    public Dinheiro {
        Objects.requireNonNull(valor, "valor é obrigatório");
    }

    public boolean isPositivo() {
        return valor.compareTo(BigDecimal.ZERO) > 0;
    }

    public boolean excedeLimite() {
        return valor.compareTo(LIMITE_MAXIMO) > 0;
    }
}

package br.edu.infnet.ecommerce.pagamento.domain;

public enum FormaPagamento {
    CARTAO,
    BOLETO,
    PIX;

    public boolean suportada() {
        return this == CARTAO;
    }

    public static FormaPagamento deTexto(String texto) {
        if (texto == null) {
            return null;
        }

        try {
            return FormaPagamento.valueOf(texto.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            return null;
        }
    }
}

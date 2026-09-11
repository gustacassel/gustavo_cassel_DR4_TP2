package br.edu.infnet.ecommerce.pagamento.domain;

public record NumeroCartao(String numero) {

    public boolean isValido() {
        return numero != null && numero.length() >= 4;
    }

    public boolean estaBloqueado() {
        return isValido() && numero.endsWith("0000");
    }

    public String mascarado() {
        if (!isValido()) {
            return "****";
        }

        return "**** **** **** " + numero.substring(numero.length() - 4);
    }
}

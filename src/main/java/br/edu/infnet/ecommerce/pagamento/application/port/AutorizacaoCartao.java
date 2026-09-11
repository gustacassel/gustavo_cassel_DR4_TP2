package br.edu.infnet.ecommerce.pagamento.application.port;

public record AutorizacaoCartao(boolean aprovado, String codigoAutorizacao, String motivoRecusa) {

    public static AutorizacaoCartao aprovada(String codigoAutorizacao) {
        return new AutorizacaoCartao(true, codigoAutorizacao, null);
    }

    public static AutorizacaoCartao recusada(String motivoRecusa) {
        return new AutorizacaoCartao(false, null, motivoRecusa);
    }
}

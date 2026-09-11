package br.edu.infnet.ecommerce.pagamento.application;

import br.edu.infnet.ecommerce.pagamento.domain.Pagamento;

public record PagamentoResultado(
        Long pagamentoId,
        boolean aprovado,
        String motivoRecusa,
        String codigoAutorizacao
) {
    public static PagamentoResultado de(Pagamento pagamento) {
        return new PagamentoResultado(
                pagamento.getId(),
                pagamento.foiAprovado(),
                pagamento.getMotivoRecusa(),
                pagamento.getCodigoAutorizacao()
        );
    }
}

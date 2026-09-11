package br.edu.infnet.ecommerce.pagamento.infrastructure.persistence;

import br.edu.infnet.ecommerce.pagamento.domain.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.FormaPagamento;
import br.edu.infnet.ecommerce.pagamento.domain.Pagamento;
import br.edu.infnet.ecommerce.pagamento.domain.StatusPagamento;

final class PagamentoMapper {

    private PagamentoMapper() {
    }

    static PagamentoJpaEntity paraEntidade(Pagamento pagamento) {
        return new PagamentoJpaEntity(
                pagamento.getId(),
                pagamento.getPedidoId(),
                pagamento.getUsuarioId(),
                pagamento.getValor().valor(),
                pagamento.getFormaPagamento() != null ? pagamento.getFormaPagamento().name() : null,
                pagamento.getNumeroCartaoMascarado(),
                pagamento.getStatus() != null ? pagamento.getStatus().name() : null,
                pagamento.getMotivoRecusa(),
                pagamento.getCodigoAutorizacao(),
                pagamento.getProcessadoEm()
        );
    }

    static Pagamento paraDominio(PagamentoJpaEntity entidade) {
        return Pagamento.reidratar(
                entidade.getId(),
                entidade.getPedidoId(),
                entidade.getUsuarioId(),
                new Dinheiro(entidade.getValor()),
                entidade.getFormaPagamento() != null ? FormaPagamento.valueOf(entidade.getFormaPagamento()) : null,
                entidade.getNumeroCartaoMascarado(),
                entidade.getStatus() != null ? StatusPagamento.valueOf(entidade.getStatus()) : null,
                entidade.getMotivoRecusa(),
                entidade.getCodigoAutorizacao(),
                entidade.getProcessadoEm()
        );
    }
}

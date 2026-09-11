package br.edu.infnet.ecommerce.pagamento.application.port;

import br.edu.infnet.ecommerce.pagamento.domain.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.NumeroCartao;

/** Abstração para o processador/adquirente de cartão externo. */
public interface ProcessadorCartaoPort {

    AutorizacaoCartao autorizar(Dinheiro valor, NumeroCartao numeroCartao);
}

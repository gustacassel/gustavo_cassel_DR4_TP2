package br.edu.infnet.ecommerce.pagamento.infrastructure.processador;

import br.edu.infnet.ecommerce.pagamento.application.port.AutorizacaoCartao;
import br.edu.infnet.ecommerce.pagamento.application.port.ProcessadorCartaoPort;
import br.edu.infnet.ecommerce.pagamento.domain.Dinheiro;
import br.edu.infnet.ecommerce.pagamento.domain.NumeroCartao;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProcessadorCartaoAdapter implements ProcessadorCartaoPort {

    @Override
    public AutorizacaoCartao autorizar(Dinheiro valor, NumeroCartao numeroCartao) {
        String codigoAutorizacao = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return AutorizacaoCartao.aprovada(codigoAutorizacao);
    }
}

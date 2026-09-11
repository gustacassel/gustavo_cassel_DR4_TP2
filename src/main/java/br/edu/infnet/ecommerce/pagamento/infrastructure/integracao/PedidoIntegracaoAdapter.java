package br.edu.infnet.ecommerce.pagamento.infrastructure.integracao;

import br.edu.infnet.ecommerce.entity.Pedido;
import br.edu.infnet.ecommerce.pagamento.application.port.PedidoIntegracao;
import br.edu.infnet.ecommerce.pagamento.application.port.PedidoInvalidoException;
import br.edu.infnet.ecommerce.repository.PedidoRepository;
import org.springframework.stereotype.Component;

/** Único ponto do contexto de Pagamento autorizado a acessar o Pedido do monólito legado. */
@Component
public class PedidoIntegracaoAdapter implements PedidoIntegracao {

    private final PedidoRepository pedidoRepository;

    public PedidoIntegracaoAdapter(PedidoRepository pedidoRepository) {
        this.pedidoRepository = pedidoRepository;
    }

    @Override
    public void validarPedidoDoUsuario(Long pedidoId, Long usuarioId) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new PedidoInvalidoException("Pedido não encontrado: " + pedidoId));

        if (!pedido.getUsuario().getId().equals(usuarioId)) {
            throw new PedidoInvalidoException("Pedido não pertence ao usuário informado");
        }
    }
}

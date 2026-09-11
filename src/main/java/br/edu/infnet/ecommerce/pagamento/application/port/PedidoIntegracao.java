package br.edu.infnet.ecommerce.pagamento.application.port;

/** Porta de integração com o contexto de Pedido — só identificadores, nunca as entidades. */
public interface PedidoIntegracao {

    void validarPedidoDoUsuario(Long pedidoId, Long usuarioId);
}

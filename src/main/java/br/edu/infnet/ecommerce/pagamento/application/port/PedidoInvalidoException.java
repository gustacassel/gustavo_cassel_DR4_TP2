package br.edu.infnet.ecommerce.pagamento.application.port;

import br.edu.infnet.ecommerce.exception.RecursoNaoEncontradoException;

public class PedidoInvalidoException extends RecursoNaoEncontradoException {

    public PedidoInvalidoException(String message) {
        super(message);
    }
}

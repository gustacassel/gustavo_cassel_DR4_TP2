package br.edu.infnet.ecommerce.pagamento.domain;

import java.time.LocalDateTime;
import java.util.Objects;

/** Aggregate Root do contexto de Pagamento. */
public class Pagamento {

    private final Long id;
    private final Long pedidoId;
    private final Long usuarioId;
    private final Dinheiro valor;
    private final FormaPagamento formaPagamento;
    private final String numeroCartaoMascarado;
    private StatusPagamento status;
    private String motivoRecusa;
    private String codigoAutorizacao;
    private LocalDateTime processadoEm;

    private Pagamento(
            Long id,
            Long pedidoId,
            Long usuarioId,
            Dinheiro valor,
            FormaPagamento formaPagamento,
            String numeroCartaoMascarado,
            StatusPagamento status,
            String motivoRecusa,
            String codigoAutorizacao,
            LocalDateTime processadoEm
    ) {
        this.id = id;
        this.pedidoId = Objects.requireNonNull(pedidoId, "pedidoId é obrigatório");
        this.usuarioId = Objects.requireNonNull(usuarioId, "usuarioId é obrigatório");
        this.valor = Objects.requireNonNull(valor, "valor é obrigatório");
        this.formaPagamento = formaPagamento;
        this.numeroCartaoMascarado = numeroCartaoMascarado;
        this.status = status;
        this.motivoRecusa = motivoRecusa;
        this.codigoAutorizacao = codigoAutorizacao;
        this.processadoEm = processadoEm;
    }

    /** Uma recusa é um resultado de negócio válido, não uma exceção. */
    public static Pagamento avaliar(
            Long pedidoId,
            Long usuarioId,
            Dinheiro valor,
            FormaPagamento formaPagamento,
            NumeroCartao numeroCartao
    ) {
        String numeroCartaoMascarado = numeroCartao != null ? numeroCartao.mascarado() : null;
        String motivoRecusa = regraDeRecusa(valor, formaPagamento, numeroCartao);

        if (motivoRecusa != null) {
            return new Pagamento(
                    null, pedidoId, usuarioId, valor, formaPagamento, numeroCartaoMascarado,
                    StatusPagamento.RECUSADO, motivoRecusa, null, LocalDateTime.now()
            );
        }

        return new Pagamento(
                null, pedidoId, usuarioId, valor, formaPagamento, numeroCartaoMascarado,
                null, null, null, null
        );
    }

    public static Pagamento reidratar(
            Long id,
            Long pedidoId,
            Long usuarioId,
            Dinheiro valor,
            FormaPagamento formaPagamento,
            String numeroCartaoMascarado,
            StatusPagamento status,
            String motivoRecusa,
            String codigoAutorizacao,
            LocalDateTime processadoEm
    ) {
        return new Pagamento(
                id, pedidoId, usuarioId, valor, formaPagamento, numeroCartaoMascarado,
                status, motivoRecusa, codigoAutorizacao, processadoEm
        );
    }

    private static String regraDeRecusa(
            Dinheiro valor,
            FormaPagamento formaPagamento,
            NumeroCartao numeroCartao
    ) {
        if (!valor.isPositivo()) {
            return "VALOR_INVALIDO";
        }
        if (formaPagamento == null || !formaPagamento.suportada()) {
            return "FORMA_PAGAMENTO_NAO_SUPORTADA";
        }
        if (numeroCartao == null || !numeroCartao.isValido()) {
            return "CARTAO_INVALIDO";
        }
        if (valor.excedeLimite()) {
            return "LIMITE_EXCEDIDO";
        }
        if (numeroCartao.estaBloqueado()) {
            return "CARTAO_BLOQUEADO";
        }
        return null;
    }

    public boolean foiRecusado() {
        return status == StatusPagamento.RECUSADO;
    }

    public boolean foiAprovado() {
        return status == StatusPagamento.APROVADO;
    }

    public void aprovar(String codigoAutorizacao) {
        if (status != null) {
            throw new IllegalStateException("Pagamento já foi processado");
        }

        this.status = StatusPagamento.APROVADO;
        this.codigoAutorizacao = Objects.requireNonNull(codigoAutorizacao, "codigoAutorizacao é obrigatório");
        this.processadoEm = LocalDateTime.now();
    }

    public void recusar(String motivo) {
        if (status != null) {
            throw new IllegalStateException("Pagamento já foi processado");
        }

        this.status = StatusPagamento.RECUSADO;
        this.motivoRecusa = motivo;
        this.processadoEm = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public Dinheiro getValor() {
        return valor;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public String getNumeroCartaoMascarado() {
        return numeroCartaoMascarado;
    }

    public StatusPagamento getStatus() {
        return status;
    }

    public String getMotivoRecusa() {
        return motivoRecusa;
    }

    public String getCodigoAutorizacao() {
        return codigoAutorizacao;
    }

    public LocalDateTime getProcessadoEm() {
        return processadoEm;
    }
}

package br.edu.infnet.ecommerce.pagamento.infrastructure.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamentos")
public class PagamentoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long pedidoId;

    @Column(nullable = false)
    private Long usuarioId;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    private String formaPagamento;

    private String numeroCartaoMascarado;

    @Column(nullable = false)
    private String status;

    private String motivoRecusa;

    private String codigoAutorizacao;

    @Column(nullable = false)
    private LocalDateTime processadoEm;

    protected PagamentoJpaEntity() {
    }

    public PagamentoJpaEntity(
            Long id,
            Long pedidoId,
            Long usuarioId,
            BigDecimal valor,
            String formaPagamento,
            String numeroCartaoMascarado,
            String status,
            String motivoRecusa,
            String codigoAutorizacao,
            LocalDateTime processadoEm
    ) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.usuarioId = usuarioId;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.numeroCartaoMascarado = numeroCartaoMascarado;
        this.status = status;
        this.motivoRecusa = motivoRecusa;
        this.codigoAutorizacao = codigoAutorizacao;
        this.processadoEm = processadoEm;
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

    public BigDecimal getValor() {
        return valor;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public String getNumeroCartaoMascarado() {
        return numeroCartaoMascarado;
    }

    public String getStatus() {
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

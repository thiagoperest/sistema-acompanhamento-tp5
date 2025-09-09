package br.edu.infnet.model.entity;

import br.edu.infnet.model.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidade Pedido - Representa um pedido realizado pelo cliente
 */
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Número do pedido é obrigatório")
    @Size(max = 50, message = "Número do pedido deve ter no máximo 50 caracteres")
    @Column(name = "numero_pedido", nullable = false, unique = true, length = 50)
    private String numeroPedido;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Data da compra é obrigatória")
    @Column(name = "data_compra", nullable = false)
    private LocalDateTime dataCompra;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @Column(name = "previsao_entrega")
    private LocalDate previsaoEntrega;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    @Column(length = 500)
    private String observacoes;

    // Relacionamento com HistoricoStatus
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HistoricoStatus> historicoStatus = new ArrayList<>();

    // Relacionamento 1:N com Notificacao
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Notificacao> notificacoes = new ArrayList<>();

    // Relacionamento 1:1 com DadosRastreamento
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private DadosRastreamento dadosRastreamento;

    public Pedido() {
        this.dataCompra = LocalDateTime.now();
        this.status = StatusPedido.CONFIRMADO;
    }

    public Pedido(String numeroPedido, Cliente cliente, BigDecimal valor) {
        this();
        this.numeroPedido = numeroPedido;
        this.cliente = cliente;
        this.valor = valor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDate getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(LocalDate previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<HistoricoStatus> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<HistoricoStatus> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }

    public List<Notificacao> getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(List<Notificacao> notificacoes) {
        this.notificacoes = notificacoes;
    }

    public DadosRastreamento getDadosRastreamento() {
        return dadosRastreamento;
    }

    public void setDadosRastreamento(DadosRastreamento dadosRastreamento) {
        this.dadosRastreamento = dadosRastreamento;
    }

    /**
     * Adiciona um novo histórico de status
     */
    public void adicionarHistorico(StatusPedido novoStatus, String observacao, String responsavel) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setPedido(this);
        historico.setStatus(novoStatus);
        historico.setObservacao(observacao);
        historico.setResponsavel(responsavel);

        this.historicoStatus.add(historico);
        this.setStatus(novoStatus);
    }

    /**
     * Verifica se o pedido pode ser cancelado
     */
    public boolean podeSerCancelado() {
        return this.status == StatusPedido.CONFIRMADO ||
                this.status == StatusPedido.PROCESSANDO;
    }

    /**
     * Verifica se o pedido está entregue
     */
    public boolean isEntregue() {
        return this.status == StatusPedido.ENTREGUE;
    }

    /**
     * Calcula dias desde a compra
     */
    public long diasDesdeCompra() {
        return java.time.temporal.ChronoUnit.DAYS.between(
                this.dataCompra.toLocalDate(),
                LocalDate.now()
        );
    }

    @PrePersist
    protected void onCreate() {
        if (dataCompra == null) {
            dataCompra = LocalDateTime.now();
        }
        if (status == null) {
            status = StatusPedido.CONFIRMADO;
        }
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", numeroPedido='" + numeroPedido + '\'' +
                ", clienteId=" + (cliente != null ? cliente.getId() : null) +
                ", dataCompra=" + dataCompra +
                ", valor=" + valor +
                ", status=" + status +
                ", previsaoEntrega=" + previsaoEntrega +
                '}';
    }
}
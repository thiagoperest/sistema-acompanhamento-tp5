package br.edu.infnet.model.entity;

import br.edu.infnet.model.enums.StatusPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Entidade HistoricoStatus - Registra o histórico de mudanças de status de um pedido
 */
@Entity
@Table(name = "historico_status")
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pedido é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusPedido status;

    @NotNull(message = "Data de atualização é obrigatória")
    @Column(name = "data_atualizacao", nullable = false)
    private LocalDateTime dataAtualizacao;

    @Size(max = 255, message = "Observação deve ter no máximo 255 caracteres")
    @Column(length = 255)
    private String observacao;

    @Size(max = 100, message = "Responsável deve ter no máximo 100 caracteres")
    @Column(length = 100)
    private String responsavel;

    public HistoricoStatus() {
        this.dataAtualizacao = LocalDateTime.now();
    }

    public HistoricoStatus(Pedido pedido, StatusPedido status, String observacao, String responsavel) {
        this();
        this.pedido = pedido;
        this.status = status;
        this.observacao = observacao;
        this.responsavel = responsavel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    @PrePersist
    protected void onCreate() {
        if (dataAtualizacao == null) {
            dataAtualizacao = LocalDateTime.now();
        }
    }

    @Override
    public String toString() {
        return "HistoricoStatus{" +
                "id=" + id +
                ", pedidoId=" + (pedido != null ? pedido.getId() : null) +
                ", status=" + status +
                ", dataAtualizacao=" + dataAtualizacao +
                ", observacao='" + observacao + '\'' +
                ", responsavel='" + responsavel + '\'' +
                '}';
    }
}
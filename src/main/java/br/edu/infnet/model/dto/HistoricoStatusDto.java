package br.edu.infnet.model.dto;

import br.edu.infnet.model.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de HistoricoStatus
 */
public class HistoricoStatusDto {

    private Long id;

    private Long pedidoId;

    @NotNull(message = "Status é obrigatório")
    private StatusPedido status;

    private String statusDescricao;

    private String dataAtualizacao;

    @Size(max = 255, message = "Observação deve ter no máximo 255 caracteres")
    private String observacao;

    @Size(max = 100, message = "Responsável deve ter no máximo 100 caracteres")
    private String responsavel;

    public HistoricoStatusDto() {
    }

    public HistoricoStatusDto(Long id, Long pedidoId, StatusPedido status, String dataAtualizacao,
                              String observacao, String responsavel) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.status = status;
        this.statusDescricao = status != null ? status.getDescricao() : null;
        this.dataAtualizacao = dataAtualizacao;
        this.observacao = observacao;
        this.responsavel = responsavel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public StatusPedido getStatus() {
        return status;
    }

    public void setStatus(StatusPedido status) {
        this.status = status;
        this.statusDescricao = status != null ? status.getDescricao() : null;
    }

    public String getStatusDescricao() {
        return statusDescricao;
    }

    public void setStatusDescricao(String statusDescricao) {
        this.statusDescricao = statusDescricao;
    }

    public String getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(String dataAtualizacao) {
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

    @Override
    public String toString() {
        return "HistoricoStatusDto{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", status=" + status +
                ", dataAtualizacao='" + dataAtualizacao + '\'' +
                ", observacao='" + observacao + '\'' +
                ", responsavel='" + responsavel + '\'' +
                '}';
    }
}
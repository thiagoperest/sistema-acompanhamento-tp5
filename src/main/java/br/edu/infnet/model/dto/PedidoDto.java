package br.edu.infnet.model.dto;

import br.edu.infnet.model.enums.StatusPedido;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO para transferência de dados de Pedido
 */
public class PedidoDto {

    private Long id;

    @NotBlank(message = "Número do pedido é obrigatório")
    @Size(max = 50, message = "Número do pedido deve ter no máximo 50 caracteres")
    private String numeroPedido;

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;

    private String nomeCliente;
    private String emailCliente;

    private String dataCompra;

    @NotNull(message = "Valor é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Valor deve ser maior que zero")
    private BigDecimal valor;

    private StatusPedido status;
    private String statusDescricao;

    private String previsaoEntrega;

    @Size(max = 500, message = "Observações devem ter no máximo 500 caracteres")
    private String observacoes;

    // Lista do histórico para exibição
    private List<HistoricoStatusDto> historicoStatus;

    // Campos calculados
    private Long diasDesdeCompra;
    private Boolean podeSerCancelado;
    private Boolean isEntregue;

    public PedidoDto() {
    }

    public PedidoDto(Long id, String numeroPedido, Long clienteId, String nomeCliente,
                     String emailCliente, String dataCompra, BigDecimal valor, StatusPedido status) {
        this.id = id;
        this.numeroPedido = numeroPedido;
        this.clienteId = clienteId;
        this.nomeCliente = nomeCliente;
        this.emailCliente = emailCliente;
        this.dataCompra = dataCompra;
        this.valor = valor;
        this.status = status;
        this.statusDescricao = status != null ? status.getDescricao() : null;
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public void setEmailCliente(String emailCliente) {
        this.emailCliente = emailCliente;
    }

    public String getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(String dataCompra) {
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
        this.statusDescricao = status != null ? status.getDescricao() : null;
    }

    public String getStatusDescricao() {
        return statusDescricao;
    }

    public void setStatusDescricao(String statusDescricao) {
        this.statusDescricao = statusDescricao;
    }

    public String getPrevisaoEntrega() {
        return previsaoEntrega;
    }

    public void setPrevisaoEntrega(String previsaoEntrega) {
        this.previsaoEntrega = previsaoEntrega;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public List<HistoricoStatusDto> getHistoricoStatus() {
        return historicoStatus;
    }

    public void setHistoricoStatus(List<HistoricoStatusDto> historicoStatus) {
        this.historicoStatus = historicoStatus;
    }

    public Long getDiasDesdeCompra() {
        return diasDesdeCompra;
    }

    public void setDiasDesdeCompra(Long diasDesdeCompra) {
        this.diasDesdeCompra = diasDesdeCompra;
    }

    public Boolean getPodeSerCancelado() {
        return podeSerCancelado;
    }

    public void setPodeSerCancelado(Boolean podeSerCancelado) {
        this.podeSerCancelado = podeSerCancelado;
    }

    public Boolean getIsEntregue() {
        return isEntregue;
    }

    public void setIsEntregue(Boolean isEntregue) {
        this.isEntregue = isEntregue;
    }

    @Override
    public String toString() {
        return "PedidoDto{" +
                "id=" + id +
                ", numeroPedido='" + numeroPedido + '\'' +
                ", clienteId=" + clienteId +
                ", nomeCliente='" + nomeCliente + '\'' +
                ", dataCompra='" + dataCompra + '\'' +
                ", valor=" + valor +
                ", status=" + status +
                ", previsaoEntrega='" + previsaoEntrega + '\'' +
                '}';
    }
}
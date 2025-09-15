package br.edu.infnet.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de DadosRastreamento
 */
public class DadosRastreamentoDto {

    private Long id;

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    @NotBlank(message = "Código de rastreamento é obrigatório")
    @Size(max = 100, message = "Código de rastreamento deve ter no máximo 100 caracteres")
    private String codigoRastreamento;

    @NotBlank(message = "Transportadora é obrigatória")
    @Size(max = 100, message = "Nome da transportadora deve ter no máximo 100 caracteres")
    private String transportadora;

    private String ultimaAtualizacao;

    @Size(max = 255, message = "Localização atual deve ter no máximo 255 caracteres")
    private String localizacaoAtual;

    // Campos adicionais para exibição
    private String numeroPedido;
    private String nomeCliente;
    private String statusPedido;
    private Boolean atualizadoRecentemente;

    public DadosRastreamentoDto() {
    }

    public DadosRastreamentoDto(Long pedidoId, String codigoRastreamento, String transportadora) {
        this.pedidoId = pedidoId;
        this.codigoRastreamento = codigoRastreamento;
        this.transportadora = transportadora;
    }

    public DadosRastreamentoDto(Long id, Long pedidoId, String codigoRastreamento, String transportadora,
                                String ultimaAtualizacao, String localizacaoAtual) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.codigoRastreamento = codigoRastreamento;
        this.transportadora = transportadora;
        this.ultimaAtualizacao = ultimaAtualizacao;
        this.localizacaoAtual = localizacaoAtual;
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

    public String getCodigoRastreamento() {
        return codigoRastreamento;
    }

    public void setCodigoRastreamento(String codigoRastreamento) {
        this.codigoRastreamento = codigoRastreamento;
    }

    public String getTransportadora() {
        return transportadora;
    }

    public void setTransportadora(String transportadora) {
        this.transportadora = transportadora;
    }

    public String getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(String ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getLocalizacaoAtual() {
        return localizacaoAtual;
    }

    public void setLocalizacaoAtual(String localizacaoAtual) {
        this.localizacaoAtual = localizacaoAtual;
    }

    public String getNumeroPedido() {
        return numeroPedido;
    }

    public void setNumeroPedido(String numeroPedido) {
        this.numeroPedido = numeroPedido;
    }

    public String getNomeCliente() {
        return nomeCliente;
    }

    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    public String getStatusPedido() {
        return statusPedido;
    }

    public void setStatusPedido(String statusPedido) {
        this.statusPedido = statusPedido;
    }

    public Boolean getAtualizadoRecentemente() {
        return atualizadoRecentemente;
    }

    public void setAtualizadoRecentemente(Boolean atualizadoRecentemente) {
        this.atualizadoRecentemente = atualizadoRecentemente;
    }

    @Override
    public String toString() {
        return "DadosRastreamentoDto{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", codigoRastreamento='" + codigoRastreamento + '\'' +
                ", transportadora='" + transportadora + '\'' +
                ", ultimaAtualizacao='" + ultimaAtualizacao + '\'' +
                ", localizacaoAtual='" + localizacaoAtual + '\'' +
                '}';
    }
}
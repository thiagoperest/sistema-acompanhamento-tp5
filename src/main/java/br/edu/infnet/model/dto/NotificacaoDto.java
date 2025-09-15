package br.edu.infnet.model.dto;

import br.edu.infnet.model.enums.TipoNotificacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO para transferência de dados de Notificacao
 */
public class NotificacaoDto {

    private Long id;

    @NotNull(message = "ID do pedido é obrigatório")
    private Long pedidoId;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "Tipo de notificação é obrigatório")
    private TipoNotificacao tipo;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    private String titulo;

    @NotBlank(message = "Mensagem é obrigatória")
    @Size(max = 500, message = "Mensagem deve ter no máximo 500 caracteres")
    private String mensagem;

    private String dataEnvio;
    private Boolean lida;
    
    // Campos adicionais para exibição
    private String numeroPedido;
    private String nomeCliente;
    private String tipoDescricao;

    public NotificacaoDto() {
    }

    public NotificacaoDto(Long pedidoId, Long clienteId, TipoNotificacao tipo, String titulo, String mensagem) {
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
    }

    public NotificacaoDto(Long id, Long pedidoId, Long clienteId, TipoNotificacao tipo,
                         String titulo, String mensagem, String dataEnvio, Boolean lida) {
        this.id = id;
        this.pedidoId = pedidoId;
        this.clienteId = clienteId;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.dataEnvio = dataEnvio;
        this.lida = lida;
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

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public TipoNotificacao getTipo() {
        return tipo;
    }

    public void setTipo(TipoNotificacao tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
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

    public String getTipoDescricao() {
        return tipoDescricao;
    }

    public void setTipoDescricao(String tipoDescricao) {
        this.tipoDescricao = tipoDescricao;
    }

    @Override
    public String toString() {
        return "NotificacaoDto{" +
                "id=" + id +
                ", pedidoId=" + pedidoId +
                ", clienteId=" + clienteId +
                ", tipo=" + tipo +
                ", titulo='" + titulo + '\'' +
                ", dataEnvio='" + dataEnvio + '\'' +
                ", lida=" + lida +
                '}';
    }
}
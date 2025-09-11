package br.edu.infnet.model.dto;

import br.edu.infnet.model.entity.Avaliacao;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de Avaliação
 */
public class AvaliacaoDto {

    private Long id;

    @NotNull(message = "Pedido é obrigatório")
    private Long pedidoId;
    private String numeroPedido;

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    private String nomeCliente;

    @NotNull(message = "Nota de acompanhamento é obrigatória")
    @Min(value = 1, message = "Nota deve ser no mínimo 1")
    @Max(value = 5, message = "Nota deve ser no máximo 5")
    private Integer notaAcompanhamento;

    @NotNull(message = "Nota de entrega é obrigatória")
    @Min(value = 1, message = "Nota deve ser no mínimo 1")
    @Max(value = 5, message = "Nota deve ser no máximo 5")
    private Integer notaEntrega;

    @Size(max = 1000, message = "Comentário deve ter no máximo 1000 caracteres")
    private String comentario;

    private LocalDateTime dataAvaliacao;
    private Double mediaNotas;

    public AvaliacaoDto() {
    }

    public AvaliacaoDto(Avaliacao avaliacao) {
        if (avaliacao != null) {
            this.id = avaliacao.getId();
            this.notaAcompanhamento = avaliacao.getNotaAcompanhamento();
            this.notaEntrega = avaliacao.getNotaEntrega();
            this.comentario = avaliacao.getComentario();
            this.dataAvaliacao = avaliacao.getDataAvaliacao();
            this.mediaNotas = avaliacao.getMediaNotas();

            if (avaliacao.getPedido() != null) {
                this.pedidoId = avaliacao.getPedido().getId();
                this.numeroPedido = avaliacao.getPedido().getNumeroPedido();
            }

            if (avaliacao.getCliente() != null) {
                this.clienteId = avaliacao.getCliente().getId();
                this.nomeCliente = avaliacao.getCliente().getNome();
            }
        }
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

    public Integer getNotaAcompanhamento() {
        return notaAcompanhamento;
    }

    public void setNotaAcompanhamento(Integer notaAcompanhamento) {
        this.notaAcompanhamento = notaAcompanhamento;
    }

    public Integer getNotaEntrega() {
        return notaEntrega;
    }

    public void setNotaEntrega(Integer notaEntrega) {
        this.notaEntrega = notaEntrega;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public LocalDateTime getDataAvaliacao() {
        return dataAvaliacao;
    }

    public void setDataAvaliacao(LocalDateTime dataAvaliacao) {
        this.dataAvaliacao = dataAvaliacao;
    }

    public Double getMediaNotas() {
        if (notaAcompanhamento != null && notaEntrega != null) {
            return (notaAcompanhamento + notaEntrega) / 2.0;
        }
        return mediaNotas;
    }

    public void setMediaNotas(Double mediaNotas) {
        this.mediaNotas = mediaNotas;
    }
}
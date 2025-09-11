package br.edu.infnet.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma avaliação de pedido no sistema
 */
@Entity
@Table(name = "avaliacao")
public class Avaliacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pedido é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @NotNull(message = "Nota de acompanhamento é obrigatória")
    @Min(value = 1, message = "Nota de acompanhamento deve ser no mínimo 1")
    @Max(value = 5, message = "Nota de acompanhamento deve ser no máximo 5")
    @Column(name = "nota_acompanhamento", nullable = false)
    private Integer notaAcompanhamento;

    @NotNull(message = "Nota de entrega é obrigatória")
    @Min(value = 1, message = "Nota de entrega deve ser no mínimo 1")
    @Max(value = 5, message = "Nota de entrega deve ser no máximo 5")
    @Column(name = "nota_entrega", nullable = false)
    private Integer notaEntrega;

    @Size(max = 1000, message = "Comentário deve ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String comentario;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao = LocalDateTime.now();

    public Avaliacao() {
    }

    public Avaliacao(Pedido pedido, Cliente cliente, Integer notaAcompanhamento, Integer notaEntrega) {
        this.pedido = pedido;
        this.cliente = cliente;
        this.notaAcompanhamento = notaAcompanhamento;
        this.notaEntrega = notaEntrega;
    }

    public Double getMediaNotas() {
        return (notaAcompanhamento + notaEntrega) / 2.0;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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
}
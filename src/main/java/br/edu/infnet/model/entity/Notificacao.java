package br.edu.infnet.model.entity;

import br.edu.infnet.model.enums.TipoNotificacao;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Entidade Notificacao - Representa notificações enviadas aos clientes
 */
@Entity
@Table(name = "notificacoes")
public class Notificacao {

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

    @NotNull(message = "Tipo é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoNotificacao tipo;

    @NotBlank(message = "Título é obrigatório")
    @Size(max = 100, message = "Título deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String titulo;

    @NotBlank(message = "Mensagem é obrigatória")
    @Size(max = 500, message = "Mensagem deve ter no máximo 500 caracteres")
    @Column(nullable = false, length = 500)
    private String mensagem;

    @NotNull(message = "Data de envio é obrigatória")
    @Column(name = "data_envio", nullable = false)
    private LocalDateTime dataEnvio;

    @NotNull(message = "Status de leitura é obrigatório")
    @Column(nullable = false)
    private Boolean lida = false;

    public Notificacao() {
        this.dataEnvio = LocalDateTime.now();
        this.lida = false;
    }

    public Notificacao(Pedido pedido, Cliente cliente, TipoNotificacao tipo, String titulo, String mensagem) {
        this();
        this.pedido = pedido;
        this.cliente = cliente;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensagem = mensagem;
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

    public LocalDateTime getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDateTime dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public Boolean getLida() {
        return lida;
    }

    public void setLida(Boolean lida) {
        this.lida = lida;
    }

    /**
     * Marca a notificação como lida
     */
    public void marcarComoLida() {
        this.lida = true;
    }

    /**
     * Marca a notificação como não lida
     */
    public void marcarComoNaoLida() {
        this.lida = false;
    }

    @PrePersist
    protected void onCreate() {
        if (dataEnvio == null) {
            dataEnvio = LocalDateTime.now();
        }
        if (lida == null) {
            lida = false;
        }
    }

    @Override
    public String toString() {
        return "Notificacao{" +
                "id=" + id +
                ", pedidoId=" + (pedido != null ? pedido.getId() : null) +
                ", clienteId=" + (cliente != null ? cliente.getId() : null) +
                ", tipo=" + tipo +
                ", titulo='" + titulo + '\'' +
                ", dataEnvio=" + dataEnvio +
                ", lida=" + lida +
                '}';
    }
}
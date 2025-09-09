package br.edu.infnet.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Entidade para armazenar informações de rastreamento externo do pedido
 */
@Entity
@Table(name = "dados_rastreamento")
public class DadosRastreamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Pedido é obrigatório")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false, unique = true)
    private Pedido pedido;

    @NotBlank(message = "Código de rastreamento é obrigatório")
    @Size(max = 100, message = "Código de rastreamento deve ter no máximo 100 caracteres")
    @Column(name = "codigo_rastreamento", nullable = false, unique = true, length = 100)
    private String codigoRastreamento;

    @NotBlank(message = "Transportadora é obrigatória")
    @Size(max = 100, message = "Nome da transportadora deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String transportadora;

    @NotNull(message = "Data da última atualização é obrigatória")
    @Column(name = "ultima_atualizacao", nullable = false)
    private LocalDateTime ultimaAtualizacao;

    @Size(max = 255, message = "Localização atual deve ter no máximo 255 caracteres")
    @Column(name = "localizacao_atual", length = 255)
    private String localizacaoAtual;

    public DadosRastreamento() {
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    public DadosRastreamento(Pedido pedido, String codigoRastreamento, String transportadora) {
        this();
        this.pedido = pedido;
        this.codigoRastreamento = codigoRastreamento;
        this.transportadora = transportadora;
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

    public LocalDateTime getUltimaAtualizacao() {
        return ultimaAtualizacao;
    }

    public void setUltimaAtualizacao(LocalDateTime ultimaAtualizacao) {
        this.ultimaAtualizacao = ultimaAtualizacao;
    }

    public String getLocalizacaoAtual() {
        return localizacaoAtual;
    }

    public void setLocalizacaoAtual(String localizacaoAtual) {
        this.localizacaoAtual = localizacaoAtual;
    }

    /**
     * Atualiza a localização atual do pedido
     */
    public void atualizarLocalizacao(String novaLocalizacao) {
        this.localizacaoAtual = novaLocalizacao;
        this.ultimaAtualizacao = LocalDateTime.now();
    }

    /**
     * Verifica se os dados de rastreamento foram atualizados recentemente (últimas 24h)
     */
    public boolean isAtualizadoRecentemente() {
        return ultimaAtualizacao != null &&
                ultimaAtualizacao.isAfter(LocalDateTime.now().minusDays(1));
    }

    @PrePersist
    protected void onCreate() {
        if (ultimaAtualizacao == null) {
            ultimaAtualizacao = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        ultimaAtualizacao = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "DadosRastreamento{" +
                "id=" + id +
                ", pedidoId=" + (pedido != null ? pedido.getId() : null) +
                ", codigoRastreamento='" + codigoRastreamento + '\'' +
                ", transportadora='" + transportadora + '\'' +
                ", ultimaAtualizacao=" + ultimaAtualizacao +
                ", localizacaoAtual='" + localizacaoAtual + '\'' +
                '}';
    }
}
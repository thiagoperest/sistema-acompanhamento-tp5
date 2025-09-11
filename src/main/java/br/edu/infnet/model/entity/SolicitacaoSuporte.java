package br.edu.infnet.model.entity;

import br.edu.infnet.model.enums.StatusSuporte;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * Entidade que representa uma solicitação de suporte no sistema
 */
@Entity
@Table(name = "solicitacao_suporte")
public class SolicitacaoSuporte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Protocolo é obrigatório")
    @Size(max = 20, message = "Protocolo deve ter no máximo 20 caracteres")
    @Column(unique = true, length = 20)
    private String protocolo;

    @NotNull(message = "Cliente é obrigatório")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atendente_id")
    private AtendenteSuporte atendente;

    @NotBlank(message = "Tipo do problema é obrigatório")
    @Size(max = 100, message = "Tipo do problema deve ter no máximo 100 caracteres")
    @Column(name = "tipo_problema", length = 100)
    private String tipoProblema;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 1000, message = "Descrição deve ter no máximo 1000 caracteres")
    @Column(length = 1000)
    private String descricao;

    @NotNull(message = "Status é obrigatório")
    @Enumerated(EnumType.STRING)
    private StatusSuporte status = StatusSuporte.ABERTO;

    @Column(name = "data_abertura", nullable = false)
    private LocalDateTime dataAbertura = LocalDateTime.now();

    @Column(name = "data_fechamento")
    private LocalDateTime dataFechamento;

    public SolicitacaoSuporte() {
    }

    public SolicitacaoSuporte(Cliente cliente, String protocolo, String tipoProblema, String descricao) {
        this.cliente = cliente;
        this.protocolo = protocolo;
        this.tipoProblema = tipoProblema;
        this.descricao = descricao;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public void setProtocolo(String protocolo) {
        this.protocolo = protocolo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public AtendenteSuporte getAtendente() {
        return atendente;
    }

    public void setAtendente(AtendenteSuporte atendente) {
        this.atendente = atendente;
    }

    public String getTipoProblema() {
        return tipoProblema;
    }

    public void setTipoProblema(String tipoProblema) {
        this.tipoProblema = tipoProblema;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public StatusSuporte getStatus() {
        return status;
    }

    public void setStatus(StatusSuporte status) {
        this.status = status;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(LocalDateTime dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public LocalDateTime getDataFechamento() {
        return dataFechamento;
    }

    public void setDataFechamento(LocalDateTime dataFechamento) {
        this.dataFechamento = dataFechamento;
    }
}
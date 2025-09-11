package br.edu.infnet.model.dto;

import br.edu.infnet.model.entity.SolicitacaoSuporte;
import br.edu.infnet.model.enums.StatusSuporte;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

/**
 * DTO para transferência de dados de SolicitacaoSuporte
 */
public class SolicitacaoSuporteDto {

    private Long id;

    @NotBlank(message = "Protocolo é obrigatório")
    private String protocolo;

    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    private String nomeCliente;

    private Long atendenteId;
    private String nomeAtendente;

    @NotBlank(message = "Tipo do problema é obrigatório")
    @Size(max = 100)
    private String tipoProblema;

    @NotBlank(message = "Descrição é obrigatória")
    @Size(max = 1000)
    private String descricao;

    @NotNull(message = "Status é obrigatório")
    private StatusSuporte status;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    public SolicitacaoSuporteDto() {
    }

    public SolicitacaoSuporteDto(SolicitacaoSuporte solicitacao) {
        if (solicitacao != null) {
            this.id = solicitacao.getId();
            this.protocolo = solicitacao.getProtocolo();
            this.tipoProblema = solicitacao.getTipoProblema();
            this.descricao = solicitacao.getDescricao();
            this.status = solicitacao.getStatus();
            this.dataAbertura = solicitacao.getDataAbertura();
            this.dataFechamento = solicitacao.getDataFechamento();

            if (solicitacao.getCliente() != null) {
                this.clienteId = solicitacao.getCliente().getId();
                this.nomeCliente = solicitacao.getCliente().getNome();
            }

            if (solicitacao.getAtendente() != null) {
                this.atendenteId = solicitacao.getAtendente().getId();
                this.nomeAtendente = solicitacao.getAtendente().getNome();
            }
        }
    }

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

    public Long getAtendenteId() {
        return atendenteId;
    }

    public void setAtendenteId(Long atendenteId) {
        this.atendenteId = atendenteId;
    }

    public String getNomeAtendente() {
        return nomeAtendente;
    }

    public void setNomeAtendente(String nomeAtendente) {
        this.nomeAtendente = nomeAtendente;
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
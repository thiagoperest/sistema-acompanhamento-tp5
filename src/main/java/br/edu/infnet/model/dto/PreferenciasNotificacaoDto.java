package br.edu.infnet.model.dto;

import jakarta.validation.constraints.NotNull;

/**
 * DTO para transferência de dados de PreferenciasNotificacao
 */
public class PreferenciasNotificacaoDto {

    private Long id;

    @NotNull(message = "ID do cliente é obrigatório")
    private Long clienteId;

    @NotNull(message = "Status do email é obrigatório")
    private Boolean emailAtivo;

    @NotNull(message = "Status do SMS é obrigatório")
    private Boolean smsAtivo;

    @NotNull(message = "Status do push é obrigatório")
    private Boolean pushAtivo;

    private String horarioInicio;
    private String horarioFim;

    // Campos adicionais para exibição
    private String nomeCliente;
    private String emailCliente;
    private Boolean temAlgumaNotificacaoAtiva;
    private Boolean horarioPermitido;

    public PreferenciasNotificacaoDto() {
    }

    public PreferenciasNotificacaoDto(Long clienteId, Boolean emailAtivo, Boolean smsAtivo, Boolean pushAtivo) {
        this.clienteId = clienteId;
        this.emailAtivo = emailAtivo;
        this.smsAtivo = smsAtivo;
        this.pushAtivo = pushAtivo;
    }

    public PreferenciasNotificacaoDto(Long id, Long clienteId, Boolean emailAtivo, Boolean smsAtivo,
                                      Boolean pushAtivo, String horarioInicio, String horarioFim) {
        this.id = id;
        this.clienteId = clienteId;
        this.emailAtivo = emailAtivo;
        this.smsAtivo = smsAtivo;
        this.pushAtivo = pushAtivo;
        this.horarioInicio = horarioInicio;
        this.horarioFim = horarioFim;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Boolean getEmailAtivo() {
        return emailAtivo;
    }

    public void setEmailAtivo(Boolean emailAtivo) {
        this.emailAtivo = emailAtivo;
    }

    public Boolean getSmsAtivo() {
        return smsAtivo;
    }

    public void setSmsAtivo(Boolean smsAtivo) {
        this.smsAtivo = smsAtivo;
    }

    public Boolean getPushAtivo() {
        return pushAtivo;
    }

    public void setPushAtivo(Boolean pushAtivo) {
        this.pushAtivo = pushAtivo;
    }

    public String getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(String horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public String getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(String horarioFim) {
        this.horarioFim = horarioFim;
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

    public Boolean getTemAlgumaNotificacaoAtiva() {
        return temAlgumaNotificacaoAtiva;
    }

    public void setTemAlgumaNotificacaoAtiva(Boolean temAlgumaNotificacaoAtiva) {
        this.temAlgumaNotificacaoAtiva = temAlgumaNotificacaoAtiva;
    }

    public Boolean getHorarioPermitido() {
        return horarioPermitido;
    }

    public void setHorarioPermitido(Boolean horarioPermitido) {
        this.horarioPermitido = horarioPermitido;
    }

    @Override
    public String toString() {
        return "PreferenciasNotificacaoDto{" +
                "id=" + id +
                ", clienteId=" + clienteId +
                ", emailAtivo=" + emailAtivo +
                ", smsAtivo=" + smsAtivo +
                ", pushAtivo=" + pushAtivo +
                ", horarioInicio='" + horarioInicio + '\'' +
                ", horarioFim='" + horarioFim + '\'' +
                '}';
    }
}
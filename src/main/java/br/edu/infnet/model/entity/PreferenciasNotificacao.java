package br.edu.infnet.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

/**
 * Entidade de configurações de notificação personalizadas do cliente
 */
@Entity
@Table(name = "preferencias_notificacao")
public class PreferenciasNotificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Cliente é obrigatório")
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false, unique = true)
    private Cliente cliente;

    @NotNull(message = "Status do email é obrigatório")
    @Column(name = "email_ativo", nullable = false)
    private Boolean emailAtivo = true;

    @NotNull(message = "Status do SMS é obrigatório")
    @Column(name = "sms_ativo", nullable = false)
    private Boolean smsAtivo = false;

    @NotNull(message = "Status do push é obrigatório")
    @Column(name = "push_ativo", nullable = false)
    private Boolean pushAtivo = true;

    @Column(name = "horario_inicio")
    private LocalTime horarioInicio = LocalTime.of(8, 0);

    @Column(name = "horario_fim")
    private LocalTime horarioFim = LocalTime.of(22, 0);

    public PreferenciasNotificacao() {
        this.emailAtivo = true;
        this.smsAtivo = false;
        this.pushAtivo = true;
        this.horarioInicio = LocalTime.of(8, 0);
        this.horarioFim = LocalTime.of(22, 0);
    }

    public PreferenciasNotificacao(Cliente cliente) {
        this();
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
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

    public LocalTime getHorarioInicio() {
        return horarioInicio;
    }

    public void setHorarioInicio(LocalTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    public LocalTime getHorarioFim() {
        return horarioFim;
    }

    public void setHorarioFim(LocalTime horarioFim) {
        this.horarioFim = horarioFim;
    }

    /**
     * Ativa todas as notificações
     */
    public void ativarTodasNotificacoes() {
        this.emailAtivo = true;
        this.smsAtivo = true;
        this.pushAtivo = true;
    }

    /**
     * Desativa todas as notificações
     */
    public void desativarTodasNotificacoes() {
        this.emailAtivo = false;
        this.smsAtivo = false;
        this.pushAtivo = false;
    }

    /**
     * Verifica se pelo menos um tipo de notificação está ativo
     */
    public boolean temAlgumaNotificacaoAtiva() {
        return emailAtivo || smsAtivo || pushAtivo;
    }

    /**
     * Verifica se o horário atual está dentro do período de notificações
     */
    public boolean isHorarioPermitido() {
        LocalTime agora = LocalTime.now();
        return agora.isAfter(horarioInicio) && agora.isBefore(horarioFim);
    }

    /**
     * Define horário padrão de funcionamento comercial (8h às 18h)
     */
    public void definirHorarioComercial() {
        this.horarioInicio = LocalTime.of(8, 0);
        this.horarioFim = LocalTime.of(18, 0);
    }

    /**
     * Define horário estendido (8h às 22h)
     */
    public void definirHorarioEstendido() {
        this.horarioInicio = LocalTime.of(8, 0);
        this.horarioFim = LocalTime.of(22, 0);
    }

    @PrePersist
    protected void onCreate() {
        if (emailAtivo == null) emailAtivo = true;
        if (smsAtivo == null) smsAtivo = false;
        if (pushAtivo == null) pushAtivo = true;
        if (horarioInicio == null) horarioInicio = LocalTime.of(8, 0);
        if (horarioFim == null) horarioFim = LocalTime.of(22, 0);
    }

    @Override
    public String toString() {
        return "PreferenciasNotificacao{" +
                "id=" + id +
                ", clienteId=" + (cliente != null ? cliente.getId() : null) +
                ", emailAtivo=" + emailAtivo +
                ", smsAtivo=" + smsAtivo +
                ", pushAtivo=" + pushAtivo +
                ", horarioInicio=" + horarioInicio +
                ", horarioFim=" + horarioFim +
                '}';
    }
}
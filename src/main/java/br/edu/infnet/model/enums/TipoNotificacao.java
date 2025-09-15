package br.edu.infnet.model.enums;

public enum TipoNotificacao {
    EMAIL("Notificação por e-mail"),
    SMS("Notificação por SMS"),
    PUSH("Notificação push (app/web)");

    private final String descricao;

    TipoNotificacao(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
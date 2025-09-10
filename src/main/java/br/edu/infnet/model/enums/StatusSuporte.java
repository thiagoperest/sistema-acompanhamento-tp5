package br.edu.infnet.model.enums;

public enum StatusSuporte {
    ABERTO("Solicitação recém-criada"),
    EM_ANDAMENTO("Sendo atendida"),
    RESOLVIDO("Problema resolvido"),
    FECHADO("Atendimento finalizado");

    private final String descricao;

    StatusSuporte(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return this.name() + " - " + this.descricao;
    }
}
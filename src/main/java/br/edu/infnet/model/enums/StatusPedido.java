package br.edu.infnet.model.enums;

public enum StatusPedido {
    CONFIRMADO("Pedido confirmado e processando"),
    PROCESSANDO("Pedido sendo preparado"),
    ENVIADO("Pedido enviado para transporte"),
    EM_TRANSITO("Pedido em trânsito"),
    SAIU_PARA_ENTREGA("Pedido saiu para entrega"),
    ENTREGUE("Pedido entregue ao cliente"),
    CANCELADO("Pedido cancelado"),
    DEVOLVIDO("Pedido devolvido");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
package br.edu.infnet.service;

import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service para gerenciar compartilhamento de status de pedidos
 */
@Service
public class CompartilhamentoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    private final Map<String, LinkCompartilhamento> linksAtivos = new HashMap<>();

    /**
     * Gerar link de compartilhamento para um pedido
     */
    public String gerarLinkCompartilhamento(Long pedidoId, Long clienteId) {
        Optional<Pedido> pedidoOpt = pedidoRepository.findById(pedidoId);
        if (pedidoOpt.isEmpty()) {
            throw new RuntimeException("Pedido não encontrado");
        }

        Pedido pedido = pedidoOpt.get();

        if (!pedido.getCliente().getId().equals(clienteId)) {
            throw new RuntimeException("Pedido não pertence ao cliente");
        }

        String token = gerarToken(pedidoId, clienteId);

        LinkCompartilhamento link = new LinkCompartilhamento(
                pedidoId,
                clienteId,
                LocalDateTime.now().plusHours(24)
        );

        linksAtivos.put(token, link);

        // simulando um link do sistema para compartilhamento
        return "https://acompanhamento.app/compartilhar/" + token;
    }

    /**
     * Validar e obter informações do link compartilhado
     */
    public Optional<Map<String, Object>> validarLinkCompartilhamento(String token) {
        LinkCompartilhamento link = linksAtivos.get(token);

        if (link == null) {
            return Optional.empty();
        }

        if (link.getExpiracao().isBefore(LocalDateTime.now())) {
            linksAtivos.remove(token);
            return Optional.empty();
        }

        Optional<Pedido> pedidoOpt = pedidoRepository.findById(link.getPedidoId());
        if (pedidoOpt.isEmpty()) {
            return Optional.empty();
        }

        Pedido pedido = pedidoOpt.get();

        Map<String, Object> info = new HashMap<>();
        info.put("numeroPedido", pedido.getNumeroPedido());
        info.put("status", pedido.getStatus().getDescricao());
        info.put("dataCompra", pedido.getDataCompra());
        info.put("previsaoEntrega", pedido.getPrevisaoEntrega());

        return Optional.of(info);
    }

    /**
     * Gerar token único baseado no pedido e cliente
     */
    private String gerarToken(Long pedidoId, Long clienteId) {
        String data = pedidoId + ":" + clienteId + ":" + System.currentTimeMillis();
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(data.getBytes());
    }

    /**
     * Classe interna para representar um link de compartilhamento
     */
    private static class LinkCompartilhamento {
        private final Long pedidoId;
        private final Long clienteId;
        private final LocalDateTime expiracao;

        public LinkCompartilhamento(Long pedidoId, Long clienteId, LocalDateTime expiracao) {
            this.pedidoId = pedidoId;
            this.clienteId = clienteId;
            this.expiracao = expiracao;
        }

        public Long getPedidoId() {
            return pedidoId;
        }

        public Long getClienteId() {
            return clienteId;
        }

        public LocalDateTime getExpiracao() {
            return expiracao;
        }
    }
}
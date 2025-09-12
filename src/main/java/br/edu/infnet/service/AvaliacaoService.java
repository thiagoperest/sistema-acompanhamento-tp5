package br.edu.infnet.service;

import br.edu.infnet.model.dto.AvaliacaoDto;
import br.edu.infnet.model.entity.Avaliacao;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.model.enums.StatusPedido;
import br.edu.infnet.repository.AvaliacaoRepository;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para gerenciar operações de Avaliação
 */
@Service
@Transactional
public class AvaliacaoService {

    @Autowired
    private AvaliacaoRepository avaliacaoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    /**
     * Criar nova avaliação
     */
    public AvaliacaoDto criarAvaliacao(AvaliacaoDto dto) {
        Pedido pedido = pedidoRepository.findById(dto.getPedidoId())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() != StatusPedido.ENTREGUE) {
            throw new RuntimeException("Só é possível avaliar pedidos entregues");
        }

        if (avaliacaoRepository.existsByPedidoId(dto.getPedidoId())) {
            throw new RuntimeException("Este pedido já foi avaliado");
        }

        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        if (!pedido.getCliente().getId().equals(cliente.getId())) {
            throw new RuntimeException("Este pedido não pertence ao cliente informado");
        }

        Avaliacao avaliacao = new Avaliacao();
        avaliacao.setPedido(pedido);
        avaliacao.setCliente(cliente);
        avaliacao.setNotaAcompanhamento(dto.getNotaAcompanhamento());
        avaliacao.setNotaEntrega(dto.getNotaEntrega());
        avaliacao.setComentario(dto.getComentario());

        avaliacao = avaliacaoRepository.save(avaliacao);
        return new AvaliacaoDto(avaliacao);
    }

    /**
     * Buscar avaliação por ID
     */
    public AvaliacaoDto buscarPorId(Long id) {
        return avaliacaoRepository.findById(id)
                .map(AvaliacaoDto::new)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada com ID: " + id));
    }

    /**
     * Buscar avaliação por pedido
     */
    public Optional<AvaliacaoDto> buscarPorPedido(Long pedidoId) {
        return avaliacaoRepository.findByPedidoId(pedidoId)
                .map(AvaliacaoDto::new);
    }

    /**
     * Listar avaliações por cliente
     */
    public List<AvaliacaoDto> listarPorCliente(Long clienteId) {
        return avaliacaoRepository.findByClienteIdOrderByDataAvaliacaoDesc(clienteId)
                .stream()
                .map(AvaliacaoDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Listar todas as avaliações
     */
    public List<AvaliacaoDto> listarTodas() {
        return avaliacaoRepository.findAll()
                .stream()
                .map(AvaliacaoDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Obter média de nota de acompanhamento
     */
    public Double getMediaNotaAcompanhamento() {
        Double media = avaliacaoRepository.getMediaNotaAcompanhamento();
        return media != null ? media : 0.0;
    }

    /**
     * Obter média de nota de entrega
     */
    public Double getMediaNotaEntrega() {
        Double media = avaliacaoRepository.getMediaNotaEntrega();
        return media != null ? media : 0.0;
    }

    /**
     * Obter média geral
     */
    public Double getMediaGeral() {
        Double media = avaliacaoRepository.getMediaGeral();
        return media != null ? media : 0.0;
    }

    /**
     * Contar avaliações com comentário
     */
    public Long contarAvaliacoesComComentario() {
        return avaliacaoRepository.countAvaliacoesComComentario();
    }

    /**
     * Verificar se pedido já foi avaliado
     */
    public boolean pedidoJaAvaliado(Long pedidoId) {
        return avaliacaoRepository.existsByPedidoId(pedidoId);
    }

    /**
     * Listar avaliações com nota mínima
     */
    public List<AvaliacaoDto> listarPorNotaMinima(Double notaMinima) {
        return avaliacaoRepository.findAll()
                .stream()
                .filter(avaliacao -> avaliacao.getMediaNotas() >= notaMinima)
                .map(AvaliacaoDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Obter estatísticas de avaliações
     */
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();

        estatisticas.put("totalAvaliacoes", avaliacaoRepository.count());
        estatisticas.put("mediaNotaAcompanhamento", getMediaNotaAcompanhamento());
        estatisticas.put("mediaNotaEntrega", getMediaNotaEntrega());
        estatisticas.put("mediaGeral", getMediaGeral());
        estatisticas.put("avaliacoesComComentario", contarAvaliacoesComComentario());

        // Distribuição de notas
        Map<Integer, Long> distribuicaoNotas = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            final int nota = i;
            long count = avaliacaoRepository.findAll()
                    .stream()
                    .filter(av -> av.getNotaAcompanhamento() == nota || av.getNotaEntrega() == nota)
                    .count();
            distribuicaoNotas.put(nota, count);
        }
        estatisticas.put("distribuicaoNotas", distribuicaoNotas);

        return estatisticas;
    }
}
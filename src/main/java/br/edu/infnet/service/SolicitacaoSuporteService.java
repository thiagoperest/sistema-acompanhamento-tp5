package br.edu.infnet.service;

import br.edu.infnet.model.dto.SolicitacaoSuporteDto;
import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.SolicitacaoSuporte;
import br.edu.infnet.model.enums.StatusSuporte;
import br.edu.infnet.repository.AtendenteSuporteRepository;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.SolicitacaoSuporteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Service para gerenciar operações de Solicitação de Suporte
 */
@Service
@Transactional
public class SolicitacaoSuporteService {

    @Autowired
    private SolicitacaoSuporteRepository solicitacaoSuporteRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AtendenteSuporteRepository atendenteSuporteRepository;

    /**
     * Gera um protocolo único para a solicitação
     */
    private String gerarProtocolo() {
        Random random = new Random();
        String protocolo;
        do {
            protocolo = String.format("SUP-%d-%04d",
                    System.currentTimeMillis() / 1000,
                    random.nextInt(10000));
        } while (solicitacaoSuporteRepository.findByProtocolo(protocolo).isPresent());
        return protocolo;
    }

    /**
     * Criar nova solicitação de suporte (UC04)
     */
    public SolicitacaoSuporteDto criarSolicitacao(SolicitacaoSuporteDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        SolicitacaoSuporte solicitacao = new SolicitacaoSuporte();
        solicitacao.setProtocolo(gerarProtocolo());
        solicitacao.setCliente(cliente);
        solicitacao.setTipoProblema(dto.getTipoProblema());
        solicitacao.setDescricao(dto.getDescricao());
        solicitacao.setStatus(StatusSuporte.ABERTO);
        solicitacao.setDataAbertura(LocalDateTime.now());

        solicitacao = solicitacaoSuporteRepository.save(solicitacao);
        return new SolicitacaoSuporteDto(solicitacao);
    }

    /**
     * Buscar solicitação por ID
     */
    public Optional<SolicitacaoSuporteDto> buscarPorId(Long id) {
        return solicitacaoSuporteRepository.findById(id)
                .map(SolicitacaoSuporteDto::new);
    }

    /**
     * Buscar solicitação por protocolo
     */
    public Optional<SolicitacaoSuporteDto> buscarPorProtocolo(String protocolo) {
        return solicitacaoSuporteRepository.findByProtocolo(protocolo)
                .map(SolicitacaoSuporteDto::new);
    }

    /**
     * Listar solicitações por cliente
     */
    public List<SolicitacaoSuporteDto> listarPorCliente(Long clienteId) {
        return solicitacaoSuporteRepository.findByClienteIdOrderByDataAberturaDesc(clienteId)
                .stream()
                .map(SolicitacaoSuporteDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Listar solicitações por status
     */
    public List<SolicitacaoSuporteDto> listarPorStatus(StatusSuporte status) {
        return solicitacaoSuporteRepository.findByStatusOrderByDataAberturaAsc(status)
                .stream()
                .map(SolicitacaoSuporteDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Atualizar status da solicitação
     */
    public SolicitacaoSuporteDto atualizarStatus(Long id, StatusSuporte novoStatus) {
        SolicitacaoSuporte solicitacao = solicitacaoSuporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        solicitacao.setStatus(novoStatus);

        if (novoStatus == StatusSuporte.FECHADO || novoStatus == StatusSuporte.RESOLVIDO) {
            solicitacao.setDataFechamento(LocalDateTime.now());
        }

        solicitacao = solicitacaoSuporteRepository.save(solicitacao);
        return new SolicitacaoSuporteDto(solicitacao);
    }

    /**
     * Listar todas as solicitações
     */
    public List<SolicitacaoSuporteDto> listarTodas() {
        return solicitacaoSuporteRepository.findAll()
                .stream()
                .map(SolicitacaoSuporteDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Contar solicitações por status
     */
    public long contarPorStatus(StatusSuporte status) {
        return solicitacaoSuporteRepository.countByStatus(status);
    }

    /**
     * Atualizar status com atendente opcional
     */
    public SolicitacaoSuporteDto atualizarStatus(Long id, StatusSuporte novoStatus, Long atendenteId) {
        SolicitacaoSuporte solicitacao = solicitacaoSuporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        solicitacao.setStatus(novoStatus);

        // Se foi informado um atendente, atribui
        if (atendenteId != null) {
            AtendenteSuporte atendente = atendenteSuporteRepository.findById(atendenteId)
                    .orElseThrow(() -> new RuntimeException("Atendente não encontrado"));
            solicitacao.setAtendente(atendente);
        }

        if (novoStatus == StatusSuporte.FECHADO || novoStatus == StatusSuporte.RESOLVIDO) {
            solicitacao.setDataFechamento(LocalDateTime.now());
        }

        solicitacao = solicitacaoSuporteRepository.save(solicitacao);
        return new SolicitacaoSuporteDto(solicitacao);
    }

    /**
     * Atribuir atendente à solicitação
     */
    public SolicitacaoSuporteDto atribuirAtendente(Long solicitacaoId, Long atendenteId) {
        SolicitacaoSuporte solicitacao = solicitacaoSuporteRepository.findById(solicitacaoId)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        AtendenteSuporte atendente = atendenteSuporteRepository.findById(atendenteId)
                .orElseThrow(() -> new RuntimeException("Atendente não encontrado"));

        solicitacao.setAtendente(atendente);

        // Se estiver aberta, muda para em andamento
        if (solicitacao.getStatus() == StatusSuporte.ABERTO) {
            solicitacao.setStatus(StatusSuporte.EM_ANDAMENTO);
        }

        solicitacao = solicitacaoSuporteRepository.save(solicitacao);
        return new SolicitacaoSuporteDto(solicitacao);
    }

    /**
     * Listar solicitações por atendente
     */
    public List<SolicitacaoSuporteDto> listarPorAtendente(Long atendenteId) {
        return solicitacaoSuporteRepository.findByAtendenteId(atendenteId)
                .stream()
                .map(SolicitacaoSuporteDto::new)
                .collect(Collectors.toList());
    }

    /**
     * Fechar solicitação
     */
    public SolicitacaoSuporteDto fecharSolicitacao(Long id) {
        SolicitacaoSuporte solicitacao = solicitacaoSuporteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitação não encontrada"));

        if (solicitacao.getStatus() != StatusSuporte.RESOLVIDO) {
            throw new RuntimeException("Apenas solicitações resolvidas podem ser fechadas");
        }

        solicitacao.setStatus(StatusSuporte.FECHADO);
        solicitacao.setDataFechamento(LocalDateTime.now());

        solicitacao = solicitacaoSuporteRepository.save(solicitacao);
        return new SolicitacaoSuporteDto(solicitacao);
    }

    /**
     * Obter estatísticas de suporte
     */
    public Map<String, Object> obterEstatisticas() {
        Map<String, Object> estatisticas = new HashMap<>();

        estatisticas.put("totalSolicitacoes", solicitacaoSuporteRepository.count());
        estatisticas.put("solicitacoesAbertas", contarPorStatus(StatusSuporte.ABERTO));
        estatisticas.put("solicitacoesEmAndamento", contarPorStatus(StatusSuporte.EM_ANDAMENTO));
        estatisticas.put("solicitacoesResolvidas", contarPorStatus(StatusSuporte.RESOLVIDO));
        estatisticas.put("solicitacoesFechadas", contarPorStatus(StatusSuporte.FECHADO));

        // Taxa de resolução
        long total = solicitacaoSuporteRepository.count();
        long resolvidas = contarPorStatus(StatusSuporte.RESOLVIDO) + contarPorStatus(StatusSuporte.FECHADO);
        double taxaResolucao = total > 0 ? (double) resolvidas / total * 100 : 0;
        estatisticas.put("taxaResolucao", String.format("%.2f%%", taxaResolucao));

        // Solicitações por tipo de problema
        Map<String, Long> porTipoProblema = new HashMap<>();
        List<SolicitacaoSuporte> todas = solicitacaoSuporteRepository.findAll();
        todas.stream()
                .collect(Collectors.groupingBy(SolicitacaoSuporte::getTipoProblema, Collectors.counting()))
                .forEach(porTipoProblema::put);
        estatisticas.put("porTipoProblema", porTipoProblema);

        return estatisticas;
    }
}
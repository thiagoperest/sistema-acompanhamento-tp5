package br.edu.infnet.service;

import br.edu.infnet.model.dto.SolicitacaoSuporteDto;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.SolicitacaoSuporte;
import br.edu.infnet.model.enums.StatusSuporte;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.SolicitacaoSuporteRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
}
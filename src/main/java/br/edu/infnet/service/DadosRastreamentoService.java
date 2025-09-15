package br.edu.infnet.service;

import br.edu.infnet.model.dto.DadosRastreamentoDto;
import br.edu.infnet.model.entity.DadosRastreamento;
import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.repository.DadosRastreamentoRepository;
import br.edu.infnet.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para regras de negócio relacionadas aos Dados de Rastreamento
 */
@Service
@Transactional
public class DadosRastreamentoService {

    @Autowired
    private DadosRastreamentoRepository dadosRastreamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Cria dados de rastreamento para um pedido
     */
    public DadosRastreamentoDto criarDadosRastreamento(DadosRastreamentoDto dadosDto) {
        validarDadosObrigatorios(dadosDto);
        
        Pedido pedido = buscarPedidoPorId(dadosDto.getPedidoId());
        
        // Verifica se o pedido já possui dados de rastreamento
        if (dadosRastreamentoRepository.findByPedidoId(dadosDto.getPedidoId()).isPresent()) {
            throw new RuntimeException("Pedido já possui dados de rastreamento cadastrados");
        }
        
        validarCodigoRastreamentoUnico(dadosDto.getCodigoRastreamento(), null);
        
        DadosRastreamento dados = converterDtoParaEntity(dadosDto);
        dados.setPedido(pedido);
        
        DadosRastreamento dadosSalvos = dadosRastreamentoRepository.save(dados);
        return converterEntityParaDto(dadosSalvos);
    }

    /**
     * Busca dados de rastreamento por ID
     */
    public Optional<DadosRastreamentoDto> buscarPorId(Long id) {
        return dadosRastreamentoRepository.findById(id)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca dados de rastreamento por pedido
     */
    public Optional<DadosRastreamentoDto> buscarPorPedido(Long pedidoId) {
        validarPedidoExiste(pedidoId);
        return dadosRastreamentoRepository.findByPedidoId(pedidoId)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca dados de rastreamento por código
     */
    public Optional<DadosRastreamentoDto> buscarPorCodigo(String codigoRastreamento) {
        if (codigoRastreamento == null || codigoRastreamento.trim().isEmpty()) {
            throw new RuntimeException("Código de rastreamento é obrigatório");
        }
        return dadosRastreamentoRepository.findByCodigoRastreamento(codigoRastreamento)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca dados de rastreamento por transportadora
     */
    public List<DadosRastreamentoDto> buscarPorTransportadora(String transportadora) {
        if (transportadora == null || transportadora.trim().isEmpty()) {
            throw new RuntimeException("Nome da transportadora é obrigatório");
        }
        return dadosRastreamentoRepository.findByTransportadoraIgnoreCaseOrderByUltimaAtualizacaoDesc(transportadora)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista todas as transportadoras cadastradas
     */
    public List<String> listarTransportadoras() {
        return dadosRastreamentoRepository.findDistinctTransportadoras();
    }

    /**
     * Atualiza dados de rastreamento
     */
    public DadosRastreamentoDto atualizarDados(Long id, DadosRastreamentoDto dadosDto) {
        DadosRastreamento dadosExistentes = dadosRastreamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dados de rastreamento não encontrados com ID: " + id));

        validarDadosObrigatorios(dadosDto);
        
        // Valida código único apenas se foi alterado
        if (!dadosExistentes.getCodigoRastreamento().equals(dadosDto.getCodigoRastreamento())) {
            validarCodigoRastreamentoUnico(dadosDto.getCodigoRastreamento(), id);
        }
        
        atualizarCamposDados(dadosExistentes, dadosDto);
        
        DadosRastreamento dadosAtualizados = dadosRastreamentoRepository.save(dadosExistentes);
        return converterEntityParaDto(dadosAtualizados);
    }

    /**
     * Atualiza localização atual
     */
    public DadosRastreamentoDto atualizarLocalizacao(Long id, String novaLocalizacao) {
        DadosRastreamento dados = dadosRastreamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Dados de rastreamento não encontrados com ID: " + id));

        dados.atualizarLocalizacao(novaLocalizacao);
        
        DadosRastreamento dadosAtualizados = dadosRastreamentoRepository.save(dados);
        return converterEntityParaDto(dadosAtualizados);
    }

    /**
     * Atualiza localização por código de rastreamento
     */
    public DadosRastreamentoDto atualizarLocalizacaoPorCodigo(String codigoRastreamento, String novaLocalizacao) {
        DadosRastreamento dados = dadosRastreamentoRepository.findByCodigoRastreamento(codigoRastreamento)
                .orElseThrow(() -> new RuntimeException("Dados de rastreamento não encontrados para código: " + codigoRastreamento));

        dados.atualizarLocalizacao(novaLocalizacao);
        
        DadosRastreamento dadosAtualizados = dadosRastreamentoRepository.save(dados);
        return converterEntityParaDto(dadosAtualizados);
    }

    /**
     * Busca rastreamentos atualizados recentemente
     */
    public List<DadosRastreamentoDto> buscarRastreamentosRecentes() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(1);
        return dadosRastreamentoRepository.findRastreamentosRecentes(dataLimite)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca rastreamentos desatualizados
     */
    public List<DadosRastreamentoDto> buscarRastreamentosDesatualizados(int dias) {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(dias);
        return dadosRastreamentoRepository.findRastreamentosDesatualizados(dataLimite)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca rastreamentos por localização
     */
    public List<DadosRastreamentoDto> buscarPorLocalizacao(String localizacao) {
        if (localizacao == null || localizacao.trim().isEmpty()) {
            throw new RuntimeException("Localização é obrigatória");
        }
        return dadosRastreamentoRepository.findByLocalizacaoAtualContainingIgnoreCase(localizacao)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os dados de rastreamento
     */
    public List<DadosRastreamentoDto> listarTodos() {
        return dadosRastreamentoRepository.findAll()
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Remove dados de rastreamento
     */
    public void removerDados(Long id) {
        if (!dadosRastreamentoRepository.existsById(id)) {
            throw new RuntimeException("Dados de rastreamento não encontrados com ID: " + id);
        }
        dadosRastreamentoRepository.deleteById(id);
    }

    /**
     * Conta rastreamentos por transportadora
     */
    public long contarPorTransportadora(String transportadora) {
        return dadosRastreamentoRepository.countByTransportadora(transportadora);
    }

    private void validarDadosObrigatorios(DadosRastreamentoDto dto) {
        if (dto.getPedidoId() == null) {
            throw new RuntimeException("ID do pedido é obrigatório");
        }
        if (dto.getCodigoRastreamento() == null || dto.getCodigoRastreamento().trim().isEmpty()) {
            throw new RuntimeException("Código de rastreamento é obrigatório");
        }
        if (dto.getTransportadora() == null || dto.getTransportadora().trim().isEmpty()) {
            throw new RuntimeException("Transportadora é obrigatória");
        }
        validarFormatoCodigo(dto.getCodigoRastreamento());
    }

    private void validarFormatoCodigo(String codigo) {
        if (codigo.length() < 5) {
            throw new RuntimeException("Código de rastreamento deve ter pelo menos 5 caracteres");
        }
    }

    private void validarCodigoRastreamentoUnico(String codigo, Long idExcluir) {
        boolean codigoJaExiste = (idExcluir == null) ?
                dadosRastreamentoRepository.existsByCodigoRastreamento(codigo) :
                dadosRastreamentoRepository.existsByCodigoRastreamentoAndIdNot(codigo, idExcluir);

        if (codigoJaExiste) {
            throw new RuntimeException("Código de rastreamento já cadastrado no sistema: " + codigo);
        }
    }

    private void validarPedidoExiste(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new RuntimeException("Pedido não encontrado com ID: " + pedidoId);
        }
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + pedidoId));
    }

    private DadosRastreamento converterDtoParaEntity(DadosRastreamentoDto dto) {
        DadosRastreamento dados = new DadosRastreamento();
        dados.setCodigoRastreamento(dto.getCodigoRastreamento());
        dados.setTransportadora(dto.getTransportadora());
        dados.setLocalizacaoAtual(dto.getLocalizacaoAtual());
        return dados;
    }

    private DadosRastreamentoDto converterEntityParaDto(DadosRastreamento dados) {
        DadosRastreamentoDto dto = new DadosRastreamentoDto();
        dto.setId(dados.getId());
        dto.setPedidoId(dados.getPedido().getId());
        dto.setCodigoRastreamento(dados.getCodigoRastreamento());
        dto.setTransportadora(dados.getTransportadora());
        dto.setUltimaAtualizacao(dados.getUltimaAtualizacao().format(FORMATTER));
        dto.setLocalizacaoAtual(dados.getLocalizacaoAtual());
        
        // Campos adicionais para exibição
        dto.setNumeroPedido(dados.getPedido().getNumeroPedido());
        dto.setNomeCliente(dados.getPedido().getCliente().getNome());
        dto.setStatusPedido(dados.getPedido().getStatus().name());
        dto.setAtualizadoRecentemente(dados.isAtualizadoRecentemente());
        
        return dto;
    }

    private void atualizarCamposDados(DadosRastreamento dados, DadosRastreamentoDto dto) {
        dados.setCodigoRastreamento(dto.getCodigoRastreamento());
        dados.setTransportadora(dto.getTransportadora());
        dados.setLocalizacaoAtual(dto.getLocalizacaoAtual());
    }
}
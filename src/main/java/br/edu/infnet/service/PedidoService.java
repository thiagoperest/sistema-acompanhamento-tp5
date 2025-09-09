package br.edu.infnet.service;

import br.edu.infnet.model.dto.HistoricoStatusDto;
import br.edu.infnet.model.dto.PedidoDto;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.HistoricoStatus;
import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.model.enums.StatusPedido;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.HistoricoStatusRepository;
import br.edu.infnet.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para regras de negócio relacionadas aos Pedidos
 */
@Service
@Transactional
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Cria um novo pedido
     */
    public PedidoDto criarPedido(PedidoDto pedidoDto) {
        // Validações de negócio
        validarDadosObrigatorios(pedidoDto);
        validarCliente(pedidoDto.getClienteId());

        // Gerar número do pedido se não informado
        if (pedidoDto.getNumeroPedido() == null || pedidoDto.getNumeroPedido().trim().isEmpty()) {
            pedidoDto.setNumeroPedido(gerarNumeroPedido());
        } else {
            validarNumeroPedidoUnico(pedidoDto.getNumeroPedido(), null);
        }

        // Converter DTO para Entity
        Pedido pedido = converterDtoParaEntity(pedidoDto);

        // Salvar pedido
        Pedido pedidoSalvo = pedidoRepository.save(pedido);

        // Criar primeiro histórico de status
        criarHistoricoStatus(pedidoSalvo, StatusPedido.CONFIRMADO,
                "Pedido criado e confirmado", "SISTEMA");

        return converterEntityParaDto(pedidoSalvo);
    }

    /**
     * Busca pedido por ID
     */
    @Transactional(readOnly = true)
    public Optional<PedidoDto> buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca pedido por número
     */
    @Transactional(readOnly = true)
    public Optional<PedidoDto> buscarPorNumeroPedido(String numeroPedido) {
        return pedidoRepository.findByNumeroPedido(numeroPedido)
                .map(this::converterEntityParaDto);
    }

    /**
     * Lista pedidos por cliente
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> listarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.findByClienteIdOrderByDataCompraDesc(clienteId)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista todos os pedidos (para atendentes)
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> listarTodosPedidos() {
        return pedidoRepository.findAll()
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista pedidos por status
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> listarPedidosPorStatus(StatusPedido status) {
        return pedidoRepository.findByStatusOrderByDataCompraDesc(status)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza status do pedido
     */
    public PedidoDto atualizarStatus(Long pedidoId, StatusPedido novoStatus,
                                     String observacao, String responsavel) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + pedidoId));

        // Validar transição de status
        validarTransicaoStatus(pedido.getStatus(), novoStatus);

        // Atualizar status do pedido
        pedido.setStatus(novoStatus);

        // Criar histórico
        criarHistoricoStatus(pedido, novoStatus, observacao, responsavel);

        // Salvar alterações
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);

        return converterEntityParaDto(pedidoAtualizado);
    }

    /**
     * Cancela pedido
     */
    public PedidoDto cancelarPedido(Long pedidoId, String motivo, String responsavel) {
        Pedido pedido = pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + pedidoId));

        if (!pedido.podeSerCancelado()) {
            throw new RuntimeException("Pedido não pode ser cancelado. Status atual: " +
                    pedido.getStatus().getDescricao());
        }

        return atualizarStatus(pedidoId, StatusPedido.CANCELADO,
                "Cancelado. Motivo: " + motivo, responsavel);
    }

    /**
     * Busca pedidos por filtros
     */
    @Transactional(readOnly = true)
    public List<PedidoDto> buscarComFiltros(Long clienteId, StatusPedido status,
                                            String numeroPedido, String nomeCliente) {
        List<Pedido> pedidos;

        if (numeroPedido != null && !numeroPedido.trim().isEmpty()) {
            pedidos = pedidoRepository.findByNumeroPedidoContainingIgnoreCase(numeroPedido);
        } else if (nomeCliente != null && !nomeCliente.trim().isEmpty()) {
            pedidos = pedidoRepository.findByClienteNomeContainingIgnoreCase(nomeCliente);
        } else if (clienteId != null && status != null) {
            pedidos = pedidoRepository.findByClienteIdAndStatus(clienteId, status);
        } else if (clienteId != null) {
            pedidos = pedidoRepository.findByClienteIdOrderByDataCompraDesc(clienteId);
        } else if (status != null) {
            pedidos = pedidoRepository.findByStatusOrderByDataCompraDesc(status);
        } else {
            pedidos = pedidoRepository.findAll();
        }

        return pedidos.stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca histórico de um pedido
     */
    @Transactional(readOnly = true)
    public List<HistoricoStatusDto> buscarHistoricoPedido(Long pedidoId) {
        return historicoStatusRepository.findByPedidoIdOrderByDataAtualizacaoDesc(pedidoId)
                .stream()
                .map(this::converterHistoricoParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Conta pedidos por status
     */
    @Transactional(readOnly = true)
    public long contarPedidosPorStatus(StatusPedido status) {
        return pedidoRepository.countByStatus(status);
    }

    /**
     * Conta pedidos por cliente
     */
    @Transactional(readOnly = true)
    public long contarPedidosPorCliente(Long clienteId) {
        return pedidoRepository.countByClienteId(clienteId);
    }

    private void validarDadosObrigatorios(PedidoDto dto) {
        if (dto.getClienteId() == null) {
            throw new RuntimeException("Cliente é obrigatório");
        }
        if (dto.getValor() == null || dto.getValor().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor deve ser maior que zero");
        }
    }

    private void validarCliente(Long clienteId) {
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));

        if (!cliente.getAtivo()) {
            throw new RuntimeException("Cliente está desativado");
        }
    }

    private void validarNumeroPedidoUnico(String numeroPedido, Long idExcluir) {
        boolean numeroPedidoJaExiste = (idExcluir == null) ?
                pedidoRepository.existsByNumeroPedido(numeroPedido) :
                pedidoRepository.existsByNumeroPedidoAndIdNot(numeroPedido, idExcluir);

        if (numeroPedidoJaExiste) {
            throw new RuntimeException("Número do pedido já existe: " + numeroPedido);
        }
    }

    private String gerarNumeroPedido() {
        String prefixo = "PED";
        long sequencial = pedidoRepository.count() + 1;
        return String.format("%s%08d", prefixo, sequencial);
    }

    private void validarTransicaoStatus(StatusPedido statusAtual, StatusPedido novoStatus) {
        // Regras de transição de status
        switch (statusAtual) {
            case CONFIRMADO:
                if (novoStatus != StatusPedido.PROCESSANDO && novoStatus != StatusPedido.CANCELADO) {
                    throw new RuntimeException("Transição inválida: " + statusAtual + " -> " + novoStatus);
                }
                break;
            case PROCESSANDO:
                if (novoStatus != StatusPedido.ENVIADO && novoStatus != StatusPedido.CANCELADO) {
                    throw new RuntimeException("Transição inválida: " + statusAtual + " -> " + novoStatus);
                }
                break;
            case ENVIADO:
                if (novoStatus != StatusPedido.EM_TRANSITO) {
                    throw new RuntimeException("Transição inválida: " + statusAtual + " -> " + novoStatus);
                }
                break;
            case EM_TRANSITO:
                if (novoStatus != StatusPedido.SAIU_PARA_ENTREGA) {
                    throw new RuntimeException("Transição inválida: " + statusAtual + " -> " + novoStatus);
                }
                break;
            case SAIU_PARA_ENTREGA:
                if (novoStatus != StatusPedido.ENTREGUE && novoStatus != StatusPedido.DEVOLVIDO) {
                    throw new RuntimeException("Transição inválida: " + statusAtual + " -> " + novoStatus);
                }
                break;
            case ENTREGUE:
            case CANCELADO:
            case DEVOLVIDO:
                throw new RuntimeException("Pedido em status final não pode ser alterado: " + statusAtual);
        }
    }

    private void criarHistoricoStatus(Pedido pedido, StatusPedido status,
                                      String observacao, String responsavel) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setPedido(pedido);
        historico.setStatus(status);
        historico.setObservacao(observacao);
        historico.setResponsavel(responsavel);

        historicoStatusRepository.save(historico);
    }

    private Pedido converterDtoParaEntity(PedidoDto dto) {
        Cliente cliente = clienteRepository.findById(dto.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        Pedido pedido = new Pedido();
        pedido.setNumeroPedido(dto.getNumeroPedido());
        pedido.setCliente(cliente);
        pedido.setValor(dto.getValor());
        pedido.setObservacoes(dto.getObservacoes());

        if (dto.getPrevisaoEntrega() != null && !dto.getPrevisaoEntrega().trim().isEmpty()) {
            pedido.setPrevisaoEntrega(LocalDate.parse(dto.getPrevisaoEntrega(), DATE_FORMATTER));
        }

        return pedido;
    }

    private PedidoDto converterEntityParaDto(Pedido pedido) {
        PedidoDto dto = new PedidoDto();
        dto.setId(pedido.getId());
        dto.setNumeroPedido(pedido.getNumeroPedido());
        dto.setClienteId(pedido.getCliente().getId());
        dto.setNomeCliente(pedido.getCliente().getNome());
        dto.setEmailCliente(pedido.getCliente().getEmail());
        dto.setDataCompra(pedido.getDataCompra().format(DATETIME_FORMATTER));
        dto.setValor(pedido.getValor());
        dto.setStatus(pedido.getStatus());
        dto.setObservacoes(pedido.getObservacoes());

        if (pedido.getPrevisaoEntrega() != null) {
            dto.setPrevisaoEntrega(pedido.getPrevisaoEntrega().format(DATE_FORMATTER));
        }

        dto.setDiasDesdeCompra(pedido.diasDesdeCompra());
        dto.setPodeSerCancelado(pedido.podeSerCancelado());
        dto.setIsEntregue(pedido.isEntregue());

        List<HistoricoStatusDto> historico = historicoStatusRepository
                .findByPedidoIdOrderByDataAtualizacaoDesc(pedido.getId())
                .stream()
                .map(this::converterHistoricoParaDto)
                .collect(Collectors.toList());
        dto.setHistoricoStatus(historico);

        return dto;
    }

    private HistoricoStatusDto converterHistoricoParaDto(HistoricoStatus historico) {
        HistoricoStatusDto dto = new HistoricoStatusDto();
        dto.setId(historico.getId());
        dto.setPedidoId(historico.getPedido().getId());
        dto.setStatus(historico.getStatus());
        dto.setDataAtualizacao(historico.getDataAtualizacao().format(DATETIME_FORMATTER));
        dto.setObservacao(historico.getObservacao());
        dto.setResponsavel(historico.getResponsavel());
        return dto;
    }
}
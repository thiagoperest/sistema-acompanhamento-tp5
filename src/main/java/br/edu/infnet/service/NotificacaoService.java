package br.edu.infnet.service;

import br.edu.infnet.model.dto.NotificacaoDto;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.Notificacao;
import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.model.enums.TipoNotificacao;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.NotificacaoRepository;
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
 * Service para regras de negócio relacionadas às Notificações
 */
@Service
@Transactional
public class NotificacaoService {

    @Autowired
    private NotificacaoRepository notificacaoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private PreferenciasNotificacaoService preferenciasService;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    /**
     * Cria uma nova notificação
     */
    public NotificacaoDto criarNotificacao(NotificacaoDto notificacaoDto) {
        validarDadosObrigatorios(notificacaoDto);
        
        Cliente cliente = buscarClientePorId(notificacaoDto.getClienteId());
        Pedido pedido = buscarPedidoPorId(notificacaoDto.getPedidoId());
        
        Notificacao notificacao = converterDtoParaEntity(notificacaoDto);
        notificacao.setCliente(cliente);
        notificacao.setPedido(pedido);
        
        Notificacao notificacaoSalva = notificacaoRepository.save(notificacao);
        return converterEntityParaDto(notificacaoSalva);
    }

    /**
     * Busca notificação por ID
     */
    public Optional<NotificacaoDto> buscarPorId(Long id) {
        return notificacaoRepository.findById(id)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca todas as notificações de um cliente
     */
    public List<NotificacaoDto> buscarPorCliente(Long clienteId) {
        validarClienteExiste(clienteId);
        return notificacaoRepository.findByClienteIdOrderByDataEnvioDesc(clienteId)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca notificações não lidas de um cliente
     */
    public List<NotificacaoDto> buscarNaoLidasPorCliente(Long clienteId) {
        validarClienteExiste(clienteId);
        return notificacaoRepository.findByClienteIdAndLidaFalseOrderByDataEnvioDesc(clienteId)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca notificações por pedido
     */
    public List<NotificacaoDto> buscarPorPedido(Long pedidoId) {
        validarPedidoExiste(pedidoId);
        return notificacaoRepository.findByPedidoIdOrderByDataEnvioDesc(pedidoId)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Busca notificações por tipo
     */
    public List<NotificacaoDto> buscarPorTipo(TipoNotificacao tipo) {
        return notificacaoRepository.findByTipoOrderByDataEnvioDesc(tipo)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Marca notificação como lida
     */
    public NotificacaoDto marcarComoLida(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));
        
        notificacao.marcarComoLida();
        Notificacao notificacaoAtualizada = notificacaoRepository.save(notificacao);
        
        return converterEntityParaDto(notificacaoAtualizada);
    }

    /**
     * Marca notificação como não lida
     */
    public NotificacaoDto marcarComoNaoLida(Long id) {
        Notificacao notificacao = notificacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificação não encontrada com ID: " + id));
        
        notificacao.marcarComoNaoLida();
        Notificacao notificacaoAtualizada = notificacaoRepository.save(notificacao);
        
        return converterEntityParaDto(notificacaoAtualizada);
    }

    /**
     * Conta notificações não lidas por cliente
     */
    public long contarNaoLidasPorCliente(Long clienteId) {
        validarClienteExiste(clienteId);
        return notificacaoRepository.countNotificacaoesNaoLidasPorCliente(clienteId);
    }

    /**
     * Busca notificações recentes (últimas 24h)
     */
    public List<NotificacaoDto> buscarRecentes() {
        LocalDateTime dataLimite = LocalDateTime.now().minusDays(1);
        return notificacaoRepository.findNotificacoesRecentes(dataLimite)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Envia notificação para cliente com base em suas preferências
     */
    public NotificacaoDto enviarNotificacao(Long pedidoId, String titulo, String mensagem) {
        Pedido pedido = buscarPedidoPorId(pedidoId);
        Cliente cliente = pedido.getCliente();
        
        // Verifica as preferências do cliente
        var preferencias = preferenciasService.buscarPorCliente(cliente.getId());
        
        if (preferencias.isEmpty() || !preferencias.get().getTemAlgumaNotificacaoAtiva()) {
            throw new RuntimeException("Cliente não possui preferências de notificação ativas");
        }
        
        var prefs = preferencias.get();
        if (!prefs.getHorarioPermitido()) {
            throw new RuntimeException("Fora do horário permitido para notificações do cliente");
        }
        
        // Determina o tipo de notificação preferencial
        TipoNotificacao tipo = determinarTipoPreferencial(prefs);
        
        NotificacaoDto notificacaoDto = new NotificacaoDto();
        notificacaoDto.setPedidoId(pedidoId);
        notificacaoDto.setClienteId(cliente.getId());
        notificacaoDto.setTipo(tipo);
        notificacaoDto.setTitulo(titulo);
        notificacaoDto.setMensagem(mensagem);
        
        return criarNotificacao(notificacaoDto);
    }

    /**
     * Remove notificação
     */
    public void removerNotificacao(Long id) {
        if (!notificacaoRepository.existsById(id)) {
            throw new RuntimeException("Notificação não encontrada com ID: " + id);
        }
        notificacaoRepository.deleteById(id);
    }

    private void validarDadosObrigatorios(NotificacaoDto dto) {
        if (dto.getPedidoId() == null) {
            throw new RuntimeException("ID do pedido é obrigatório");
        }
        if (dto.getClienteId() == null) {
            throw new RuntimeException("ID do cliente é obrigatório");
        }
        if (dto.getTipo() == null) {
            throw new RuntimeException("Tipo de notificação é obrigatório");
        }
        if (dto.getTitulo() == null || dto.getTitulo().trim().isEmpty()) {
            throw new RuntimeException("Título é obrigatório");
        }
        if (dto.getMensagem() == null || dto.getMensagem().trim().isEmpty()) {
            throw new RuntimeException("Mensagem é obrigatória");
        }
    }

    private void validarClienteExiste(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + clienteId);
        }
    }

    private void validarPedidoExiste(Long pedidoId) {
        if (!pedidoRepository.existsById(pedidoId)) {
            throw new RuntimeException("Pedido não encontrado com ID: " + pedidoId);
        }
    }

    private Cliente buscarClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
    }

    private Pedido buscarPedidoPorId(Long pedidoId) {
        return pedidoRepository.findById(pedidoId)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado com ID: " + pedidoId));
    }

    private TipoNotificacao determinarTipoPreferencial(br.edu.infnet.model.dto.PreferenciasNotificacaoDto prefs) {
        if (prefs.getEmailAtivo()) return TipoNotificacao.EMAIL;
        if (prefs.getPushAtivo()) return TipoNotificacao.PUSH;
        if (prefs.getSmsAtivo()) return TipoNotificacao.SMS;
        return TipoNotificacao.EMAIL; // fallback
    }

    private Notificacao converterDtoParaEntity(NotificacaoDto dto) {
        Notificacao notificacao = new Notificacao();
        notificacao.setTipo(dto.getTipo());
        notificacao.setTitulo(dto.getTitulo());
        notificacao.setMensagem(dto.getMensagem());
        return notificacao;
    }

    private NotificacaoDto converterEntityParaDto(Notificacao notificacao) {
        NotificacaoDto dto = new NotificacaoDto();
        dto.setId(notificacao.getId());
        dto.setPedidoId(notificacao.getPedido().getId());
        dto.setClienteId(notificacao.getCliente().getId());
        dto.setTipo(notificacao.getTipo());
        dto.setTitulo(notificacao.getTitulo());
        dto.setMensagem(notificacao.getMensagem());
        dto.setDataEnvio(notificacao.getDataEnvio().format(FORMATTER));
        dto.setLida(notificacao.getLida());
        
        // Campos adicionais para exibição
        dto.setNumeroPedido(notificacao.getPedido().getNumeroPedido());
        dto.setNomeCliente(notificacao.getCliente().getNome());
        dto.setTipoDescricao(notificacao.getTipo().getDescricao());
        
        return dto;
    }
}
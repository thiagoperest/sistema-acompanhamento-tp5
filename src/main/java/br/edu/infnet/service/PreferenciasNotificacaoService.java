package br.edu.infnet.service;

import br.edu.infnet.model.dto.PreferenciasNotificacaoDto;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.PreferenciasNotificacao;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.PreferenciasNotificacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service para regras de negócio relacionadas às Preferências de Notificação
 */
@Service
@Transactional
public class PreferenciasNotificacaoService {

    @Autowired
    private PreferenciasNotificacaoRepository preferenciasRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Cria preferências de notificação para um cliente
     */
    public PreferenciasNotificacaoDto criarPreferencias(PreferenciasNotificacaoDto preferenciasDto) {
        validarDadosObrigatorios(preferenciasDto);
        
        Cliente cliente = buscarClientePorId(preferenciasDto.getClienteId());
        
        // Verifica se o cliente já tem preferências
        if (preferenciasRepository.existsByClienteId(preferenciasDto.getClienteId())) {
            throw new RuntimeException("Cliente já possui preferências de notificação configuradas");
        }
        
        PreferenciasNotificacao preferencias = converterDtoParaEntity(preferenciasDto);
        preferencias.setCliente(cliente);
        
        PreferenciasNotificacao preferenciasSalvas = preferenciasRepository.save(preferencias);
        return converterEntityParaDto(preferenciasSalvas);
    }

    /**
     * Busca preferências por ID
     */
    public Optional<PreferenciasNotificacaoDto> buscarPorId(Long id) {
        return preferenciasRepository.findById(id)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca preferências por cliente
     */
    public Optional<PreferenciasNotificacaoDto> buscarPorCliente(Long clienteId) {
        validarClienteExiste(clienteId);
        return preferenciasRepository.findByClienteId(clienteId)
                .map(this::converterEntityParaDto);
    }

    /**
     * Busca ou cria preferências padrão para um cliente
     */
    public PreferenciasNotificacaoDto buscarOuCriarPreferencias(Long clienteId) {
        Optional<PreferenciasNotificacaoDto> preferencias = buscarPorCliente(clienteId);
        
        if (preferencias.isPresent()) {
            return preferencias.get();
        }
        
        // Cria preferências padrão
        PreferenciasNotificacaoDto novasPreferencias = new PreferenciasNotificacaoDto();
        novasPreferencias.setClienteId(clienteId);
        novasPreferencias.setEmailAtivo(true);
        novasPreferencias.setSmsAtivo(false);
        novasPreferencias.setPushAtivo(true);
        novasPreferencias.setHorarioInicio("08:00");
        novasPreferencias.setHorarioFim("22:00");
        
        return criarPreferencias(novasPreferencias);
    }

    /**
     * Atualiza preferências de notificação
     */
    public PreferenciasNotificacaoDto atualizarPreferencias(Long id, PreferenciasNotificacaoDto preferenciasDto) {
        PreferenciasNotificacao preferenciasExistentes = preferenciasRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Preferências não encontradas com ID: " + id));

        validarDadosObrigatorios(preferenciasDto);
        validarHorariosValidos(preferenciasDto);
        
        atualizarCamposPreferencias(preferenciasExistentes, preferenciasDto);
        
        PreferenciasNotificacao preferenciasAtualizadas = preferenciasRepository.save(preferenciasExistentes);
        return converterEntityParaDto(preferenciasAtualizadas);
    }

    /**
     * Ativa todas as notificações para um cliente
     */
    public PreferenciasNotificacaoDto ativarTodasNotificacoes(Long clienteId) {
        PreferenciasNotificacao preferencias = buscarPreferenciasPorCliente(clienteId);
        
        preferencias.ativarTodasNotificacoes();
        
        PreferenciasNotificacao preferenciasAtualizadas = preferenciasRepository.save(preferencias);
        return converterEntityParaDto(preferenciasAtualizadas);
    }

    /**
     * Desativa todas as notificações para um cliente
     */
    public PreferenciasNotificacaoDto desativarTodasNotificacoes(Long clienteId) {
        PreferenciasNotificacao preferencias = buscarPreferenciasPorCliente(clienteId);
        
        preferencias.desativarTodasNotificacoes();
        
        PreferenciasNotificacao preferenciasAtualizadas = preferenciasRepository.save(preferencias);
        return converterEntityParaDto(preferenciasAtualizadas);
    }

    /**
     * Define horário comercial (8h às 18h)
     */
    public PreferenciasNotificacaoDto definirHorarioComercial(Long clienteId) {
        PreferenciasNotificacao preferencias = buscarPreferenciasPorCliente(clienteId);
        
        preferencias.definirHorarioComercial();
        
        PreferenciasNotificacao preferenciasAtualizadas = preferenciasRepository.save(preferencias);
        return converterEntityParaDto(preferenciasAtualizadas);
    }

    /**
     * Define horário estendido (8h às 22h)
     */
    public PreferenciasNotificacaoDto definirHorarioEstendido(Long clienteId) {
        PreferenciasNotificacao preferencias = buscarPreferenciasPorCliente(clienteId);
        
        preferencias.definirHorarioEstendido();
        
        PreferenciasNotificacao preferenciasAtualizadas = preferenciasRepository.save(preferencias);
        return converterEntityParaDto(preferenciasAtualizadas);
    }

    /**
     * Lista clientes com alguma notificação ativa
     */
    public List<PreferenciasNotificacaoDto> listarClientesComNotificacaoAtiva() {
        return preferenciasRepository.findComAlgumaNotificacaoAtiva()
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Lista clientes disponíveis para notificação no horário atual
     */
    public List<PreferenciasNotificacaoDto> listarClientesDisponiveis() {
        LocalTime agora = LocalTime.now();
        return preferenciasRepository.findClientesDisponiveis(agora)
                .stream()
                .map(this::converterEntityParaDto)
                .collect(Collectors.toList());
    }

    /**
     * Remove preferências
     */
    public void removerPreferencias(Long id) {
        if (!preferenciasRepository.existsById(id)) {
            throw new RuntimeException("Preferências não encontradas com ID: " + id);
        }
        preferenciasRepository.deleteById(id);
    }

    /**
     * Conta clientes por tipo de notificação ativa
     */
    public long contarClientesPorTipo(String tipo) {
        return switch (tipo.toLowerCase()) {
            case "email" -> preferenciasRepository.countByEmailAtivoTrue();
            case "sms" -> preferenciasRepository.countBySmsAtivoTrue();
            case "push" -> preferenciasRepository.countByPushAtivoTrue();
            default -> 0L;
        };
    }

    private void validarDadosObrigatorios(PreferenciasNotificacaoDto dto) {
        if (dto.getClienteId() == null) {
            throw new RuntimeException("ID do cliente é obrigatório");
        }
        if (dto.getEmailAtivo() == null) {
            throw new RuntimeException("Status do email é obrigatório");
        }
        if (dto.getSmsAtivo() == null) {
            throw new RuntimeException("Status do SMS é obrigatório");
        }
        if (dto.getPushAtivo() == null) {
            throw new RuntimeException("Status do push é obrigatório");
        }
    }

    private void validarHorariosValidos(PreferenciasNotificacaoDto dto) {
        if (dto.getHorarioInicio() != null && dto.getHorarioFim() != null) {
            try {
                LocalTime inicio = LocalTime.parse(dto.getHorarioInicio(), TIME_FORMATTER);
                LocalTime fim = LocalTime.parse(dto.getHorarioFim(), TIME_FORMATTER);
                
                if (inicio.isAfter(fim)) {
                    throw new RuntimeException("Horário de início deve ser anterior ao horário de fim");
                }
            } catch (Exception e) {
                throw new RuntimeException("Formato de horário inválido. Use o formato HH:mm");
            }
        }
    }

    private void validarClienteExiste(Long clienteId) {
        if (!clienteRepository.existsById(clienteId)) {
            throw new RuntimeException("Cliente não encontrado com ID: " + clienteId);
        }
    }

    private Cliente buscarClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado com ID: " + clienteId));
    }

    private PreferenciasNotificacao buscarPreferenciasPorCliente(Long clienteId) {
        return preferenciasRepository.findByClienteId(clienteId)
                .orElseThrow(() -> new RuntimeException("Preferências não encontradas para cliente ID: " + clienteId));
    }

    private PreferenciasNotificacao converterDtoParaEntity(PreferenciasNotificacaoDto dto) {
        PreferenciasNotificacao preferencias = new PreferenciasNotificacao();
        preferencias.setEmailAtivo(dto.getEmailAtivo());
        preferencias.setSmsAtivo(dto.getSmsAtivo());
        preferencias.setPushAtivo(dto.getPushAtivo());
        
        if (dto.getHorarioInicio() != null) {
            preferencias.setHorarioInicio(LocalTime.parse(dto.getHorarioInicio(), TIME_FORMATTER));
        }
        if (dto.getHorarioFim() != null) {
            preferencias.setHorarioFim(LocalTime.parse(dto.getHorarioFim(), TIME_FORMATTER));
        }
        
        return preferencias;
    }

    private PreferenciasNotificacaoDto converterEntityParaDto(PreferenciasNotificacao preferencias) {
        PreferenciasNotificacaoDto dto = new PreferenciasNotificacaoDto();
        dto.setId(preferencias.getId());
        dto.setClienteId(preferencias.getCliente().getId());
        dto.setEmailAtivo(preferencias.getEmailAtivo());
        dto.setSmsAtivo(preferencias.getSmsAtivo());
        dto.setPushAtivo(preferencias.getPushAtivo());
        dto.setHorarioInicio(preferencias.getHorarioInicio().format(TIME_FORMATTER));
        dto.setHorarioFim(preferencias.getHorarioFim().format(TIME_FORMATTER));
        
        // Campos adicionais para exibição
        dto.setNomeCliente(preferencias.getCliente().getNome());
        dto.setEmailCliente(preferencias.getCliente().getEmail());
        dto.setTemAlgumaNotificacaoAtiva(preferencias.temAlgumaNotificacaoAtiva());
        dto.setHorarioPermitido(preferencias.isHorarioPermitido());
        
        return dto;
    }

    private void atualizarCamposPreferencias(PreferenciasNotificacao preferencias, PreferenciasNotificacaoDto dto) {
        preferencias.setEmailAtivo(dto.getEmailAtivo());
        preferencias.setSmsAtivo(dto.getSmsAtivo());
        preferencias.setPushAtivo(dto.getPushAtivo());
        
        if (dto.getHorarioInicio() != null) {
            preferencias.setHorarioInicio(LocalTime.parse(dto.getHorarioInicio(), TIME_FORMATTER));
        }
        if (dto.getHorarioFim() != null) {
            preferencias.setHorarioFim(LocalTime.parse(dto.getHorarioFim(), TIME_FORMATTER));
        }
    }
}
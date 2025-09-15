package br.edu.infnet.controller;

import br.edu.infnet.model.dto.PreferenciasNotificacaoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.service.PreferenciasNotificacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Controller REST para operações relacionadas às Preferências de Notificação
 */
@RestController
@RequestMapping("/preferencias-notificacao")
@Tag(name = "Preferências de Notificação", description = "Operações relacionadas ao gerenciamento de preferências de notificação")
public class PreferenciasNotificacaoController {

    @Autowired
    private PreferenciasNotificacaoService preferenciasService;

    /**
     * Cria preferências de notificação para um cliente
     * POST /api/preferencias-notificacao
     */
    @PostMapping
    @Operation(summary = "Criar preferências de notificação", description = "Cria preferências de notificação para um cliente")
    @ApiResponse(responseCode = "201", description = "Preferências criadas com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> criarPreferencias(
            @Valid @RequestBody PreferenciasNotificacaoDto preferenciasDto) {

        try {
            PreferenciasNotificacaoDto preferenciasCriadas = preferenciasService.criarPreferencias(preferenciasDto);

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                    "Preferências de notificação criadas com sucesso! ID: " + preferenciasCriadas.getId(),
                    preferenciasCriadas
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca preferências por ID
     * GET /api/preferencias-notificacao/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar preferências por ID", description = "Retorna as preferências de notificação por ID")
    @ApiResponse(responseCode = "200", description = "Preferências encontradas")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> buscarPorId(
            @Parameter(description = "ID das preferências") @PathVariable Long id) {

        try {
            Optional<PreferenciasNotificacaoDto> preferencias = preferenciasService.buscarPorId(id);

            if (preferencias.isPresent()) {
                ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                        "Preferências encontradas",
                        preferencias.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Preferências não encontradas com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca preferências por cliente
     * GET /api/preferencias-notificacao/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar preferências por cliente", description = "Retorna as preferências de notificação de um cliente")
    @ApiResponse(responseCode = "200", description = "Preferências encontradas")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> buscarPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            Optional<PreferenciasNotificacaoDto> preferencias = preferenciasService.buscarPorCliente(clienteId);

            if (preferencias.isPresent()) {
                ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                        "Preferências do cliente encontradas",
                        preferencias.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Preferências não encontradas para cliente ID: " + clienteId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca ou cria preferências padrão para um cliente
     * GET /api/preferencias-notificacao/cliente/{clienteId}/buscar-ou-criar
     */
    @GetMapping("/cliente/{clienteId}/buscar-ou-criar")
    @Operation(summary = "Buscar ou criar preferências", description = "Retorna preferências existentes ou cria padrão")
    @ApiResponse(responseCode = "200", description = "Preferências encontradas ou criadas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> buscarOuCriarPreferencias(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            PreferenciasNotificacaoDto preferencias = preferenciasService.buscarOuCriarPreferencias(clienteId);

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                    "Preferências recuperadas ou criadas com sucesso",
                    preferencias
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualiza preferências de notificação
     * PUT /api/preferencias-notificacao/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar preferências", description = "Atualiza as preferências de notificação")
    @ApiResponse(responseCode = "200", description = "Preferências atualizadas com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> atualizarPreferencias(
            @Parameter(description = "ID das preferências") @PathVariable Long id,
            @Valid @RequestBody PreferenciasNotificacaoDto preferenciasDto) {

        try {
            PreferenciasNotificacaoDto preferenciasAtualizadas = preferenciasService.atualizarPreferencias(id, preferenciasDto);

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                    "Preferências atualizadas com sucesso",
                    preferenciasAtualizadas
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontradas") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Ativa todas as notificações para um cliente
     * PUT /api/preferencias-notificacao/cliente/{clienteId}/ativar-todas
     */
    @PutMapping("/cliente/{clienteId}/ativar-todas")
    @Operation(summary = "Ativar todas as notificações", description = "Ativa todos os tipos de notificação para o cliente")
    @ApiResponse(responseCode = "200", description = "Todas as notificações ativadas")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> ativarTodasNotificacoes(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            PreferenciasNotificacaoDto preferencias = preferenciasService.ativarTodasNotificacoes(clienteId);

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                    "Todas as notificações foram ativadas",
                    preferencias
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Desativa todas as notificações para um cliente
     * PUT /api/preferencias-notificacao/cliente/{clienteId}/desativar-todas
     */
    @PutMapping("/cliente/{clienteId}/desativar-todas")
    @Operation(summary = "Desativar todas as notificações", description = "Desativa todos os tipos de notificação para o cliente")
    @ApiResponse(responseCode = "200", description = "Todas as notificações desativadas")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> desativarTodasNotificacoes(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            PreferenciasNotificacaoDto preferencias = preferenciasService.desativarTodasNotificacoes(clienteId);

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                    "Todas as notificações foram desativadas",
                    preferencias
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Define horário comercial (8h às 18h)
     * PUT /api/preferencias-notificacao/cliente/{clienteId}/horario-comercial
     */
    @PutMapping("/cliente/{clienteId}/horario-comercial")
    @Operation(summary = "Definir horário comercial", description = "Define horário comercial (8h às 18h) para notificações")
    @ApiResponse(responseCode = "200", description = "Horário comercial definido")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> definirHorarioComercial(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            PreferenciasNotificacaoDto preferencias = preferenciasService.definirHorarioComercial(clienteId);

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                    "Horário comercial (8h às 18h) definido com sucesso",
                    preferencias
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Define horário estendido (8h às 22h)
     * PUT /api/preferencias-notificacao/cliente/{clienteId}/horario-estendido
     */
    @PutMapping("/cliente/{clienteId}/horario-estendido")
    @Operation(summary = "Definir horário estendido", description = "Define horário estendido (8h às 22h) para notificações")
    @ApiResponse(responseCode = "200", description = "Horário estendido definido")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> definirHorarioEstendido(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            PreferenciasNotificacaoDto preferencias = preferenciasService.definirHorarioEstendido(clienteId);

            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.sucesso(
                    "Horário estendido (8h às 22h) definido com sucesso",
                    preferencias
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<PreferenciasNotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista clientes com alguma notificação ativa
     * GET /api/preferencias-notificacao/clientes-ativos
     */
    @GetMapping("/clientes-ativos")
    @Operation(summary = "Listar clientes com notificações ativas", description = "Retorna clientes com pelo menos um tipo de notificação ativo")
    @ApiResponse(responseCode = "200", description = "Lista de clientes com notificações ativas")
    public ResponseEntity<ResponseDto<List<PreferenciasNotificacaoDto>>> listarClientesComNotificacaoAtiva() {

        try {
            List<PreferenciasNotificacaoDto> preferencias = preferenciasService.listarClientesComNotificacaoAtiva();

            ResponseDto<List<PreferenciasNotificacaoDto>> response = ResponseDto.sucesso(
                    "Clientes com notificações ativas recuperados. Total: " + preferencias.size(),
                    preferencias
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<PreferenciasNotificacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista clientes disponíveis para notificação no horário atual
     * GET /api/preferencias-notificacao/clientes-disponiveis
     */
    @GetMapping("/clientes-disponiveis")
    @Operation(summary = "Listar clientes disponíveis agora", description = "Retorna clientes que podem receber notificações no horário atual")
    @ApiResponse(responseCode = "200", description = "Lista de clientes disponíveis")
    public ResponseEntity<ResponseDto<List<PreferenciasNotificacaoDto>>> listarClientesDisponiveis() {

        try {
            List<PreferenciasNotificacaoDto> preferencias = preferenciasService.listarClientesDisponiveis();

            ResponseDto<List<PreferenciasNotificacaoDto>> response = ResponseDto.sucesso(
                    "Clientes disponíveis para notificação recuperados. Total: " + preferencias.size(),
                    preferencias
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<PreferenciasNotificacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Conta clientes por tipo de notificação ativa
     * GET /api/preferencias-notificacao/contar-por-tipo/{tipo}
     */
    @GetMapping("/contar-por-tipo/{tipo}")
    @Operation(summary = "Contar clientes por tipo", description = "Retorna quantidade de clientes com tipo de notificação ativo")
    @ApiResponse(responseCode = "200", description = "Contagem realizada")
    public ResponseEntity<ResponseDto<Long>> contarClientesPorTipo(
            @Parameter(description = "Tipo de notificação (email, sms, push)") @PathVariable String tipo) {

        try {
            long quantidade = preferenciasService.contarClientesPorTipo(tipo);

            ResponseDto<Long> response = ResponseDto.sucesso(
                    "Contagem de clientes com " + tipo + " ativo realizada",
                    quantidade
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<Long> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Remove preferências
     * DELETE /api/preferencias-notificacao/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover preferências", description = "Remove as preferências de notificação")
    @ApiResponse(responseCode = "200", description = "Preferências removidas com sucesso")
    @ApiResponse(responseCode = "404", description = "Preferências não encontradas")
    public ResponseEntity<ResponseDto<Void>> removerPreferencias(
            @Parameter(description = "ID das preferências") @PathVariable Long id) {

        try {
            preferenciasService.removerPreferencias(id);

            ResponseDto<Void> response = ResponseDto.sucesso("Preferências removidas com sucesso");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<Void> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<Void> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
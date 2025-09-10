package br.edu.infnet.controller;

import br.edu.infnet.model.dto.NotificacaoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.enums.TipoNotificacao;
import br.edu.infnet.service.NotificacaoService;
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
 * Controller REST para operações relacionadas às Notificações
 */
@RestController
@RequestMapping("/notificacoes")
@Tag(name = "Notificações", description = "Operações relacionadas ao gerenciamento de notificações")
public class NotificacaoController {

    @Autowired
    private NotificacaoService notificacaoService;

    /**
     * Cria uma nova notificação
     * POST /api/notificacoes
     */
    @PostMapping
    @Operation(summary = "Criar nova notificação", description = "Cria uma nova notificação para um cliente")
    @ApiResponse(responseCode = "201", description = "Notificação criada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<ResponseDto<NotificacaoDto>> criarNotificacao(
            @Valid @RequestBody NotificacaoDto notificacaoDto) {

        try {
            NotificacaoDto notificacaoCriada = notificacaoService.criarNotificacao(notificacaoDto);

            ResponseDto<NotificacaoDto> response = ResponseDto.sucesso(
                    "Notificação criada com sucesso! ID: " + notificacaoCriada.getId(),
                    notificacaoCriada
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            ResponseDto<NotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDto<NotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca notificação por ID
     * GET /api/notificacoes/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar notificação por ID", description = "Retorna os dados de uma notificação específica")
    @ApiResponse(responseCode = "200", description = "Notificação encontrada")
    @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    public ResponseEntity<ResponseDto<NotificacaoDto>> buscarPorId(
            @Parameter(description = "ID da notificação") @PathVariable Long id) {

        try {
            Optional<NotificacaoDto> notificacao = notificacaoService.buscarPorId(id);

            if (notificacao.isPresent()) {
                ResponseDto<NotificacaoDto> response = ResponseDto.sucesso(
                        "Notificação encontrada",
                        notificacao.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<NotificacaoDto> response = ResponseDto.erro("Notificação não encontrada com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<NotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca notificações por cliente
     * GET /api/notificacoes/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar notificações por cliente", description = "Retorna todas as notificações de um cliente")
    @ApiResponse(responseCode = "200", description = "Lista de notificações")
    public ResponseEntity<ResponseDto<List<NotificacaoDto>>> buscarPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarPorCliente(clienteId);

            ResponseDto<List<NotificacaoDto>> response = ResponseDto.sucesso(
                    "Notificações recuperadas com sucesso. Total: " + notificacoes.size(),
                    notificacoes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<NotificacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca notificações não lidas por cliente
     * GET /api/notificacoes/cliente/{clienteId}/nao-lidas
     */
    @GetMapping("/cliente/{clienteId}/nao-lidas")
    @Operation(summary = "Buscar notificações não lidas", description = "Retorna notificações não lidas de um cliente")
    @ApiResponse(responseCode = "200", description = "Lista de notificações não lidas")
    public ResponseEntity<ResponseDto<List<NotificacaoDto>>> buscarNaoLidasPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarNaoLidasPorCliente(clienteId);

            ResponseDto<List<NotificacaoDto>> response = ResponseDto.sucesso(
                    "Notificações não lidas recuperadas. Total: " + notificacoes.size(),
                    notificacoes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<NotificacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca notificações por pedido
     * GET /api/notificacoes/pedido/{pedidoId}
     */
    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Buscar notificações por pedido", description = "Retorna todas as notificações de um pedido")
    @ApiResponse(responseCode = "200", description = "Lista de notificações")
    public ResponseEntity<ResponseDto<List<NotificacaoDto>>> buscarPorPedido(
            @Parameter(description = "ID do pedido") @PathVariable Long pedidoId) {

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarPorPedido(pedidoId);

            ResponseDto<List<NotificacaoDto>> response = ResponseDto.sucesso(
                    "Notificações do pedido recuperadas. Total: " + notificacoes.size(),
                    notificacoes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<NotificacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca notificações por tipo
     * GET /api/notificacoes/tipo/{tipo}
     */
    @GetMapping("/tipo/{tipo}")
    @Operation(summary = "Buscar notificações por tipo", description = "Retorna notificações de um tipo específico")
    @ApiResponse(responseCode = "200", description = "Lista de notificações")
    public ResponseEntity<ResponseDto<List<NotificacaoDto>>> buscarPorTipo(
            @Parameter(description = "Tipo da notificação") @PathVariable TipoNotificacao tipo) {

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarPorTipo(tipo);

            ResponseDto<List<NotificacaoDto>> response = ResponseDto.sucesso(
                    "Notificações por tipo recuperadas. Total: " + notificacoes.size(),
                    notificacoes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<NotificacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Marca notificação como lida
     * PUT /api/notificacoes/{id}/marcar-lida
     */
    @PutMapping("/{id}/marcar-lida")
    @Operation(summary = "Marcar notificação como lida", description = "Marca uma notificação como lida")
    @ApiResponse(responseCode = "200", description = "Notificação marcada como lida")
    @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    public ResponseEntity<ResponseDto<NotificacaoDto>> marcarComoLida(
            @Parameter(description = "ID da notificação") @PathVariable Long id) {

        try {
            NotificacaoDto notificacao = notificacaoService.marcarComoLida(id);

            ResponseDto<NotificacaoDto> response = ResponseDto.sucesso(
                    "Notificação marcada como lida",
                    notificacao
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<NotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<NotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Marca notificação como não lida
     * PUT /api/notificacoes/{id}/marcar-nao-lida
     */
    @PutMapping("/{id}/marcar-nao-lida")
    @Operation(summary = "Marcar notificação como não lida", description = "Marca uma notificação como não lida")
    @ApiResponse(responseCode = "200", description = "Notificação marcada como não lida")
    @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    public ResponseEntity<ResponseDto<NotificacaoDto>> marcarComoNaoLida(
            @Parameter(description = "ID da notificação") @PathVariable Long id) {

        try {
            NotificacaoDto notificacao = notificacaoService.marcarComoNaoLida(id);

            ResponseDto<NotificacaoDto> response = ResponseDto.sucesso(
                    "Notificação marcada como não lida",
                    notificacao
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<NotificacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<NotificacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Conta notificações não lidas por cliente
     * GET /api/notificacoes/cliente/{clienteId}/contar-nao-lidas
     */
    @GetMapping("/cliente/{clienteId}/contar-nao-lidas")
    @Operation(summary = "Contar notificações não lidas", description = "Retorna o número de notificações não lidas de um cliente")
    @ApiResponse(responseCode = "200", description = "Contagem realizada")
    public ResponseEntity<ResponseDto<Long>> contarNaoLidasPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            long quantidade = notificacaoService.contarNaoLidasPorCliente(clienteId);

            ResponseDto<Long> response = ResponseDto.sucesso(
                    "Contagem de notificações não lidas realizada",
                    quantidade
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<Long> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca notificações recentes
     * GET /api/notificacoes/recentes
     */
    @GetMapping("/recentes")
    @Operation(summary = "Buscar notificações recentes", description = "Retorna notificações das últimas 24 horas")
    @ApiResponse(responseCode = "200", description = "Lista de notificações recentes")
    public ResponseEntity<ResponseDto<List<NotificacaoDto>>> buscarRecentes() {

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarRecentes();

            ResponseDto<List<NotificacaoDto>> response = ResponseDto.sucesso(
                    "Notificações recentes recuperadas. Total: " + notificacoes.size(),
                    notificacoes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<NotificacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Remove notificação
     * DELETE /api/notificacoes/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover notificação", description = "Remove uma notificação do sistema")
    @ApiResponse(responseCode = "200", description = "Notificação removida com sucesso")
    @ApiResponse(responseCode = "404", description = "Notificação não encontrada")
    public ResponseEntity<ResponseDto<Void>> removerNotificacao(
            @Parameter(description = "ID da notificação") @PathVariable Long id) {

        try {
            notificacaoService.removerNotificacao(id);

            ResponseDto<Void> response = ResponseDto.sucesso("Notificação removida com sucesso");
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
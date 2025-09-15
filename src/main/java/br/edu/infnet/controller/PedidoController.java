package br.edu.infnet.controller;

import br.edu.infnet.model.dto.HistoricoStatusDto;
import br.edu.infnet.model.dto.PedidoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.enums.StatusPedido;
import br.edu.infnet.service.PedidoService;
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
import java.util.Map;
import java.util.Optional;

/**
 * Controller REST para operações relacionadas aos Pedidos
 */
@RestController
@RequestMapping("/pedidos")
@Tag(name = "Pedidos", description = "Operações relacionadas ao gerenciamento de pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    /**
     * Cria um novo pedido
     * POST /api/pedidos
     */
    @PostMapping
    @Operation(summary = "Criar novo pedido", description = "Cadastra um novo pedido no sistema")
    @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ResponseDto<PedidoDto>> criarPedido(
            @Valid @RequestBody PedidoDto pedidoDto) {

        try {
            PedidoDto pedidoCriado = pedidoService.criarPedido(pedidoDto);

            ResponseDto<PedidoDto> response = ResponseDto.sucesso(
                    "Pedido criado com sucesso! Número: " + pedidoCriado.getNumeroPedido(),
                    pedidoCriado
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            ResponseDto<PedidoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDto<PedidoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca pedido por ID
     * GET /api/pedidos/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pedido por ID", description = "Retorna os dados de um pedido específico")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<ResponseDto<PedidoDto>> buscarPorId(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {

        try {
            Optional<PedidoDto> pedido = pedidoService.buscarPorId(id);

            if (pedido.isPresent()) {
                ResponseDto<PedidoDto> response = ResponseDto.sucesso(
                        "Pedido encontrado",
                        pedido.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<PedidoDto> response = ResponseDto.erro("Pedido não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<PedidoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca pedido por número
     * GET /api/pedidos/numero/{numeroPedido}
     */
    @GetMapping("/numero/{numeroPedido}")
    @Operation(summary = "Buscar pedido por número", description = "Retorna os dados de um pedido pelo número")
    @ApiResponse(responseCode = "200", description = "Pedido encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<ResponseDto<PedidoDto>> buscarPorNumeroPedido(
            @Parameter(description = "Número do pedido") @PathVariable String numeroPedido) {

        try {
            Optional<PedidoDto> pedido = pedidoService.buscarPorNumeroPedido(numeroPedido);

            if (pedido.isPresent()) {
                ResponseDto<PedidoDto> response = ResponseDto.sucesso(
                        "Pedido encontrado",
                        pedido.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<PedidoDto> response = ResponseDto.erro("Pedido não encontrado com número: " + numeroPedido);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<PedidoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista pedidos por cliente
     * GET /api/pedidos/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Listar pedidos por cliente", description = "Retorna lista de pedidos de um cliente específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    public ResponseEntity<ResponseDto<List<PedidoDto>>> listarPedidosPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {

        try {
            List<PedidoDto> pedidos = pedidoService.listarPedidosPorCliente(clienteId);

            ResponseDto<List<PedidoDto>> response = ResponseDto.sucesso(
                    "Lista de pedidos recuperada com sucesso. Total: " + pedidos.size(),
                    pedidos
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<PedidoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista todos os pedidos
     * GET /api/pedidos
     */
    @GetMapping
    @Operation(summary = "Listar todos os pedidos", description = "Retorna lista de todos os pedidos (para atendentes)")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    public ResponseEntity<ResponseDto<List<PedidoDto>>> listarTodosPedidos() {

        try {
            List<PedidoDto> pedidos = pedidoService.listarTodosPedidos();

            ResponseDto<List<PedidoDto>> response = ResponseDto.sucesso(
                    "Lista de pedidos recuperada com sucesso. Total: " + pedidos.size(),
                    pedidos
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<PedidoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista pedidos por status
     * GET /api/pedidos/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pedidos por status", description = "Retorna lista de pedidos com status específico")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    public ResponseEntity<ResponseDto<List<PedidoDto>>> listarPedidosPorStatus(
            @Parameter(description = "Status do pedido") @PathVariable StatusPedido status) {

        try {
            List<PedidoDto> pedidos = pedidoService.listarPedidosPorStatus(status);

            ResponseDto<List<PedidoDto>> response = ResponseDto.sucesso(
                    "Lista de pedidos com status " + status + " recuperada com sucesso. Total: " + pedidos.size(),
                    pedidos
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<PedidoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualiza status do pedido
     * PUT /api/pedidos/{id}/status
     */
    @PutMapping("/{id}/status")
    @Operation(summary = "Atualizar status do pedido", description = "Atualiza o status de um pedido")
    @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Transição de status inválida")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<ResponseDto<PedidoDto>> atualizarStatus(
            @Parameter(description = "ID do pedido") @PathVariable Long id,
            @RequestBody Map<String, String> dados) {

        try {
            String novoStatusStr = dados.get("status");
            String observacao = dados.get("observacao");
            String responsavel = dados.get("responsavel");

            if (novoStatusStr == null) {
                ResponseDto<PedidoDto> response = ResponseDto.erro("Status é obrigatório");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            StatusPedido novoStatus = StatusPedido.valueOf(novoStatusStr.toUpperCase());

            PedidoDto pedidoAtualizado = pedidoService.atualizarStatus(id, novoStatus, observacao, responsavel);

            ResponseDto<PedidoDto> response = ResponseDto.sucesso(
                    "Status atualizado com sucesso para: " + novoStatus.getDescricao(),
                    pedidoAtualizado
            );

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException e) {
            ResponseDto<PedidoDto> response = ResponseDto.erro("Status inválido: " + dados.get("status"));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrado") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            ResponseDto<PedidoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<PedidoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Cancela pedido
     * PUT /api/pedidos/{id}/cancelar
     */
    @PutMapping("/{id}/cancelar")
    @Operation(summary = "Cancelar pedido", description = "Cancela um pedido")
    @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso")
    @ApiResponse(responseCode = "400", description = "Pedido não pode ser cancelado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<ResponseDto<PedidoDto>> cancelarPedido(
            @Parameter(description = "ID do pedido") @PathVariable Long id,
            @RequestBody Map<String, String> dados) {

        try {
            String motivo = dados.get("motivo");
            String responsavel = dados.get("responsavel");

            if (motivo == null || motivo.trim().isEmpty()) {
                ResponseDto<PedidoDto> response = ResponseDto.erro("Motivo do cancelamento é obrigatório");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            PedidoDto pedidoCancelado = pedidoService.cancelarPedido(id, motivo, responsavel);

            ResponseDto<PedidoDto> response = ResponseDto.sucesso(
                    "Pedido cancelado com sucesso",
                    pedidoCancelado
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrado") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            ResponseDto<PedidoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<PedidoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca pedidos com filtros
     * GET /api/pedidos/buscar
     */
    @GetMapping("/buscar")
    @Operation(summary = "Buscar pedidos com filtros", description = "Busca pedidos usando diversos filtros")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos encontrados")
    public ResponseEntity<ResponseDto<List<PedidoDto>>> buscarComFiltros(
            @Parameter(description = "ID do cliente") @RequestParam(required = false) Long clienteId,
            @Parameter(description = "Status do pedido") @RequestParam(required = false) StatusPedido status,
            @Parameter(description = "Número do pedido") @RequestParam(required = false) String numeroPedido,
            @Parameter(description = "Nome do cliente") @RequestParam(required = false) String nomeCliente) {

        try {
            List<PedidoDto> pedidos = pedidoService.buscarComFiltros(clienteId, status, numeroPedido, nomeCliente);

            ResponseDto<List<PedidoDto>> response = ResponseDto.sucesso(
                    "Busca realizada com sucesso. Encontrados: " + pedidos.size() + " pedido(s)",
                    pedidos
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<PedidoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca histórico de um pedido
     * GET /api/pedidos/{id}/historico
     */
    @GetMapping("/{id}/historico")
    @Operation(summary = "Buscar histórico do pedido", description = "Retorna o histórico de mudanças de status de um pedido")
    @ApiResponse(responseCode = "200", description = "Histórico encontrado")
    @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
    public ResponseEntity<ResponseDto<List<HistoricoStatusDto>>> buscarHistoricoPedido(
            @Parameter(description = "ID do pedido") @PathVariable Long id) {

        try {
            // Verificar se o pedido existe
            Optional<PedidoDto> pedido = pedidoService.buscarPorId(id);
            if (pedido.isEmpty()) {
                ResponseDto<List<HistoricoStatusDto>> response = ResponseDto.erro("Pedido não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            List<HistoricoStatusDto> historico = pedidoService.buscarHistoricoPedido(id);

            ResponseDto<List<HistoricoStatusDto>> response = ResponseDto.sucesso(
                    "Histórico recuperado com sucesso. Total de mudanças: " + historico.size(),
                    historico
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<HistoricoStatusDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Estatísticas de pedidos
     * GET /api/pedidos/estatisticas
     */
    @GetMapping("/estatisticas")
    @Operation(summary = "Estatísticas de pedidos", description = "Retorna estatísticas gerais dos pedidos")
    @ApiResponse(responseCode = "200", description = "Estatísticas recuperadas")
    public ResponseEntity<ResponseDto<Map<String, Object>>> obterEstatisticas() {

        try {
            Map<String, Object> estatisticas = Map.of(
                    "totalConfirmados", pedidoService.contarPedidosPorStatus(StatusPedido.CONFIRMADO),
                    "totalProcessando", pedidoService.contarPedidosPorStatus(StatusPedido.PROCESSANDO),
                    "totalEnviados", pedidoService.contarPedidosPorStatus(StatusPedido.ENVIADO),
                    "totalEmTransito", pedidoService.contarPedidosPorStatus(StatusPedido.EM_TRANSITO),
                    "totalSaiuParaEntrega", pedidoService.contarPedidosPorStatus(StatusPedido.SAIU_PARA_ENTREGA),
                    "totalEntregues", pedidoService.contarPedidosPorStatus(StatusPedido.ENTREGUE),
                    "totalCancelados", pedidoService.contarPedidosPorStatus(StatusPedido.CANCELADO),
                    "totalDevolvidos", pedidoService.contarPedidosPorStatus(StatusPedido.DEVOLVIDO)
            );

            ResponseDto<Map<String, Object>> response = ResponseDto.sucesso(
                    "Estatísticas recuperadas com sucesso",
                    estatisticas
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<Map<String, Object>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
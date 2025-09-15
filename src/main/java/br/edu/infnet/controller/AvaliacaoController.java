package br.edu.infnet.controller;

import br.edu.infnet.model.dto.AvaliacaoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.service.AvaliacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
 * Controller REST para operações relacionadas às Avaliações de Pedidos
 * Implementa UC05 - Avaliar Experiência de Entrega
 */
@RestController
@RequestMapping("/avaliacoes")
@Tag(name = "Avaliações", description = "Sistema de avaliação de experiência de entrega - Sprint 4")
@SecurityRequirement(name = "basicAuth")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    /**
     * Criar nova avaliação
     * POST /api/avaliacoes
     */
    @PostMapping
    @Operation(
        summary = "Criar avaliação",
        description = "Permite que um cliente avalie a experiência de entrega de um pedido"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Avaliação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou pedido já avaliado"),
        @ApiResponse(responseCode = "404", description = "Pedido ou cliente não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<AvaliacaoDto>> criarAvaliacao(
            @Valid @RequestBody AvaliacaoDto avaliacaoDto) {
        
        try {
            AvaliacaoDto avaliacaoCriada = avaliacaoService.criarAvaliacao(avaliacaoDto);
            
            ResponseDto<AvaliacaoDto> response = ResponseDto.sucesso(
                "Avaliação criada com sucesso! Obrigado pelo seu feedback.",
                avaliacaoCriada
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrado") ? 
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
                
            ResponseDto<AvaliacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);
            
        } catch (Exception e) {
            ResponseDto<AvaliacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar avaliação por ID
     * GET /api/avaliacoes/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar avaliação por ID",
        description = "Retorna os detalhes de uma avaliação específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Avaliação encontrada"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<AvaliacaoDto>> buscarPorId(
            @Parameter(description = "ID da avaliação") @PathVariable Long id) {
        
        try {
            AvaliacaoDto avaliacao = avaliacaoService.buscarPorId(id);
            
            ResponseDto<AvaliacaoDto> response = ResponseDto.sucesso(
                "Avaliação encontrada",
                avaliacao
            );
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            ResponseDto<AvaliacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            ResponseDto<AvaliacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar avaliações do cliente
     * GET /api/avaliacoes/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    @Operation(
        summary = "Listar avaliações do cliente",
        description = "Retorna todas as avaliações feitas por um cliente específico"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de avaliações"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<List<AvaliacaoDto>>> listarPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {
        
        try {
            List<AvaliacaoDto> avaliacoes = avaliacaoService.listarPorCliente(clienteId);
            
            ResponseDto<List<AvaliacaoDto>> response = ResponseDto.sucesso(
                "Total de avaliações encontradas: " + avaliacoes.size(),
                avaliacoes
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<List<AvaliacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar avaliações do pedido
     * GET /api/avaliacoes/pedido/{pedidoId}
     */
    @GetMapping("/pedido/{pedidoId}")
    @Operation(
        summary = "Buscar avaliação do pedido",
        description = "Retorna a avaliação de um pedido específico (se existir)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Avaliação encontrada"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada para este pedido"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<AvaliacaoDto>> buscarPorPedido(
            @Parameter(description = "ID do pedido") @PathVariable Long pedidoId) {
        
        try {
            Optional<AvaliacaoDto> avaliacaoOpt = avaliacaoService.buscarPorPedido(pedidoId);
            
            if (avaliacaoOpt.isPresent()) {
                ResponseDto<AvaliacaoDto> response = ResponseDto.sucesso(
                    "Avaliação encontrada para o pedido",
                    avaliacaoOpt.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<AvaliacaoDto> response = ResponseDto.erro("Avaliação não encontrada para o pedido: " + pedidoId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (RuntimeException e) {
            ResponseDto<AvaliacaoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            ResponseDto<AvaliacaoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Verificar se pedido já foi avaliado
     * GET /api/avaliacoes/pedido/{pedidoId}/existe
     */
    @GetMapping("/pedido/{pedidoId}/existe")
    @Operation(
        summary = "Verificar se pedido foi avaliado",
        description = "Verifica se um pedido já possui avaliação"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Verificação realizada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<Map<String, Boolean>>> verificarAvaliacaoExistente(
            @Parameter(description = "ID do pedido") @PathVariable Long pedidoId) {
        
        try {
            boolean existe = avaliacaoService.pedidoJaAvaliado(pedidoId);
            
            Map<String, Boolean> resultado = Map.of("avaliacaoExiste", existe);
            
            String mensagem = existe ? 
                "Este pedido já foi avaliado" : 
                "Este pedido ainda não foi avaliado";
            
            ResponseDto<Map<String, Boolean>> response = ResponseDto.sucesso(mensagem, resultado);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<Map<String, Boolean>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Obter estatísticas de avaliações
     * GET /api/avaliacoes/estatisticas
     */
    @GetMapping("/estatisticas")
    @Operation(
        summary = "Estatísticas de avaliações",
        description = "Retorna estatísticas gerais das avaliações do sistema"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estatísticas calculadas"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<Map<String, Object>>> obterEstatisticas() {
        
        try {
            Map<String, Object> estatisticas = avaliacaoService.obterEstatisticas();
            
            ResponseDto<Map<String, Object>> response = ResponseDto.sucesso(
                "Estatísticas de avaliações",
                estatisticas
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<Map<String, Object>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar todas as avaliações (admin)
     * GET /api/avaliacoes
     */
    @GetMapping
    @Operation(
        summary = "Listar todas as avaliações",
        description = "Retorna todas as avaliações do sistema (acesso administrativo)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de avaliações"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<List<AvaliacaoDto>>> listarTodas() {
        
        try {
            List<AvaliacaoDto> avaliacoes = avaliacaoService.listarTodas();
            
            ResponseDto<List<AvaliacaoDto>> response = ResponseDto.sucesso(
                "Total de avaliações: " + avaliacoes.size(),
                avaliacoes
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<List<AvaliacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar avaliações por nota
     * GET /api/avaliacoes/nota/{nota}
     */
    @GetMapping("/nota/{nota}")
    @Operation(
        summary = "Filtrar avaliações por nota média",
        description = "Retorna avaliações com média igual ou superior à nota especificada"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de avaliações filtradas"),
        @ApiResponse(responseCode = "400", description = "Nota inválida (deve ser entre 1 e 5)"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<List<AvaliacaoDto>>> listarPorNota(
            @Parameter(description = "Nota mínima (1-5)") @PathVariable Double nota) {
        
        try {
            if (nota < 1 || nota > 5) {
                ResponseDto<List<AvaliacaoDto>> response = ResponseDto.erro(
                    "Nota deve estar entre 1 e 5"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
            List<AvaliacaoDto> avaliacoes = avaliacaoService.listarPorNotaMinima(nota);
            
            ResponseDto<List<AvaliacaoDto>> response = ResponseDto.sucesso(
                "Avaliações com média >= " + nota + ": " + avaliacoes.size(),
                avaliacoes
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<List<AvaliacaoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
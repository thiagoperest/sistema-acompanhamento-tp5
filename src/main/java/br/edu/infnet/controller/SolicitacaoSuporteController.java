package br.edu.infnet.controller;

import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.dto.SolicitacaoSuporteDto;
import br.edu.infnet.model.enums.StatusSuporte;
import br.edu.infnet.service.SolicitacaoSuporteService;
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
 * Controller REST para operações relacionadas às Solicitações de Suporte
 * Implementa UC04 - Solicitar Suporte
 */
@RestController
@RequestMapping("/suporte")
@Tag(name = "Solicitações de Suporte", description = "Abertura e acompanhamento de tickets de suporte - Sprint 4")
@SecurityRequirement(name = "basicAuth")
public class SolicitacaoSuporteController {

    @Autowired
    private SolicitacaoSuporteService solicitacaoSuporteService;

    /**
     * Criar nova solicitação de suporte
     * POST /api/suporte
     */
    @PostMapping
    @Operation(
        summary = "Criar solicitação de suporte",
        description = "Permite que um cliente abra uma nova solicitação de suporte"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Solicitação criada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> criarSolicitacao(
            @Valid @RequestBody SolicitacaoSuporteDto solicitacaoDto) {
        
        try {
            SolicitacaoSuporteDto solicitacaoCriada = solicitacaoSuporteService.criarSolicitacao(solicitacaoDto);
            
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.sucesso(
                "Solicitação de suporte criada com sucesso! Protocolo: " + solicitacaoCriada.getProtocolo(),
                solicitacaoCriada
            );
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrado") ? 
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
                
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);
            
        } catch (Exception e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar solicitação por ID
     * GET /api/suporte/{id}
     */
    @GetMapping("/{id}")
    @Operation(
        summary = "Buscar solicitação por ID",
        description = "Retorna os detalhes de uma solicitação de suporte específica"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitação encontrada"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> buscarPorId(
            @Parameter(description = "ID da solicitação") @PathVariable Long id) {
        
        try {
            Optional<SolicitacaoSuporteDto> solicitacaoOpt = solicitacaoSuporteService.buscarPorId(id);
            
            if (solicitacaoOpt.isPresent()) {
                ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.sucesso(
                    "Solicitação encontrada",
                    solicitacaoOpt.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Solicitação não encontrada com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (RuntimeException e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Buscar solicitação por protocolo
     * GET /api/suporte/protocolo/{protocolo}
     */
    @GetMapping("/protocolo/{protocolo}")
    @Operation(
        summary = "Buscar solicitação por protocolo",
        description = "Busca uma solicitação usando o número de protocolo"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitação encontrada"),
        @ApiResponse(responseCode = "404", description = "Protocolo não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> buscarPorProtocolo(
            @Parameter(description = "Número do protocolo") @PathVariable String protocolo) {
        
        try {
            Optional<SolicitacaoSuporteDto> solicitacaoOpt = solicitacaoSuporteService.buscarPorProtocolo(protocolo);
            
            if (solicitacaoOpt.isPresent()) {
                ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.sucesso(
                    "Solicitação encontrada",
                    solicitacaoOpt.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Protocolo não encontrado: " + protocolo);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            
        } catch (RuntimeException e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            
        } catch (Exception e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar solicitações do cliente
     * GET /api/suporte/cliente/{clienteId}
     */
    @GetMapping("/cliente/{clienteId}")
    @Operation(
        summary = "Listar solicitações do cliente",
        description = "Retorna todas as solicitações de suporte de um cliente específico"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de solicitações"),
        @ApiResponse(responseCode = "404", description = "Cliente não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> listarPorCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long clienteId) {
        
        try {
            List<SolicitacaoSuporteDto> solicitacoes = solicitacaoSuporteService.listarPorCliente(clienteId);
            
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.sucesso(
                "Total de solicitações encontradas: " + solicitacoes.size(),
                solicitacoes
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar solicitações por status
     * GET /api/suporte/status/{status}
     */
    @GetMapping("/status/{status}")
    @Operation(
        summary = "Listar solicitações por status",
        description = "Retorna solicitações filtradas por status (ABERTO, EM_ANDAMENTO, RESOLVIDO, FECHADO)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de solicitações"),
        @ApiResponse(responseCode = "400", description = "Status inválido"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> listarPorStatus(
            @Parameter(description = "Status da solicitação") @PathVariable String status) {
        
        try {
            StatusSuporte statusEnum = StatusSuporte.valueOf(status.toUpperCase());
            List<SolicitacaoSuporteDto> solicitacoes = solicitacaoSuporteService.listarPorStatus(statusEnum);
            
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.sucesso(
                "Solicitações com status " + status + ": " + solicitacoes.size(),
                solicitacoes
            );
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.erro(
                "Status inválido. Use: ABERTO, EM_ANDAMENTO, RESOLVIDO ou FECHADO"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (Exception e) {
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualizar status da solicitação
     * PUT /api/suporte/{id}/status
     */
    @PutMapping("/{id}/status")
    @Operation(
        summary = "Atualizar status da solicitação",
        description = "Atualiza o status de uma solicitação e opcionalmente atribui um atendente"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> atualizarStatus(
            @Parameter(description = "ID da solicitação") @PathVariable Long id,
            @RequestBody Map<String, Object> atualizacao) {
        
        try {
            String novoStatus = (String) atualizacao.get("status");
            Long atendenteId = atualizacao.containsKey("atendenteId") ? 
                Long.valueOf(atualizacao.get("atendenteId").toString()) : null;
            
            StatusSuporte statusEnum = StatusSuporte.valueOf(novoStatus.toUpperCase());
            
            SolicitacaoSuporteDto solicitacaoAtualizada = 
                solicitacaoSuporteService.atualizarStatus(id, statusEnum, atendenteId);
            
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.sucesso(
                "Status da solicitação atualizado para: " + statusEnum.getDescricao(),
                solicitacaoAtualizada
            );
            
            return ResponseEntity.ok(response);
            
        } catch (IllegalArgumentException e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro(
                "Status inválido. Use: ABERTO, EM_ANDAMENTO, RESOLVIDO ou FECHADO"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrad") ? 
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
                
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);
            
        } catch (Exception e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atribuir atendente à solicitação
     * PUT /api/suporte/{id}/atribuir/{atendenteId}
     */
    @PutMapping("/{id}/atribuir/{atendenteId}")
    @Operation(
        summary = "Atribuir atendente",
        description = "Atribui um atendente de suporte a uma solicitação"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Atendente atribuído com sucesso"),
        @ApiResponse(responseCode = "404", description = "Solicitação ou atendente não encontrado"),
        @ApiResponse(responseCode = "400", description = "Erro na atribuição"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> atribuirAtendente(
            @Parameter(description = "ID da solicitação") @PathVariable Long id,
            @Parameter(description = "ID do atendente") @PathVariable Long atendenteId) {
        
        try {
            SolicitacaoSuporteDto solicitacaoAtualizada = 
                solicitacaoSuporteService.atribuirAtendente(id, atendenteId);
            
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.sucesso(
                "Atendente atribuído com sucesso à solicitação",
                solicitacaoAtualizada
            );
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrad") ? 
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
                
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);
            
        } catch (Exception e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar todas as solicitações (admin)
     * GET /api/suporte
     */
    @GetMapping
    @Operation(
        summary = "Listar todas as solicitações",
        description = "Retorna todas as solicitações de suporte do sistema (acesso administrativo)"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de solicitações"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> listarTodas() {
        
        try {
            List<SolicitacaoSuporteDto> solicitacoes = solicitacaoSuporteService.listarTodas();
            
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.sucesso(
                "Total de solicitações: " + solicitacoes.size(),
                solicitacoes
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Listar solicitações do atendente
     * GET /api/suporte/atendente/{atendenteId}
     */
    @GetMapping("/atendente/{atendenteId}")
    @Operation(
        summary = "Listar solicitações do atendente",
        description = "Retorna todas as solicitações atribuídas a um atendente específico"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Lista de solicitações"),
        @ApiResponse(responseCode = "404", description = "Atendente não encontrado"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> listarPorAtendente(
            @Parameter(description = "ID do atendente") @PathVariable Long atendenteId) {
        
        try {
            List<SolicitacaoSuporteDto> solicitacoes = solicitacaoSuporteService.listarPorAtendente(atendenteId);
            
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.sucesso(
                "Solicitações atribuídas ao atendente: " + solicitacoes.size(),
                solicitacoes
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<List<SolicitacaoSuporteDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Fechar solicitação
     * PUT /api/suporte/{id}/fechar
     */
    @PutMapping("/{id}/fechar")
    @Operation(
        summary = "Fechar solicitação",
        description = "Fecha uma solicitação de suporte resolvida"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Solicitação fechada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Solicitação não pode ser fechada"),
        @ApiResponse(responseCode = "404", description = "Solicitação não encontrada"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> fecharSolicitacao(
            @Parameter(description = "ID da solicitação") @PathVariable Long id) {
        
        try {
            SolicitacaoSuporteDto solicitacaoFechada = solicitacaoSuporteService.fecharSolicitacao(id);
            
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.sucesso(
                "Solicitação de suporte fechada com sucesso",
                solicitacaoFechada
            );
            
            return ResponseEntity.ok(response);
            
        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrad") ? 
                HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
                
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);
            
        } catch (Exception e) {
            ResponseDto<SolicitacaoSuporteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Estatísticas de suporte
     * GET /api/suporte/estatisticas
     */
    @GetMapping("/estatisticas")
    @Operation(
        summary = "Estatísticas de suporte",
        description = "Retorna estatísticas gerais das solicitações de suporte"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Estatísticas calculadas"),
        @ApiResponse(responseCode = "401", description = "Não autorizado")
    })
    public ResponseEntity<ResponseDto<Map<String, Object>>> obterEstatisticas() {
        
        try {
            Map<String, Object> estatisticas = solicitacaoSuporteService.obterEstatisticas();
            
            ResponseDto<Map<String, Object>> response = ResponseDto.sucesso(
                "Estatísticas de suporte",
                estatisticas
            );
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            ResponseDto<Map<String, Object>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
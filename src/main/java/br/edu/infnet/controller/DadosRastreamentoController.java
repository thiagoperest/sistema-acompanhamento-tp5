package br.edu.infnet.controller;

import br.edu.infnet.model.dto.DadosRastreamentoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.service.DadosRastreamentoService;
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
 * Controller REST para operações relacionadas aos Dados de Rastreamento
 */
@RestController
@RequestMapping("/dados-rastreamento")
@Tag(name = "Dados de Rastreamento", description = "Operações relacionadas ao gerenciamento de dados de rastreamento")
public class DadosRastreamentoController {

    @Autowired
    private DadosRastreamentoService dadosRastreamentoService;

    /**
     * Cria dados de rastreamento para um pedido
     * POST /api/dados-rastreamento
     */
    @PostMapping
    @Operation(summary = "Criar dados de rastreamento", description = "Cria dados de rastreamento para um pedido")
    @ApiResponse(responseCode = "201", description = "Dados de rastreamento criados com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    public ResponseEntity<ResponseDto<DadosRastreamentoDto>> criarDadosRastreamento(
            @Valid @RequestBody DadosRastreamentoDto dadosDto) {

        try {
            DadosRastreamentoDto dadosCriados = dadosRastreamentoService.criarDadosRastreamento(dadosDto);

            ResponseDto<DadosRastreamentoDto> response = ResponseDto.sucesso(
                    "Dados de rastreamento criados com sucesso! ID: " + dadosCriados.getId(),
                    dadosCriados
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca dados de rastreamento por ID
     * GET /api/dados-rastreamento/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar dados por ID", description = "Retorna os dados de rastreamento por ID")
    @ApiResponse(responseCode = "200", description = "Dados encontrados")
    @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    public ResponseEntity<ResponseDto<DadosRastreamentoDto>> buscarPorId(
            @Parameter(description = "ID dos dados de rastreamento") @PathVariable Long id) {

        try {
            Optional<DadosRastreamentoDto> dados = dadosRastreamentoService.buscarPorId(id);

            if (dados.isPresent()) {
                ResponseDto<DadosRastreamentoDto> response = ResponseDto.sucesso(
                        "Dados de rastreamento encontrados",
                        dados.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Dados não encontrados com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca dados de rastreamento por pedido
     * GET /api/dados-rastreamento/pedido/{pedidoId}
     */
    @GetMapping("/pedido/{pedidoId}")
    @Operation(summary = "Buscar dados por pedido", description = "Retorna os dados de rastreamento de um pedido")
    @ApiResponse(responseCode = "200", description = "Dados encontrados")
    @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    public ResponseEntity<ResponseDto<DadosRastreamentoDto>> buscarPorPedido(
            @Parameter(description = "ID do pedido") @PathVariable Long pedidoId) {

        try {
            Optional<DadosRastreamentoDto> dados = dadosRastreamentoService.buscarPorPedido(pedidoId);

            if (dados.isPresent()) {
                ResponseDto<DadosRastreamentoDto> response = ResponseDto.sucesso(
                        "Dados de rastreamento do pedido encontrados",
                        dados.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Dados não encontrados para pedido ID: " + pedidoId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca dados de rastreamento por código
     * GET /api/dados-rastreamento/codigo/{codigo}
     */
    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar dados por código", description = "Retorna os dados de rastreamento por código")
    @ApiResponse(responseCode = "200", description = "Dados encontrados")
    @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    public ResponseEntity<ResponseDto<DadosRastreamentoDto>> buscarPorCodigo(
            @Parameter(description = "Código de rastreamento") @PathVariable String codigo) {

        try {
            Optional<DadosRastreamentoDto> dados = dadosRastreamentoService.buscarPorCodigo(codigo);

            if (dados.isPresent()) {
                ResponseDto<DadosRastreamentoDto> response = ResponseDto.sucesso(
                        "Dados de rastreamento encontrados",
                        dados.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Dados não encontrados para código: " + codigo);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca dados de rastreamento por transportadora
     * GET /api/dados-rastreamento/transportadora/{transportadora}
     */
    @GetMapping("/transportadora/{transportadora}")
    @Operation(summary = "Buscar dados por transportadora", description = "Retorna dados de rastreamento de uma transportadora")
    @ApiResponse(responseCode = "200", description = "Lista de dados encontrados")
    public ResponseEntity<ResponseDto<List<DadosRastreamentoDto>>> buscarPorTransportadora(
            @Parameter(description = "Nome da transportadora") @PathVariable String transportadora) {

        try {
            List<DadosRastreamentoDto> dados = dadosRastreamentoService.buscarPorTransportadora(transportadora);

            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.sucesso(
                    "Dados da transportadora recuperados. Total: " + dados.size(),
                    dados
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista todas as transportadoras cadastradas
     * GET /api/dados-rastreamento/transportadoras
     */
    @GetMapping("/transportadoras")
    @Operation(summary = "Listar transportadoras", description = "Retorna lista de todas as transportadoras cadastradas")
    @ApiResponse(responseCode = "200", description = "Lista de transportadoras")
    public ResponseEntity<ResponseDto<List<String>>> listarTransportadoras() {

        try {
            List<String> transportadoras = dadosRastreamentoService.listarTransportadoras();

            ResponseDto<List<String>> response = ResponseDto.sucesso(
                    "Transportadoras recuperadas. Total: " + transportadoras.size(),
                    transportadoras
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<String>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualiza dados de rastreamento
     * PUT /api/dados-rastreamento/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados de rastreamento", description = "Atualiza os dados de rastreamento")
    @ApiResponse(responseCode = "200", description = "Dados atualizados com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    public ResponseEntity<ResponseDto<DadosRastreamentoDto>> atualizarDados(
            @Parameter(description = "ID dos dados") @PathVariable Long id,
            @Valid @RequestBody DadosRastreamentoDto dadosDto) {

        try {
            DadosRastreamentoDto dadosAtualizados = dadosRastreamentoService.atualizarDados(id, dadosDto);

            ResponseDto<DadosRastreamentoDto> response = ResponseDto.sucesso(
                    "Dados de rastreamento atualizados com sucesso",
                    dadosAtualizados
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrados") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualiza localização atual
     * PUT /api/dados-rastreamento/{id}/localizacao
     */
    @PutMapping("/{id}/localizacao")
    @Operation(summary = "Atualizar localização", description = "Atualiza a localização atual do rastreamento")
    @ApiResponse(responseCode = "200", description = "Localização atualizada")
    @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    public ResponseEntity<ResponseDto<DadosRastreamentoDto>> atualizarLocalizacao(
            @Parameter(description = "ID dos dados") @PathVariable Long id,
            @Parameter(description = "Nova localização") @RequestParam String localizacao) {

        try {
            DadosRastreamentoDto dados = dadosRastreamentoService.atualizarLocalizacao(id, localizacao);

            ResponseDto<DadosRastreamentoDto> response = ResponseDto.sucesso(
                    "Localização atualizada com sucesso",
                    dados
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualiza localização por código de rastreamento
     * PUT /api/dados-rastreamento/codigo/{codigo}/localizacao
     */
    @PutMapping("/codigo/{codigo}/localizacao")
    @Operation(summary = "Atualizar localização por código", description = "Atualiza localização usando código de rastreamento")
    @ApiResponse(responseCode = "200", description = "Localização atualizada")
    @ApiResponse(responseCode = "404", description = "Código não encontrado")
    public ResponseEntity<ResponseDto<DadosRastreamentoDto>> atualizarLocalizacaoPorCodigo(
            @Parameter(description = "Código de rastreamento") @PathVariable String codigo,
            @Parameter(description = "Nova localização") @RequestParam String localizacao) {

        try {
            DadosRastreamentoDto dados = dadosRastreamentoService.atualizarLocalizacaoPorCodigo(codigo, localizacao);

            ResponseDto<DadosRastreamentoDto> response = ResponseDto.sucesso(
                    "Localização atualizada com sucesso",
                    dados
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<DadosRastreamentoDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca rastreamentos atualizados recentemente
     * GET /api/dados-rastreamento/recentes
     */
    @GetMapping("/recentes")
    @Operation(summary = "Buscar rastreamentos recentes", description = "Retorna rastreamentos atualizados nas últimas 24h")
    @ApiResponse(responseCode = "200", description = "Lista de rastreamentos recentes")
    public ResponseEntity<ResponseDto<List<DadosRastreamentoDto>>> buscarRastreamentosRecentes() {

        try {
            List<DadosRastreamentoDto> dados = dadosRastreamentoService.buscarRastreamentosRecentes();

            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.sucesso(
                    "Rastreamentos recentes recuperados. Total: " + dados.size(),
                    dados
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca rastreamentos desatualizados
     * GET /api/dados-rastreamento/desatualizados
     */
    @GetMapping("/desatualizados")
    @Operation(summary = "Buscar rastreamentos desatualizados", description = "Retorna rastreamentos não atualizados há X dias")
    @ApiResponse(responseCode = "200", description = "Lista de rastreamentos desatualizados")
    public ResponseEntity<ResponseDto<List<DadosRastreamentoDto>>> buscarRastreamentosDesatualizados(
            @Parameter(description = "Dias sem atualização") @RequestParam(defaultValue = "7") int dias) {

        try {
            List<DadosRastreamentoDto> dados = dadosRastreamentoService.buscarRastreamentosDesatualizados(dias);

            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.sucesso(
                    "Rastreamentos desatualizados recuperados. Total: " + dados.size(),
                    dados
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca rastreamentos por localização
     * GET /api/dados-rastreamento/buscar/localizacao
     */
    @GetMapping("/buscar/localizacao")
    @Operation(summary = "Buscar por localização", description = "Busca rastreamentos por localização (busca parcial)")
    @ApiResponse(responseCode = "200", description = "Lista de rastreamentos encontrados")
    public ResponseEntity<ResponseDto<List<DadosRastreamentoDto>>> buscarPorLocalizacao(
            @Parameter(description = "Localização para busca") @RequestParam String localizacao) {

        try {
            List<DadosRastreamentoDto> dados = dadosRastreamentoService.buscarPorLocalizacao(localizacao);

            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.sucesso(
                    "Busca por localização realizada. Encontrados: " + dados.size(),
                    dados
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista todos os dados de rastreamento
     * GET /api/dados-rastreamento
     */
    @GetMapping
    @Operation(summary = "Listar todos os dados", description = "Retorna todos os dados de rastreamento")
    @ApiResponse(responseCode = "200", description = "Lista de dados de rastreamento")
    public ResponseEntity<ResponseDto<List<DadosRastreamentoDto>>> listarTodos() {

        try {
            List<DadosRastreamentoDto> dados = dadosRastreamentoService.listarTodos();

            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.sucesso(
                    "Dados de rastreamento recuperados. Total: " + dados.size(),
                    dados
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<DadosRastreamentoDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Remove dados de rastreamento
     * DELETE /api/dados-rastreamento/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Remover dados de rastreamento", description = "Remove os dados de rastreamento")
    @ApiResponse(responseCode = "200", description = "Dados removidos com sucesso")
    @ApiResponse(responseCode = "404", description = "Dados não encontrados")
    public ResponseEntity<ResponseDto<Void>> removerDados(
            @Parameter(description = "ID dos dados") @PathVariable Long id) {

        try {
            dadosRastreamentoService.removerDados(id);

            ResponseDto<Void> response = ResponseDto.sucesso("Dados de rastreamento removidos com sucesso");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            ResponseDto<Void> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            ResponseDto<Void> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Conta rastreamentos por transportadora
     * GET /api/dados-rastreamento/contar-por-transportadora/{transportadora}
     */
    @GetMapping("/contar-por-transportadora/{transportadora}")
    @Operation(summary = "Contar por transportadora", description = "Retorna quantidade de rastreamentos por transportadora")
    @ApiResponse(responseCode = "200", description = "Contagem realizada")
    public ResponseEntity<ResponseDto<Long>> contarPorTransportadora(
            @Parameter(description = "Nome da transportadora") @PathVariable String transportadora) {

        try {
            long quantidade = dadosRastreamentoService.contarPorTransportadora(transportadora);

            ResponseDto<Long> response = ResponseDto.sucesso(
                    "Contagem de rastreamentos da " + transportadora + " realizada",
                    quantidade
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<Long> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
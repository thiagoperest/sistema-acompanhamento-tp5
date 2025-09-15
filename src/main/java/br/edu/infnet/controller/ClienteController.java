package br.edu.infnet.controller;

import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.service.ClienteService;
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
 * Controller REST para operações relacionadas aos Clientes
 */
@RestController
@RequestMapping("/clientes")
@Tag(name = "Clientes", description = "Operações relacionadas ao gerenciamento de clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    /**
     * Registra um novo cliente
     * POST /api/clientes
     */
    @PostMapping
    @Operation(summary = "Registrar novo cliente", description = "Cadastra um novo cliente no sistema")
    @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "409", description = "Email ou CPF já cadastrado")
    public ResponseEntity<ResponseDto<ClienteDto>> registrarCliente(
            @Valid @RequestBody ClienteDto clienteDto) {

        try {
            ClienteDto clienteRegistrado = clienteService.registrarCliente(clienteDto);

            ResponseDto<ClienteDto> response = ResponseDto.sucesso(
                    "Cliente registrado com sucesso! ID: " + clienteRegistrado.getId(),
                    clienteRegistrado
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (RuntimeException e) {
            ResponseDto<ClienteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            ResponseDto<ClienteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca cliente por ID
     * GET /api/clientes/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar cliente por ID", description = "Retorna os dados de um cliente específico")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ResponseDto<ClienteDto>> buscarPorId(
            @Parameter(description = "ID do cliente") @PathVariable Long id) {

        try {
            Optional<ClienteDto> cliente = clienteService.buscarPorId(id);

            if (cliente.isPresent()) {
                ResponseDto<ClienteDto> response = ResponseDto.sucesso(
                        "Cliente encontrado",
                        cliente.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<ClienteDto> response = ResponseDto.erro("Cliente não encontrado com ID: " + id);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<ClienteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Lista todos os clientes ativos
     * GET /api/clientes
     */
    @GetMapping
    @Operation(summary = "Listar clientes ativos", description = "Retorna lista de todos os clientes ativos")
    @ApiResponse(responseCode = "200", description = "Lista de clientes")
    public ResponseEntity<ResponseDto<List<ClienteDto>>> listarClientes() {

        try {
            List<ClienteDto> clientes = clienteService.listarClientesAtivos();

            ResponseDto<List<ClienteDto>> response = ResponseDto.sucesso(
                    "Lista de clientes recuperada com sucesso. Total: " + clientes.size(),
                    clientes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<ClienteDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualiza dados do cliente
     * PUT /api/clientes/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar dados do cliente", description = "Atualiza os dados de um cliente existente")
    @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ResponseDto<ClienteDto>> atualizarCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long id,
            @Valid @RequestBody ClienteDto clienteDto) {

        try {
            ClienteDto clienteAtualizado = clienteService.atualizarCliente(id, clienteDto);

            ResponseDto<ClienteDto> response = ResponseDto.sucesso(
                    "Cliente atualizado com sucesso",
                    clienteAtualizado
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrado") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            ResponseDto<ClienteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<ClienteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Desativa cliente
     * DELETE /api/clientes/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Desativar cliente", description = "Desativa um cliente (soft delete)")
    @ApiResponse(responseCode = "200", description = "Cliente desativado com sucesso")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ResponseDto<Void>> desativarCliente(
            @Parameter(description = "ID do cliente") @PathVariable Long id) {

        try {
            clienteService.desativarCliente(id);

            ResponseDto<Void> response = ResponseDto.sucesso("Cliente desativado com sucesso");
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
     * Busca clientes por nome
     * GET /api/clientes/buscar/nome?nome={nome}
     */
    @GetMapping("/buscar/nome")
    @Operation(summary = "Buscar clientes por nome", description = "Busca clientes pelo nome (busca parcial)")
    @ApiResponse(responseCode = "200", description = "Lista de clientes encontrados")
    public ResponseEntity<ResponseDto<List<ClienteDto>>> buscarPorNome(
            @Parameter(description = "Nome para busca") @RequestParam String nome) {

        try {
            List<ClienteDto> clientes = clienteService.buscarPorNome(nome);

            ResponseDto<List<ClienteDto>> response = ResponseDto.sucesso(
                    "Busca realizada com sucesso. Encontrados: " + clientes.size() + " cliente(s)",
                    clientes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<ClienteDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca clientes por cidade
     * GET /api/clientes/buscar/cidade?cidade={cidade}
     */
    @GetMapping("/buscar/cidade")
    @Operation(summary = "Buscar clientes por cidade", description = "Busca clientes pela cidade")
    @ApiResponse(responseCode = "200", description = "Lista de clientes encontrados")
    public ResponseEntity<ResponseDto<List<ClienteDto>>> buscarPorCidade(
            @Parameter(description = "Cidade para busca") @RequestParam String cidade) {

        try {
            List<ClienteDto> clientes = clienteService.buscarPorCidade(cidade);

            ResponseDto<List<ClienteDto>> response = ResponseDto.sucesso(
                    "Busca realizada com sucesso. Encontrados: " + clientes.size() + " cliente(s)",
                    clientes
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<List<ClienteDto>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Busca cliente por email
     * GET /api/clientes/buscar/email?email={email}
     */
    @GetMapping("/buscar/email")
    @Operation(summary = "Buscar cliente por email", description = "Busca cliente pelo email")
    @ApiResponse(responseCode = "200", description = "Cliente encontrado")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ResponseDto<ClienteDto>> buscarPorEmail(
            @Parameter(description = "Email para busca") @RequestParam String email) {

        try {
            Optional<ClienteDto> cliente = clienteService.buscarPorEmail(email);

            if (cliente.isPresent()) {
                ResponseDto<ClienteDto> response = ResponseDto.sucesso(
                        "Cliente encontrado",
                        cliente.get()
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<ClienteDto> response = ResponseDto.erro("Cliente não encontrado com email: " + email);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

        } catch (Exception e) {
            ResponseDto<ClienteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
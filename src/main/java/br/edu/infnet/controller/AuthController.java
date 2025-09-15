package br.edu.infnet.controller;

import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.dto.LoginDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Controller REST para operações de autenticação
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "Operações de login e autenticação")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Realiza login do cliente
     * POST /api/auth/login/cliente
     */
    @PostMapping("/login/cliente")
    @Operation(summary = "Login do cliente", description = "Autentica um cliente no sistema")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "400", description = "Dados de login inválidos")
    public ResponseEntity<ResponseDto<ClienteDto>> loginCliente(
            @Valid @RequestBody LoginDto loginDto) {

        try {
            ClienteDto cliente = authService.loginCliente(loginDto);

            ResponseDto<ClienteDto> response = ResponseDto.sucesso(
                    "Login realizado com sucesso! Bem-vindo(a), " + cliente.getNome(),
                    cliente
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("Credenciais inválidas") ||
                    e.getMessage().contains("desativada") ?
                    HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST;

            ResponseDto<ClienteDto> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<ClienteDto> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Realiza login do atendente de suporte
     * POST /api/auth/login/atendente
     */
    @PostMapping("/login/atendente")
    @Operation(summary = "Login do atendente", description = "Autentica um atendente de suporte no sistema")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "400", description = "Dados de login inválidos")
    public ResponseEntity<ResponseDto<AtendenteSuporte>> loginAtendente(
            @Valid @RequestBody LoginDto loginDto) {

        try {
            AtendenteSuporte atendente = authService.loginAtendente(loginDto);

            ResponseDto<AtendenteSuporte> response = ResponseDto.sucesso(
                    "Login realizado com sucesso! Bem-vindo(a), " + atendente.getNome(),
                    atendente
            );

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("Credenciais inválidas") ||
                    e.getMessage().contains("desativada") ?
                    HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST;

            ResponseDto<AtendenteSuporte> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<AtendenteSuporte> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Login genérico que identifica automaticamente o tipo de usuário
     * POST /api/auth/login
     */
    @PostMapping("/login")
    @Operation(summary = "Login genérico", description = "Identifica automaticamente se é cliente ou atendente")
    @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    @ApiResponse(responseCode = "400", description = "Dados de login inválidos")
    public ResponseEntity<ResponseDto<Object>> login(
            @Valid @RequestBody LoginDto loginDto) {

        try {
            String tipoUsuario = authService.identificarTipoUsuario(loginDto.getEmail());

            if ("CLIENTE".equals(tipoUsuario)) {
                ClienteDto cliente = authService.loginCliente(loginDto);
                ResponseDto<Object> response = ResponseDto.sucesso(
                        "Login realizado com sucesso! Bem-vindo(a), " + cliente.getNome() + " (Cliente)",
                        cliente
                );
                return ResponseEntity.ok(response);

            } else if ("ATENDENTE".equals(tipoUsuario)) {
                AtendenteSuporte atendente = authService.loginAtendente(loginDto);
                ResponseDto<Object> response = ResponseDto.sucesso(
                        "Login realizado com sucesso! Bem-vindo(a), " + atendente.getNome() + " (Atendente)",
                        atendente
                );
                return ResponseEntity.ok(response);

            } else {
                ResponseDto<Object> response = ResponseDto.erro("Email não encontrado no sistema");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("Credenciais inválidas") ||
                    e.getMessage().contains("desativada") ?
                    HttpStatus.UNAUTHORIZED : HttpStatus.BAD_REQUEST;

            ResponseDto<Object> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<Object> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Verifica se o email já está cadastrado
     * GET /api/auth/verificar-email?email={email}
     */
    @GetMapping("/verificar-email")
    @Operation(summary = "Verificar email", description = "Verifica se um email já está cadastrado no sistema")
    @ApiResponse(responseCode = "200", description = "Verificação realizada")
    public ResponseEntity<ResponseDto<Map<String, Object>>> verificarEmail(
            @Parameter(description = "Email para verificação") @RequestParam String email) {

        try {
            boolean emailCadastrado = authService.emailJaCadastrado(email);
            String tipoUsuario = emailCadastrado ? authService.identificarTipoUsuario(email) : "NAO_ENCONTRADO";

            Map<String, Object> resultado = Map.of(
                    "emailCadastrado", emailCadastrado,
                    "tipoUsuario", tipoUsuario
            );

            String mensagem = emailCadastrado ?
                    "Email já cadastrado como " + tipoUsuario :
                    "Email disponível para cadastro";

            ResponseDto<Map<String, Object>> response = ResponseDto.sucesso(mensagem, resultado);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            ResponseDto<Map<String, Object>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Valida credenciais sem realizar login
     * POST /api/auth/validar
     */
    @PostMapping("/validar")
    @Operation(summary = "Validar credenciais", description = "Valida credenciais sem retornar dados do usuário")
    @ApiResponse(responseCode = "200", description = "Credenciais válidas")
    @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
    public ResponseEntity<ResponseDto<Map<String, Boolean>>> validarCredenciais(
            @Valid @RequestBody LoginDto loginDto) {

        try {
            boolean loginValido = authService.validarLogin(loginDto);

            Map<String, Boolean> resultado = Map.of("credenciaisValidas", loginValido);

            if (loginValido) {
                ResponseDto<Map<String, Boolean>> response = ResponseDto.sucesso(
                        "Credenciais válidas", resultado
                );
                return ResponseEntity.ok(response);
            } else {
                ResponseDto<Map<String, Boolean>> response = ResponseDto.erro(
                        "Credenciais inválidas"
                );
                response.setDados(resultado);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

        } catch (Exception e) {
            ResponseDto<Map<String, Boolean>> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Atualiza senha do cliente
     * PUT /api/auth/senha/cliente
     */
    @PutMapping("/senha/cliente")
    @Operation(summary = "Atualizar senha do cliente", description = "Atualiza a senha de um cliente")
    @ApiResponse(responseCode = "200", description = "Senha atualizada com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    public ResponseEntity<ResponseDto<Void>> atualizarSenhaCliente(
            @RequestBody Map<String, String> dados) {

        try {
            String email = dados.get("email");
            String novaSenha = dados.get("novaSenha");

            if (email == null || novaSenha == null) {
                ResponseDto<Void> response = ResponseDto.erro("Email e nova senha são obrigatórios");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            authService.atualizarSenhaCliente(email, novaSenha);

            ResponseDto<Void> response = ResponseDto.sucesso("Senha atualizada com sucesso");
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            HttpStatus status = e.getMessage().contains("não encontrado") ?
                    HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;

            ResponseDto<Void> response = ResponseDto.erro(e.getMessage());
            return ResponseEntity.status(status).body(response);

        } catch (Exception e) {
            ResponseDto<Void> response = ResponseDto.erro("Erro interno do servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Logout (placeholder - para futuras implementações com sessões/tokens)
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    @Operation(summary = "Logout", description = "Realiza logout do usuário (placeholder)")
    @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso")
    public ResponseEntity<ResponseDto<Void>> logout() {
        ResponseDto<Void> response = ResponseDto.sucesso("Logout realizado com sucesso");
        return ResponseEntity.ok(response);
    }
}
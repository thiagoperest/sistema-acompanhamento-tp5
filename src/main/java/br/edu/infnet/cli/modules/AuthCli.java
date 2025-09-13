package br.edu.infnet.cli.modules;

import br.edu.infnet.controller.AuthController;
import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.dto.LoginDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.entity.AtendenteSuporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Scanner;

/**
 * CLI para operações relacionadas à Autenticação
 */
@Component
public class AuthCli {

    @Autowired
    private AuthController authController;

    private Scanner scanner;

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public ClienteDto realizarLoginCliente() {
        limparTela();
        System.out.println("=== LOGIN CLIENTE ===");
        System.out.println();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        try {
            LoginDto loginDto = new LoginDto(email, senha);
            ResponseEntity<ResponseDto<ClienteDto>> response = authController.loginCliente(loginDto);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ClienteDto cliente = response.getBody().getDados();
                System.out.println();
                System.out.println("Login realizado com sucesso!");
                System.out.println("Bem-vindo(a), " + cliente.getNome() + "!");
                pausa();
                return cliente;
            } else {
                System.out.println();
                System.out.println("Erro no login: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Credenciais inválidas"));
                pausa();
                return null;
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro no login: " + e.getMessage());
            pausa();
            return null;
        }
    }

    public AtendenteSuporte realizarLoginAtendente() {
        limparTela();
        System.out.println("=== LOGIN ATENDENTE ===");
        System.out.println();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        try {
            LoginDto loginDto = new LoginDto(email, senha);
            ResponseEntity<ResponseDto<AtendenteSuporte>> response = authController.loginAtendente(loginDto);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AtendenteSuporte atendente = response.getBody().getDados();
                System.out.println();
                System.out.println("Login realizado com sucesso!");
                System.out.println("Bem-vindo(a), " + atendente.getNome() + "!");
                System.out.println("Departamento: " + atendente.getDepartamento());
                pausa();
                return atendente;
            } else {
                System.out.println();
                System.out.println("Erro no login: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Credenciais inválidas"));
                pausa();
                return null;
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro no login: " + e.getMessage());
            pausa();
            return null;
        }
    }

    public void verificarEmail() {
        limparTela();
        System.out.println("=== VERIFICAR EMAIL ===");
        System.out.println();

        System.out.print("Digite o email para verificar: ");
        String email = scanner.nextLine().trim();

        try {
            ResponseEntity<ResponseDto<Map<String, Object>>> response = authController.verificarEmail(email);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> resultado = response.getBody().getDados();
                boolean emailCadastrado = (boolean) resultado.get("emailCadastrado");
                String tipoUsuario = (String) resultado.get("tipoUsuario");

                System.out.println();
                if (emailCadastrado) {
                    System.out.println("Email já cadastrado no sistema");
                    System.out.println("Tipo de usuário: " + tipoUsuario);
                } else {
                    System.out.println("Email não encontrado no sistema");
                    System.out.println("Este email está disponível para cadastro");
                }
            } else {
                System.out.println();
                System.out.println("Erro ao verificar email: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao verificar email: " + e.getMessage());
        }

        pausa();
    }

    public void realizarLogout() {
        limparTela();
        System.out.println("=== LOGOUT ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<Void>> response = authController.logout();
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Logout realizado com sucesso!");
                System.out.println("Obrigado por usar o sistema!");
            } else {
                System.out.println("Erro ao realizar logout");
            }
        } catch (Exception e) {
            System.out.println("Erro ao realizar logout: " + e.getMessage());
        }
        
        pausa();
    }

    private void limparTela() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[2J\033[H");
            }
        } catch (Exception e) {
            for (int i = 0; i < 50; i++) {
                System.out.println();
            }
        }
    }

    private void pausa() {
        System.out.println();
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
    }
}
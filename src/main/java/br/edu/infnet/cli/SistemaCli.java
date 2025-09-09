package br.edu.infnet.cli;

import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.dto.LoginDto;
import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.service.AuthService;
import br.edu.infnet.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Interface de linha de comando para o Sistema de Acompanhamento de Pedidos
 */
@Component
@Order(2) // Executa após a configuração do banco
public class SistemaCli implements CommandLineRunner {

    @Autowired
    private AuthService authService;

    @Autowired
    private ClienteService clienteService;

    private Scanner scanner;
    private ClienteDto clienteLogado;
    private AtendenteSuporte atendenteLogado;

    @Override
    public void run(String... args) throws Exception {
        scanner = new Scanner(System.in);

        exibirBemVindo();

        boolean continuar = true;
        while (continuar) {
            try {
                if (clienteLogado == null && atendenteLogado == null) {
                    continuar = exibirMenuPrincipal();
                } else if (clienteLogado != null) {
                    continuar = exibirMenuCliente();
                } else {
                    continuar = exibirMenuAtendente();
                }
            } catch (Exception e) {
                System.err.println("Erro inesperado: " + e.getMessage());
                System.out.println("Pressione Enter para continuar...");
                scanner.nextLine();
            }
        }

        scanner.close();
        System.out.println("\nObrigado por usar o Sistema de Acompanhamento de Pedidos!");
        System.out.println("Até logo! 👋");
    }

    private void exibirBemVindo() {
        limparTela();
        System.out.println("╔══════════════════════════════════════════════════════╗");
        System.out.println("║          SISTEMA DE ACOMPANHAMENTO DE PEDIDOS        ║");
        System.out.println("║                                                      ║");
        System.out.println("║              Instituto Infnet                        ║");
        System.out.println("║              Thiago Teodoro Peres                    ║");
        System.out.println("║                                                      ║");
        System.out.println("║  Bem-vindo ao sistema de acompanhamento de pedidos!  ║");
        System.out.println("║              Versão: 1.0.0                           ║");
        System.out.println("╚══════════════════════════════════════════════════════╝");
        System.out.println();
    }

    private boolean exibirMenuPrincipal() {
        limparTela();
        System.out.println("=== MENU PRINCIPAL ===");
        System.out.println();
        System.out.println("1. Login Cliente");
        System.out.println("2. Login Atendente");
        System.out.println("3. Registrar Novo Cliente");
        System.out.println("4. Verificar Email");
        System.out.println("5. Listar Clientes (Demo)");
        System.out.println("6. Documentação da API (Swagger)");
        System.out.println("0. Sair");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    realizarLoginCliente();
                    break;
                case 2:
                    realizarLoginAtendente();
                    break;
                case 3:
                    registrarNovoCliente();
                    break;
                case 4:
                    verificarEmail();
                    break;
                case 5:
                    listarClientes();
                    break;
                case 6:
                    exibirInformacoesSwagger();
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    pausa();
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
            pausa();
        }

        return true;
    }

    private boolean exibirMenuCliente() {
        limparTela();
        System.out.println("=== ÁREA DO CLIENTE ===");
        System.out.println("Bem-vindo(a), " + clienteLogado.getNome() + "!");
        System.out.println("Email: " + clienteLogado.getEmail());
        System.out.println();
        System.out.println("1. Ver Meus Dados");
        System.out.println("2. Atualizar Meus Dados");
        System.out.println("3. Meus Pedidos (Pendente)");
        System.out.println("4. Notificações (Pendente)");
        System.out.println("5. Suporte (Pendente)");
        System.out.println("0. Logout");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    exibirDadosCliente();
                    break;
                case 2:
                    atualizarDadosCliente();
                    break;
                case 3:
                case 4:
                case 5:
                    System.out.println("Funcionalidade será implementada nas próximas sprints!");
                    pausa();
                    break;
                case 0:
                    realizarLogout();
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    pausa();
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
            pausa();
        }

        return true;
    }

    private boolean exibirMenuAtendente() {
        limparTela();
        System.out.println("=== ÁREA DO ATENDENTE ===");
        System.out.println("Bem-vindo(a), " + atendenteLogado.getNome() + "!");
        System.out.println("Departamento: " + atendenteLogado.getDepartamento());
        System.out.println("Matrícula: " + atendenteLogado.getMatricula());
        System.out.println();
        System.out.println("1. Listar Clientes");
        System.out.println("2. Buscar Cliente");
        System.out.println("3. Pedidos (Pendente)");
        System.out.println("4. Suporte (Pendente)");
        System.out.println("0. Logout");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listarClientes();
                    break;
                case 2:
                    buscarCliente();
                    break;
                case 3:
                case 4:
                    System.out.println("Funcionalidade será implementada nas próximas sprints!");
                    pausa();
                    break;
                case 0:
                    realizarLogout();
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
                    pausa();
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
            pausa();
        }

        return true;
    }

    private void realizarLoginCliente() {
        limparTela();
        System.out.println("=== LOGIN CLIENTE ===");
        System.out.println();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        try {
            LoginDto loginDto = new LoginDto(email, senha);
            ClienteDto cliente = authService.loginCliente(loginDto);

            clienteLogado = cliente;
            System.out.println();
            System.out.println("Login realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + cliente.getNome() + "!");
            pausa();

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro no login: " + e.getMessage());
            pausa();
        }
    }

    private void realizarLoginAtendente() {
        limparTela();
        System.out.println("=== LOGIN ATENDENTE ===");
        System.out.println();

        System.out.print("Email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        try {
            LoginDto loginDto = new LoginDto(email, senha);
            AtendenteSuporte atendente = authService.loginAtendente(loginDto);

            atendenteLogado = atendente;
            System.out.println();
            System.out.println("Login realizado com sucesso!");
            System.out.println("Bem-vindo(a), " + atendente.getNome() + "!");
            System.out.println("Departamento: " + atendente.getDepartamento());
            pausa();

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro no login: " + e.getMessage());
            pausa();
        }
    }

    private void registrarNovoCliente() {
        limparTela();
        System.out.println("=== REGISTRAR NOVO CLIENTE ===");
        System.out.println();

        try {
            System.out.print("Nome completo: ");
            String nome = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Telefone (opcional): ");
            String telefone = scanner.nextLine().trim();

            System.out.print("CPF (11 dígitos): ");
            String cpf = scanner.nextLine().trim();

            System.out.print("Endereço: ");
            String endereco = scanner.nextLine().trim();

            System.out.print("Cidade: ");
            String cidade = scanner.nextLine().trim();

            System.out.print("CEP (8 dígitos): ");
            String cep = scanner.nextLine().trim();

            System.out.print("Senha (mínimo 8 caracteres): ");
            String senha = scanner.nextLine().trim();

            ClienteDto clienteDto = new ClienteDto(nome, email, telefone, cpf, endereco, cidade, cep, senha);

            ClienteDto clienteRegistrado = clienteService.registrarCliente(clienteDto);

            System.out.println();
            System.out.println("Cliente registrado com sucesso!");
            System.out.println("ID: " + clienteRegistrado.getId());
            System.out.println("Nome: " + clienteRegistrado.getNome());
            System.out.println("Email: " + clienteRegistrado.getEmail());

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao registrar cliente: " + e.getMessage());
        }

        pausa();
    }

    private void exibirDadosCliente() {
        limparTela();
        System.out.println("=== MEUS DADOS ===");
        System.out.println();

        System.out.printf("%-15s: %s%n", "ID", clienteLogado.getId());
        System.out.printf("%-15s: %s%n", "Nome", clienteLogado.getNome());
        System.out.printf("%-15s: %s%n", "Email", clienteLogado.getEmail());
        System.out.printf("%-15s: %s%n", "Telefone", clienteLogado.getTelefone() != null ? clienteLogado.getTelefone() : "Não informado");
        System.out.printf("%-15s: %s%n", "CPF", clienteLogado.getCpf());
        System.out.printf("%-15s: %s%n", "Endereço", clienteLogado.getEndereco());
        System.out.printf("%-15s: %s%n", "Cidade", clienteLogado.getCidade());
        System.out.printf("%-15s: %s%n", "CEP", clienteLogado.getCep());
        System.out.printf("%-15s: %s%n", "Cadastrado em", clienteLogado.getDataCadastro());
        System.out.printf("%-15s: %s%n", "Status", clienteLogado.getAtivo() ? "Ativo" : "Inativo");

        pausa();
    }

    private void atualizarDadosCliente() {
        limparTela();
        System.out.println("=== ATUALIZAR MEUS DADOS ===");
        System.out.println("Deixe em branco para manter o valor atual");
        System.out.println();

        try {
            System.out.print("Nome (" + clienteLogado.getNome() + "): ");
            String nome = scanner.nextLine().trim();
            if (nome.isEmpty()) nome = clienteLogado.getNome();

            System.out.print("Email (" + clienteLogado.getEmail() + "): ");
            String email = scanner.nextLine().trim();
            if (email.isEmpty()) email = clienteLogado.getEmail();

            System.out.print("Telefone (" + (clienteLogado.getTelefone() != null ? clienteLogado.getTelefone() : "Não informado") + "): ");
            String telefone = scanner.nextLine().trim();
            if (telefone.isEmpty()) telefone = clienteLogado.getTelefone();

            System.out.print("CPF (" + clienteLogado.getCpf() + "): ");
            String cpf = scanner.nextLine().trim();
            if (cpf.isEmpty()) cpf = clienteLogado.getCpf();

            System.out.print("Endereço (" + clienteLogado.getEndereco() + "): ");
            String endereco = scanner.nextLine().trim();
            if (endereco.isEmpty()) endereco = clienteLogado.getEndereco();

            System.out.print("Cidade (" + clienteLogado.getCidade() + "): ");
            String cidade = scanner.nextLine().trim();
            if (cidade.isEmpty()) cidade = clienteLogado.getCidade();

            System.out.print("CEP (" + clienteLogado.getCep() + "): ");
            String cep = scanner.nextLine().trim();
            if (cep.isEmpty()) cep = clienteLogado.getCep();

            ClienteDto clienteAtualizado = new ClienteDto();
            clienteAtualizado.setNome(nome);
            clienteAtualizado.setEmail(email);
            clienteAtualizado.setTelefone(telefone);
            clienteAtualizado.setCpf(cpf);
            clienteAtualizado.setEndereco(endereco);
            clienteAtualizado.setCidade(cidade);
            clienteAtualizado.setCep(cep);

            ClienteDto clienteResult = clienteService.atualizarCliente(clienteLogado.getId(), clienteAtualizado);

            clienteLogado = clienteResult;

            System.out.println();
            System.out.println("Dados atualizados com sucesso!");

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao atualizar dados: " + e.getMessage());
        }

        pausa();
    }

    private void verificarEmail() {
        limparTela();
        System.out.println("=== VERIFICAR EMAIL ===");
        System.out.println();

        System.out.print("Digite o email para verificar: ");
        String email = scanner.nextLine().trim();

        try {
            boolean emailCadastrado = authService.emailJaCadastrado(email);
            String tipoUsuario = emailCadastrado ? authService.identificarTipoUsuario(email) : "NAO_ENCONTRADO";

            System.out.println();
            if (emailCadastrado) {
                System.out.println("Email já cadastrado no sistema");
                System.out.println("Tipo de usuário: " + tipoUsuario);
            } else {
                System.out.println("Email não encontrado no sistema");
                System.out.println("Este email está disponível para cadastro");
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao verificar email: " + e.getMessage());
        }

        pausa();
    }

    private void listarClientes() {
        limparTela();
        System.out.println("=== LISTA DE CLIENTES ===");
        System.out.println();

        try {
            List<ClienteDto> clientes = clienteService.listarClientesAtivos();

            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado.");
            } else {
                System.out.printf("%-5s %-25s %-30s %-15s %-20s%n", "ID", "Nome", "Email", "Telefone", "Cidade");
                System.out.println("─".repeat(100));

                for (ClienteDto cliente : clientes) {
                    System.out.printf("%-5d %-25s %-30s %-15s %-20s%n",
                            cliente.getId(),
                            cliente.getNome().length() > 24 ? cliente.getNome().substring(0, 21) + "..." : cliente.getNome(),
                            cliente.getEmail().length() > 29 ? cliente.getEmail().substring(0, 26) + "..." : cliente.getEmail(),
                            cliente.getTelefone() != null ? cliente.getTelefone() : "N/A",
                            cliente.getCidade().length() > 19 ? cliente.getCidade().substring(0, 16) + "..." : cliente.getCidade()
                    );
                }

                System.out.println("─".repeat(100));
                System.out.println("Total de clientes: " + clientes.size());
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        pausa();
    }

    private void buscarCliente() {
        limparTela();
        System.out.println("=== BUSCAR CLIENTE ===");
        System.out.println();
        System.out.println("1. Buscar por ID");
        System.out.println("2. Buscar por Email");
        System.out.println("3. Buscar por Nome");
        System.out.println("4. Buscar por Cidade");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            switch (opcao) {
                case 1:
                    buscarClientePorId();
                    break;
                case 2:
                    buscarClientePorEmail();
                    break;
                case 3:
                    buscarClientePorNome();
                    break;
                case 4:
                    buscarClientePorCidade();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Opção inválida!");
                    pausa();
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
            pausa();
        }
    }

    private void buscarClientePorId() {
        System.out.print("Digite o ID do cliente: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<ClienteDto> clienteOpt = clienteService.buscarPorId(id);

            System.out.println();
            if (clienteOpt.isPresent()) {
                exibirDetalhesCliente(clienteOpt.get());
            } else {
                System.out.println("Cliente não encontrado com ID: " + id);
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }

        pausa();
    }

    private void buscarClientePorEmail() {
        System.out.print("Digite o email do cliente: ");
        String email = scanner.nextLine().trim();

        try {
            Optional<ClienteDto> clienteOpt = clienteService.buscarPorEmail(email);

            System.out.println();
            if (clienteOpt.isPresent()) {
                exibirDetalhesCliente(clienteOpt.get());
            } else {
                System.out.println("Cliente não encontrado com email: " + email);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar cliente: " + e.getMessage());
        }

        pausa();
    }

    private void buscarClientePorNome() {
        System.out.print("Digite o nome (ou parte do nome) do cliente: ");
        String nome = scanner.nextLine().trim();

        try {
            List<ClienteDto> clientes = clienteService.buscarPorNome(nome);

            System.out.println();
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado com nome: " + nome);
            } else {
                System.out.println("Clientes encontrados: " + clientes.size());
                System.out.println();
                System.out.printf("%-5s %-25s %-30s %-15s %-20s%n", "ID", "Nome", "Email", "Telefone", "Cidade");
                System.out.println("-".repeat(100));

                for (ClienteDto cliente : clientes) {
                    System.out.printf("%-5d %-25s %-30s %-15s %-20s%n",
                            cliente.getId(),
                            cliente.getNome().length() > 24 ? cliente.getNome().substring(0, 21) + "..." : cliente.getNome(),
                            cliente.getEmail().length() > 29 ? cliente.getEmail().substring(0, 26) + "..." : cliente.getEmail(),
                            cliente.getTelefone() != null ? cliente.getTelefone() : "N/A",
                            cliente.getCidade().length() > 19 ? cliente.getCidade().substring(0, 16) + "..." : cliente.getCidade()
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar clientes: " + e.getMessage());
        }

        pausa();
    }

    private void buscarClientePorCidade() {
        System.out.print("Digite a cidade: ");
        String cidade = scanner.nextLine().trim();

        try {
            List<ClienteDto> clientes = clienteService.buscarPorCidade(cidade);

            System.out.println();
            if (clientes.isEmpty()) {
                System.out.println("Nenhum cliente encontrado na cidade: " + cidade);
            } else {
                System.out.println("Clientes encontrados em " + cidade + ": " + clientes.size());
                System.out.println();
                System.out.printf("%-5s %-25s %-30s %-15s%n", "ID", "Nome", "Email", "Telefone");
                System.out.println("-".repeat(80));

                for (ClienteDto cliente : clientes) {
                    System.out.printf("%-5d %-25s %-30s %-15s%n",
                            cliente.getId(),
                            cliente.getNome().length() > 24 ? cliente.getNome().substring(0, 21) + "..." : cliente.getNome(),
                            cliente.getEmail().length() > 29 ? cliente.getEmail().substring(0, 26) + "..." : cliente.getEmail(),
                            cliente.getTelefone() != null ? cliente.getTelefone() : "N/A"
                    );
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar clientes: " + e.getMessage());
        }

        pausa();
    }

    private void exibirDetalhesCliente(ClienteDto cliente) {
        System.out.println("=== DETALHES DO CLIENTE ===");
        System.out.println();
        System.out.printf("%-15s: %s%n", "ID", cliente.getId());
        System.out.printf("%-15s: %s%n", "Nome", cliente.getNome());
        System.out.printf("%-15s: %s%n", "Email", cliente.getEmail());
        System.out.printf("%-15s: %s%n", "Telefone", cliente.getTelefone() != null ? cliente.getTelefone() : "Não informado");
        System.out.printf("%-15s: %s%n", "CPF", cliente.getCpf());
        System.out.printf("%-15s: %s%n", "Endereço", cliente.getEndereco());
        System.out.printf("%-15s: %s%n", "Cidade", cliente.getCidade());
        System.out.printf("%-15s: %s%n", "CEP", cliente.getCep());
        System.out.printf("%-15s: %s%n", "Cadastrado em", cliente.getDataCadastro());
        System.out.printf("%-15s: %s%n", "Status", cliente.getAtivo() ? "Ativo" : "Inativo");
    }

    private void exibirInformacoesSwagger() {
        limparTela();
        System.out.println("=== DOCUMENTAÇÃO DA API ===");
        System.out.println();
        System.out.println("A documentação completa da API está disponível através do Swagger UI.");
        System.out.println();
        System.out.println("Como acessar:");
        System.out.println("1. Certifique-se de que a aplicação está rodando");
        System.out.println("2. Abra seu navegador web");
        System.out.println("3. Acesse: http://localhost:8080/api/swagger-ui.html");
        System.out.println();
        System.out.println("Através do Swagger você pode:");
        System.out.println("- Ver todos os endpoints disponíveis");
        System.out.println("- Testar as APIs diretamente pelo navegador");
        System.out.println("- Ver exemplos de requisições e respostas");
        System.out.println("- Baixar a especificação OpenAPI");
        System.out.println();
        System.out.println("Endpoints principais implementados no Sprint 1:");
        System.out.println("- POST /api/auth/login - Login genérico");
        System.out.println("- POST /api/auth/login/cliente - Login de cliente");
        System.out.println("- POST /api/auth/login/atendente - Login de atendente");
        System.out.println("- POST /api/clientes - Registrar novo cliente");
        System.out.println("- GET /api/clientes - Listar clientes");
        System.out.println("- GET /api/clientes/{id} - Buscar cliente por ID");
        System.out.println("- PUT /api/clientes/{id} - Atualizar cliente");
        System.out.println();
        System.out.println("Console H2 Database:");
        System.out.println("- Acesse: http://localhost:8080/api/h2-console");
        System.out.println("- JDBC URL: jdbc:h2:mem:testdb");
        System.out.println("- Username: sa");
        System.out.println("- Password: (deixe em branco)");

        pausa();
    }

    private void realizarLogout() {
        clienteLogado = null;
        atendenteLogado = null;

        limparTela();
        System.out.println("=== LOGOUT ===");
        System.out.println();
        System.out.println("Logout realizado com sucesso!");
        System.out.println("Obrigado por usar o sistema!");
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
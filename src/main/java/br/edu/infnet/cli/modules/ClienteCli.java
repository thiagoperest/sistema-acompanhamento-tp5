package br.edu.infnet.cli.modules;

import br.edu.infnet.controller.ClienteController;
import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * CLI para operações relacionadas aos Clientes
 */
@Component
public class ClienteCli {

    @Autowired
    private ClienteController clienteController;

    private Scanner scanner;

    public void setScanner(Scanner scanner) {
        this.scanner = scanner;
    }

    public void registrarNovoCliente() {
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

            ResponseEntity<ResponseDto<ClienteDto>> response = clienteController.registrarCliente(clienteDto);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ClienteDto clienteRegistrado = response.getBody().getDados();
                System.out.println();
                System.out.println("Cliente registrado com sucesso!");
                System.out.println("ID: " + clienteRegistrado.getId());
                System.out.println("Nome: " + clienteRegistrado.getNome());
                System.out.println("Email: " + clienteRegistrado.getEmail());
            } else {
                System.out.println();
                System.out.println("Erro ao registrar cliente: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao registrar cliente: " + e.getMessage());
        }

        pausa();
    }

    public void exibirDadosCliente(ClienteDto clienteLogado) {
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

    public ClienteDto atualizarDadosCliente(ClienteDto clienteLogado) {
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

            ResponseEntity<ResponseDto<ClienteDto>> response = 
                clienteController.atualizarCliente(clienteLogado.getId(), clienteAtualizado);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ClienteDto clienteResult = response.getBody().getDados();
                System.out.println();
                System.out.println("Dados atualizados com sucesso!");
                pausa();
                return clienteResult;
            } else {
                System.out.println();
                System.out.println("Erro ao atualizar dados: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
                pausa();
                return clienteLogado;
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao atualizar dados: " + e.getMessage());
            pausa();
            return clienteLogado;
        }
    }

    public void listarClientes() {
        limparTela();
        System.out.println("=== LISTA DE CLIENTES ===");
        System.out.println();

        try {
            ResponseEntity<ResponseDto<List<ClienteDto>>> response = clienteController.listarClientes();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<ClienteDto> clientes = response.getBody().getDados();

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
            } else {
                System.out.println("Erro ao listar clientes: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar clientes: " + e.getMessage());
        }

        pausa();
    }

    public void buscarCliente() {
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

            ResponseEntity<ResponseDto<ClienteDto>> response = clienteController.buscarPorId(id);

            System.out.println();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ClienteDto cliente = response.getBody().getDados();
                exibirDetalhesCliente(cliente);
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
            ResponseEntity<ResponseDto<ClienteDto>> response = clienteController.buscarPorEmail(email);

            System.out.println();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                ClienteDto cliente = response.getBody().getDados();
                exibirDetalhesCliente(cliente);
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
            ResponseEntity<ResponseDto<List<ClienteDto>>> response = clienteController.buscarPorNome(nome);

            System.out.println();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<ClienteDto> clientes = response.getBody().getDados();
                
                if (clientes.isEmpty()) {
                    System.out.println("Nenhum cliente encontrado com nome: " + nome);
                } else {
                    System.out.println("Clientes encontrados: " + clientes.size());
                    System.out.println();
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
                }
            } else {
                System.out.println("Erro ao buscar clientes: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
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
            ResponseEntity<ResponseDto<List<ClienteDto>>> response = clienteController.buscarPorCidade(cidade);

            System.out.println();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<ClienteDto> clientes = response.getBody().getDados();
                
                if (clientes.isEmpty()) {
                    System.out.println("Nenhum cliente encontrado na cidade: " + cidade);
                } else {
                    System.out.println("Clientes encontrados em " + cidade + ": " + clientes.size());
                    System.out.println();
                    System.out.printf("%-5s %-25s %-30s %-15s%n", "ID", "Nome", "Email", "Telefone");
                    System.out.println("─".repeat(80));

                    for (ClienteDto cliente : clientes) {
                        System.out.printf("%-5d %-25s %-30s %-15s%n",
                                cliente.getId(),
                                cliente.getNome().length() > 24 ? cliente.getNome().substring(0, 21) + "..." : cliente.getNome(),
                                cliente.getEmail().length() > 29 ? cliente.getEmail().substring(0, 26) + "..." : cliente.getEmail(),
                                cliente.getTelefone() != null ? cliente.getTelefone() : "N/A"
                        );
                    }
                }
            } else {
                System.out.println("Erro ao buscar clientes: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar clientes: " + e.getMessage());
        }

        pausa();
    }


    public void exibirDetalhesCliente(ClienteDto cliente) {
        System.out.println();
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
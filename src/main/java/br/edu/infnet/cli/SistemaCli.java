package br.edu.infnet.cli;

import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.dto.HistoricoStatusDto;
import br.edu.infnet.model.dto.LoginDto;
import br.edu.infnet.model.dto.NotificacaoDto;
import br.edu.infnet.model.dto.PedidoDto;
import br.edu.infnet.model.dto.PreferenciasNotificacaoDto;
import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.model.enums.StatusPedido;
import br.edu.infnet.model.enums.TipoNotificacao;
import br.edu.infnet.service.AuthService;
import br.edu.infnet.service.ClienteService;
import br.edu.infnet.service.NotificacaoService;
import br.edu.infnet.service.PedidoService;
import br.edu.infnet.service.PreferenciasNotificacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
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

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private NotificacaoService notificacaoService;

    @Autowired
    private PreferenciasNotificacaoService preferenciasNotificacaoService;

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
        System.out.println("Ate logo!");
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
        System.out.println("6. Consultar Pedido por Número (Público)");
        System.out.println("7. Documentação da API (Swagger)");
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
                    consultarPedidoPublico();
                    break;
                case 7:
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
        System.out.println("3. Meus Pedidos");
        System.out.println("4. Criar Novo Pedido");
        System.out.println("5. Buscar Pedido");
        System.out.println("6. Acompanhar Pedido");
        System.out.println("7. Gerenciar Notificações");
        System.out.println("8. Suporte (Pendente)");
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
                    listarMeusPedidos();
                    break;
                case 4:
                    criarNovoPedido();
                    break;
                case 5:
                    buscarMeuPedido();
                    break;
                case 6:
                    acompanharPedido();
                    break;
                case 7:
                    gerenciarNotificacoesCliente();
                    break;
                case 8:
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
        System.out.println("3. Gerenciar Pedidos");
        System.out.println("4. Buscar Pedidos");
        System.out.println("5. Atualizar Status de Pedido");
        System.out.println("6. Estatísticas de Pedidos");
        System.out.println("7. Gerenciar Notificações");
        System.out.println("8. Suporte (Pendente Sprint 4)");
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
                    gerenciarPedidos();
                    break;
                case 4:
                    buscarPedidos();
                    break;
                case 5:
                    atualizarStatusPedido();
                    break;
                case 6:
                    exibirEstatisticasPedidos();
                    break;
                case 7:
                    gerenciarNotificacoesAtendente();
                    break;
                case 8:
                    System.out.println("Funcionalidade será implementada na Sprint 4!");
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

    private void listarMeusPedidos() {
        limparTela();
        System.out.println("=== MEUS PEDIDOS ===");
        System.out.println();

        try {
            List<PedidoDto> pedidos = pedidoService.listarPedidosPorCliente(clienteLogado.getId());

            if (pedidos.isEmpty()) {
                System.out.println("Você ainda não possui pedidos.");
            } else {
                System.out.printf("%-5s %-15s %-15s %-15s %-20s%n",
                        "ID", "Número", "Data Compra", "Valor", "Status");
                System.out.println("─".repeat(80));

                for (PedidoDto pedido : pedidos) {
                    System.out.printf("%-5d %-15s %-15s R$ %-10.2f %-20s%n",
                            pedido.getId(),
                            pedido.getNumeroPedido(),
                            pedido.getDataCompra().substring(0, 10),
                            pedido.getValor(),
                            pedido.getStatus().getDescricao()
                    );
                }

                System.out.println("─".repeat(80));
                System.out.println("Total de pedidos: " + pedidos.size());
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }

        pausa();
    }

    private void criarNovoPedido() {
        limparTela();
        System.out.println("=== CRIAR NOVO PEDIDO ===");
        System.out.println();

        try {
            System.out.print("Valor do pedido (R$): ");
            BigDecimal valor = scanner.nextBigDecimal();
            scanner.nextLine();

            System.out.print("Observações (opcional): ");
            String observacoes = scanner.nextLine().trim();

            System.out.print("Previsão de entrega (dd/mm/aaaa - opcional): ");
            String previsaoEntrega = scanner.nextLine().trim();

            PedidoDto novoPedido = new PedidoDto();
            novoPedido.setClienteId(clienteLogado.getId());
            novoPedido.setValor(valor);

            if (!observacoes.isEmpty()) {
                novoPedido.setObservacoes(observacoes);
            }

            if (!previsaoEntrega.isEmpty() && previsaoEntrega.matches("\\d{2}/\\d{2}/\\d{4}")) {
                novoPedido.setPrevisaoEntrega(previsaoEntrega);
            }

            PedidoDto pedidoCriado = pedidoService.criarPedido(novoPedido);

            System.out.println();
            System.out.println("Pedido criado com sucesso!");
            System.out.println("Número do pedido: " + pedidoCriado.getNumeroPedido());
            System.out.println("ID: " + pedidoCriado.getId());
            System.out.println("Valor: R$ " + pedidoCriado.getValor());
            System.out.println("Status: " + pedidoCriado.getStatus().getDescricao());

        } catch (InputMismatchException e) {
            System.out.println("Valor inválido! Digite um número decimal.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao criar pedido: " + e.getMessage());
        }

        pausa();
    }

    private void buscarMeuPedido() {
        limparTela();
        System.out.println("=== BUSCAR MEU PEDIDO ===");
        System.out.println();
        System.out.println("1. Buscar por ID");
        System.out.println("2. Buscar por Número do Pedido");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarMeuPedidoPorId();
                    break;
                case 2:
                    buscarMeuPedidoPorNumero();
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

    private void buscarMeuPedidoPorId() {
        System.out.print("Digite o ID do pedido: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();

            Optional<PedidoDto> pedidoOpt = pedidoService.buscarPorId(id);

            System.out.println();
            if (pedidoOpt.isPresent()) {
                PedidoDto pedido = pedidoOpt.get();

                // Verificar se o pedido pertence ao cliente logado
                if (!pedido.getClienteId().equals(clienteLogado.getId())) {
                    System.out.println("Pedido não encontrado ou não pertence a você.");
                } else {
                    exibirDetalhesPedido(pedido);
                }
            } else {
                System.out.println("Pedido não encontrado com ID: " + id);
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar pedido: " + e.getMessage());
        }

        pausa();
    }

    private void buscarMeuPedidoPorNumero() {
        System.out.print("Digite o número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            Optional<PedidoDto> pedidoOpt = pedidoService.buscarPorNumeroPedido(numeroPedido);

            System.out.println();
            if (pedidoOpt.isPresent()) {
                PedidoDto pedido = pedidoOpt.get();

                // Verificar se o pedido pertence ao cliente logado
                if (!pedido.getClienteId().equals(clienteLogado.getId())) {
                    System.out.println("Pedido não encontrado ou não pertence a você.");
                } else {
                    exibirDetalhesPedido(pedido);
                }
            } else {
                System.out.println("Pedido não encontrado com número: " + numeroPedido);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar pedido: " + e.getMessage());
        }

        pausa();
    }

    private void acompanharPedido() {
        limparTela();
        System.out.println("=== ACOMPANHAR PEDIDO ===");
        System.out.println();

        System.out.print("Digite o número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            Optional<PedidoDto> pedidoOpt = pedidoService.buscarPorNumeroPedido(numeroPedido);

            if (pedidoOpt.isPresent()) {
                PedidoDto pedido = pedidoOpt.get();

                // Verificar se o pedido pertence ao cliente logado
                if (!pedido.getClienteId().equals(clienteLogado.getId())) {
                    System.out.println("Pedido não encontrado ou não pertence a você.");
                } else {
                    exibirDetalhesPedido(pedido);
                    System.out.println();
                    exibirHistoricoPedido(pedido.getId());
                }
            } else {
                System.out.println("Pedido não encontrado com número: " + numeroPedido);
            }
        } catch (Exception e) {
            System.out.println("Erro ao acompanhar pedido: " + e.getMessage());
        }

        pausa();
    }

    private void consultarPedidoPublico() {
        limparTela();
        System.out.println("=== CONSULTA PÚBLICA DE PEDIDO ===");
        System.out.println();

        System.out.print("Digite o número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            Optional<PedidoDto> pedidoOpt = pedidoService.buscarPorNumeroPedido(numeroPedido);

            System.out.println();
            if (pedidoOpt.isPresent()) {
                PedidoDto pedido = pedidoOpt.get();

                System.out.println("=== INFORMAÇÕES DO PEDIDO ===");
                System.out.println();
                System.out.printf("%-20s: %s%n", "Número do Pedido", pedido.getNumeroPedido());
                System.out.printf("%-20s: %s%n", "Data da Compra", pedido.getDataCompra());
                System.out.printf("%-20s: R$ %.2f%n", "Valor", pedido.getValor());
                System.out.printf("%-20s: %s%n", "Status Atual", pedido.getStatus().getDescricao());

                if (pedido.getPrevisaoEntrega() != null) {
                    System.out.printf("%-20s: %s%n", "Previsão Entrega", pedido.getPrevisaoEntrega());
                }

                System.out.printf("%-20s: %d dias%n", "Dias desde compra", pedido.getDiasDesdeCompra());

            } else {
                System.out.println("Pedido não encontrado com número: " + numeroPedido);
            }
        } catch (Exception e) {
            System.out.println("Erro ao consultar pedido: " + e.getMessage());
        }

        pausa();
    }

    private void gerenciarPedidos() {
        limparTela();
        System.out.println("=== GERENCIAR PEDIDOS ===");
        System.out.println();

        try {
            List<PedidoDto> pedidos = pedidoService.listarTodosPedidos();

            if (pedidos.isEmpty()) {
                System.out.println("Nenhum pedido encontrado.");
            } else {
                System.out.printf("%-5s %-15s %-25s %-15s %-20s%n",
                        "ID", "Número", "Cliente", "Valor", "Status");
                System.out.println("─".repeat(90));

                for (PedidoDto pedido : pedidos) {
                    String nomeCliente = pedido.getNomeCliente();
                    if (nomeCliente.length() > 24) {
                        nomeCliente = nomeCliente.substring(0, 21) + "...";
                    }

                    System.out.printf("%-5d %-15s %-25s R$ %-10.2f %-20s%n",
                            pedido.getId(),
                            pedido.getNumeroPedido(),
                            nomeCliente,
                            pedido.getValor(),
                            pedido.getStatus().getDescricao()
                    );
                }

                System.out.println("─".repeat(90));
                System.out.println("Total de pedidos: " + pedidos.size());
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }

        pausa();
    }

    private void buscarPedidos() {
        limparTela();
        System.out.println("=== BUSCAR PEDIDOS ===");
        System.out.println();
        System.out.println("1. Buscar por Status");
        System.out.println("2. Buscar por Número");
        System.out.println("3. Buscar por Nome do Cliente");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    buscarPedidosPorStatus();
                    break;
                case 2:
                    buscarPedidosPorNumero();
                    break;
                case 3:
                    buscarPedidosPorCliente();
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

    private void buscarPedidosPorStatus() {
        System.out.println("Status disponíveis:");
        StatusPedido[] statuses = StatusPedido.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i] + " - " + statuses[i].getDescricao());
        }

        System.out.print("Escolha o status (1-" + statuses.length + "): ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao >= 1 && opcao <= statuses.length) {
                StatusPedido status = statuses[opcao - 1];

                List<PedidoDto> pedidos = pedidoService.listarPedidosPorStatus(status);

                System.out.println();
                System.out.println("Pedidos com status: " + status.getDescricao());
                System.out.println("Total encontrado: " + pedidos.size());
                System.out.println();

                if (!pedidos.isEmpty()) {
                    for (PedidoDto pedido : pedidos) {
                        System.out.printf("ID: %d | Número: %s | Cliente: %s | Valor: R$ %.2f%n",
                                pedido.getId(), pedido.getNumeroPedido(),
                                pedido.getNomeCliente(), pedido.getValor());
                    }
                }
            } else {
                System.out.println("Opção inválida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar pedidos: " + e.getMessage());
        }

        pausa();
    }

    private void buscarPedidosPorNumero() {
        System.out.print("Digite parte do número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            List<PedidoDto> pedidos = pedidoService.buscarComFiltros(null, null, numeroPedido, null);

            System.out.println();
            System.out.println("Pedidos encontrados: " + pedidos.size());
            System.out.println();

            if (!pedidos.isEmpty()) {
                for (PedidoDto pedido : pedidos) {
                    System.out.printf("ID: %d | Número: %s | Cliente: %s | Status: %s%n",
                            pedido.getId(), pedido.getNumeroPedido(),
                            pedido.getNomeCliente(), pedido.getStatus().getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar pedidos: " + e.getMessage());
        }

        pausa();
    }

    private void buscarPedidosPorCliente() {
        System.out.print("Digite parte do nome do cliente: ");
        String nomeCliente = scanner.nextLine().trim();

        try {
            List<PedidoDto> pedidos = pedidoService.buscarComFiltros(null, null, null, nomeCliente);

            System.out.println();
            System.out.println("Pedidos encontrados: " + pedidos.size());
            System.out.println();

            if (!pedidos.isEmpty()) {
                for (PedidoDto pedido : pedidos) {
                    System.out.printf("ID: %d | Número: %s | Cliente: %s | Status: %s%n",
                            pedido.getId(), pedido.getNumeroPedido(),
                            pedido.getNomeCliente(), pedido.getStatus().getDescricao());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar pedidos: " + e.getMessage());
        }

        pausa();
    }

    private void atualizarStatusPedido() {
        limparTela();
        System.out.println("=== ATUALIZAR STATUS DO PEDIDO ===");
        System.out.println();

        System.out.print("Digite o ID do pedido: ");
        try {
            Long pedidoId = scanner.nextLong();
            scanner.nextLine();

            // Buscar o pedido
            Optional<PedidoDto> pedidoOpt = pedidoService.buscarPorId(pedidoId);

            if (pedidoOpt.isEmpty()) {
                System.out.println("Pedido não encontrado com ID: " + pedidoId);
                pausa();
                return;
            }

            PedidoDto pedido = pedidoOpt.get();

            System.out.println();
            System.out.println("Pedido encontrado:");
            System.out.println("Número: " + pedido.getNumeroPedido());
            System.out.println("Cliente: " + pedido.getNomeCliente());
            System.out.println("Status atual: " + pedido.getStatus().getDescricao());
            System.out.println();

            // Exibir status disponíveis
            System.out.println("Novos status disponíveis:");
            StatusPedido[] statuses = StatusPedido.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i] + " - " + statuses[i].getDescricao());
            }

            System.out.print("Escolha o novo status (1-" + statuses.length + "): ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao >= 1 && opcao <= statuses.length) {
                StatusPedido novoStatus = statuses[opcao - 1];

                System.out.print("Observação (opcional): ");
                String observacao = scanner.nextLine().trim();

                if (observacao.isEmpty()) {
                    observacao = "Status atualizado via CLI";
                }

                PedidoDto pedidoAtualizado = pedidoService.atualizarStatus(
                        pedidoId, novoStatus, observacao, atendenteLogado.getNome()
                );

                System.out.println();
                System.out.println("Status atualizado com sucesso!");
                System.out.println("Novo status: " + pedidoAtualizado.getStatus().getDescricao());

            } else {
                System.out.println("Opção inválida!");
            }

        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar status: " + e.getMessage());
        }

        pausa();
    }

    private void exibirEstatisticasPedidos() {
        limparTela();
        System.out.println("=== ESTATÍSTICAS DE PEDIDOS ===");
        System.out.println();

        try {
            StatusPedido[] statuses = StatusPedido.values();

            System.out.println("Pedidos por Status:");
            System.out.println("─".repeat(50));

            long total = 0;
            for (StatusPedido status : statuses) {
                long count = pedidoService.contarPedidosPorStatus(status);
                total += count;
                System.out.printf("%-20s: %d%n", status.getDescricao(), count);
            }

            System.out.println("─".repeat(50));
            System.out.printf("%-20s: %d%n", "TOTAL", total);

        } catch (Exception e) {
            System.out.println("Erro ao obter estatísticas: " + e.getMessage());
        }

        pausa();
    }

    private void exibirDetalhesPedido(PedidoDto pedido) {
        System.out.println("=== DETALHES DO PEDIDO ===");
        System.out.println();
        System.out.printf("%-20s: %s%n", "ID", pedido.getId());
        System.out.printf("%-20s: %s%n", "Número", pedido.getNumeroPedido());
        System.out.printf("%-20s: %s%n", "Cliente", pedido.getNomeCliente());
        System.out.printf("%-20s: %s%n", "Email Cliente", pedido.getEmailCliente());
        System.out.printf("%-20s: %s%n", "Data da Compra", pedido.getDataCompra());
        System.out.printf("%-20s: R$ %.2f%n", "Valor", pedido.getValor());
        System.out.printf("%-20s: %s%n", "Status", pedido.getStatus().getDescricao());

        if (pedido.getPrevisaoEntrega() != null) {
            System.out.printf("%-20s: %s%n", "Previsão Entrega", pedido.getPrevisaoEntrega());
        }

        if (pedido.getObservacoes() != null && !pedido.getObservacoes().isEmpty()) {
            System.out.printf("%-20s: %s%n", "Observações", pedido.getObservacoes());
        }

        System.out.printf("%-20s: %d dias%n", "Dias desde compra", pedido.getDiasDesdeCompra());
        System.out.printf("%-20s: %s%n", "Pode ser cancelado", pedido.getPodeSerCancelado() ? "Sim" : "Não");
        System.out.printf("%-20s: %s%n", "Entregue", pedido.getIsEntregue() ? "Sim" : "Não");
    }

    private void exibirHistoricoPedido(Long pedidoId) {
        try {
            List<HistoricoStatusDto> historico = pedidoService.buscarHistoricoPedido(pedidoId);

            if (!historico.isEmpty()) {
                System.out.println();
                System.out.println("=== HISTÓRICO DE STATUS ===");
                System.out.println();

                for (HistoricoStatusDto item : historico) {
                    System.out.println("• " + item.getDataAtualizacao() + " - " + item.getStatus().getDescricao());

                    if (item.getObservacao() != null && !item.getObservacao().isEmpty()) {
                        System.out.println("  Observação: " + item.getObservacao());
                    }

                    if (item.getResponsavel() != null && !item.getResponsavel().isEmpty()) {
                        System.out.println("  Responsável: " + item.getResponsavel());
                    }

                    System.out.println();
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter histórico: " + e.getMessage());
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
        System.out.println("Endpoints implementados na Sprint 2:");
        System.out.println("- POST /api/pedidos - Criar novo pedido");
        System.out.println("- GET /api/pedidos/{id} - Buscar pedido por ID");
        System.out.println("- GET /api/pedidos/numero/{numero} - Buscar por número");
        System.out.println("- GET /api/pedidos/cliente/{clienteId} - Pedidos do cliente");
        System.out.println("- GET /api/pedidos - Listar todos os pedidos");
        System.out.println("- PUT /api/pedidos/{id}/status - Atualizar status");
        System.out.println("- PUT /api/pedidos/{id}/cancelar - Cancelar pedido");
        System.out.println("- GET /api/pedidos/buscar - Buscar com filtros");
        System.out.println("- GET /api/pedidos/{id}/historico - Histórico do pedido");
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

    private void gerenciarNotificacoesCliente() {
        limparTela();
        System.out.println("=== GERENCIAR NOTIFICAÇÕES ===");
        System.out.println();
        System.out.println("1. Minhas Notificações");
        System.out.println("2. Notificações Não Lidas");
        System.out.println("3. Marcar Notificação como Lida");
        System.out.println("4. Buscar Notificações por Tipo");
        System.out.println("5. Configurar Preferências de Notificação");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listarMinhasNotificacoes();
                    break;
                case 2:
                    listarNotificacoesNaoLidas();
                    break;
                case 3:
                    marcarNotificacaoComoLida();
                    break;
                case 4:
                    buscarNotificacoesPorTipo();
                    break;
                case 5:
                    gerenciarPreferenciasNotificacao();
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

    private void listarMinhasNotificacoes() {
        limparTela();
        System.out.println("=== MINHAS NOTIFICAÇÕES ===");
        System.out.println();

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarPorCliente(clienteLogado.getId());

            if (notificacoes.isEmpty()) {
                System.out.println("Você não possui notificações.");
            } else {
                System.out.printf("%-5s %-15s %-20s %-10s %-30s%n",
                        "ID", "Tipo", "Data/Hora", "Lida", "Título");
                System.out.println("─".repeat(85));

                for (NotificacaoDto notificacao : notificacoes) {
                    String titulo = notificacao.getTitulo();
                    if (titulo.length() > 29) {
                        titulo = titulo.substring(0, 26) + "...";
                    }

                    System.out.printf("%-5d %-15s %-20s %-10s %-30s%n",
                            notificacao.getId(),
                            notificacao.getTipo().getDescricao(),
                            notificacao.getDataEnvio(),
                            notificacao.getLida() ? "Sim" : "Não",
                            titulo
                    );
                }

                System.out.println("─".repeat(85));
                System.out.println("Total de notificações: " + notificacoes.size());
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar notificações: " + e.getMessage());
        }

        pausa();
    }

    private void listarNotificacoesNaoLidas() {
        limparTela();
        System.out.println("=== NOTIFICAÇÕES NÃO LIDAS ===");
        System.out.println();

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarNaoLidasPorCliente(clienteLogado.getId());

            if (notificacoes.isEmpty()) {
                System.out.println("Você não possui notificações não lidas.");
            } else {
                System.out.printf("%-5s %-15s %-20s %-40s%n",
                        "ID", "Tipo", "Data/Hora", "Título");
                System.out.println("─".repeat(85));

                for (NotificacaoDto notificacao : notificacoes) {
                    String titulo = notificacao.getTitulo();
                    if (titulo.length() > 39) {
                        titulo = titulo.substring(0, 36) + "...";
                    }

                    System.out.printf("%-5d %-15s %-20s %-40s%n",
                            notificacao.getId(),
                            notificacao.getTipo().getDescricao(),
                            notificacao.getDataEnvio(),
                            titulo
                    );
                }

                System.out.println("─".repeat(85));
                System.out.println("Total não lidas: " + notificacoes.size());
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar notificações não lidas: " + e.getMessage());
        }

        pausa();
    }

    private void marcarNotificacaoComoLida() {
        System.out.print("Digite o ID da notificação: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();

            NotificacaoDto notificacao = notificacaoService.marcarComoLida(id);

            System.out.println();
            System.out.println("Notificação marcada como lida com sucesso!");
            System.out.println("Título: " + notificacao.getTitulo());

        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao marcar notificação: " + e.getMessage());
        }

        pausa();
    }

    private void buscarNotificacoesPorTipo() {
        System.out.println("Tipos de notificação disponíveis:");
        TipoNotificacao[] tipos = TipoNotificacao.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i].getDescricao());
        }

        System.out.print("Escolha o tipo (1-" + tipos.length + "): ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao >= 1 && opcao <= tipos.length) {
                TipoNotificacao tipo = tipos[opcao - 1];

                // Buscar todas as notificações do cliente e filtrar por tipo
                List<NotificacaoDto> todasNotificacoes = notificacaoService.buscarPorCliente(clienteLogado.getId());
                List<NotificacaoDto> notificacoes = todasNotificacoes.stream()
                        .filter(n -> n.getTipo().equals(tipo))
                        .collect(java.util.stream.Collectors.toList());

                System.out.println();
                System.out.println("Notificações do tipo: " + tipo.getDescricao());
                System.out.println("Total encontrado: " + notificacoes.size());
                System.out.println();

                if (!notificacoes.isEmpty()) {
                    for (NotificacaoDto notificacao : notificacoes) {
                        System.out.printf("ID: %d | Data: %s | Lida: %s | Título: %s%n",
                                notificacao.getId(),
                                notificacao.getDataEnvio(),
                                notificacao.getLida() ? "Sim" : "Não",
                                notificacao.getTitulo());
                    }
                }
            } else {
                System.out.println("Opção inválida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar notificações: " + e.getMessage());
        }

        pausa();
    }

    private void gerenciarNotificacoesAtendente() {
        limparTela();
        System.out.println("=== GERENCIAR NOTIFICAÇÕES (ATENDENTE) ===");
        System.out.println();
        System.out.println("1. Listar Todas as Notificações");
        System.out.println("2. Buscar Notificações por Cliente");
        System.out.println("3. Buscar Notificações por Tipo");
        System.out.println("4. Notificações Não Lidas (Sistema)");
        System.out.println("5. Estatísticas de Notificações");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    listarTodasNotificacoes();
                    break;
                case 2:
                    buscarNotificacoesPorClienteAtendente();
                    break;
                case 3:
                    buscarNotificacoesPorTipoAtendente();
                    break;
                case 4:
                    listarNotificacoesNaoLidasSistema();
                    break;
                case 5:
                    exibirEstatisticasNotificacoes();
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

    private void listarTodasNotificacoes() {
        limparTela();
        System.out.println("=== TODAS AS NOTIFICAÇÕES ===");
        System.out.println();

        try {
            List<NotificacaoDto> notificacoes = notificacaoService.buscarRecentes();

            if (notificacoes.isEmpty()) {
                System.out.println("Nenhuma notificação encontrada.");
            } else {
                System.out.printf("%-5s %-25s %-15s %-20s %-10s %-30s%n",
                        "ID", "Cliente", "Tipo", "Data/Hora", "Lida", "Título");
                System.out.println("─".repeat(110));

                for (NotificacaoDto notificacao : notificacoes) {
                    String nomeCliente = notificacao.getNomeCliente();
                    if (nomeCliente != null && nomeCliente.length() > 24) {
                        nomeCliente = nomeCliente.substring(0, 21) + "...";
                    }

                    String titulo = notificacao.getTitulo();
                    if (titulo.length() > 29) {
                        titulo = titulo.substring(0, 26) + "...";
                    }

                    System.out.printf("%-5d %-25s %-15s %-20s %-10s %-30s%n",
                            notificacao.getId(),
                            nomeCliente != null ? nomeCliente : "N/A",
                            notificacao.getTipo().getDescricao(),
                            notificacao.getDataEnvio(),
                            notificacao.getLida() ? "Sim" : "Não",
                            titulo
                    );
                }

                System.out.println("─".repeat(110));
                System.out.println("Total de notificações: " + notificacoes.size());
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar notificações: " + e.getMessage());
        }

        pausa();
    }

    private void buscarNotificacoesPorClienteAtendente() {
        System.out.print("Digite o ID do cliente: ");
        try {
            Long clienteId = scanner.nextLong();
            scanner.nextLine();

            List<NotificacaoDto> notificacoes = notificacaoService.buscarPorCliente(clienteId);

            System.out.println();
            System.out.println("Notificações do cliente ID: " + clienteId);
            System.out.println("Total encontrado: " + notificacoes.size());
            System.out.println();

            if (!notificacoes.isEmpty()) {
                for (NotificacaoDto notificacao : notificacoes) {
                    System.out.printf("ID: %d | Tipo: %s | Data: %s | Lida: %s%n",
                            notificacao.getId(),
                            notificacao.getTipo().getDescricao(),
                            notificacao.getDataEnvio(),
                            notificacao.getLida() ? "Sim" : "Não");
                    System.out.println("  Título: " + notificacao.getTitulo());
                    System.out.println();
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar notificações: " + e.getMessage());
        }

        pausa();
    }

    private void buscarNotificacoesPorTipoAtendente() {
        System.out.println("Tipos de notificação disponíveis:");
        TipoNotificacao[] tipos = TipoNotificacao.values();
        for (int i = 0; i < tipos.length; i++) {
            System.out.println((i + 1) + ". " + tipos[i].getDescricao());
        }

        System.out.print("Escolha o tipo (1-" + tipos.length + "): ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            if (opcao >= 1 && opcao <= tipos.length) {
                TipoNotificacao tipo = tipos[opcao - 1];

                List<NotificacaoDto> notificacoes = notificacaoService.buscarPorTipo(tipo);

                System.out.println();
                System.out.println("Notificações do tipo: " + tipo.getDescricao());
                System.out.println("Total encontrado: " + notificacoes.size());
                System.out.println();

                if (!notificacoes.isEmpty()) {
                    for (NotificacaoDto notificacao : notificacoes) {
                        System.out.printf("ID: %d | Cliente: %s | Data: %s | Lida: %s%n",
                                notificacao.getId(),
                                notificacao.getNomeCliente() != null ?
                                        notificacao.getNomeCliente() : "N/A",
                                notificacao.getDataEnvio(),
                                notificacao.getLida() ? "Sim" : "Não");
                        System.out.println("  Título: " + notificacao.getTitulo());
                        System.out.println();
                    }
                }
            } else {
                System.out.println("Opção inválida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar notificações: " + e.getMessage());
        }

        pausa();
    }

    private void listarNotificacoesNaoLidasSistema() {
        limparTela();
        System.out.println("=== NOTIFICAÇÕES NÃO LIDAS (SISTEMA) ===");
        System.out.println();

        try {
            List<NotificacaoDto> todasRecentes = notificacaoService.buscarRecentes();
            List<NotificacaoDto> notificacoes = todasRecentes.stream()
                    .filter(n -> !n.getLida())
                    .collect(java.util.stream.Collectors.toList());

            if (notificacoes.isEmpty()) {
                System.out.println("Não há notificações não lidas no sistema.");
            } else {
                System.out.printf("%-5s %-25s %-15s %-20s %-30s%n",
                        "ID", "Cliente", "Tipo", "Data/Hora", "Título");
                System.out.println("─".repeat(100));

                for (NotificacaoDto notificacao : notificacoes) {
                    String nomeCliente = notificacao.getNomeCliente();
                    if (nomeCliente != null && nomeCliente.length() > 24) {
                        nomeCliente = nomeCliente.substring(0, 21) + "...";
                    }

                    String titulo = notificacao.getTitulo();
                    if (titulo.length() > 29) {
                        titulo = titulo.substring(0, 26) + "...";
                    }

                    System.out.printf("%-5d %-25s %-15s %-20s %-30s%n",
                            notificacao.getId(),
                            nomeCliente != null ? nomeCliente : "N/A",
                            notificacao.getTipo().getDescricao(),
                            notificacao.getDataEnvio(),
                            titulo
                    );
                }

                System.out.println("─".repeat(100));
                System.out.println("Total não lidas: " + notificacoes.size());
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar notificações não lidas: " + e.getMessage());
        }

        pausa();
    }

    private void exibirEstatisticasNotificacoes() {
        limparTela();
        System.out.println("=== ESTATÍSTICAS DE NOTIFICAÇÕES ===");
        System.out.println();

        try {
            List<NotificacaoDto> todasRecentes = notificacaoService.buscarRecentes();
            TipoNotificacao[] tipos = TipoNotificacao.values();

            System.out.println("Notificações Recentes por Tipo:");
            System.out.println("─".repeat(50));

            long total = 0;
            for (TipoNotificacao tipo : tipos) {
                long count = todasRecentes.stream()
                        .filter(n -> n.getTipo().equals(tipo))
                        .count();
                total += count;
                System.out.printf("%-25s: %d%n", tipo.getDescricao(), count);
            }

            System.out.println("─".repeat(50));
            System.out.printf("%-25s: %d%n", "TOTAL", total);

            long naoLidas = todasRecentes.stream()
                    .filter(n -> !n.getLida())
                    .count();
            System.out.printf("%-25s: %d%n", "NÃO LIDAS", naoLidas);

        } catch (Exception e) {
            System.out.println("Erro ao obter estatísticas: " + e.getMessage());
        }

        pausa();
    }

    private void gerenciarPreferenciasNotificacao() {
        limparTela();
        System.out.println("=== CONFIGURAÇÕES DE NOTIFICAÇÃO ===");
        System.out.println();
        System.out.println("1. Ver Minhas Preferências");
        System.out.println("2. Configurar Preferências");
        System.out.println("3. Ativar/Desativar Todas as Notificações");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    verMinhasPreferencias();
                    break;
                case 2:
                    configurarPreferencias();
                    break;
                case 3:
                    ativarDesativarTodasNotificacoes();
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

    private void verMinhasPreferencias() {
        limparTela();
        System.out.println("=== MINHAS PREFERÊNCIAS DE NOTIFICAÇÃO ===");
        System.out.println();

        try {
            Optional<PreferenciasNotificacaoDto> preferenciaOpt =
                    preferenciasNotificacaoService.buscarPorCliente(clienteLogado.getId());

            if (preferenciaOpt.isPresent()) {
                PreferenciasNotificacaoDto preferencia = preferenciaOpt.get();

                System.out.printf("%-25s: %s%n", "Tem Notificação Ativa", preferencia.getTemAlgumaNotificacaoAtiva() ? "Sim" : "Não");
                System.out.printf("%-25s: %s%n", "Email Ativo", preferencia.getEmailAtivo() ? "Sim" : "Não");
                System.out.printf("%-25s: %s%n", "SMS Ativo", preferencia.getSmsAtivo() ? "Sim" : "Não");
                System.out.printf("%-25s: %s%n", "Push Ativo", preferencia.getPushAtivo() ? "Sim" : "Não");
                System.out.printf("%-25s: %s%n", "Horário Permitido", preferencia.getHorarioPermitido() ? "Sim" : "Não");
                
                if (preferencia.getHorarioInicio() != null && preferencia.getHorarioFim() != null) {
                    System.out.printf("%-25s: %s às %s%n", "Horário",
                            preferencia.getHorarioInicio(), preferencia.getHorarioFim());
                }

            } else {
                System.out.println("Você ainda não possui preferências configuradas.");
                System.out.print("Deseja criar as preferências padrão? (s/n): ");
                String resposta = scanner.nextLine().trim().toLowerCase();

                if (resposta.equals("s") || resposta.equals("sim")) {
                    criarPreferenciasPadrao();
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar preferências: " + e.getMessage());
        }

        pausa();
    }

    private void criarPreferenciasPadrao() {
        try {
            PreferenciasNotificacaoDto novaPreferencia = new PreferenciasNotificacaoDto();
            novaPreferencia.setClienteId(clienteLogado.getId());

            PreferenciasNotificacaoDto preferenciaCriada =
                    preferenciasNotificacaoService.criarPreferencias(novaPreferencia);

            System.out.println();
            System.out.println("Preferências padrão criadas com sucesso!");
            System.out.println("ID: " + preferenciaCriada.getId());

        } catch (Exception e) {
            System.out.println("Erro ao criar preferências: " + e.getMessage());
        }
    }

    private void configurarPreferencias() {
        limparTela();
        System.out.println("=== CONFIGURAR PREFERÊNCIAS ===");
        System.out.println();

        try {
            PreferenciasNotificacaoDto preferencia = new PreferenciasNotificacaoDto();
            preferencia.setClienteId(clienteLogado.getId());

            System.out.print("Ativar notificações por email? (s/n): ");
            String email = scanner.nextLine().trim().toLowerCase();
            preferencia.setEmailAtivo(email.equals("s") || email.equals("sim"));

            System.out.print("Ativar notificações por SMS? (s/n): ");
            String sms = scanner.nextLine().trim().toLowerCase();
            preferencia.setSmsAtivo(sms.equals("s") || sms.equals("sim"));

            System.out.print("Ativar notificações push? (s/n): ");
            String push = scanner.nextLine().trim().toLowerCase();
            preferencia.setPushAtivo(push.equals("s") || push.equals("sim"));

            System.out.print("Horário de início (HH:mm, ex: 08:00): ");
            String horarioInicio = scanner.nextLine().trim();
            if (!horarioInicio.isEmpty()) {
                preferencia.setHorarioInicio(horarioInicio);
            }

            System.out.print("Horário de fim (HH:mm, ex: 22:00): ");
            String horarioFim = scanner.nextLine().trim();
            if (!horarioFim.isEmpty()) {
                preferencia.setHorarioFim(horarioFim);
            }

            Optional<PreferenciasNotificacaoDto> existente =
                    preferenciasNotificacaoService.buscarPorCliente(clienteLogado.getId());

            PreferenciasNotificacaoDto resultado;
            if (existente.isPresent()) {
                resultado = preferenciasNotificacaoService.atualizarPreferencias(
                        existente.get().getId(), preferencia);
                System.out.println();
                System.out.println("Preferências atualizadas com sucesso!");
            } else {
                resultado = preferenciasNotificacaoService.criarPreferencias(preferencia);
                System.out.println();
                System.out.println("Preferências criadas com sucesso!");
            }

            System.out.println("ID: " + resultado.getId());

        } catch (Exception e) {
            System.out.println("Erro ao configurar preferências: " + e.getMessage());
        }

        pausa();
    }

    private void ativarDesativarTodasNotificacoes() {
        System.out.print("Ativar (a) ou Desativar (d) todas as notificações? ");
        String acao = scanner.nextLine().trim().toLowerCase();
        boolean ativar = acao.equals("a") || acao.equals("ativar");

        try {
            if (ativar) {
                preferenciasNotificacaoService.ativarTodasNotificacoes(clienteLogado.getId());
                System.out.println();
                System.out.println("Todas as notificações foram ativadas!");
            } else {
                preferenciasNotificacaoService.desativarTodasNotificacoes(clienteLogado.getId());
                System.out.println();
                System.out.println("Todas as notificações foram desativadas!");
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar notificações: " + e.getMessage());
        }

        pausa();
    }
}
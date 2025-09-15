package br.edu.infnet.cli;

import br.edu.infnet.cli.modules.*;
import br.edu.infnet.model.dto.ClienteDto;
import br.edu.infnet.model.entity.AtendenteSuporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Interface de linha de comando refatorada para o Sistema de Acompanhamento de Pedidos
 * Esta é uma versão simplificada para demonstrar o conceito do refactor
 */
@Component
@Order(2)
public class SistemaCli implements CommandLineRunner {

    @Autowired
    private AuthCli authCli;
    
    @Autowired
    private ClienteCli clienteCli;
    
    @Autowired
    private PedidoCli pedidoCli;
    
    @Autowired
    private NotificacaoCli notificacaoCli;
    
    @Autowired
    private PreferenciasCli preferenciasCli;
    
    @Autowired
    private SuporteCli suporteCli;
    
    @Autowired
    private AvaliacaoCli avaliacaoCli;

    private Scanner scanner;
    private ClienteDto clienteLogado;
    private AtendenteSuporte atendenteLogado;

    @Override
    public void run(String... args) throws Exception {
        scanner = new Scanner(System.in);
        
        // Inicializar scanners em todos os módulos CLI
        authCli.setScanner(scanner);
        clienteCli.setScanner(scanner);
        pedidoCli.setScanner(scanner);
        notificacaoCli.setScanner(scanner);
        preferenciasCli.setScanner(scanner);
        suporteCli.setScanner(scanner);
        avaliacaoCli.setScanner(scanner);

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
                    clienteLogado = authCli.realizarLoginCliente();
                    break;
                case 2:
                    atendenteLogado = authCli.realizarLoginAtendente();
                    break;
                case 3:
                    clienteCli.registrarNovoCliente();
                    break;
                case 4:
                    authCli.verificarEmail();
                    break;
                case 5:
                    clienteCli.listarClientes();
                    break;
                case 6:
                    pedidoCli.consultarPedidoPublico();
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
        System.out.println("8. Suporte");
        System.out.println("9. Avaliar Pedido Entregue");
        System.out.println("10. Ver Minhas Avaliações");
        System.out.println("11. Compartilhar Status do Pedido");
        System.out.println("0. Logout");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    clienteCli.exibirDadosCliente(clienteLogado);
                    break;
                case 2:
                    clienteLogado = clienteCli.atualizarDadosCliente(clienteLogado);
                    break;
                case 3:
                    pedidoCli.listarMeusPedidos(clienteLogado.getId());
                    break;
                case 4:
                    pedidoCli.criarNovoPedido(clienteLogado.getId());
                    break;
                case 5:
                    pedidoCli.buscarMeuPedido(clienteLogado.getId());
                    break;
                case 6:
                    pedidoCli.acompanharPedido(clienteLogado.getId());
                    break;
                case 7:
                    gerenciarNotificacoesCliente();
                    break;
                case 8:
                    gerenciarSuporteCliente();
                    break;
                case 9:
                    avaliacaoCli.avaliarPedidoEntregue(clienteLogado.getId());
                    break;
                case 10:
                    avaliacaoCli.verMinhasAvaliacoes(clienteLogado.getId());
                    break;
                case 11:
                    pedidoCli.compartilharStatusPedido(clienteLogado.getId());
                    break;
                case 0:
                    authCli.realizarLogout();
                    clienteLogado = null;
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
        System.out.println("8. Gerenciar Suporte");
        System.out.println("9. Gerenciar Avaliações");
        System.out.println("0. Logout");
        System.out.println();
        System.out.print("Escolha uma opção: ");

        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    clienteCli.listarClientes();
                    break;
                case 2:
                    clienteCli.buscarCliente();
                    break;
                case 3:
                    pedidoCli.gerenciarPedidos();
                    break;
                case 4:
                    pedidoCli.buscarPedidos();
                    break;
                case 5:
                    pedidoCli.atualizarStatusPedido(atendenteLogado.getNome());
                    break;
                case 6:
                    pedidoCli.exibirEstatisticasPedidos();
                    break;
                case 7:
                    notificacaoCli.gerenciarNotificacoesAtendente();
                    break;
                case 8:
                    suporteCli.gerenciarSuporteAtendente();
                    break;
                case 9:
                    avaliacaoCli.gerenciarAvaliacoesAtendente();
                    break;
                case 0:
                    authCli.realizarLogout();
                    atendenteLogado = null;
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
                    notificacaoCli.listarMinhasNotificacoes(clienteLogado.getId());
                    break;
                case 2:
                    notificacaoCli.listarNotificacoesNaoLidas(clienteLogado.getId());
                    break;
                case 3:
                    notificacaoCli.marcarNotificacaoComoLida();
                    break;
                case 4:
                    notificacaoCli.buscarNotificacoesPorTipo(clienteLogado.getId());
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
                    preferenciasCli.verMinhasPreferencias(clienteLogado.getId());
                    break;
                case 2:
                    preferenciasCli.configurarPreferencias(clienteLogado.getId());
                    break;
                case 3:
                    preferenciasCli.ativarDesativarTodasNotificacoes(clienteLogado.getId());
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

    private void gerenciarSuporteCliente() {
        limparTela();
        System.out.println("=== SUPORTE - ÁREA DO CLIENTE ===");
        System.out.println();
        System.out.println("1. Criar Nova Solicitação de Suporte");
        System.out.println("2. Minhas Solicitações de Suporte");
        System.out.println("3. Acompanhar Solicitação");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    suporteCli.criarSolicitacaoSuporte(clienteLogado.getId());
                    break;
                case 2:
                    suporteCli.listarMinhasSolicitacoes(clienteLogado.getId());
                    break;
                case 3:
                    suporteCli.acompanharSolicitacao();
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
        System.out.println("Console H2 Database:");
        System.out.println("- Acesse: http://localhost:8080/api/h2-console");
        System.out.println("- JDBC URL: jdbc:h2:mem:testdb");
        System.out.println("- Username: sa");
        System.out.println("- Password: (deixe em branco)");

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
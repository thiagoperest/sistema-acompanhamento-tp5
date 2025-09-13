package br.edu.infnet.cli.modules;

import br.edu.infnet.controller.PedidoController;
import br.edu.infnet.model.dto.HistoricoStatusDto;
import br.edu.infnet.model.dto.PedidoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.enums.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;

/**
 * CLI para operações relacionadas aos Pedidos
 */
@Component
public class PedidoCli extends BaseCli {

    @Autowired
    private PedidoController pedidoController;

    public void listarMeusPedidos(Long clienteId) {
        limparTela();
        System.out.println("=== MEUS PEDIDOS ===");
        System.out.println();

        try {
            ResponseEntity<ResponseDto<List<PedidoDto>>> response = 
                pedidoController.listarPedidosPorCliente(clienteId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<PedidoDto> pedidos = response.getBody().getDados();

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
            } else {
                System.out.println("Erro ao listar pedidos: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }

        pausa();
    }

    public void criarNovoPedido(Long clienteId) {
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
            novoPedido.setClienteId(clienteId);
            novoPedido.setValor(valor);

            if (!observacoes.isEmpty()) {
                novoPedido.setObservacoes(observacoes);
            }

            if (!previsaoEntrega.isEmpty() && previsaoEntrega.matches("\\d{2}/\\d{2}/\\d{4}")) {
                novoPedido.setPrevisaoEntrega(previsaoEntrega);
            }

            ResponseEntity<ResponseDto<PedidoDto>> response = pedidoController.criarPedido(novoPedido);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PedidoDto pedidoCriado = response.getBody().getDados();
                System.out.println();
                System.out.println("Pedido criado com sucesso!");
                System.out.println("Número do pedido: " + pedidoCriado.getNumeroPedido());
                System.out.println("ID: " + pedidoCriado.getId());
                System.out.println("Valor: R$ " + pedidoCriado.getValor());
                System.out.println("Status: " + pedidoCriado.getStatus().getDescricao());
            } else {
                System.out.println();
                System.out.println("Erro ao criar pedido: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (InputMismatchException e) {
            System.out.println("Valor inválido! Digite um número decimal.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao criar pedido: " + e.getMessage());
        }

        pausa();
    }

    public void buscarMeuPedido(Long clienteId) {
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
                    buscarMeuPedidoPorId(clienteId);
                    break;
                case 2:
                    buscarMeuPedidoPorNumero(clienteId);
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

    private void buscarMeuPedidoPorId(Long clienteId) {
        System.out.print("Digite o ID do pedido: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();

            ResponseEntity<ResponseDto<PedidoDto>> response = pedidoController.buscarPorId(id);

            System.out.println();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PedidoDto pedido = response.getBody().getDados();

                // Verificar se o pedido pertence ao cliente logado
                if (!pedido.getClienteId().equals(clienteId)) {
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

    private void buscarMeuPedidoPorNumero(Long clienteId) {
        System.out.print("Digite o número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            ResponseEntity<ResponseDto<PedidoDto>> response = 
                pedidoController.buscarPorNumeroPedido(numeroPedido);

            System.out.println();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PedidoDto pedido = response.getBody().getDados();

                // Verificar se o pedido pertence ao cliente logado
                if (!pedido.getClienteId().equals(clienteId)) {
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

    public void acompanharPedido(Long clienteId) {
        limparTela();
        System.out.println("=== ACOMPANHAR PEDIDO ===");
        System.out.println();

        System.out.print("Digite o número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            ResponseEntity<ResponseDto<PedidoDto>> response = 
                pedidoController.buscarPorNumeroPedido(numeroPedido);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PedidoDto pedido = response.getBody().getDados();

                // Verificar se o pedido pertence ao cliente logado
                if (!pedido.getClienteId().equals(clienteId)) {
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

    public void consultarPedidoPublico() {
        limparTela();
        System.out.println("=== CONSULTA PÚBLICA DE PEDIDO ===");
        System.out.println();

        System.out.print("Digite o número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            ResponseEntity<ResponseDto<PedidoDto>> response = 
                pedidoController.buscarPorNumeroPedido(numeroPedido);

            System.out.println();
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PedidoDto pedido = response.getBody().getDados();

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

    public void gerenciarPedidos() {
        limparTela();
        System.out.println("=== GERENCIAR PEDIDOS ===");
        System.out.println();

        try {
            ResponseEntity<ResponseDto<List<PedidoDto>>> response = pedidoController.listarTodosPedidos();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<PedidoDto> pedidos = response.getBody().getDados();

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
            } else {
                System.out.println("Erro ao listar pedidos: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao listar pedidos: " + e.getMessage());
        }

        pausa();
    }

    public void buscarPedidos() {
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

                ResponseEntity<ResponseDto<List<PedidoDto>>> response = 
                    pedidoController.listarPedidosPorStatus(status);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    List<PedidoDto> pedidos = response.getBody().getDados();

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
            ResponseEntity<ResponseDto<List<PedidoDto>>> response = 
                pedidoController.buscarComFiltros(null, null, numeroPedido, null);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<PedidoDto> pedidos = response.getBody().getDados();

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
            ResponseEntity<ResponseDto<List<PedidoDto>>> response = 
                pedidoController.buscarComFiltros(null, null, null, nomeCliente);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<PedidoDto> pedidos = response.getBody().getDados();

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
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar pedidos: " + e.getMessage());
        }

        pausa();
    }

    public void atualizarStatusPedido(String responsavel) {
        limparTela();
        System.out.println("=== ATUALIZAR STATUS DO PEDIDO ===");
        System.out.println();

        System.out.print("Digite o ID do pedido: ");
        try {
            Long pedidoId = scanner.nextLong();
            scanner.nextLine();

            // Buscar o pedido
            ResponseEntity<ResponseDto<PedidoDto>> responseBusca = pedidoController.buscarPorId(pedidoId);

            if (!responseBusca.getStatusCode().is2xxSuccessful() || responseBusca.getBody() == null) {
                System.out.println("Pedido não encontrado com ID: " + pedidoId);
                pausa();
                return;
            }

            PedidoDto pedido = responseBusca.getBody().getDados();

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

                Map<String, String> dados = new HashMap<>();
                dados.put("status", novoStatus.name());
                dados.put("observacao", observacao);
                dados.put("responsavel", responsavel);

                ResponseEntity<ResponseDto<PedidoDto>> response = 
                    pedidoController.atualizarStatus(pedidoId, dados);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    PedidoDto pedidoAtualizado = response.getBody().getDados();
                    System.out.println();
                    System.out.println("Status atualizado com sucesso!");
                    System.out.println("Novo status: " + pedidoAtualizado.getStatus().getDescricao());
                } else {
                    System.out.println();
                    System.out.println("Erro ao atualizar status: " + 
                        (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
                }

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

    public void exibirEstatisticasPedidos() {
        limparTela();
        System.out.println("=== ESTATÍSTICAS DE PEDIDOS ===");
        System.out.println();

        try {
            ResponseEntity<ResponseDto<Map<String, Object>>> response = 
                pedidoController.obterEstatisticas();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> estatisticas = response.getBody().getDados();

                System.out.println("Pedidos por Status:");
                System.out.println("─".repeat(50));

                long total = 0;
                for (StatusPedido status : StatusPedido.values()) {
                    String chave = "total" + status.name().substring(0, 1).toUpperCase() + 
                                   status.name().substring(1).toLowerCase().replace("_", "");
                    Long count = (Long) estatisticas.getOrDefault(chave, 0L);
                    total += count;
                    System.out.printf("%-20s: %d%n", status.getDescricao(), count);
                }

                System.out.println("─".repeat(50));
                System.out.printf("%-20s: %d%n", "TOTAL", total);
            } else {
                System.out.println("Erro ao obter estatísticas: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (Exception e) {
            System.out.println("Erro ao obter estatísticas: " + e.getMessage());
        }

        pausa();
    }

    public void compartilharStatusPedido(Long clienteId) {
        limparTela();
        System.out.println("=== COMPARTILHAR STATUS DO PEDIDO ===");
        System.out.println();

        System.out.print("Digite o número do pedido: ");
        String numeroPedido = scanner.nextLine().trim();

        try {
            ResponseEntity<ResponseDto<PedidoDto>> response = 
                pedidoController.buscarPorNumeroPedido(numeroPedido);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PedidoDto pedido = response.getBody().getDados();

                // Verificar se o pedido pertence ao cliente
                if (!pedido.getClienteId().equals(clienteId)) {
                    System.out.println("Pedido não encontrado ou não pertence a você.");
                } else {
                    System.out.println();
                    System.out.println("=== LINK DE COMPARTILHAMENTO ===");
                    System.out.println();
                    System.out.println("Compartilhe este link para que outras pessoas possam");
                    System.out.println("acompanhar o status do seu pedido:");
                    System.out.println();
                    System.out.println("http://localhost:8080/pedido/acompanhar/" + pedido.getNumeroPedido());
                    System.out.println();
                    System.out.println("Status atual: " + pedido.getStatus().getDescricao());
                    if (pedido.getPrevisaoEntrega() != null) {
                        System.out.println("Previsão de entrega: " + pedido.getPrevisaoEntrega());
                    }
                }
            } else {
                System.out.println("Pedido não encontrado com número: " + numeroPedido);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar pedido: " + e.getMessage());
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
            ResponseEntity<ResponseDto<List<HistoricoStatusDto>>> response = 
                pedidoController.buscarHistoricoPedido(pedidoId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<HistoricoStatusDto> historico = response.getBody().getDados();

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
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter histórico: " + e.getMessage());
        }
    }
}
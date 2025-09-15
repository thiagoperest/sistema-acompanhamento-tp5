package br.edu.infnet.cli.modules;

import br.edu.infnet.controller.NotificacaoController;
import br.edu.infnet.model.dto.NotificacaoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.enums.TipoNotificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * CLI para operações relacionadas às Notificações
 */
@Component
public class NotificacaoCli extends BaseCli {

    @Autowired
    private NotificacaoController notificacaoController;

    public void listarMinhasNotificacoes(Long clienteId) {
        limparTela();
        System.out.println("=== MINHAS NOTIFICAÇÕES ===");
        System.out.println();

        try {
            ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                notificacaoController.buscarPorCliente(clienteId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<NotificacaoDto> notificacoes = response.getBody().getDados();

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
            } else {
                System.out.println("Erro ao listar notificações: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar notificações: " + e.getMessage());
        }

        pausa();
    }

    public void listarNotificacoesNaoLidas(Long clienteId) {
        limparTela();
        System.out.println("=== NOTIFICAÇÕES NÃO LIDAS ===");
        System.out.println();

        try {
            ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                notificacaoController.buscarNaoLidasPorCliente(clienteId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<NotificacaoDto> notificacoes = response.getBody().getDados();

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
            } else {
                System.out.println("Erro ao listar notificações não lidas: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar notificações não lidas: " + e.getMessage());
        }

        pausa();
    }

    public void marcarNotificacaoComoLida() {
        System.out.print("Digite o ID da notificação: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();

            ResponseEntity<ResponseDto<NotificacaoDto>> response = 
                notificacaoController.marcarComoLida(id);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                NotificacaoDto notificacao = response.getBody().getDados();
                System.out.println();
                System.out.println("Notificação marcada como lida com sucesso!");
                System.out.println("Título: " + notificacao.getTitulo());
            } else {
                System.out.println();
                System.out.println("Erro ao marcar notificação: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }

        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao marcar notificação: " + e.getMessage());
        }

        pausa();
    }

    public void buscarNotificacoesPorTipo(Long clienteId) {
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
                ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                    notificacaoController.buscarPorCliente(clienteId);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    List<NotificacaoDto> todasNotificacoes = response.getBody().getDados();
                    List<NotificacaoDto> notificacoes = todasNotificacoes.stream()
                            .filter(n -> n.getTipo().equals(tipo))
                            .collect(Collectors.toList());

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

    public void gerenciarNotificacoesAtendente() {
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
            ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                notificacaoController.buscarRecentes();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<NotificacaoDto> notificacoes = response.getBody().getDados();

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
            } else {
                System.out.println("Erro ao listar notificações: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
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

            ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                notificacaoController.buscarPorCliente(clienteId);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<NotificacaoDto> notificacoes = response.getBody().getDados();

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
            } else {
                System.out.println();
                System.out.println("Erro ao buscar notificações: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
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

                ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                    notificacaoController.buscarPorTipo(tipo);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    List<NotificacaoDto> notificacoes = response.getBody().getDados();

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
            ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                notificacaoController.buscarRecentes();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<NotificacaoDto> todasRecentes = response.getBody().getDados();
                List<NotificacaoDto> notificacoes = todasRecentes.stream()
                        .filter(n -> !n.getLida())
                        .collect(Collectors.toList());

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
            ResponseEntity<ResponseDto<List<NotificacaoDto>>> response = 
                notificacaoController.buscarRecentes();

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<NotificacaoDto> todasRecentes = response.getBody().getDados();
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
            }

        } catch (Exception e) {
            System.out.println("Erro ao obter estatísticas: " + e.getMessage());
        }

        pausa();
    }
}
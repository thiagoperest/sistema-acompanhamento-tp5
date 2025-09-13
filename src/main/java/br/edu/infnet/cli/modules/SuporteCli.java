package br.edu.infnet.cli.modules;

import br.edu.infnet.controller.SolicitacaoSuporteController;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.dto.SolicitacaoSuporteDto;
import br.edu.infnet.model.enums.StatusSuporte;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * CLI para operações relacionadas ao Suporte
 */
@Component
public class SuporteCli extends BaseCli {

    @Autowired
    private SolicitacaoSuporteController solicitacaoSuporteController;

    public void criarSolicitacaoSuporte(Long clienteId) {
        limparTela();
        System.out.println("=== CRIAR SOLICITAÇÃO DE SUPORTE ===");
        System.out.println();
        
        System.out.println("Tipo de problema:");
        System.out.println("1. Atraso na Entrega");
        System.out.println("2. Produto Danificado");
        System.out.println("3. Produto Não Recebido");
        System.out.println("4. Dúvida sobre Pedido");
        System.out.println("5. Outro");
        System.out.print("Escolha o tipo: ");
        
        try {
            int tipoOpcao = scanner.nextInt();
            scanner.nextLine();
            
            String tipoProblema;
            switch (tipoOpcao) {
                case 1:
                    tipoProblema = "Atraso na Entrega";
                    break;
                case 2:
                    tipoProblema = "Produto Danificado";
                    break;
                case 3:
                    tipoProblema = "Produto Não Recebido";
                    break;
                case 4:
                    tipoProblema = "Dúvida sobre Pedido";
                    break;
                case 5:
                    System.out.print("Digite o tipo de problema: ");
                    tipoProblema = scanner.nextLine().trim();
                    break;
                default:
                    System.out.println("Opção inválida!");
                    pausa();
                    return;
            }
            
            System.out.print("Descreva o problema detalhadamente: ");
            String descricao = scanner.nextLine().trim();
            
            if (descricao.isEmpty()) {
                System.out.println("A descrição é obrigatória!");
                pausa();
                return;
            }
            
            SolicitacaoSuporteDto dto = new SolicitacaoSuporteDto();
            dto.setClienteId(clienteId);
            dto.setTipoProblema(tipoProblema);
            dto.setDescricao(descricao);
            
            ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> response = 
                solicitacaoSuporteController.criarSolicitacao(dto);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                SolicitacaoSuporteDto solicitacao = response.getBody().getDados();
                System.out.println();
                System.out.println("Solicitação criada com sucesso!");
                System.out.println("Protocolo: " + solicitacao.getProtocolo());
                System.out.println("Status: " + solicitacao.getStatus().getDescricao());
                System.out.println();
                System.out.println("Guarde este protocolo para acompanhamento.");
            } else {
                System.out.println();
                System.out.println("Erro ao criar solicitação: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida!");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao criar solicitação: " + e.getMessage());
        }
        
        pausa();
    }
    
    public void listarMinhasSolicitacoes(Long clienteId) {
        limparTela();
        System.out.println("=== MINHAS SOLICITAÇÕES DE SUPORTE ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> response = 
                solicitacaoSuporteController.listarPorCliente(clienteId);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<SolicitacaoSuporteDto> solicitacoes = response.getBody().getDados();
                
                if (solicitacoes.isEmpty()) {
                    System.out.println("Você não possui solicitações de suporte.");
                } else {
                    System.out.printf("%-15s %-20s %-15s %-20s%n",
                            "Protocolo", "Tipo", "Status", "Data Abertura");
                    System.out.println("─".repeat(75));
                    
                    for (SolicitacaoSuporteDto s : solicitacoes) {
                        System.out.printf("%-15s %-20s %-15s %-20s%n",
                                s.getProtocolo(),
                                s.getTipoProblema().length() > 20 ? s.getTipoProblema().substring(0, 17) + "..." : s.getTipoProblema(),
                                s.getStatus().getDescricao(),
                                s.getDataAbertura().toString().substring(0, 19)
                        );
                    }
                    
                    System.out.println("─".repeat(75));
                    System.out.println("Total: " + solicitacoes.size());
                }
            } else {
                System.out.println("Erro ao listar solicitações: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar solicitações: " + e.getMessage());
        }
        
        pausa();
    }
    
    public void acompanharSolicitacao() {
        System.out.print("Digite o protocolo da solicitação: ");
        String protocolo = scanner.nextLine().trim();
        
        try {
            ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> response = 
                solicitacaoSuporteController.buscarPorProtocolo(protocolo);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                SolicitacaoSuporteDto s = response.getBody().getDados();
                
                System.out.println();
                System.out.println("=== DETALHES DA SOLICITAÇÃO ===");
                System.out.println();
                System.out.printf("%-20s: %s%n", "Protocolo", s.getProtocolo());
                System.out.printf("%-20s: %s%n", "Tipo", s.getTipoProblema());
                System.out.printf("%-20s: %s%n", "Status", s.getStatus().getDescricao());
                System.out.printf("%-20s: %s%n", "Data Abertura", s.getDataAbertura());
                
                if (s.getDataFechamento() != null) {
                    System.out.printf("%-20s: %s%n", "Data Fechamento", s.getDataFechamento());
                }
                
                if (s.getNomeAtendente() != null) {
                    System.out.printf("%-20s: %s%n", "Atendente", s.getNomeAtendente());
                }
                
                System.out.println();
                System.out.println("Descrição:");
                System.out.println(s.getDescricao());
            } else {
                System.out.println("Solicitação não encontrada com protocolo: " + protocolo);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar solicitação: " + e.getMessage());
        }
        
        pausa();
    }

    public void gerenciarSuporteAtendente() {
        limparTela();
        System.out.println("=== GERENCIAR SUPORTE (ATENDENTE) ===");
        System.out.println();
        System.out.println("1. Listar Todas as Solicitações");
        System.out.println("2. Buscar Solicitação por Protocolo");
        System.out.println("3. Solicitações por Status");
        System.out.println("4. Minhas Solicitações Atribuídas");
        System.out.println("5. Atualizar Status de Solicitação");
        System.out.println("6. Atribuir Solicitação");
        System.out.println("7. Estatísticas de Suporte");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    listarTodasSolicitacoes();
                    break;
                case 2:
                    buscarSolicitacaoPorProtocolo();
                    break;
                case 3:
                    listarSolicitacoesPorStatus();
                    break;
                case 4:
                    listarMinhasSolicitacoesAtendente();
                    break;
                case 5:
                    atualizarStatusSolicitacao();
                    break;
                case 6:
                    atribuirSolicitacao();
                    break;
                case 7:
                    exibirEstatisticasSuporte();
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

    private void listarTodasSolicitacoes() {
        limparTela();
        System.out.println("=== TODAS AS SOLICITAÇÕES DE SUPORTE ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> response = 
                solicitacaoSuporteController.listarTodas();
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<SolicitacaoSuporteDto> solicitacoes = response.getBody().getDados();
                
                if (solicitacoes.isEmpty()) {
                    System.out.println("Nenhuma solicitação encontrada.");
                } else {
                    System.out.printf("%-15s %-20s %-15s %-20s %-15s%n",
                            "Protocolo", "Cliente", "Tipo", "Status", "Atendente");
                    System.out.println("─".repeat(90));
                    
                    for (SolicitacaoSuporteDto s : solicitacoes) {
                        String nomeCliente = s.getNomeCliente() != null ? s.getNomeCliente() : "N/A";
                        if (nomeCliente.length() > 19) {
                            nomeCliente = nomeCliente.substring(0, 16) + "...";
                        }
                        
                        String tipo = s.getTipoProblema();
                        if (tipo.length() > 14) {
                            tipo = tipo.substring(0, 11) + "...";
                        }
                        
                        String atendente = s.getNomeAtendente() != null ? s.getNomeAtendente() : "Não atribuído";
                        if (atendente.length() > 14) {
                            atendente = atendente.substring(0, 11) + "...";
                        }
                        
                        System.out.printf("%-15s %-20s %-15s %-20s %-15s%n",
                                s.getProtocolo(),
                                nomeCliente,
                                tipo,
                                s.getStatus().getDescricao(),
                                atendente
                        );
                    }
                    
                    System.out.println("─".repeat(90));
                    System.out.println("Total: " + solicitacoes.size());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar solicitações: " + e.getMessage());
        }
        
        pausa();
    }

    private void buscarSolicitacaoPorProtocolo() {
        System.out.print("Digite o protocolo da solicitação: ");
        String protocolo = scanner.nextLine().trim();
        
        try {
            ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> response = 
                solicitacaoSuporteController.buscarPorProtocolo(protocolo);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                SolicitacaoSuporteDto s = response.getBody().getDados();
                
                System.out.println();
                System.out.println("=== DETALHES DA SOLICITAÇÃO ===");
                System.out.println();
                System.out.printf("%-20s: %s%n", "Protocolo", s.getProtocolo());
                System.out.printf("%-20s: %s%n", "Cliente", s.getNomeCliente() != null ? s.getNomeCliente() : "N/A");
                System.out.printf("%-20s: %s%n", "Tipo", s.getTipoProblema());
                System.out.printf("%-20s: %s%n", "Status", s.getStatus().getDescricao());
                System.out.printf("%-20s: %s%n", "Data Abertura", s.getDataAbertura());
                
                if (s.getDataFechamento() != null) {
                    System.out.printf("%-20s: %s%n", "Data Fechamento", s.getDataFechamento());
                }
                
                if (s.getNomeAtendente() != null) {
                    System.out.printf("%-20s: %s%n", "Atendente", s.getNomeAtendente());
                }
                
                System.out.println();
                System.out.println("Descrição:");
                System.out.println(s.getDescricao());
            } else {
                System.out.println("Solicitação não encontrada com protocolo: " + protocolo);
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar solicitação: " + e.getMessage());
        }
        
        pausa();
    }

    private void listarSolicitacoesPorStatus() {
        System.out.println("Status disponíveis:");
        StatusSuporte[] statuses = StatusSuporte.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println((i + 1) + ". " + statuses[i].getDescricao());
        }
        
        System.out.print("Escolha o status (1-" + statuses.length + "): ");
        
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            if (opcao >= 1 && opcao <= statuses.length) {
                StatusSuporte status = statuses[opcao - 1];
                
                ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> response = 
                    solicitacaoSuporteController.listarPorStatus(status.name());
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    List<SolicitacaoSuporteDto> solicitacoes = response.getBody().getDados();
                    
                    System.out.println();
                    System.out.println("Solicitações com status: " + status.getDescricao());
                    System.out.println("Total encontrado: " + solicitacoes.size());
                    System.out.println();
                    
                    if (!solicitacoes.isEmpty()) {
                        for (SolicitacaoSuporteDto s : solicitacoes) {
                            System.out.printf("Protocolo: %s | Cliente: %s | Tipo: %s%n",
                                    s.getProtocolo(),
                                    s.getNomeCliente() != null ? s.getNomeCliente() : "N/A",
                                    s.getTipoProblema());
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
            System.out.println("Erro ao buscar solicitações: " + e.getMessage());
        }
        
        pausa();
    }

    private void listarMinhasSolicitacoesAtendente() {
        System.out.print("Digite seu ID de atendente: ");
        try {
            Long atendenteId = scanner.nextLong();
            scanner.nextLine();
            
            ResponseEntity<ResponseDto<List<SolicitacaoSuporteDto>>> response = 
                solicitacaoSuporteController.listarPorAtendente(atendenteId);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<SolicitacaoSuporteDto> solicitacoes = response.getBody().getDados();
                
                System.out.println();
                System.out.println("Suas solicitações atribuídas: " + solicitacoes.size());
                System.out.println();
                
                if (!solicitacoes.isEmpty()) {
                    for (SolicitacaoSuporteDto s : solicitacoes) {
                        System.out.printf("Protocolo: %s | Cliente: %s | Status: %s | Tipo: %s%n",
                                s.getProtocolo(),
                                s.getNomeCliente() != null ? s.getNomeCliente() : "N/A",
                                s.getStatus().getDescricao(),
                                s.getTipoProblema());
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar solicitações: " + e.getMessage());
        }
        
        pausa();
    }

    private void atualizarStatusSolicitacao() {
        System.out.print("Digite o ID da solicitação: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();
            
            System.out.println("Novo status:");
            StatusSuporte[] statuses = StatusSuporte.values();
            for (int i = 0; i < statuses.length; i++) {
                System.out.println((i + 1) + ". " + statuses[i].getDescricao());
            }
            
            System.out.print("Escolha o status (1-" + statuses.length + "): ");
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            if (opcao >= 1 && opcao <= statuses.length) {
                StatusSuporte novoStatus = statuses[opcao - 1];
                
                System.out.print("ID do atendente (opcional, 0 para pular): ");
                Long atendenteId = scanner.nextLong();
                scanner.nextLine();
                
                Map<String, Object> atualizacao = new HashMap<>();
                atualizacao.put("status", novoStatus.name());
                if (atendenteId > 0) {
                    atualizacao.put("atendenteId", atendenteId);
                }
                
                ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> response = 
                    solicitacaoSuporteController.atualizarStatus(id, atualizacao);
                
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    System.out.println();
                    System.out.println("Status atualizado com sucesso!");
                    System.out.println("Novo status: " + novoStatus.getDescricao());
                } else {
                    System.out.println();
                    System.out.println("Erro ao atualizar status: " + 
                        (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
                }
            } else {
                System.out.println("Opção inválida!");
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar status: " + e.getMessage());
        }
        
        pausa();
    }

    private void atribuirSolicitacao() {
        System.out.print("Digite o ID da solicitação: ");
        try {
            Long solicitacaoId = scanner.nextLong();
            scanner.nextLine();
            
            System.out.print("Digite o ID do atendente: ");
            Long atendenteId = scanner.nextLong();
            scanner.nextLine();
            
            ResponseEntity<ResponseDto<SolicitacaoSuporteDto>> response = 
                solicitacaoSuporteController.atribuirAtendente(solicitacaoId, atendenteId);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                SolicitacaoSuporteDto solicitacao = response.getBody().getDados();
                System.out.println();
                System.out.println("Atendente atribuído com sucesso!");
                System.out.println("Protocolo: " + solicitacao.getProtocolo());
                System.out.println("Atendente: " + solicitacao.getNomeAtendente());
            } else {
                System.out.println();
                System.out.println("Erro ao atribuir atendente: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao atribuir atendente: " + e.getMessage());
        }
        
        pausa();
    }

    private void exibirEstatisticasSuporte() {
        limparTela();
        System.out.println("=== ESTATÍSTICAS DE SUPORTE ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<Map<String, Object>>> response = 
                solicitacaoSuporteController.obterEstatisticas();
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> estatisticas = response.getBody().getDados();
                
                System.out.println("Estatísticas Gerais:");
                System.out.println("─".repeat(50));
                
                for (Map.Entry<String, Object> entry : estatisticas.entrySet()) {
                    System.out.printf("%-30s: %s%n", entry.getKey(), entry.getValue());
                }
                
                System.out.println("─".repeat(50));
            }
        } catch (Exception e) {
            System.out.println("Erro ao obter estatísticas: " + e.getMessage());
        }
        
        pausa();
    }
}
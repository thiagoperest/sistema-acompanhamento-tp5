package br.edu.infnet.cli.modules;

import br.edu.infnet.controller.AvaliacaoController;
import br.edu.infnet.model.dto.AvaliacaoDto;
import br.edu.infnet.model.dto.ResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Map;

/**
 * CLI para operações relacionadas às Avaliações
 */
@Component
public class AvaliacaoCli extends BaseCli {

    @Autowired
    private AvaliacaoController avaliacaoController;

    public void avaliarPedidoEntregue(Long clienteId) {
        limparTela();
        System.out.println("=== AVALIAR PEDIDO ENTREGUE ===");
        System.out.println();
        
        System.out.print("Digite o ID do pedido: ");
        try {
            Long pedidoId = scanner.nextLong();
            scanner.nextLine();
            
            // Verificar se já foi avaliado
            ResponseEntity<ResponseDto<Map<String, Boolean>>> responseVerificacao = 
                avaliacaoController.verificarAvaliacaoExistente(pedidoId);
            
            if (responseVerificacao.getStatusCode().is2xxSuccessful() && 
                responseVerificacao.getBody() != null) {
                Map<String, Boolean> resultado = responseVerificacao.getBody().getDados();
                if (resultado.get("avaliacaoExiste")) {
                    System.out.println();
                    System.out.println("Este pedido já foi avaliado!");
                    pausa();
                    return;
                }
            }
            
            System.out.println();
            System.out.println("Por favor, avalie os seguintes aspectos:");
            System.out.println();
            
            System.out.println("NOTA PARA O ACOMPANHAMENTO DO PEDIDO (1-5):");
            System.out.println("1 - Muito ruim");
            System.out.println("2 - Ruim");
            System.out.println("3 - Regular");
            System.out.println("4 - Bom");
            System.out.println("5 - Excelente");
            System.out.print("Sua nota: ");
            int notaAcompanhamento = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println();
            System.out.println("NOTA PARA A ENTREGA (1-5):");
            System.out.println("1 - Muito ruim");
            System.out.println("2 - Ruim");
            System.out.println("3 - Regular");
            System.out.println("4 - Boa");
            System.out.println("5 - Excelente");
            System.out.print("Sua nota: ");
            int notaEntrega = scanner.nextInt();
            scanner.nextLine();
            
            System.out.println();
            System.out.print("Comentário (opcional): ");
            String comentario = scanner.nextLine().trim();
            
            AvaliacaoDto avaliacaoDto = new AvaliacaoDto();
            avaliacaoDto.setClienteId(clienteId);
            avaliacaoDto.setPedidoId(pedidoId);
            avaliacaoDto.setNotaAcompanhamento(notaAcompanhamento);
            avaliacaoDto.setNotaEntrega(notaEntrega);
            avaliacaoDto.setComentario(comentario.isEmpty() ? null : comentario);
            
            ResponseEntity<ResponseDto<AvaliacaoDto>> response = 
                avaliacaoController.criarAvaliacao(avaliacaoDto);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AvaliacaoDto avaliacao = response.getBody().getDados();
                System.out.println();
                System.out.println("Avaliação registrada com sucesso!");
                System.out.printf("Média das notas: %.1f%n", avaliacao.getMediaNotas());
                System.out.println("Obrigado pelo seu feedback!");
            } else {
                System.out.println();
                System.out.println("Erro ao registrar avaliação: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao avaliar pedido: " + e.getMessage());
        }
        
        pausa();
    }
    
    public void verMinhasAvaliacoes(Long clienteId) {
        limparTela();
        System.out.println("=== MINHAS AVALIAÇÕES ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<List<AvaliacaoDto>>> response = 
                avaliacaoController.listarPorCliente(clienteId);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<AvaliacaoDto> avaliacoes = response.getBody().getDados();
                
                if (avaliacoes.isEmpty()) {
                    System.out.println("Você ainda não fez nenhuma avaliação.");
                } else {
                    System.out.printf("%-10s %-15s %-10s %-10s %-10s %-20s%n",
                            "ID", "Pedido", "Acomp.", "Entrega", "Média", "Data");
                    System.out.println("─".repeat(80));
                    
                    for (AvaliacaoDto avaliacao : avaliacoes) {
                        System.out.printf("%-10d %-15s %-10d %-10d %-10.1f %-20s%n",
                                avaliacao.getId(),
                                avaliacao.getNumeroPedido() != null ? avaliacao.getNumeroPedido() : "PED-" + avaliacao.getPedidoId(),
                                avaliacao.getNotaAcompanhamento(),
                                avaliacao.getNotaEntrega(),
                                avaliacao.getMediaNotas(),
                                avaliacao.getDataAvaliacao().toString().substring(0, 10)
                        );
                    }
                    
                    System.out.println("─".repeat(80));
                    System.out.println("Total de avaliações: " + avaliacoes.size());
                    
                    // Calcular média geral
                    double mediaGeral = avaliacoes.stream()
                            .mapToDouble(AvaliacaoDto::getMediaNotas)
                            .average()
                            .orElse(0.0);
                    System.out.printf("Média geral de suas avaliações: %.1f%n", mediaGeral);
                }
            } else {
                System.out.println("Erro ao buscar avaliações: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar avaliações: " + e.getMessage());
        }
        
        pausa();
    }

    public void gerenciarAvaliacoesAtendente() {
        limparTela();
        System.out.println("=== GERENCIAR AVALIAÇÕES (ATENDENTE) ===");
        System.out.println();
        System.out.println("1. Listar Todas as Avaliações");
        System.out.println("2. Buscar Avaliação por ID");
        System.out.println("3. Avaliações por Cliente");
        System.out.println("4. Avaliação de Pedido");
        System.out.println("5. Filtrar por Nota");
        System.out.println("6. Estatísticas de Avaliações");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            switch (opcao) {
                case 1:
                    listarTodasAvaliacoes();
                    break;
                case 2:
                    buscarAvaliacaoPorId();
                    break;
                case 3:
                    avaliacoesPorCliente();
                    break;
                case 4:
                    avaliacaoDePedido();
                    break;
                case 5:
                    filtrarPorNota();
                    break;
                case 6:
                    estatisticasAvaliacoes();
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

    private void listarTodasAvaliacoes() {
        limparTela();
        System.out.println("=== TODAS AS AVALIAÇÕES ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<List<AvaliacaoDto>>> response = 
                avaliacaoController.listarTodas();
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<AvaliacaoDto> avaliacoes = response.getBody().getDados();
                
                if (avaliacoes.isEmpty()) {
                    System.out.println("Nenhuma avaliação encontrada.");
                } else {
                    System.out.printf("%-8s %-15s %-15s %-10s %-10s %-8s%n",
                            "ID", "Cliente", "Pedido", "Acompanha.", "Entrega", "Média");
                    System.out.println("─".repeat(75));
                    
                    for (AvaliacaoDto avaliacao : avaliacoes) {
                        String nomeCliente = avaliacao.getNomeCliente() != null ? 
                            avaliacao.getNomeCliente() : "N/A";
                        if (nomeCliente.length() > 14) {
                            nomeCliente = nomeCliente.substring(0, 11) + "...";
                        }
                        
                        System.out.printf("%-8d %-15s %-15d %-10d %-10d %-8.1f%n",
                                avaliacao.getId(),
                                nomeCliente,
                                avaliacao.getPedidoId(),
                                avaliacao.getNotaAcompanhamento(),
                                avaliacao.getNotaEntrega(),
                                avaliacao.getMediaNotas()
                        );
                    }
                    
                    System.out.println("─".repeat(90));
                    System.out.println("Total: " + avaliacoes.size());
                }
            }
        } catch (Exception e) {
            System.out.println("Erro ao listar avaliações: " + e.getMessage());
        }
        
        pausa();
    }

    private void buscarAvaliacaoPorId() {
        System.out.print("Digite o ID da avaliação: ");
        try {
            Long id = scanner.nextLong();
            scanner.nextLine();
            
            ResponseEntity<ResponseDto<AvaliacaoDto>> response = 
                avaliacaoController.buscarPorId(id);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AvaliacaoDto avaliacao = response.getBody().getDados();
                
                System.out.println();
                System.out.println("=== DETALHES DA AVALIAÇÃO ===");
                System.out.println();
                System.out.printf("%-20s: %d%n", "ID", avaliacao.getId());
                System.out.printf("%-20s: %s%n", "Cliente", avaliacao.getNomeCliente() != null ? 
                    avaliacao.getNomeCliente() : "N/A");
                System.out.printf("%-20s: %d%n", "Pedido", avaliacao.getPedidoId());
                System.out.printf("%-20s: %s%n", "Data", avaliacao.getDataAvaliacao());
                System.out.println();
                System.out.println("NOTAS:");
                System.out.printf("%-20s: %d%n", "Acompanhamento", avaliacao.getNotaAcompanhamento());
                System.out.printf("%-20s: %d%n", "Entrega", avaliacao.getNotaEntrega());
                System.out.printf("%-20s: %.1f%n", "MÉDIA", avaliacao.getMediaNotas());
                
                if (avaliacao.getComentario() != null && !avaliacao.getComentario().isEmpty()) {
                    System.out.println();
                    System.out.println("Comentário:");
                    System.out.println(avaliacao.getComentario());
                }
            } else {
                System.out.println("Avaliação não encontrada com ID: " + id);
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar avaliação: " + e.getMessage());
        }
        
        pausa();
    }

    private void avaliacoesPorCliente() {
        System.out.print("Digite o ID do cliente: ");
        try {
            Long clienteId = scanner.nextLong();
            scanner.nextLine();
            
            ResponseEntity<ResponseDto<List<AvaliacaoDto>>> response = 
                avaliacaoController.listarPorCliente(clienteId);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<AvaliacaoDto> avaliacoes = response.getBody().getDados();
                
                System.out.println();
                System.out.println("Avaliações do cliente ID: " + clienteId);
                System.out.println("Total encontrado: " + avaliacoes.size());
                System.out.println();
                
                if (!avaliacoes.isEmpty()) {
                    for (AvaliacaoDto avaliacao : avaliacoes) {
                        System.out.printf("ID: %d | Pedido: %d | Média: %.1f | Data: %s%n",
                                avaliacao.getId(),
                                avaliacao.getPedidoId(),
                                avaliacao.getMediaNotas(),
                                avaliacao.getDataAvaliacao().toString().substring(0, 10));
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar avaliações: " + e.getMessage());
        }
        
        pausa();
    }

    private void avaliacaoDePedido() {
        System.out.print("Digite o ID do pedido: ");
        try {
            Long pedidoId = scanner.nextLong();
            scanner.nextLine();
            
            ResponseEntity<ResponseDto<AvaliacaoDto>> response = 
                avaliacaoController.buscarPorPedido(pedidoId);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                AvaliacaoDto avaliacao = response.getBody().getDados();
                
                System.out.println();
                System.out.println("=== AVALIAÇÃO DO PEDIDO ===");
                System.out.println();
                System.out.printf("%-20s: %d%n", "Pedido", avaliacao.getPedidoId());
                System.out.printf("%-20s: %s%n", "Cliente", avaliacao.getNomeCliente() != null ? 
                    avaliacao.getNomeCliente() : "N/A");
                System.out.printf("%-20s: %s%n", "Data", avaliacao.getDataAvaliacao());
                System.out.printf("%-20s: %.1f%n", "Nota Média", avaliacao.getMediaNotas());
                
                if (avaliacao.getComentario() != null && !avaliacao.getComentario().isEmpty()) {
                    System.out.println();
                    System.out.println("Comentário:");
                    System.out.println(avaliacao.getComentario());
                }
            } else {
                System.out.println("Avaliação não encontrada para o pedido: " + pedidoId);
            }
        } catch (InputMismatchException e) {
            System.out.println("ID inválido! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao buscar avaliação: " + e.getMessage());
        }
        
        pausa();
    }

    private void filtrarPorNota() {
        System.out.print("Digite a nota mínima (1-5): ");
        try {
            double notaMinima = scanner.nextDouble();
            scanner.nextLine();
            
            if (notaMinima < 1 || notaMinima > 5) {
                System.out.println("Nota deve estar entre 1 e 5!");
                pausa();
                return;
            }
            
            ResponseEntity<ResponseDto<List<AvaliacaoDto>>> response = 
                avaliacaoController.listarPorNota(notaMinima);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                List<AvaliacaoDto> avaliacoes = response.getBody().getDados();
                
                System.out.println();
                System.out.println("Avaliações com média >= " + notaMinima);
                System.out.println("Total encontrado: " + avaliacoes.size());
                System.out.println();
                
                if (!avaliacoes.isEmpty()) {
                    for (AvaliacaoDto avaliacao : avaliacoes) {
                        System.out.printf("ID: %d | Cliente: %s | Pedido: %d | Média: %.1f%n",
                                avaliacao.getId(),
                                avaliacao.getNomeCliente() != null ? avaliacao.getNomeCliente() : "N/A",
                                avaliacao.getPedidoId(),
                                avaliacao.getMediaNotas());
                    }
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Nota inválida! Digite um número decimal.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao filtrar avaliações: " + e.getMessage());
        }
        
        pausa();
    }

    private void estatisticasAvaliacoes() {
        limparTela();
        System.out.println("=== ESTATÍSTICAS DE AVALIAÇÕES ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<Map<String, Object>>> response = 
                avaliacaoController.obterEstatisticas();
            
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
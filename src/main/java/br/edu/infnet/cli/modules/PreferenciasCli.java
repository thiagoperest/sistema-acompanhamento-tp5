package br.edu.infnet.cli.modules;

import br.edu.infnet.controller.PreferenciasNotificacaoController;
import br.edu.infnet.model.dto.PreferenciasNotificacaoDto;
import br.edu.infnet.model.dto.ResponseDto;
import br.edu.infnet.model.enums.TipoNotificacao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.InputMismatchException;

/**
 * CLI para operações relacionadas às Preferências de Notificação
 */
@Component
public class PreferenciasCli extends BaseCli {

    @Autowired
    private PreferenciasNotificacaoController preferenciasController;

    public void verMinhasPreferencias(Long clienteId) {
        limparTela();
        System.out.println("=== MINHAS PREFERÊNCIAS DE NOTIFICAÇÃO ===");
        System.out.println();
        
        try {
            ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> response = 
                preferenciasController.buscarPorCliente(clienteId);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PreferenciasNotificacaoDto prefs = response.getBody().getDados();
                
                if (prefs == null) {
                    System.out.println("Você ainda não configurou suas preferências.");
                    System.out.println("Todas as notificações estão ativadas por padrão.");
                    System.out.println();
                    System.out.print("Deseja criar as preferências padrão? (S/N): ");
                    
                    if (lerSimNao()) {
                        criarPreferenciasPadrao(clienteId);
                    }
                } else {
                    System.out.println("Configurações atuais:");
                    System.out.println("─".repeat(50));
                    System.out.printf("%-35s: %s%n", "Tem Notificação Ativa", 
                        prefs.getTemAlgumaNotificacaoAtiva() != null && prefs.getTemAlgumaNotificacaoAtiva() ? "SIM" : "NÃO");
                    System.out.printf("%-35s: %s%n", "E-mail", 
                        prefs.getEmailAtivo() ? "ATIVADO" : "DESATIVADO");
                    System.out.printf("%-35s: %s%n", "SMS", 
                        prefs.getSmsAtivo() ? "ATIVADO" : "DESATIVADO");
                    System.out.printf("%-35s: %s%n", "Push", 
                        prefs.getPushAtivo() ? "ATIVADO" : "DESATIVADO");
                    System.out.printf("%-35s: %s%n", "Horário Permitido", 
                        prefs.getHorarioPermitido() != null && prefs.getHorarioPermitido() ? "SIM" : "NÃO");
                    
                    if (prefs.getHorarioInicio() != null && prefs.getHorarioFim() != null) {
                        System.out.printf("%-35s: %s às %s%n", "Horário",
                                prefs.getHorarioInicio(), prefs.getHorarioFim());
                    }
                    System.out.println("─".repeat(50));
                }
            } else {
                System.out.println("Erro ao buscar preferências: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao buscar preferências: " + e.getMessage());
        }
        
        pausa();
    }

    public void configurarPreferencias(Long clienteId) {
        limparTela();
        System.out.println("=== CONFIGURAR PREFERÊNCIAS DE NOTIFICAÇÃO ===");
        System.out.println();
        
        try {
            // Buscar preferências atuais ou criar novo objeto
            ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> responseAtual = 
                preferenciasController.buscarPorCliente(clienteId);
            
            PreferenciasNotificacaoDto prefs;
            if (responseAtual.getStatusCode().is2xxSuccessful() && 
                responseAtual.getBody() != null && 
                responseAtual.getBody().getDados() != null) {
                prefs = responseAtual.getBody().getDados();
            } else {
                prefs = new PreferenciasNotificacaoDto();
                prefs.setClienteId(clienteId);
            }
            
            System.out.println("Configure suas preferências:");
            System.out.println();
            
            System.out.print("Ativar notificações por E-mail? (S/N): ");
            prefs.setEmailAtivo(lerSimNao());
            
            System.out.print("Ativar notificações por SMS? (S/N): ");
            prefs.setSmsAtivo(lerSimNao());
            
            System.out.print("Ativar notificações Push? (S/N): ");
            prefs.setPushAtivo(lerSimNao());
            
            System.out.println();
            System.out.print("Definir horário específico para notificações? (S/N): ");
            if (lerSimNao()) {
                System.out.print("Horário de início (HH:mm, ex: 08:00): ");
                String horarioInicio = scanner.nextLine().trim();
                if (!horarioInicio.isEmpty()) {
                    prefs.setHorarioInicio(horarioInicio);
                }
                
                System.out.print("Horário de fim (HH:mm, ex: 22:00): ");
                String horarioFim = scanner.nextLine().trim();
                if (!horarioFim.isEmpty()) {
                    prefs.setHorarioFim(horarioFim);
                }
            }
            
            // Salvar preferências
            ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> response;
            if (prefs.getId() != null) {
                response = preferenciasController.atualizarPreferencias(prefs.getId(), prefs);
            } else {
                response = preferenciasController.criarPreferencias(prefs);
            }
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println();
                System.out.println("Preferências salvas com sucesso!");
            } else {
                System.out.println();
                System.out.println("Erro ao salvar preferências: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
            
        } catch (Exception e) {
            System.out.println("Erro ao configurar preferências: " + e.getMessage());
        }
        
        pausa();
    }

    public void ativarDesativarTodasNotificacoes(Long clienteId) {
        limparTela();
        System.out.println("=== ATIVAR/DESATIVAR TODAS AS NOTIFICAÇÕES ===");
        System.out.println();
        
        System.out.println("1. Ativar todas as notificações");
        System.out.println("2. Desativar todas as notificações");
        System.out.println("0. Voltar");
        System.out.println();
        System.out.print("Escolha uma opção: ");
        
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            
            if (opcao == 0) return;
            
            if (opcao != 1 && opcao != 2) {
                System.out.println("Opção inválida!");
                pausa();
                return;
            }
            
            boolean ativar = (opcao == 1);
            
            // Buscar preferências atuais ou criar novo objeto
            ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> responseAtual = 
                preferenciasController.buscarPorCliente(clienteId);
            
            PreferenciasNotificacaoDto prefs;
            if (responseAtual.getStatusCode().is2xxSuccessful() && 
                responseAtual.getBody() != null && 
                responseAtual.getBody().getDados() != null) {
                prefs = responseAtual.getBody().getDados();
            } else {
                prefs = new PreferenciasNotificacaoDto();
                prefs.setClienteId(clienteId);
            }
            
            // Configurar todas as preferências
            prefs.setEmailAtivo(ativar);
            prefs.setSmsAtivo(ativar);
            prefs.setPushAtivo(ativar);
            
            // Salvar preferências
            ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> response;
            if (prefs.getId() != null) {
                response = preferenciasController.atualizarPreferencias(prefs.getId(), prefs);
            } else {
                response = preferenciasController.criarPreferencias(prefs);
            }
            
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println();
                System.out.println(ativar ? 
                    "Todas as notificações foram ATIVADAS!" : 
                    "Todas as notificações foram DESATIVADAS!");
            } else {
                System.out.println();
                System.out.println("Erro ao atualizar preferências: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
            
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida! Digite apenas números.");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Erro ao atualizar preferências: " + e.getMessage());
        }
        
        pausa();
    }
    
    private void criarPreferenciasPadrao(Long clienteId) {
        try {
            PreferenciasNotificacaoDto novaPreferencia = new PreferenciasNotificacaoDto();
            novaPreferencia.setClienteId(clienteId);
            
            // Configurar valores padrão
            novaPreferencia.setEmailAtivo(true);
            novaPreferencia.setSmsAtivo(false); // SMS desativado por padrão
            novaPreferencia.setPushAtivo(true);
            novaPreferencia.setHorarioInicio("08:00");
            novaPreferencia.setHorarioFim("22:00");
            
            ResponseEntity<ResponseDto<PreferenciasNotificacaoDto>> response = 
                preferenciasController.criarPreferencias(novaPreferencia);
            
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                PreferenciasNotificacaoDto preferenciaCriada = response.getBody().getDados();
                System.out.println();
                System.out.println("Preferências padrão criadas com sucesso!");
                System.out.println("ID: " + preferenciaCriada.getId());
                System.out.println();
                System.out.println("Configurações padrão:");
                System.out.println("- E-mail: ATIVADO");
                System.out.println("- SMS: DESATIVADO");
                System.out.println("- Push: ATIVADO");
                System.out.println("- Horário: 08:00 às 22:00");
            } else {
                System.out.println();
                System.out.println("Erro ao criar preferências padrão: " + 
                    (response.getBody() != null ? response.getBody().getMensagem() : "Erro desconhecido"));
            }
        } catch (Exception e) {
            System.out.println("Erro ao criar preferências: " + e.getMessage());
        }
    }
    
    private boolean lerSimNao() {
        while (true) {
            String resposta = scanner.nextLine().trim().toUpperCase();
            if (resposta.equals("S") || resposta.equals("SIM")) {
                return true;
            } else if (resposta.equals("N") || resposta.equals("NAO") || resposta.equals("NÃO")) {
                return false;
            } else {
                System.out.print("Resposta inválida. Digite S para Sim ou N para Não: ");
            }
        }
    }
}
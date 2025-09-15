package br.edu.infnet.config;

import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.model.entity.Avaliacao;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.DadosRastreamento;
import br.edu.infnet.model.entity.HistoricoStatus;
import br.edu.infnet.model.entity.Notificacao;
import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.model.entity.PreferenciasNotificacao;
import br.edu.infnet.model.entity.SolicitacaoSuporte;
import br.edu.infnet.model.enums.StatusPedido;
import br.edu.infnet.model.enums.StatusSuporte;
import br.edu.infnet.model.enums.TipoNotificacao;
import br.edu.infnet.repository.AtendenteSuporteRepository;
import br.edu.infnet.repository.AvaliacaoRepository;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.DadosRastreamentoRepository;
import br.edu.infnet.repository.HistoricoStatusRepository;
import br.edu.infnet.repository.NotificacaoRepository;
import br.edu.infnet.repository.PedidoRepository;
import br.edu.infnet.repository.PreferenciasNotificacaoRepository;
import br.edu.infnet.repository.SolicitacaoSuporteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/**
 * Configuração do banco de dados e dados iniciais
 * Cria dados de teste para desenvolvimento
 */
@Configuration
public class DatabaseConfig {

    /**
     * Carrega dados iniciais para desenvolvimento e testes
     * Executa apenas no profile 'dev' ou quando não há profile específico
     */
    @Bean
    @Order(1)
    @Profile({"dev", "default"})
    public CommandLineRunner loadInitialData(
            ClienteRepository clienteRepository,
            AtendenteSuporteRepository atendenteSuporteRepository,
            PedidoRepository pedidoRepository,
            HistoricoStatusRepository historicoStatusRepository,
            PreferenciasNotificacaoRepository preferenciasNotificacaoRepository,
            NotificacaoRepository notificacaoRepository,
            DadosRastreamentoRepository dadosRastreamentoRepository,
            AvaliacaoRepository avaliacaoRepository,
            SolicitacaoSuporteRepository solicitacaoSuporteRepository) {

        return args -> {
            // Verificar se já existem dados para evitar duplicação
            if (clienteRepository.count() == 0) {
                criarClientesIniciais(clienteRepository);
            }

            if (atendenteSuporteRepository.count() == 0) {
                criarAtendentesIniciais(atendenteSuporteRepository);
            }

            // Criar pedidos apenas se não existirem
            if (pedidoRepository.count() == 0) {
                criarPedidosIniciais(clienteRepository, pedidoRepository, historicoStatusRepository);
            }

            if (preferenciasNotificacaoRepository.count() == 0) {
                criarPreferenciasNotificacaoIniciais(clienteRepository, preferenciasNotificacaoRepository);
            }

            if (notificacaoRepository.count() == 0) {
                criarNotificacoesIniciais(clienteRepository, pedidoRepository, notificacaoRepository);
            }

            if (dadosRastreamentoRepository.count() == 0) {
                criarDadosRastreamentoIniciais(pedidoRepository, dadosRastreamentoRepository);
            }

            // Sprint 4 - Avaliações e Solicitações de Suporte
            if (avaliacaoRepository.count() == 0) {
                criarAvaliacoesIniciais(clienteRepository, pedidoRepository, avaliacaoRepository);
            }

            if (solicitacaoSuporteRepository.count() == 0) {
                criarSolicitacoesSuporteIniciais(clienteRepository, atendenteSuporteRepository,
                        solicitacaoSuporteRepository);
            }

            System.out.println("Clientes cadastrados: " + clienteRepository.count());
            System.out.println("Atendentes cadastrados: " + atendenteSuporteRepository.count());
            System.out.println("Pedidos cadastrados: " + pedidoRepository.count());
            System.out.println("Histórico de status: " + historicoStatusRepository.count());
            System.out.println("Preferências de notificação: " + preferenciasNotificacaoRepository.count());
            System.out.println("Notificações: " + notificacaoRepository.count());
            System.out.println("Dados de rastreamento: " + dadosRastreamentoRepository.count());
            System.out.println("Avaliações: " + avaliacaoRepository.count());
            System.out.println("Solicitações de suporte: " + solicitacaoSuporteRepository.count());
            System.out.println("===========================================");
        };
    }

    private void criarClientesIniciais(ClienteRepository clienteRepository) {
        // Cliente 1 - João Silva
        Cliente cliente1 = new Cliente(
                "João Silva",
                "joao.silva@email.com",
                "12345678", // senha
                "12345678901", // CPF
                "Rua das Flores, 123",
                "São Paulo",
                "01234567" // CEP
        );
        cliente1.setTelefone("11987654321");
        clienteRepository.save(cliente1);

        // Cliente 2 - Maria Santos
        Cliente cliente2 = new Cliente(
                "Maria Santos",
                "maria.santos@email.com",
                "87654321", // senha
                "98765432109", // CPF
                "Av. Brasil, 456",
                "Rio de Janeiro",
                "20123456" // CEP
        );
        cliente2.setTelefone("21987654321");
        clienteRepository.save(cliente2);

        // Cliente 3 - Pedro Oliveira
        Cliente cliente3 = new Cliente(
                "Pedro Oliveira",
                "pedro.oliveira@email.com",
                "senha123", // senha
                "11122233344", // CPF
                "Rua XV de Novembro, 789",
                "Curitiba",
                "80012345" // CEP
        );
        cliente3.setTelefone("41987654321");
        clienteRepository.save(cliente3);

        // Cliente 4 - Ana Costa
        Cliente cliente4 = new Cliente(
                "Ana Costa",
                "ana.costa@email.com",
                "minhasenha", // senha
                "55566677788", // CPF
                "Rua da Praia, 321",
                "Salvador",
                "40123456" // CEP
        );
        cliente4.setTelefone("71987654321");
        clienteRepository.save(cliente4);

        // Cliente 5 - Carlos Ferreira (Desativado para testes)
        Cliente cliente5 = new Cliente(
                "Carlos Ferreira",
                "carlos.ferreira@email.com",
                "password", // senha
                "99988877766", // CPF
                "Av. Paulista, 1000",
                "São Paulo",
                "01310100" // CEP
        );
        cliente5.setTelefone("11888777666");
        cliente5.setAtivo(false); // Cliente desativado para testes
        clienteRepository.save(cliente5);
    }

    private void criarAtendentesIniciais(AtendenteSuporteRepository atendenteSuporteRepository) {
        // Atendente 1 - Suporte Técnico
        AtendenteSuporte atendente1 = new AtendenteSuporte(
                "Lucas Suporte",
                "lucas.suporte@empresa.com",
                "suporte123", // senha
                "AT001",
                "Suporte Técnico"
        );
        atendente1.setTelefone("1133334444");
        atendenteSuporteRepository.save(atendente1);

        // Atendente 2 - Atendimento ao Cliente
        AtendenteSuporte atendente2 = new AtendenteSuporte(
                "Fernanda Atendimento",
                "fernanda.atendimento@empresa.com",
                "atende456", // senha
                "AT002",
                "Atendimento ao Cliente"
        );
        atendente2.setTelefone("1144445555");
        atendenteSuporteRepository.save(atendente2);

        // Atendente 3 - Suporte Avançado
        AtendenteSuporte atendente3 = new AtendenteSuporte(
                "Roberto Especialista",
                "roberto.especialista@empresa.com",
                "expert789", // senha
                "AT003",
                "Suporte Avançado"
        );
        atendente3.setTelefone("1155556666");
        atendenteSuporteRepository.save(atendente3);

        // Atendente 4 - Supervisão (Desativado para testes)
        AtendenteSuporte atendente4 = new AtendenteSuporte(
                "Mariana Supervisora",
                "mariana.supervisora@empresa.com",
                "supervisor", // senha
                "AT004",
                "Supervisão"
        );
        atendente4.setTelefone("1166667777");
        atendente4.setAtivo(false); // Atendente desativado para testes
        atendenteSuporteRepository.save(atendente4);
    }

    private void criarPedidosIniciais(ClienteRepository clienteRepository,
                                      PedidoRepository pedidoRepository,
                                      HistoricoStatusRepository historicoStatusRepository) {

        List<Cliente> clientes = clienteRepository.findByAtivoTrue();

        if (clientes.size() < 4) {
            System.out.println("Erro: Não há clientes suficientes para criar pedidos de demonstração");
            return;
        }

        // Pedido 1 - João Silva - ENTREGUE
        Pedido pedido1 = new Pedido("PED00000001", clientes.get(0), new BigDecimal("299.99"));
        pedido1.setDataCompra(LocalDateTime.now().minusDays(15));
        pedido1.setPrevisaoEntrega(LocalDate.now().minusDays(3));
        pedido1.setStatus(StatusPedido.ENTREGUE);
        pedido1.setObservacoes("Primeiro pedido do cliente - Notebook");
        pedidoRepository.save(pedido1);

        // Histórico do Pedido 1
        criarHistoricoCompleto(pedido1, historicoStatusRepository);

        // Pedido 2 - Maria Santos - EM_TRANSITO
        Pedido pedido2 = new Pedido("PED00000002", clientes.get(1), new BigDecimal("149.90"));
        pedido2.setDataCompra(LocalDateTime.now().minusDays(5));
        pedido2.setPrevisaoEntrega(LocalDate.now().plusDays(2));
        pedido2.setStatus(StatusPedido.EM_TRANSITO);
        pedido2.setObservacoes("Smartphone - Entrega expressa");
        pedidoRepository.save(pedido2);

        // Histórico do Pedido 2
        criarHistoricoStatus(pedido2, StatusPedido.CONFIRMADO,
                "Pedido confirmado automaticamente", "SISTEMA",
                pedido2.getDataCompra(), historicoStatusRepository);
        criarHistoricoStatus(pedido2, StatusPedido.PROCESSANDO,
                "Pedido sendo preparado", "Lucas Suporte",
                pedido2.getDataCompra().plusHours(2), historicoStatusRepository);
        criarHistoricoStatus(pedido2, StatusPedido.ENVIADO,
                "Produto enviado via transportadora", "Fernanda Atendimento",
                pedido2.getDataCompra().plusDays(1), historicoStatusRepository);
        criarHistoricoStatus(pedido2, StatusPedido.EM_TRANSITO,
                "Produto em trânsito para entrega", "SISTEMA",
                pedido2.getDataCompra().plusDays(2), historicoStatusRepository);

        // Pedido 3 - Pedro Oliveira - PROCESSANDO
        Pedido pedido3 = new Pedido("PED00000003", clientes.get(2), new BigDecimal("89.99"));
        pedido3.setDataCompra(LocalDateTime.now().minusDays(2));
        pedido3.setPrevisaoEntrega(LocalDate.now().plusDays(5));
        pedido3.setStatus(StatusPedido.PROCESSANDO);
        pedido3.setObservacoes("Fones de ouvido Bluetooth");
        pedidoRepository.save(pedido3);

        // Histórico do Pedido 3
        criarHistoricoStatus(pedido3, StatusPedido.CONFIRMADO,
                "Pedido confirmado automaticamente", "SISTEMA",
                pedido3.getDataCompra(), historicoStatusRepository);
        criarHistoricoStatus(pedido3, StatusPedido.PROCESSANDO,
                "Produto sendo separado no estoque", "Roberto Especialista",
                pedido3.getDataCompra().plusHours(6), historicoStatusRepository);

        // Pedido 4 - Ana Costa - CONFIRMADO
        Pedido pedido4 = new Pedido("PED00000004", clientes.get(3), new BigDecimal("199.99"));
        pedido4.setDataCompra(LocalDateTime.now().minusHours(8));
        pedido4.setPrevisaoEntrega(LocalDate.now().plusDays(7));
        pedido4.setStatus(StatusPedido.CONFIRMADO);
        pedido4.setObservacoes("Tablet - Primeira compra");
        pedidoRepository.save(pedido4);

        // Histórico do Pedido 4
        criarHistoricoStatus(pedido4, StatusPedido.CONFIRMADO,
                "Pedido recebido e confirmado", "SISTEMA",
                pedido4.getDataCompra(), historicoStatusRepository);

        // Pedido 5 - João Silva - SAIU_PARA_ENTREGA
        Pedido pedido5 = new Pedido("PED00000005", clientes.get(0), new BigDecimal("459.90"));
        pedido5.setDataCompra(LocalDateTime.now().minusDays(3));
        pedido5.setPrevisaoEntrega(LocalDate.now().plusDays(1));
        pedido5.setStatus(StatusPedido.SAIU_PARA_ENTREGA);
        pedido5.setObservacoes("Monitor 24 polegadas");
        pedidoRepository.save(pedido5);

        // Histórico do Pedido 5
        criarHistoricoStatus(pedido5, StatusPedido.CONFIRMADO,
                "Pedido confirmado", "SISTEMA",
                pedido5.getDataCompra(), historicoStatusRepository);
        criarHistoricoStatus(pedido5, StatusPedido.PROCESSANDO,
                "Preparando produto", "Lucas Suporte",
                pedido5.getDataCompra().plusHours(4), historicoStatusRepository);
        criarHistoricoStatus(pedido5, StatusPedido.ENVIADO,
                "Enviado para transportadora", "Fernanda Atendimento",
                pedido5.getDataCompra().plusDays(1), historicoStatusRepository);
        criarHistoricoStatus(pedido5, StatusPedido.EM_TRANSITO,
                "Em rota de entrega", "SISTEMA",
                pedido5.getDataCompra().plusDays(2), historicoStatusRepository);
        criarHistoricoStatus(pedido5, StatusPedido.SAIU_PARA_ENTREGA,
                "Saiu para entrega - Previsão hoje", "SISTEMA",
                LocalDateTime.now().minusHours(2), historicoStatusRepository);

        // Pedido 6 - Maria Santos - CANCELADO
        Pedido pedido6 = new Pedido("PED00000006", clientes.get(1), new BigDecimal("79.90"));
        pedido6.setDataCompra(LocalDateTime.now().minusDays(7));
        pedido6.setStatus(StatusPedido.CANCELADO);
        pedido6.setObservacoes("Produto cancelado a pedido do cliente");
        pedidoRepository.save(pedido6);

        // Histórico do Pedido 6
        criarHistoricoStatus(pedido6, StatusPedido.CONFIRMADO,
                "Pedido confirmado", "SISTEMA",
                pedido6.getDataCompra(), historicoStatusRepository);
        criarHistoricoStatus(pedido6, StatusPedido.CANCELADO,
                "Cancelado a pedido do cliente", "Fernanda Atendimento",
                pedido6.getDataCompra().plusDays(1), historicoStatusRepository);

        // Pedido 7 - Pedro Oliveira - ENVIADO
        Pedido pedido7 = new Pedido("PED00000007", clientes.get(2), new BigDecimal("329.99"));
        pedido7.setDataCompra(LocalDateTime.now().minusDays(4));
        pedido7.setPrevisaoEntrega(LocalDate.now().plusDays(3));
        pedido7.setStatus(StatusPedido.ENVIADO);
        pedido7.setObservacoes("Impressora multifuncional");
        pedidoRepository.save(pedido7);

        // Histórico do Pedido 7
        criarHistoricoStatus(pedido7, StatusPedido.CONFIRMADO,
                "Pedido confirmado", "SISTEMA",
                pedido7.getDataCompra(), historicoStatusRepository);
        criarHistoricoStatus(pedido7, StatusPedido.PROCESSANDO,
                "Produto em separação", "Roberto Especialista",
                pedido7.getDataCompra().plusHours(8), historicoStatusRepository);
        criarHistoricoStatus(pedido7, StatusPedido.ENVIADO,
                "Produto enviado - Código de rastreamento: BR123456789", "Lucas Suporte",
                pedido7.getDataCompra().plusDays(2), historicoStatusRepository);
    }

    private void criarHistoricoCompleto(Pedido pedido, HistoricoStatusRepository historicoStatusRepository) {
        // Simular um pedido que passou por todo o ciclo
        LocalDateTime baseTime = pedido.getDataCompra();

        criarHistoricoStatus(pedido, StatusPedido.CONFIRMADO,
                "Pedido confirmado automaticamente", "SISTEMA",
                baseTime, historicoStatusRepository);

        criarHistoricoStatus(pedido, StatusPedido.PROCESSANDO,
                "Produto sendo preparado", "Lucas Suporte",
                baseTime.plusHours(3), historicoStatusRepository);

        criarHistoricoStatus(pedido, StatusPedido.ENVIADO,
                "Produto enviado via Correios", "Fernanda Atendimento",
                baseTime.plusDays(1), historicoStatusRepository);

        criarHistoricoStatus(pedido, StatusPedido.EM_TRANSITO,
                "Produto em trânsito", "SISTEMA",
                baseTime.plusDays(2), historicoStatusRepository);

        criarHistoricoStatus(pedido, StatusPedido.SAIU_PARA_ENTREGA,
                "Saiu para entrega final", "SISTEMA",
                baseTime.plusDays(3), historicoStatusRepository);

        criarHistoricoStatus(pedido, StatusPedido.ENTREGUE,
                "Produto entregue com sucesso", "SISTEMA",
                baseTime.plusDays(4), historicoStatusRepository);
    }

    private void criarHistoricoStatus(Pedido pedido, StatusPedido status, String observacao,
                                      String responsavel, LocalDateTime dataAtualizacao,
                                      HistoricoStatusRepository historicoStatusRepository) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setPedido(pedido);
        historico.setStatus(status);
        historico.setObservacao(observacao);
        historico.setResponsavel(responsavel);
        historico.setDataAtualizacao(dataAtualizacao);

        historicoStatusRepository.save(historico);
    }

    private void criarPreferenciasNotificacaoIniciais(ClienteRepository clienteRepository,
                                                      PreferenciasNotificacaoRepository preferenciasNotificacaoRepository) {
        List<Cliente> clientes = clienteRepository.findByAtivoTrue();

        for (Cliente cliente : clientes) {
            PreferenciasNotificacao preferencias = new PreferenciasNotificacao();
            preferencias.setCliente(cliente);

            switch (cliente.getNome()) {
                case "João Silva":
                    preferencias.setEmailAtivo(true);
                    preferencias.setSmsAtivo(true);
                    preferencias.setPushAtivo(true);
                    preferencias.setHorarioInicio(LocalTime.of(8, 0));
                    preferencias.setHorarioFim(LocalTime.of(22, 0));
                    break;

                case "Maria Santos":
                    preferencias.setEmailAtivo(true);
                    preferencias.setSmsAtivo(false);
                    preferencias.setPushAtivo(false);
                    preferencias.setHorarioInicio(LocalTime.of(9, 0));
                    preferencias.setHorarioFim(LocalTime.of(18, 0));
                    break;

                case "Pedro Oliveira":
                    preferencias.setEmailAtivo(false);
                    preferencias.setSmsAtivo(true);
                    preferencias.setPushAtivo(true);
                    preferencias.setHorarioInicio(LocalTime.of(7, 0));
                    preferencias.setHorarioFim(LocalTime.of(20, 0));
                    break;

                case "Ana Costa":
                    preferencias.setEmailAtivo(true);
                    preferencias.setSmsAtivo(false);
                    preferencias.setPushAtivo(true);
                    preferencias.setHorarioInicio(LocalTime.of(10, 0));
                    preferencias.setHorarioFim(LocalTime.of(21, 0));
                    break;

                default:
                    preferencias.setEmailAtivo(true);
                    preferencias.setSmsAtivo(false);
                    preferencias.setPushAtivo(true);
                    preferencias.setHorarioInicio(LocalTime.of(8, 0));
                    preferencias.setHorarioFim(LocalTime.of(22, 0));
                    break;
            }

            preferenciasNotificacaoRepository.save(preferencias);
        }
    }

    private void criarNotificacoesIniciais(ClienteRepository clienteRepository,
                                           PedidoRepository pedidoRepository,
                                           NotificacaoRepository notificacaoRepository) {
        List<Cliente> clientes = clienteRepository.findByAtivoTrue();
        List<Pedido> pedidos = pedidoRepository.findAll();

        if (pedidos.isEmpty()) {
            return;
        }

        Cliente joao = clientes.stream().filter(c -> c.getNome().equals("João Silva")).findFirst().orElse(null);
        if (joao != null) {
            Pedido pedidoJoao = pedidos.stream().filter(p -> p.getCliente().getId().equals(joao.getId())).findFirst().orElse(null);
            if (pedidoJoao != null) {
                criarNotificacao(pedidoJoao, joao, TipoNotificacao.EMAIL,
                        "Pedido Confirmado",
                        "Seu pedido " + pedidoJoao.getNumeroPedido() + " foi confirmado com sucesso!",
                        LocalDateTime.now().minusDays(2), true, notificacaoRepository);

                criarNotificacao(pedidoJoao, joao, TipoNotificacao.SMS,
                        "Pedido Enviado",
                        "Seu pedido está a caminho! Acompanhe pelo código de rastreamento.",
                        LocalDateTime.now().minusDays(1), true, notificacaoRepository);

                criarNotificacao(pedidoJoao, joao, TipoNotificacao.PUSH,
                        "Novo Desconto Disponível",
                        "Aproveite 15% de desconto em sua próxima compra!",
                        LocalDateTime.now().minusHours(3), false, notificacaoRepository);
            }
        }

        Cliente maria = clientes.stream().filter(c -> c.getNome().equals("Maria Santos")).findFirst().orElse(null);
        if (maria != null) {
            Pedido pedidoMaria = pedidos.stream().filter(p -> p.getCliente().getId().equals(maria.getId())).findFirst().orElse(null);
            if (pedidoMaria != null) {
                criarNotificacao(pedidoMaria, maria, TipoNotificacao.EMAIL,
                        "Pedido em Trânsito",
                        "Seu pedido " + pedidoMaria.getNumeroPedido() + " está em trânsito para entrega.",
                        LocalDateTime.now().minusHours(6), false, notificacaoRepository);

                criarNotificacao(pedidoMaria, maria, TipoNotificacao.EMAIL,
                        "Atualização de Status",
                        "Status do seu pedido foi atualizado. Verifique os detalhes.",
                        LocalDateTime.now().minusHours(12), true, notificacaoRepository);
            }
        }

        Cliente pedro = clientes.stream().filter(c -> c.getNome().equals("Pedro Oliveira")).findFirst().orElse(null);
        if (pedro != null) {
            Pedido pedidoPedro = pedidos.stream().filter(p -> p.getCliente().getId().equals(pedro.getId())).findFirst().orElse(null);
            if (pedidoPedro != null) {
                criarNotificacao(pedidoPedro, pedro, TipoNotificacao.PUSH,
                        "Preparando Pedido",
                        "Seu pedido " + pedidoPedro.getNumeroPedido() + " está sendo preparado.",
                        LocalDateTime.now().minusHours(8), true, notificacaoRepository);

                criarNotificacao(pedidoPedro, pedro, TipoNotificacao.SMS,
                        "Lembrete de Acompanhamento",
                        "Não se esqueça de acompanhar seu pedido pelo nosso sistema!",
                        LocalDateTime.now().minusMinutes(30), false, notificacaoRepository);
            }
        }

        Cliente ana = clientes.stream().filter(c -> c.getNome().equals("Ana Costa")).findFirst().orElse(null);
        if (ana != null) {
            Pedido pedidoAna = pedidos.stream().filter(p -> p.getCliente().getId().equals(ana.getId())).findFirst().orElse(null);
            if (pedidoAna != null) {
                criarNotificacao(pedidoAna, ana, TipoNotificacao.EMAIL,
                        "Bem-vinda!",
                        "Obrigado por realizar seu primeiro pedido conosco, " + ana.getNome() + "!",
                        LocalDateTime.now().minusDays(1), true, notificacaoRepository);

                criarNotificacao(pedidoAna, ana, TipoNotificacao.PUSH,
                        "Pedido Confirmado",
                        "Seu pedido " + pedidoAna.getNumeroPedido() + " foi confirmado e será processado em breve.",
                        LocalDateTime.now().minusHours(2), false, notificacaoRepository);
            }
        }
    }

    private void criarNotificacao(Pedido pedido, Cliente cliente, TipoNotificacao tipo,
                                  String titulo, String mensagem, LocalDateTime dataEnvio,
                                  boolean lida, NotificacaoRepository notificacaoRepository) {
        Notificacao notificacao = new Notificacao();
        notificacao.setPedido(pedido);
        notificacao.setCliente(cliente);
        notificacao.setTipo(tipo);
        notificacao.setTitulo(titulo);
        notificacao.setMensagem(mensagem);
        notificacao.setDataEnvio(dataEnvio);
        notificacao.setLida(lida);

        notificacaoRepository.save(notificacao);
    }

    private void criarDadosRastreamentoIniciais(PedidoRepository pedidoRepository,
                                                DadosRastreamentoRepository dadosRastreamentoRepository) {
        List<Pedido> pedidos = pedidoRepository.findAll();

        for (Pedido pedido : pedidos) {
            if (pedido.getStatus() == StatusPedido.ENVIADO ||
                    pedido.getStatus() == StatusPedido.EM_TRANSITO ||
                    pedido.getStatus() == StatusPedido.SAIU_PARA_ENTREGA ||
                    pedido.getStatus() == StatusPedido.ENTREGUE) {

                DadosRastreamento dados = new DadosRastreamento();
                dados.setPedido(pedido);

                String codigo = "BR" + pedido.getNumeroPedido().substring(3) + "SP";
                dados.setCodigoRastreamento(codigo);

                switch ((int) (pedido.getId() % 3)) {
                    case 0:
                        dados.setTransportadora("Correios");
                        break;
                    case 1:
                        dados.setTransportadora("Loggi");
                        break;
                    case 2:
                        dados.setTransportadora("Jadlog");
                        break;
                }

                switch (pedido.getStatus()) {
                    case ENVIADO:
                        dados.setLocalizacaoAtual("Centro de Distribuição - São Paulo");
                        break;
                    case EM_TRANSITO:
                        dados.setLocalizacaoAtual("Em trânsito para o destino");
                        break;
                    case SAIU_PARA_ENTREGA:
                        dados.setLocalizacaoAtual("Veículo de entrega - Rota local");
                        break;
                    case ENTREGUE:
                        dados.setLocalizacaoAtual("Entregue no destino");
                        break;
                    default:
                        dados.setLocalizacaoAtual("Centro de Distribuição");
                        break;
                }

                dados.setUltimaAtualizacao(LocalDateTime.now().minusHours(pedido.getId() % 24));

                dadosRastreamentoRepository.save(dados);
            }
        }
    }

    private void criarAvaliacoesIniciais(ClienteRepository clienteRepository,
                                         PedidoRepository pedidoRepository,
                                         AvaliacaoRepository avaliacaoRepository) {

        List<Cliente> clientes = clienteRepository.findByAtivoTrue();
        List<Pedido> pedidosEntregues = pedidoRepository.findAll().stream()
                .filter(p -> p.getStatus() == StatusPedido.ENTREGUE)
                .toList();

        if (pedidosEntregues.isEmpty()) {
            System.out.println("Nenhum pedido entregue encontrado para criar avaliações");
            return;
        }

        // Avaliação 1 - João Silva - Pedido Entregue - Muito satisfeito
        Cliente joao = clientes.stream().filter(c -> c.getNome().equals("João Silva")).findFirst().orElse(null);
        if (joao != null) {
            Pedido pedidoJoao = pedidosEntregues.stream()
                    .filter(p -> p.getCliente().getId().equals(joao.getId()))
                    .findFirst().orElse(null);

            if (pedidoJoao != null) {
                Avaliacao avaliacao1 = new Avaliacao(pedidoJoao, joao, 5, 5);
                avaliacao1.setComentario("Excelente serviço! O acompanhamento em tempo real funcionou perfeitamente e a entrega foi muito rápida. Recomendo!");
                avaliacao1.setDataAvaliacao(LocalDateTime.now().minusDays(10));
                avaliacaoRepository.save(avaliacao1);
            }
        }

        // Avaliação 2 - Maria Santos - Nota média
        Cliente maria = clientes.stream().filter(c -> c.getNome().equals("Maria Santos")).findFirst().orElse(null);
        if (maria != null) {
            // Criar um pedido entregue para Maria se não existir
            Pedido pedidoMariaEntregue = new Pedido("PED00000008", maria, new BigDecimal("89.90"));
            pedidoMariaEntregue.setDataCompra(LocalDateTime.now().minusDays(20));
            pedidoMariaEntregue.setPrevisaoEntrega(LocalDate.now().minusDays(15));
            pedidoMariaEntregue.setStatus(StatusPedido.ENTREGUE);
            pedidoMariaEntregue.setObservacoes("Capa de celular personalizada");
            pedidoRepository.save(pedidoMariaEntregue);

            Avaliacao avaliacao2 = new Avaliacao(pedidoMariaEntregue, maria, 3, 4);
            avaliacao2.setComentario("O produto chegou bem, mas o acompanhamento poderia ter mais atualizações durante o trajeto.");
            avaliacao2.setDataAvaliacao(LocalDateTime.now().minusDays(14));
            avaliacaoRepository.save(avaliacao2);
        }

        // Avaliação 3 - Pedro Oliveira - Insatisfeito com acompanhamento
        Cliente pedro = clientes.stream().filter(c -> c.getNome().equals("Pedro Oliveira")).findFirst().orElse(null);
        if (pedro != null) {
            Pedido pedidoPedroEntregue = new Pedido("PED00000009", pedro, new BigDecimal("249.99"));
            pedidoPedroEntregue.setDataCompra(LocalDateTime.now().minusDays(25));
            pedidoPedroEntregue.setPrevisaoEntrega(LocalDate.now().minusDays(18));
            pedidoPedroEntregue.setStatus(StatusPedido.ENTREGUE);
            pedidoPedroEntregue.setObservacoes("Smartwatch fitness");
            pedidoRepository.save(pedidoPedroEntregue);

            Avaliacao avaliacao3 = new Avaliacao(pedidoPedroEntregue, pedro, 2, 5);
            avaliacao3.setComentario("A entrega foi perfeita, mas o sistema de rastreamento ficou desatualizado por 3 dias. Tive que ligar no suporte para saber onde estava meu pedido.");
            avaliacao3.setDataAvaliacao(LocalDateTime.now().minusDays(17));
            avaliacaoRepository.save(avaliacao3);
        }

        // Avaliação 4 - Ana Costa - Satisfeita
        Cliente ana = clientes.stream().filter(c -> c.getNome().equals("Ana Costa")).findFirst().orElse(null);
        if (ana != null) {
            Pedido pedidoAnaEntregue = new Pedido("PED00000010", ana, new BigDecimal("179.90"));
            pedidoAnaEntregue.setDataCompra(LocalDateTime.now().minusDays(12));
            pedidoAnaEntregue.setPrevisaoEntrega(LocalDate.now().minusDays(5));
            pedidoAnaEntregue.setStatus(StatusPedido.ENTREGUE);
            pedidoAnaEntregue.setObservacoes("Kit de maquiagem profissional");
            pedidoRepository.save(pedidoAnaEntregue);

            Avaliacao avaliacao4 = new Avaliacao(pedidoAnaEntregue, ana, 4, 4);
            avaliacao4.setComentario("Boa experiência geral. As notificações por email funcionaram muito bem.");
            avaliacao4.setDataAvaliacao(LocalDateTime.now().minusDays(4));
            avaliacaoRepository.save(avaliacao4);
        }

        // Avaliação 5 - João Silva (segunda compra) - Nota máxima
        if (joao != null) {
            Pedido segundoPedidoJoao = new Pedido("PED00000011", joao, new BigDecimal("599.99"));
            segundoPedidoJoao.setDataCompra(LocalDateTime.now().minusDays(8));
            segundoPedidoJoao.setPrevisaoEntrega(LocalDate.now().minusDays(2));
            segundoPedidoJoao.setStatus(StatusPedido.ENTREGUE);
            segundoPedidoJoao.setObservacoes("Console de videogame");
            pedidoRepository.save(segundoPedidoJoao);

            Avaliacao avaliacao5 = new Avaliacao(segundoPedidoJoao, joao, 5, 5);
            avaliacao5.setComentario("Perfeito novamente! O sistema está cada vez melhor.");
            avaliacao5.setDataAvaliacao(LocalDateTime.now().minusDays(1));
            avaliacaoRepository.save(avaliacao5);
        }
    }

    private void criarSolicitacoesSuporteIniciais(ClienteRepository clienteRepository,
                                                  AtendenteSuporteRepository atendenteSuporteRepository,
                                                  SolicitacaoSuporteRepository solicitacaoSuporteRepository) {

        List<Cliente> clientes = clienteRepository.findByAtivoTrue();
        List<AtendenteSuporte> atendentes = atendenteSuporteRepository.findByAtivoTrue();

        if (clientes.isEmpty() || atendentes.isEmpty()) {
            System.out.println("Não há clientes ou atendentes suficientes para criar solicitações de suporte");
            return;
        }

        // Solicitação 1 - João Silva - Problema com rastreamento - RESOLVIDO
        Cliente joao = clientes.stream().filter(c -> c.getNome().equals("João Silva")).findFirst().orElse(null);
        AtendenteSuporte lucas = atendentes.stream().filter(a -> a.getNome().equals("Lucas Suporte")).findFirst().orElse(null);

        if (joao != null && lucas != null) {
            SolicitacaoSuporte solicitacao1 = new SolicitacaoSuporte(
                    joao,
                    "SUP20240001",
                    "Rastreamento não atualiza",
                    "Meu pedido PED00000005 está há 2 dias sem atualização no rastreamento. Podem verificar?"
            );
            solicitacao1.setAtendente(lucas);
            solicitacao1.setStatus(StatusSuporte.RESOLVIDO);
            solicitacao1.setDataAbertura(LocalDateTime.now().minusDays(5));
            solicitacao1.setDataFechamento(LocalDateTime.now().minusDays(4).plusHours(2));
            solicitacaoSuporteRepository.save(solicitacao1);
        }

        // Solicitação 2 - Maria Santos - Dúvida sobre entrega - FECHADO
        Cliente maria = clientes.stream().filter(c -> c.getNome().equals("Maria Santos")).findFirst().orElse(null);
        AtendenteSuporte fernanda = atendentes.stream().filter(a -> a.getNome().equals("Fernanda Atendimento")).findFirst().orElse(null);

        if (maria != null && fernanda != null) {
            SolicitacaoSuporte solicitacao2 = new SolicitacaoSuporte(
                    maria,
                    "SUP20240002",
                    "Dúvida sobre prazo de entrega",
                    "Gostaria de saber se é possível alterar o endereço de entrega do pedido PED00000002?"
            );
            solicitacao2.setAtendente(fernanda);
            solicitacao2.setStatus(StatusSuporte.FECHADO);
            solicitacao2.setDataAbertura(LocalDateTime.now().minusDays(3));
            solicitacao2.setDataFechamento(LocalDateTime.now().minusDays(3).plusHours(1));
            solicitacaoSuporteRepository.save(solicitacao2);
        }

        // Solicitação 3 - Pedro Oliveira - Problema técnico - EM_ANDAMENTO
        Cliente pedro = clientes.stream().filter(c -> c.getNome().equals("Pedro Oliveira")).findFirst().orElse(null);
        AtendenteSuporte roberto = atendentes.stream().filter(a -> a.getNome().equals("Roberto Especialista")).findFirst().orElse(null);

        if (pedro != null && roberto != null) {
            SolicitacaoSuporte solicitacao3 = new SolicitacaoSuporte(
                    pedro,
                    "SUP20240003",
                    "Erro ao acessar detalhes do pedido",
                    "Quando tento ver os detalhes do meu pedido PED00000003, aparece erro 500. Já tentei em diferentes navegadores."
            );
            solicitacao3.setAtendente(roberto);
            solicitacao3.setStatus(StatusSuporte.EM_ANDAMENTO);
            solicitacao3.setDataAbertura(LocalDateTime.now().minusDays(1));
            solicitacaoSuporteRepository.save(solicitacao3);
        }

        // Solicitação 4 - Ana Costa - Reclamação - ABERTO
        Cliente ana = clientes.stream().filter(c -> c.getNome().equals("Ana Costa")).findFirst().orElse(null);

        if (ana != null) {
            SolicitacaoSuporte solicitacao4 = new SolicitacaoSuporte(
                    ana,
                    "SUP20240004",
                    "Notificações não chegam",
                    "Configurei para receber notificações por email e push, mas não estou recebendo nenhuma atualização do meu pedido PED00000004."
            );
            solicitacao4.setStatus(StatusSuporte.ABERTO);
            solicitacao4.setDataAbertura(LocalDateTime.now().minusHours(6));
            solicitacaoSuporteRepository.save(solicitacao4);
        }

        // Solicitação 5 - João Silva - Sugestão - ABERTO
        if (joao != null) {
            SolicitacaoSuporte solicitacao5 = new SolicitacaoSuporte(
                    joao,
                    "SUP20240005",
                    "Sugestão de melhoria",
                    "Seria muito útil se o sistema mostrasse a localização em tempo real do entregador quando o pedido sai para entrega, como fazem alguns aplicativos de delivery."
            );
            solicitacao5.setStatus(StatusSuporte.ABERTO);
            solicitacao5.setDataAbertura(LocalDateTime.now().minusHours(2));
            solicitacaoSuporteRepository.save(solicitacao5);
        }

        // Solicitação 6 - Maria Santos - Problema resolvido rapidamente
        if (maria != null && lucas != null) {
            SolicitacaoSuporte solicitacao6 = new SolicitacaoSuporte(
                    maria,
                    "SUP20240006",
                    "Código de rastreamento inválido",
                    "O código de rastreamento do pedido PED00000006 não funciona no site dos Correios."
            );
            solicitacao6.setAtendente(lucas);
            solicitacao6.setStatus(StatusSuporte.RESOLVIDO);
            solicitacao6.setDataAbertura(LocalDateTime.now().minusDays(7));
            solicitacao6.setDataFechamento(LocalDateTime.now().minusDays(7).plusMinutes(30));
            solicitacaoSuporteRepository.save(solicitacao6);
        }

        // Solicitação 7 - Pedro Oliveira - Urgente - EM_ANDAMENTO
        if (pedro != null && fernanda != null) {
            SolicitacaoSuporte solicitacao7 = new SolicitacaoSuporte(
                    pedro,
                    "SUP20240007",
                    "Pedido urgente atrasado",
                    "Meu pedido PED00000007 era urgente e já passou 2 dias do prazo de entrega. Preciso de uma posição urgente!"
            );
            solicitacao7.setAtendente(fernanda);
            solicitacao7.setStatus(StatusSuporte.EM_ANDAMENTO);
            solicitacao7.setDataAbertura(LocalDateTime.now().minusHours(4));
            solicitacaoSuporteRepository.save(solicitacao7);
        }
    }
}
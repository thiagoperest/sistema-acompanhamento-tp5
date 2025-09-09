package br.edu.infnet.config;

import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.model.entity.HistoricoStatus;
import br.edu.infnet.model.entity.Pedido;
import br.edu.infnet.model.enums.StatusPedido;
import br.edu.infnet.repository.AtendenteSuporteRepository;
import br.edu.infnet.repository.ClienteRepository;
import br.edu.infnet.repository.HistoricoStatusRepository;
import br.edu.infnet.repository.PedidoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
            HistoricoStatusRepository historicoStatusRepository) {

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

            System.out.println("=== DADOS INICIAIS CARREGADOS - SPRINT 2 ===");
            System.out.println("Clientes cadastrados: " + clienteRepository.count());
            System.out.println("Atendentes cadastrados: " + atendenteSuporteRepository.count());
            System.out.println("Pedidos cadastrados: " + pedidoRepository.count());
            System.out.println("Histórico de status: " + historicoStatusRepository.count());
            System.out.println("=====================================");
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
}
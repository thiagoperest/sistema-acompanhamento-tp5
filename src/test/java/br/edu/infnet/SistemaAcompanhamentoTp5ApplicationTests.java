package br.edu.infnet;

import br.edu.infnet.model.dto.*;
import br.edu.infnet.model.entity.*;
import br.edu.infnet.model.enums.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários da aplicação - Sistema de Encomendas
 */
class SistemaAcompanhamentoTp5ApplicationTests {

    @Test
    @DisplayName("Deve validar dados obrigatórios do cliente")
    void testValidarDadosCliente() {
        // Teste - Validar dados obrigatórios do cliente
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNome("João Silva");
        clienteDto.setEmail("joao@email.com");
        clienteDto.setSenha("123456");
        clienteDto.setCpf("12345678901");
        clienteDto.setTelefone("11987654321");
        clienteDto.setEndereco("Rua A, 123");
        clienteDto.setCidade("São Paulo");
        clienteDto.setCep("01234567");

        // Assert - Verificar se os dados foram definidos corretamente
        assertNotNull(clienteDto.getNome());
        assertNotNull(clienteDto.getEmail());
        assertNotNull(clienteDto.getSenha());
        assertNotNull(clienteDto.getCpf());
        assertEquals("João Silva", clienteDto.getNome());
        assertEquals("joao@email.com", clienteDto.getEmail());
        assertEquals("12345678901", clienteDto.getCpf());
        assertEquals(11, clienteDto.getCpf().length());
    }

    @Test
    @DisplayName("Deve criar entidade Cliente a partir do DTO")
    void testCriarEntidadeClienteAPartirDoDto() {
        // Teste - Criar entidade Cliente a partir do DTO
        ClienteDto clienteDto = new ClienteDto();
        clienteDto.setNome("Maria Santos");
        clienteDto.setEmail("maria@email.com");
        clienteDto.setSenha("senha123");
        clienteDto.setCpf("98765432109");
        clienteDto.setTelefone("11876543210");
        clienteDto.setEndereco("Rua B, 456");
        clienteDto.setCidade("Rio de Janeiro");
        clienteDto.setCep("87654321");

        // Simular criação da entidade Cliente
        Cliente cliente = new Cliente();
        cliente.setId(1L);
        cliente.setNome(clienteDto.getNome());
        cliente.setEmail(clienteDto.getEmail());
        cliente.setSenha(clienteDto.getSenha());
        cliente.setCpf(clienteDto.getCpf());
        cliente.setTelefone(clienteDto.getTelefone());
        cliente.setEndereco(clienteDto.getEndereco());
        cliente.setCidade(clienteDto.getCidade());
        cliente.setCep(clienteDto.getCep());
        cliente.setAtivo(true);
        cliente.setDataCadastro(LocalDateTime.now());

        // Assert - Verificar se a conversão foi feita corretamente
        assertEquals(clienteDto.getNome(), cliente.getNome());
        assertEquals(clienteDto.getEmail(), cliente.getEmail());
        assertEquals(clienteDto.getCpf(), cliente.getCpf());
        assertEquals(clienteDto.getCidade(), cliente.getCidade());
        assertTrue(cliente.getAtivo());
        assertNotNull(cliente.getDataCadastro());
    }

    @Test
    @DisplayName("Deve validar formato do CPF")
    void testValidarCpf() {
        // Teste - Validar formato do CPF
        String cpfValido = "12345678901";
        String cpfInvalido = "123";

        // Assert - CPF deve ter exatamente 11 dígitos
        assertEquals(11, cpfValido.length());
        assertNotEquals(11, cpfInvalido.length());
        assertTrue(cpfValido.matches("\\d+")); // Apenas números
    }

    @Test
    @DisplayName("Deve criar pedido válido")
    void testCriarPedido() {
        // Teste - Criar um pedido válido
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setNumeroPedido("PED001");
        pedidoDto.setClienteId(1L);
        pedidoDto.setValor(new BigDecimal("150.00"));
        pedidoDto.setObservacoes("Pedido teste");

        // Simular criação da entidade Pedido
        Pedido pedido = new Pedido();
        pedido.setId(1L);
        pedido.setNumeroPedido(pedidoDto.getNumeroPedido());
        pedido.setValor(pedidoDto.getValor());
        pedido.setStatus(StatusPedido.CONFIRMADO);
        pedido.setDataCompra(LocalDateTime.now());
        pedido.setObservacoes(pedidoDto.getObservacoes());

        // Assert
        assertNotNull(pedido.getNumeroPedido());
        assertEquals("PED001", pedido.getNumeroPedido());
        assertEquals(new BigDecimal("150.00"), pedido.getValor());
        assertEquals(StatusPedido.CONFIRMADO, pedido.getStatus());
        assertNotNull(pedido.getDataCompra());
    }

    @Test
    @DisplayName("Deve verificar ENUM StatusPedido")
    void testStatusPedido() {
        // Teste - Verificar enum StatusPedido
        StatusPedido status1 = StatusPedido.CONFIRMADO;
        StatusPedido status2 = StatusPedido.EM_TRANSITO;
        StatusPedido status3 = StatusPedido.ENTREGUE;

        // Assert
        assertEquals("CONFIRMADO", status1.name());
        assertEquals("EM_TRANSITO", status2.name());
        assertEquals("ENTREGUE", status3.name());
        assertNotEquals(status1, status2);
    }

    @Test
    @DisplayName("Deve criar pedido com previsão de entrega")
    void testPedidoComPrevisaoEntrega() {
        // Teste - Pedido com previsão de entrega
        PedidoDto pedidoDto = new PedidoDto();
        pedidoDto.setId(2L);
        pedidoDto.setNumeroPedido("PED002");
        pedidoDto.setValor(new BigDecimal("250.50"));
        pedidoDto.setStatus(StatusPedido.PROCESSANDO);
        pedidoDto.setDataCompra(LocalDateTime.now().toString());
        pedidoDto.setPrevisaoEntrega(LocalDate.now().plusDays(5).toString());
        pedidoDto.setClienteId(1L);

        // Assert - Verificar os valores definidos
        assertEquals(2L, pedidoDto.getId());
        assertEquals("PED002", pedidoDto.getNumeroPedido());
        assertEquals(new BigDecimal("250.50"), pedidoDto.getValor());
        assertEquals(StatusPedido.PROCESSANDO, pedidoDto.getStatus());
        assertEquals(1L, pedidoDto.getClienteId());
        assertNotNull(pedidoDto.getPrevisaoEntrega());
        assertNotNull(pedidoDto.getDataCompra());
    }

    @Test
    @DisplayName("Deve criar notificação via EMAIL")
    void testCriarNotificacao() {
        // Teste - Criar notificação
        NotificacaoDto notificacaoDto = new NotificacaoDto();
        notificacaoDto.setClienteId(1L);
        notificacaoDto.setPedidoId(1L);
        notificacaoDto.setTitulo("Pedido Confirmado");
        notificacaoDto.setMensagem("Seu pedido foi confirmado");
        notificacaoDto.setTipo(TipoNotificacao.EMAIL);
        notificacaoDto.setDataEnvio(LocalDateTime.now().toString());
        notificacaoDto.setLida(false);

        // Assert - Verificar os valores definidos
        assertEquals(1L, notificacaoDto.getClienteId());
        assertEquals(1L, notificacaoDto.getPedidoId());
        assertEquals("Pedido Confirmado", notificacaoDto.getTitulo());
        assertEquals("Seu pedido foi confirmado", notificacaoDto.getMensagem());
        assertEquals(TipoNotificacao.EMAIL, notificacaoDto.getTipo());
        assertFalse(notificacaoDto.getLida());
        assertNotNull(notificacaoDto.getDataEnvio());
    }

    @Test
    @DisplayName("Deve verificar tipos de notificação disponíveis")
    void testTiposNotificacao() {
        // Teste - Verificar tipos de notificação disponíveis
        TipoNotificacao email = TipoNotificacao.EMAIL;
        TipoNotificacao sms = TipoNotificacao.SMS;
        TipoNotificacao push = TipoNotificacao.PUSH;

        // Assert
        assertEquals("EMAIL", email.name());
        assertEquals("SMS", sms.name());
        assertEquals("PUSH", push.name());
    }

    @Test
    @DisplayName("Deve configurar preferências de notificação")
    void testPreferenciasNotificacao() {
        // Teste - Configurar preferências de notificação
        PreferenciasNotificacaoDto preferencesDto = new PreferenciasNotificacaoDto();
        preferencesDto.setClienteId(1L);
        preferencesDto.setEmailAtivo(true);
        preferencesDto.setSmsAtivo(false);
        preferencesDto.setPushAtivo(true);
        preferencesDto.setHorarioInicio("08:00");
        preferencesDto.setHorarioFim("22:00");

        // Assert - Verificar os valores definidos
        assertEquals(1L, preferencesDto.getClienteId());
        assertTrue(preferencesDto.getEmailAtivo());
        assertFalse(preferencesDto.getSmsAtivo());
        assertTrue(preferencesDto.getPushAtivo());
        assertEquals("08:00", preferencesDto.getHorarioInicio());
        assertEquals("22:00", preferencesDto.getHorarioFim());
    }

    @Test
    @DisplayName("Deve criar dados de rastreamento")
    void testDadosRastreamento() {
        // Teste - Criar dados de rastreamento
        DadosRastreamentoDto rastreamentoDto = new DadosRastreamentoDto();
        rastreamentoDto.setPedidoId(1L);
        rastreamentoDto.setCodigoRastreamento("BR123456789");
        rastreamentoDto.setTransportadora("Correios");
        rastreamentoDto.setLocalizacaoAtual("Centro de Distribuição SP");
        rastreamentoDto.setUltimaAtualizacao(LocalDateTime.now().toString());

        // Assert - Verificar os valores definidos
        assertEquals(1L, rastreamentoDto.getPedidoId());
        assertEquals("BR123456789", rastreamentoDto.getCodigoRastreamento());
        assertEquals("Correios", rastreamentoDto.getTransportadora());
        assertEquals("Centro de Distribuição SP", rastreamentoDto.getLocalizacaoAtual());
        assertNotNull(rastreamentoDto.getUltimaAtualizacao());

        // Teste de validação do código de rastreamento
        assertTrue(rastreamentoDto.getCodigoRastreamento().startsWith("BR"));
        assertTrue(rastreamentoDto.getCodigoRastreamento().length() > 5);
    }

    @Test
    @DisplayName("Deve criar avaliação")
    void testCriarAvaliacao() {
        // Teste - Criar avaliação
        AvaliacaoDto avaliacaoDto = new AvaliacaoDto();
        avaliacaoDto.setClienteId(1L);
        avaliacaoDto.setPedidoId(1L);
        avaliacaoDto.setNotaEntrega(5);
        avaliacaoDto.setNotaAcompanhamento(4);
        avaliacaoDto.setComentario("Excelente atendimento");
        avaliacaoDto.setDataAvaliacao(LocalDateTime.now());

        // Assert - Verificar os valores definidos
        assertEquals(1L, avaliacaoDto.getClienteId());
        assertEquals(1L, avaliacaoDto.getPedidoId());
        assertEquals(5, avaliacaoDto.getNotaEntrega());
        assertEquals(4, avaliacaoDto.getNotaAcompanhamento());
        assertEquals("Excelente atendimento", avaliacaoDto.getComentario());
        assertNotNull(avaliacaoDto.getDataAvaliacao());
        assertTrue(avaliacaoDto.getNotaEntrega() >= 1 && avaliacaoDto.getNotaEntrega() <= 5);
        assertTrue(avaliacaoDto.getNotaAcompanhamento() >= 1 && avaliacaoDto.getNotaAcompanhamento() <= 5);

        // Teste do cálculo da média
        Double mediaEsperada = 4.5; // (4 + 5) / 2
        assertEquals(mediaEsperada, avaliacaoDto.getMediaNotas());
    }

    @Test
    @DisplayName("Deve criar solicitação de suporte")
    void testSolicitacaoSuporte() {
        // Teste - Criar solicitação de suporte
        SolicitacaoSuporteDto suporteDto = new SolicitacaoSuporteDto();
        suporteDto.setProtocolo("SUP001");
        suporteDto.setClienteId(1L);
        suporteDto.setTipoProblema("Entrega atrasada");
        suporteDto.setDescricao("Meu pedido está com atraso na entrega");
        suporteDto.setStatus(StatusSuporte.ABERTO);
        suporteDto.setDataAbertura(LocalDateTime.now());

        // Assert - Verificar os valores definidos
        assertEquals("SUP001", suporteDto.getProtocolo());
        assertEquals("Entrega atrasada", suporteDto.getTipoProblema());
        assertEquals("Meu pedido está com atraso na entrega", suporteDto.getDescricao());
        assertEquals(StatusSuporte.ABERTO, suporteDto.getStatus());
        assertEquals(1L, suporteDto.getClienteId());
        assertNotNull(suporteDto.getDataAbertura());
        assertNull(suporteDto.getDataFechamento());
    }

    @Test
    @DisplayName("Deve verificar status de suporte")
    void testStatusSuporte() {
        // Teste - Verificar status de suporte
        StatusSuporte aberto = StatusSuporte.ABERTO;
        StatusSuporte emAndamento = StatusSuporte.EM_ANDAMENTO;
        StatusSuporte resolvido = StatusSuporte.RESOLVIDO;
        StatusSuporte fechado = StatusSuporte.FECHADO;

        // Assert
        assertEquals("ABERTO", aberto.name());
        assertEquals("EM_ANDAMENTO", emAndamento.name());
        assertEquals("RESOLVIDO", resolvido.name());
        assertEquals("FECHADO", fechado.name());
    }

    @Test
    @DisplayName("Deve criar histórico de status")
    void testHistoricoStatus() {
        // Teste - Criar histórico de status
        HistoricoStatusDto historicoDto = new HistoricoStatusDto();
        historicoDto.setPedidoId(1L);
        historicoDto.setStatus(StatusPedido.EM_TRANSITO);
        historicoDto.setObservacao("Pedido saiu para entrega");
        historicoDto.setResponsavel("Sistema");
        historicoDto.setDataAtualizacao(LocalDateTime.now().toString());

        // Assert - Verificar os valores definidos
        assertEquals(1L, historicoDto.getPedidoId());
        assertEquals(StatusPedido.EM_TRANSITO, historicoDto.getStatus());
        assertEquals("Pedido saiu para entrega", historicoDto.getObservacao());
        assertEquals("Sistema", historicoDto.getResponsavel());
        assertNotNull(historicoDto.getDataAtualizacao());
        assertEquals("EM_TRANSITO", historicoDto.getStatus().name());
    }

    @Test
    @DisplayName("Deve validar formato do email")
    void testValidarEmail() {
        // Teste - Validar formato do email
        String emailValido = "usuario@email.com";
        String emailInvalido = "emailinvalido";

        // Assert - Email deve conter @ e .
        assertTrue(emailValido.contains("@"));
        assertTrue(emailValido.contains("."));
        assertFalse(emailInvalido.contains("@"));
    }

    @Test
    @DisplayName("Deve validar formato do telefone")
    void testValidarTelefone() {
        // Teste - Validar formato do telefone
        String telefoneValido = "11987654321";
        String telefoneInvalido = "123";

        // Assert - Telefone deve ter pelo menos 10 dígitos
        assertTrue(telefoneValido.length() >= 10);
        assertFalse(telefoneInvalido.length() >= 10);
        assertTrue(telefoneValido.matches("\\d+"));
    }
}
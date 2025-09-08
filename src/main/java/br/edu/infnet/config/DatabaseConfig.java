package br.edu.infnet.config;

import br.edu.infnet.model.entity.AtendenteSuporte;
import br.edu.infnet.model.entity.Cliente;
import br.edu.infnet.repository.AtendenteSuporteRepository;
import br.edu.infnet.repository.ClienteRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

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
    @Profile({"dev", "default"})
    public CommandLineRunner loadInitialData(
            ClienteRepository clienteRepository,
            AtendenteSuporteRepository atendenteSuporteRepository) {

        return args -> {
            // Verificar se já existem dados para evitar duplicação
            if (clienteRepository.count() == 0) {
                criarClientesIniciais(clienteRepository);
            }

            if (atendenteSuporteRepository.count() == 0) {
                criarAtendentesIniciais(atendenteSuporteRepository);
            }

            System.out.println("=== DADOS INICIAIS CARREGADOS ===");
            System.out.println("Clientes cadastrados: " + clienteRepository.count());
            System.out.println("Atendentes cadastrados: " + atendenteSuporteRepository.count());
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
}
package br.edu.infnet.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * Configuração do Swagger/OpenAPI para documentação da API
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Sistema de Acompanhamento de Pedidos - API")
                        .description("API REST para o Sistema de Acompanhamento de Pedidos\n\n" +
                                "Este sistema permite que clientes acompanhem seus pedidos desde a confirmação até a entrega final. " +
                                "A API fornece endpoints para autenticação, gerenciamento de clientes, pedidos, notificações e suporte.\n\n" +
                                "**Principais funcionalidades:**\n" +
                                "- Autenticação de clientes e atendentes\n" +
                                "- Gerenciamento completo de clientes\n" +
                                "- Acompanhamento de pedidos em tempo real\n" +
                                "- Sistema de notificações personalizáveis\n" +
                                "- Rastreamento de entregas\n" +
                                "- Suporte ao cliente integrado\n" +
                                "- Avaliações de experiência de entrega\n\n" +
                                "**Casos de Uso Implementados:**\n" +
                                "- UC01: Visualizar Lista de Pedidos\n" +
                                "- UC02: Visualizar Detalhes do Pedido\n" +
                                "- UC03: Receber Notificações de Status\n" +
                                "- UC04: Solicitar Suporte\n" +
                                "- UC05: Avaliar Experiência de Entrega\n" +
                                "- UC06: Fazer Login no Sistema\n\n" +
                                "**Endpoints Disponíveis:**\n" +
                                "- **Auth**: Login de clientes e atendentes\n" +
                                "- **Clientes**: CRUD completo de clientes\n" +
                                "- **Pedidos**: Consulta e acompanhamento de pedidos\n" +
                                "- **Notificações**: Gerenciamento de notificações\n" +
                                "- **Preferências**: Configurações de notificações\n" +
                                "- **Rastreamento**: Dados de rastreamento de entregas\n" +
                                "- **Avaliações**: Sistema de feedback de entregas\n" +
                                "- **Suporte**: Abertura e acompanhamento de tickets\n\n" +
                                "**Observações:**\n" +
                                "- A API utiliza autenticação básica (Basic Auth)\n" +
                                "- Todas as respostas seguem o padrão ResponseDto\n" +
                                "- Validações são aplicadas em todos os endpoints\n" +
                                "- Soft delete é utilizado para exclusões")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thiago Teodoro Peres")
                                .email("thiago.peres@al.infnet.edu.br")
                                .url("https://github.com/thiagoperest"))
                        .license(new License()
                                .name("Instituto Infnet - Projeto Acadêmico")
                                .url("https://www.infnet.edu.br")))
                .servers(getServers())
                .tags(getTags())
                .components(new Components()
                        .addSecuritySchemes("basicAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("basic")
                                        .description("Autenticação básica usando email e senha")))
                .addSecurityItem(new SecurityRequirement().addList("basicAuth"));
    }

    private List<Server> getServers() {
        Server localServer = new Server()
                .url("http://localhost:8080/api")
                .description("Servidor Local de Desenvolvimento");

        Server h2ConsoleInfo = new Server()
                .url("http://localhost:8080/h2-console")
                .description("Console H2 Database (username: sa, password: vazio)");

        return Arrays.asList(localServer, h2ConsoleInfo);
    }

    private List<Tag> getTags() {
        return Arrays.asList(
                new Tag().name("Autenticação")
                        .description("Endpoints de autenticação para clientes e atendentes"),
                
                new Tag().name("Clientes")
                        .description("Gerenciamento completo de clientes - CRUD e consultas"),
                
                new Tag().name("Pedidos")
                        .description("Consulta e acompanhamento de pedidos em tempo real"),
                
                new Tag().name("Notificações")
                        .description("Sistema de notificações - consulta e marcação como lida"),
                
                new Tag().name("Preferências de Notificação")
                        .description("Configurações personalizadas de notificações por cliente"),
                
                new Tag().name("Dados de Rastreamento")
                        .description("Informações de rastreamento e localização de pedidos"),
                
                new Tag().name("Avaliações")
                        .description("Sistema de avaliação de experiência de entrega (Sprint 4)"),
                
                new Tag().name("Solicitações de Suporte")
                        .description("Abertura e acompanhamento de tickets de suporte (Sprint 4)")
        );
    }
}
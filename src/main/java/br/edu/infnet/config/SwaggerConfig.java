package br.edu.infnet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
                                "- Sistema de notificações\n" +
                                "- Suporte ao cliente integrado\n" +
                                "- Avaliações de experiência de entrega\n\n" +
                                "**Casos de Uso Implementados:**\n" +
                                "- UC01: Visualizar Lista de Pedidos\n" +
                                "- UC02: Visualizar Detalhes do Pedido\n" +
                                "- UC03: Receber Notificações de Status\n" +
                                "- UC04: Solicitar Suporte\n" +
                                "- UC05: Avaliar Experiência de Entrega\n" +
                                "- UC06: Fazer Login no Sistema")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Thiago Teodoro Peres")
                                .email("thiago.peres@al.infnet.edu.br")
                                .url("https://github.com/thiagoperest"))
                        .license(new License()
                                .name("Instituto Infnet - Projeto Acadêmico")
                                .url("https://www.infnet.edu.br")));
    }
}
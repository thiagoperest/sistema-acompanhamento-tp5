# Sistema de Acompanhamento de Pedidos - INFNET - PB TP5

![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)
![Maven](https://img.shields.io/badge/Maven-3.6+-green.svg)
![License](https://img.shields.io/badge/License-MIT-yellow.svg)
![Status](https://img.shields.io/badge/Status-Ativo-success.svg)

Sistema desenvolvido como projeto prático para gerenciamento e acompanhamento de pedidos, implementando uma API REST completa com funcionalidades de rastreamento, notificações, avaliações e suporte ao cliente, utilizando Spring Boot e banco de dados H2.

## Sobre o Projeto

Este projeto foi desenvolvido como parte do **TP5 - Projeto de Bloco: Desenvolvimento Back-end** do Instituto Infnet, implementando um sistema completo de acompanhamento de pedidos com API REST, documentação Swagger, persistência em banco de dados H2 e interface CLI para operações administrativas.

**Instituto Infnet** - Projeto de Bloco  
**Disciplina:** Desenvolvimento Back-end  
**Aluno:** Thiago Teodoro Peres

## Arquitetura

O sistema implementa uma arquitetura em camadas seguindo os padrões MVC e princípios RESTful:

```
Presentation Layer (Controllers REST + CLI)
        ↓
Service Layer (Business Logic)
        ↓
Repository Layer (Spring Data JPA)
        ↓
Model Layer (Entities + DTOs)
        ↓
Data Layer (H2 Database)
```

O sistema foi modelado seguindo princípios de orientação a objetos e padrões de design, com separação clara de responsabilidades entre as camadas de apresentação, serviço, repositório e modelo.

### Decisões Arquiteturais

As decisões arquiteturais estão documentadas no formato [ADR](docs/adrs) (Architecture Decision Records).

## Funcionalidades Implementadas

- **Sistema de Autenticação** - Login e controle de acesso via API REST
- **Gestão de Clientes** - Gerenciamento completo com validações e relacionamentos
- **Gestão de Pedidos** - Criação, acompanhamento e atualização de status
- **Sistema de Rastreamento** - Dados de localização e movimentação em tempo real
- **Sistema de Notificações** - Notificações personalizadas com preferências por cliente
- **Sistema de Avaliações** - Rating e feedback dos clientes sobre pedidos
- **Sistema de Suporte** - Abertura e acompanhamento de tickets de suporte
- **Interface CLI** - Comandos administrativos organizados em módulos
- **Documentação Swagger** - API completamente documentada com OpenAPI 3.0
- **Persistência H2** - Banco de dados em memória com console web

## Como Executar

### Pré-requisitos
- Java 17 ou superior
- Maven 3.8 ou superior

### Execução

1. **Clone e compile:**
   ```bash
   git clone https://github.com/thiagoperest/sistema-acompanhamento-tp5.git
   cd sistema-acompanhamento-tp5
   mvn clean install
   ```

2. **Execute a aplicação:**
   ```bash
   mvn spring-boot:run
   ```

3. **Acesse os serviços:**
   - API REST: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/api/swagger-ui.html
   - H2 Console: http://localhost:8080/api/h2-console

### Credenciais H2 Database
```
JDBC URL: jdbc:h2:mem:testdb
Username: sa
Password: (deixar em branco - sem senha)
```

## Estrutura do Projeto

```
src/main/java/br/edu/infnet/
├── cli/
│   └── modules/
├── config/
├── controller/
├── model/
│   ├── dto/
│   ├── entity/
│   └── enums/
├── repository/
└── service/

src/main/resources/

docs/
├── adrs/
└── guia-do-desenvolvedor.md
```

Para mais detalhes sobre a estrutura de pastas MVC, consulte o [Guia do Desenvolvedor](docs/guia-do-desenvolvedor.md).

## Banco de Dados H2

O projeto utiliza o H2 Database, um banco de dados em memória que facilita o desenvolvimento e testes. O banco é configurado para ser recriado a cada execução da aplicação (modo create-drop).

Para acessar o console do H2:
1. Navegue para http://localhost:8080/api/h2-console
2. Use as credenciais configuradas no application.yml

## Documentação Swagger

A documentação completa da API está disponível através do Swagger UI. Após iniciar a aplicação, acesse:

- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **OpenAPI JSON**: http://localhost:8080/api/api-docs

O Swagger fornece uma interface interativa onde é possível visualizar todos os endpoints, seus parâmetros, respostas esperadas e até mesmo testar as requisições diretamente.

## Interface CLI

Além da API REST, o sistema oferece uma interface de linha de comando (CLI) para operações administrativas. A CLI está organizada em módulos específicos para cada funcionalidade do sistema, facilitando a manutenção e operações diretas no servidor.

## Tecnologias Utilizadas

- **Java 17** - Linguagem de programação principal
- **Spring Boot 3.5.5** - Framework web
- **Spring Data JPA** - Persistência de dados
- **H2 Database** - Banco de dados em memória
- **Swagger/OpenAPI 3.0** - Documentação da API
- **Maven** - Gerenciamento de dependências
- **Spring Validation** - Validação de dados
- **Jackson** - Serialização JSON

## Contato

**Thiago Teodoro Peres**  
Email: thiago.peres@al.infnet.edu.br  
Instituto Infnet - Desenvolvimento Back-end

---

**Projeto desenvolvido para o Instituto Infnet - TP5**
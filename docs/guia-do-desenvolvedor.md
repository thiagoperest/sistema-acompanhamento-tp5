# Guia do Desenvolvedor

## Estrutura de Pastas MVC

O projeto segue o padrão MVC com a seguinte organização:

```
src/main/java/br/edu/infnet/
├── cli/                    # Interface de linha de comando
│   └── modules/           # Módulos CLI por funcionalidade
├── config/                # Configurações do Spring Boot
├── controller/            # Controllers REST
├── model/                 # Modelos de dados
│   ├── dto/              # Objetos de transferência
│   ├── entity/           # Entidades do banco
│   └── enums/            # Enumerações
├── repository/            # Acesso aos dados
└── service/              # Lógica de negócio
```

## Banco de Dados H2

O projeto utiliza H2, um banco de dados em memória.

### Acesso ao Console
1. Iniciar a aplicação
2. Acessar: `http://localhost:8080/api/h2-console`
3. Configurar:
   - JDBC URL: `jdbc:h2:mem:testdb`
   - Username: `sa`
   - Password: (vazio)

### Características
- Banco em memória (dados perdidos ao reiniciar)
- Schema criado automaticamente
- Console web para consultas SQL

## Swagger

A documentação da API está disponível em:
- **Swagger UI**: `http://localhost:8080/api/swagger-ui.html`
- **API Docs**: `http://localhost:8080/api/api-docs`

O Swagger permite:
- Visualizar todos os endpoints
- Testar requisições
- Ver schemas de request/response

## Como Executar

### Requisitos
- Java 17+
- Maven 3.8+

### Comandos
```bash
# Instalar dependências
mvn clean install

# Executar aplicação
mvn spring-boot:run
```

### URLs Importantes
- API: `http://localhost:8080/api`
- Swagger: `http://localhost:8080/api/swagger-ui.html`
- H2 Console: `http://localhost:8080/api/h2-console`

## Arquitetura

O sistema usa arquitetura em camadas:

1. **Controller**: Recebe requisições HTTP
2. **Service**: Processa lógica de negócio
3. **Repository**: Acessa o banco de dados
4. **Model**: Define estruturas de dados

### Fluxo de uma Requisição
```
Cliente → Controller → Service → Repository → Banco H2
```

## Configurações

As configurações estão em `src/main/resources/application.yml`:

- Porta: 8080
- Context Path: /api
- Banco: H2 em memória
- Logs: Nível DEBUG para a aplicação

---

**Sistema de Acompanhamento de Pedidos - TP5**
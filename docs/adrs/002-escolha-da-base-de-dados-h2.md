# 002: Escolha da Base de Dados H2

## Status

- Aceito

## Contexto

Para o sistema de acompanhamento do TP5, preciso definir qual base de dados utilizar durante o desenvolvimento e execução da aplicação. As principais opções consideradas incluem bases de dados em memória como H2, bases relacionais tradicionais como PostgreSQL ou MySQL, ou ainda bases NoSQL como MongoDB.

O projeto tem características específicas que influenciam esta decisão:
- Foco no desenvolvimento local e demonstração do sistema
- Necessidade de simular um ecossistema real de comunicação com base de dados
- Simplicidade de configuração
- Facilidade para executar testes automatizados
- Demonstração das funcionalidades de persistência sem complexidade de infraestrutura
- Possibilidade de executar a aplicação em diferentes ambientes sem dependências externas

O sistema precisa demonstrar adequadamente as operações CRUD, relacionamentos entre entidades, consultas complexas e transações, simulando um ambiente real de produção, porém com a praticidade necessária para desenvolvimento e testes locais.

## Decisão

Vou adotar H2 Database como base de dados principal para o sistema de acompanhamento. Esta decisão baseia-se na necessidade de ter uma solução otimizada para desenvolvimento local que simule adequadamente a comunicação com uma base de dados real, sem as complexidades de configuração e manutenção de uma base externa.

A implementação utilizará H2 em modo file-based para persistir os dados entre execuções, aproveitando:
- Configuração automática através do Spring Boot
- Console web integrado para visualização e manipulação dos dados
- Sintaxe SQL compatível com bases relacionais tradicionais
- Suporte completo ao JPA e Hibernate
- Facilidade para popular dados iniciais através de scripts SQL
- Capacidade de backup e restore dos dados de desenvolvimento

## Consequências

**Consequências Positivas:**
- Configuração extremamente simplificada, sem necessidade de instalação externa
- Execução imediata da aplicação em qualquer ambiente com JVM
- Console web integrado facilita debugging e verificação dos dados
- Performance excelente para desenvolvimento e testes
- Não requer conhecimento específico de administração de base de dados
- Scripts de inicialização permitem cenários de teste consistentes
- Facilita demonstrações da aplicação sem dependências de infraestrutura
- Transição futura para outras bases de dados é transparente via JPA

**Consequências Negativas:**
- Não é adequada para ambientes de produção com alta concorrência
- Limitações de performance em cenários com grande volume de dados
- Recursos avançados específicos de bases enterprise não estão disponíveis
- Menor robustez comparada a soluções de produção como PostgreSQL
- Características de durabilidade e recuperação limitadas

**Consequências Neutras:**
- Dados são armazenados em arquivo local, facilitando backup manual
- Sintaxe SQL pode ter pequenas diferenças comparada a outras bases
- Possibilidade de configurar tanto em modo in-memory quanto file-based
- Necessidade de documentar a estratégia de migração para produção no futuro
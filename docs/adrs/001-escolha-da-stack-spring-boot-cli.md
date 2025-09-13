# 001: Escolha da Stack Spring Boot + Terminal CLI

## Status

- Aceito

## Contexto

Para o desenvolvimento do sistema de acompanhamento do TP5, preciso decidir entre duas principais opções: Spring Boot com interface de linha de comando (CLI) ou C# com Razor Pages para interface web. O projeto requer uma aplicação que seja capaz de gerenciar eficientemente os dados do sistema, com foco na funcionalidade e performance, sem necessariamente priorizar uma interface gráfica complexa.

As principais considerações técnicas incluem a necessidade de:
- Desenvolvimento ágil e produtivo
- Manutenibilidade do código
- Robustez e confiabilidade da aplicação
- Capacidade de integração com diferentes sistemas

Tenho experiência significativamente maior com o ecossistema Spring Boot, incluindo Spring Data, Spring Security, e desenvolvimento de aplicações CLI em Java. Por outro lado, minha experiência com C# e Razor Pages é mais limitada, o que poderia impactar na velocidade de desenvolvimento e na qualidade final do código.

## Decisão

Vou adotar Spring Boot como framework principal combinado com uma interface de linha de comando (CLI) para o sistema de acompanhamento. Esta decisão baseia-se principalmente na minha maior vivência e experiência com o ecossistema Spring, que me permitirá desenvolver uma solução mais robusta e eficiente em menor tempo.

A implementação utilizará Spring Boot como base, aproveitando suas funcionalidades de:
- Auto-configuração para reduzir código boilerplate
- Spring Data JPA para persistência de dados
- Spring Shell para implementação da interface CLI
- Injeção de dependência nativa do Spring

## Consequências

**Consequências Positivas:**
- Desenvolvimento mais rápido devido à minha familiaridade com Spring Boot
- Menor curva de aprendizado, permitindo foco na lógica de negócio
- Aproveitamento de bibliotecas do ecossistema Spring
- Interface CLI oferece simplicidade e eficiência para usuários técnicos
- Menor consumo de recursos comparado a uma aplicação web completa

**Consequências Negativas:**
- Interface CLI pode ser menos amigável para usuários não técnicos
- Limitações na apresentação visual de dados complexos
- Menor apelo visual comparado a uma interface web moderna
- Possível necessidade futura de migração para interface web se os requisitos mudarem

**Consequências Neutras:**
- Java requer JVM instalada no ambiente de execução
- Necessidade de documentação clara dos comandos CLI para usuários finais
- Possibilidade de extensão futura para outras interfaces (web, mobile) mantendo a lógica de negócio
# 003: Padrão de Estruturação de Pastas MVC

## Status

- Aceito

## Contexto

Para o desenvolvimento do sistema de acompanhamento do TP5, preciso definir uma estrutura organizacional clara e consistente para o código fonte da aplicação. A organização adequada das pastas e pacotes é fundamental para manter a manutenibilidade, legibilidade e escalabilidade do projeto.

Considerando que estou utilizando Spring Boot com interface CLI, preciso de uma organização que facilite:
- Separação clara de responsabilidades entre camadas
- Facilidade para localizar e modificar componentes específicos
- Testabilidade individual de cada camada
- Manutenção e evolução do código
- Aderência às boas práticas do ecossistema Java/Spring

A estrutura deve seguir princípios do padrão MVC adaptado para o contexto de uma aplicação Spring Boot com CLI.

## Decisão

Vou adotar o seguinte padrão de estruturação de pastas baseado no padrão MVC para organizar o código da aplicação:

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
```

Esta estrutura separa claramente as responsabilidades:
- **cli/**: Interface de linha de comando e seus módulos
- **config/**: Configurações da aplicação Spring
- **controller/**: Controladores REST (camada de apresentação)
- **model/**: Entidades, DTOs e enums (camada de modelo)
- **repository/**: Acesso a dados (camada de persistência)
- **service/**: Lógica de negócio (camada de serviço)

## Consequências

**Consequências Positivas:**
- Separação clara de responsabilidades seguindo o padrão MVC
- Facilidade para localizar código específico baseado na responsabilidade
- Melhor organização para desenvolvimento em equipe
- Estrutura familiar para desenvolvedores Java/Spring
- Facilita testes unitários e de integração por camada
- Possibilita futuras extensões mantendo a organização
- Aderência às convenções do Spring Framework

**Consequências Negativas:**
- Necessidade de navegar entre diferentes pacotes para implementar uma funcionalidade completa
- Pode parecer complexo para funcionalidades muito simples
- Requer disciplina para manter a organização consistente

**Consequências Neutras:**
- Estrutura padrão reconhecida pela comunidade Java/Spring
- Permite evolução da aplicação sem grandes refatorações na estrutura
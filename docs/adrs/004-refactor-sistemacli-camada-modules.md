# 004: Refactor da SistemaCli e Criação da Camada Modules

## Status

- Aceito

## Contexto

Durante o desenvolvimento do sistema de acompanhamento do TP5, identifiquei que a classe SistemaCli estava se tornando cada vez mais complexa e difícil de manter. A classe concentrava toda a lógica de interação com o usuário através da interface de linha de comando, resultando em um arquivo extenso com múltiplas responsabilidades.

Os principais problemas identificados na estrutura original incluem:
- Classe SistemaCli com muitas responsabilidades (violação do Single Responsibility Principle)
- Código extenso e difícil de navegar
- Dificuldade para manutenção e adição de novas funcionalidades
- Baixa testabilidade devido ao acoplamento de diferentes funcionalidades
- Repetição de código em diferentes seções do CLI
- Dificuldade para outros desenvolvedores entenderem e modificarem o código

A aplicação possui diferentes módulos funcionais como autenticação, clientes, pedidos, avaliações, notificações, suporte e preferências, cada um com suas próprias regras de apresentação e interação específicas na interface CLI.

## Decisão

Vou realizar um refactor significativo na classe SistemaCli, criando uma nova camada chamada **modules** dentro da pasta cli. Esta refatoração abstrairá a lógica em camadas separadas, com cada módulo sendo responsável por uma área funcional específica da aplicação.

A nova estrutura será organizada da seguinte forma:

```
src/main/java/br/edu/infnet/cli/
├── SistemaCli.java (refatorada como orquestrador principal)
└── modules/
    ├── AuthCli.java
    ├── AvaliacaoCli.java
    ├── BaseCli.java
    ├── ClienteCli.java
    ├── NotificacaoCli.java
    ├── PedidoCli.java
    ├── PreferenciasCli.java
    └── SuporteCli.java
```

Cada módulo será responsável por:
- Apresentar menus específicos de sua área funcional
- Gerenciar interações do usuário para suas operações
- Validar entradas específicas do seu domínio
- Chamar os serviços apropriados
- Formatar e apresentar resultados de forma consistente

A classe SistemaCli refatorada atuará como um orquestrador principal, delegando as responsabilidades específicas para cada módulo.

## Consequências

**Consequências Positivas:**
- Separação clara de responsabilidades por área funcional
- Código mais organizado e fácil de navegar
- Melhor manutenibilidade com módulos independentes
- Facilita adição de novas funcionalidades sem impactar outros módulos
- Maior testabilidade com possibilidade de testar módulos isoladamente
- Melhoria na legibilidade do código
- Facilita trabalho em equipe com desenvolvedores trabalhando em módulos específicos
- Reutilização de código comum através da classe BaseCli
- Melhor experiência do usuário com interfaces mais consistentes

**Consequências Negativas:**
- Aumento inicial no número de arquivos da aplicação
- Necessidade de navegação entre múltiplos arquivos para entender o fluxo completo
- Possível overhead de comunicação entre módulos

**Consequências Neutras:**
- Necessidade de documentação da nova estrutura modular
- Possível necessidade de refatoração adicional conforme novos padrões emergem
- Manutenção da consistência de interface entre os diferentes módulos
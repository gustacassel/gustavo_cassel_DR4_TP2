# E-commerce legado — atividade de DDD

Aplicação monolítica propositalmente acoplada para uma atividade de refatoração.

## Tecnologias

- Java 25
- Spring Boot 4.1.0
- Maven
- Spring Web
- Spring Data JPA
- Bean Validation
- H2 em memória

## Requisitos

- JDK 25
- Maven 3.6.3 ou superior

## Executar

```bash
mvn spring-boot:run
```

A API ficará disponível em:

```text
http://localhost:8080
```

## Console H2

```text
http://localhost:8080/h2-console
```

Dados da conexão:

```text
JDBC URL: jdbc:h2:mem:ecommerce
User Name: sa
Password:
```

## Fluxo principal

Use o arquivo `requests.http` ou execute:

```bash
curl -X POST http://localhost:8080/pedidos   -H "Content-Type: application/json"   -d '{
    "usuarioId": 1,
    "itens": [
      {
        "produtoId": 1,
        "quantidade": 2
      }
    ],
    "formaPagamento": "CARTAO",
    "numeroCartao": "4111111111111111"
  }'
```

## Regras simuladas de pagamento

- Valor menor ou igual a zero: recusado.
- Valor acima de R$ 10.000,00: recusado por limite.
- Cartão terminado em `0000`: cartão bloqueado.
- Cartão terminado em `1111`: aprovado.
- Outros cartões: aprovados quando o valor for válido.

## Aviso pedagógico

A arquitetura original foi intencionalmente construída com problemas, como base para a atividade de refatoração (TP1):

- organização horizontal por camada técnica;
- entidades JPA usadas diretamente nos controllers;
- relacionamentos entre entidades de contextos diferentes;
- `PedidoService` com múltiplas responsabilidades;
- acesso direto a vários repositórios;
- pagamento acoplado a pedido e usuário;
- dependência de um processador concreto;
- regras distribuídas em services;
- uma única transação envolvendo pedido, estoque e pagamento;
- ausência de Aggregate Root, Value Objects, portas e adaptadores.

Os contextos de Usuário, Produto e Estoque permanecem como no projeto base (fora do escopo do TP1). O contexto de Pagamento foi extraído conforme descrito abaixo.

## Refatoração aplicada (TP1)

O contexto de Pagamento foi extraído para `br.edu.infnet.ecommerce.pagamento`, seguindo DDD tático (Aggregate Root, Value Objects, portas e adapters), usando Branch by Abstraction + Strangler Fig como estratégia de migração — o `PedidoService` deixou de acessar `Pagamento`/`PagamentoRepository` diretamente e passou a depender só do serviço de aplicação do novo contexto.

```
br.edu.infnet.ecommerce.pagamento
├── domain             // Pagamento (Aggregate Root), Dinheiro/NumeroCartao (VOs),
│                       // StatusPagamento/FormaPagamento, PagamentoRepository (porta)
├── application        // PagamentoApplicationService, PedidoIntegracao e
│                       // ProcessadorCartaoPort (portas)
└── infrastructure      // PagamentoJpaEntity (sem relacionamento JPA com outros
                         // contextos), adapters de persistência, integração
                         // com Pedido e processador de cartão
```

O contexto de Pagamento nunca acessa `UsuarioRepository`, `ProdutoRepository`, `EstoqueRepository` ou `PedidoRepository` diretamente — recebe apenas `pedidoId`/`usuarioId`. O único ponto de contato com o monólito legado é o adapter `PedidoIntegracaoAdapter`; numa extração futura para um serviço separado, só ele mudaria.

As regras de pagamento simuladas (seção acima) continuam as mesmas, agora encapsuladas no Aggregate Root `Pagamento`.

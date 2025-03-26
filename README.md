# Order App - Sistema de Gerenciamento de Pedidos

## Visão Geral
O **Order App** é um serviço desenvolvido em **Java 11** com **Spring Boot**, responsável por gerenciar pedidos. Ele implementa um fluxo de processamento assíncrono utilizando **RabbitMQ**, armazenando os dados no **PostgreSQL** e fornecendo APIs REST para interação.

---

## Fluxo de Processamento
1. **Recebimento do Pedido** → O Produto A envia um pedido via API (`POST /orders`).
2. **Armazenamento no Banco** → O pedido é salvo no PostgreSQL com status `PENDENTE`.
3. **Envio para Fila** → O pedido é publicado na fila `orders.queue` (RabbitMQ).
4. **Processamento Assíncrono** → Um consumidor processa o pedido, calcula o valor total e atualiza seu status para `PROCESSADO`.
5. **Envio do Pedido Processado** → O pedido é publicado na fila `processed-orders.queue` para o Produto B.
6. **Consulta via API** → O Produto A e o Produto B podem buscar pedidos (`GET /orders`) e (`GET /orders/{id}`).

---

## Infraestrutura
A infraestrutura local é gerenciada com **Docker Compose**, incluindo o banco **PostgreSQL** e a fila **RabbitMQ**.

---

## Como Rodar Localmente
### Clonar o repositório
```bash
git clone https://github.com/victormor12/order-app.git
cd order-app
```

### Subir os containers Docker
```bash
docker-compose up -d
```

### Executar a aplicação
```bash
mvn spring-boot:run
```

---

## APIs Disponíveis
A aplicação expõe APIs REST para interação com pedidos.

### Criar Pedido
**Endpoint:** `POST /orders`
```json
{
  "idExterno": "12345",
  "items": [
    { "nome": "Cerveja Brahma", "quantidade": 2, "preco": 5.50 }
  ]
}
```

### Buscar Pedido por ID
**Endpoint:** `GET /orders/{id}`

### Listar Todos os Pedidos
**Endpoint:** `GET /orders`

---

## Documentação da API
A documentação da API está disponível em:
```
http://localhost:8080/swagger-ui.html
```

---

## RabbitMQ
Para visualizar as mensagens disparadas para as filas:
```
http://localhost:15672
```

Login: ```admin```

Senha: ```admin```

---

## Tecnologias Utilizadas
- **Java 11**
- **Spring Boot**
- **PostgreSQL**
- **RabbitMQ**
- **Flyway**
- **Spring Data JPA**
- **Spring AMQP**
- **JUnit & Mockito**
- **Test Container**
- **Docker Compose**
- **Swagger OpenAPI**

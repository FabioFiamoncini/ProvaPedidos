Este projeto implementa um sistema básico de cadastro de produtos/serviços, pedidos e itens de pedido, utilizando Java, Spring Boot, JPA/Hibernate e PostgreSQL.
Através do uso de requests de API REST em JSON, as operações permitem criar itens e incluí-los em pedidos, podendo aplicar desconto nos pedidos apenas sobre itens do tipo PRODUTO.

Requisitos
Para executar o projeto são necessários:

  Docker ou Docker Desktop
  Java 17 ou superior
  Maven (ou o wrapper mvnw incluso no projeto)


Subindo o banco de dados
  Na raiz do projeto há um arquivo docker-compose.yml, devendo executar o comando "docker compose up -d" para subir o banco de dados em PostgreSQL.

Credenciais fixadas:
  Database: postgres
  Usuário: postgres
  Senha: masterkey
  Porta: 5432


Executando a aplicação
Com o banco ativo, executar na pasta raíz:
  ./mvnw spring-boot:run

A API ficará disponível em:
  http://localhost:8080


Endpoints da API (REST)
1) Catálogo (Produto/Serviço)
  Criar item:
  POST /catalogo
  Content-Type: application/json

{
  "nome": "Notebook",
  "tipo": "PRODUTO",
  "precoUnitario": 3500.00
}

Listar itens:
  GET /catalogo

Obter por ID:
  GET /catalogo/{id}

Atualizar item:
  PUT /catalogo/{id}
  Content-Type: application/json

{
  "nome": "Notebook Gamer",
  "tipo": "PRODUTO",
  "precoUnitario": 4200.00
}

Excluir item:
  DELETE /catalogo/{id}


2) Pedido
Criar pedido:
  POST /pedidos
  Content-Type: application/json

{
  "percentualDesconto": 10
}

Listar pedidos:
  GET /pedidos

Obter pedido (já inclui totais dos produtos/serviços calculados com desconto aplicado aos produtos, se existente):
  GET /pedidos/{id}

Atualizar desconto:
  PUT /pedidos/{id}
  Content-Type: application/json

{
  "percentualDesconto": 12.5
}

Excluir pedido:
  DELETE /pedidos/{id}


3) Itens de Pedido
Adicionar item:
  POST /pedidos/{pedidoId}/itens
  Content-Type: application/json

{
  "itemCatalogoId": "{idDoItemDoCatalogo}",
  "quantidade": 2
}

Remover item:
  DELETE /pedidos/{pedidoId}/itens/{itemId}

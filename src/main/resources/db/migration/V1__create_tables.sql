CREATE TABLE orders (
    id UUID PRIMARY KEY,
    id_externo VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    valor_total DECIMAL(10,2),
    data_criacao TIMESTAMP DEFAULT NOW(),
    data_processamento TIMESTAMP
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY,
    order_id UUID REFERENCES orders(id) ON DELETE CASCADE,
    nome VARCHAR(255) NOT NULL,
    quantidade INT NOT NULL,
    preco DECIMAL(10,2) NOT NULL
);

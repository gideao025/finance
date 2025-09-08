CREATE TABLE contas (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    nome VARCHAR(100) NOT NULL,
    saldo_inicial DECIMAL(15,2) DEFAULT 0.00,
    saldo_atual DECIMAL(15,2) DEFAULT 0.00,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_contas_usuario ON contas(usuario_id);
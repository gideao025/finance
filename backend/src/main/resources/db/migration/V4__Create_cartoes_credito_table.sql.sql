CREATE TABLE cartoes_credito (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    nome VARCHAR(100) NOT NULL,
    limite_total DECIMAL(15,2) DEFAULT 0.00,
    limite_disponivel DECIMAL(15,2) DEFAULT 0.00,
    vencimento DATE,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cartoes_usuario ON cartoes_credito(usuario_id);
CREATE TABLE vales (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    tipo VARCHAR(50) NOT NULL,
    saldo_inicial DECIMAL(15,2) DEFAULT 0.00,
    saldo_atual DECIMAL(15,2) DEFAULT 0.00,
    validade TIMESTAMP,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_vales_usuario ON vales(usuario_id);
CREATE INDEX idx_vales_tipo ON vales(tipo);
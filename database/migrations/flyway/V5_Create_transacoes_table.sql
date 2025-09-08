CREATE TABLE transacoes (
    id BIGSERIAL PRIMARY KEY,
    conta_id BIGINT REFERENCES contas(id),
    cartao_credito_id BIGINT REFERENCES cartoes_credito(id),
    categoria_id BIGINT NOT NULL REFERENCES categorias(id),
    valor DECIMAL(15,2) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    data TIMESTAMP NOT NULL,
    recorrencia VARCHAR(50),
    descricao VARCHAR(500),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_origem CHECK (
        (conta_id IS NOT NULL AND cartao_credito_id IS NULL) OR
        (conta_id IS NULL AND cartao_credito_id IS NOT NULL)
    )
);

CREATE INDEX idx_transacoes_conta ON transacoes(conta_id);
CREATE INDEX idx_transacoes_cartao ON transacoes(cartao_credito_id);
CREATE INDEX idx_transacoes_categoria ON transacoes(categoria_id);
CREATE INDEX idx_transacoes_data ON transacoes(data);
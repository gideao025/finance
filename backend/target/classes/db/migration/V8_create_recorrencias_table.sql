-- Criação da tabela de transações recorrentes
CREATE TABLE transacoes_recorrentes (
    id BIGSERIAL PRIMARY KEY,
    descricao VARCHAR(255) NOT NULL,
    valor DECIMAL(15,2) NOT NULL CHECK (valor > 0),
    tipo VARCHAR(20) NOT NULL CHECK (tipo IN ('RECEITA', 'DESPESA')),
    periodicidade VARCHAR(20) NOT NULL CHECK (periodicidade IN ('SEMANAL', 'MENSAL', 'TRIMESTRAL', 'ANUAL')),
    data_inicio DATE NOT NULL,
    data_fim DATE,
    proxima_execucao DATE NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,

    -- Foreign Keys
    conta_id BIGINT REFERENCES contas(id),
    cartao_credito_id BIGINT REFERENCES cartoes_credito(id),
    categoria_id BIGINT NOT NULL REFERENCES categorias(id),
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),

    -- Timestamps
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Constraints
    CONSTRAINT check_conta_or_cartao CHECK (
        (conta_id IS NOT NULL AND cartao_credito_id IS NULL) OR
        (conta_id IS NULL AND cartao_credito_id IS NOT NULL)
    ),
    CONSTRAINT check_data_fim CHECK (data_fim IS NULL OR data_fim >= data_inicio)
);

-- Índices para performance
CREATE INDEX idx_recorrencias_proxima_execucao ON transacoes_recorrentes(proxima_execucao);
CREATE INDEX idx_recorrencias_usuario_ativo ON transacoes_recorrentes(usuario_id, ativo);
CREATE INDEX idx_recorrencias_ativo_proxima ON transacoes_recorrentes(ativo, proxima_execucao);

-- Adicionar coluna recorrente na tabela transacoes
ALTER TABLE transacoes ADD COLUMN recorrente BOOLEAN DEFAULT FALSE;
ALTER TABLE transacoes ADD COLUMN recorrencia_id BIGINT REFERENCES transacoes_recorrentes(id);

CREATE INDEX idx_transacoes_recorrente ON transacoes(recorrente);

-- Trigger para atualizar atualizado_em
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.atualizado_em = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_recorrencias_updated_at
    BEFORE UPDATE ON transacoes_recorrentes
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

-- Dados de exemplo
INSERT INTO transacoes_recorrentes (
    descricao, valor, tipo, periodicidade,
    data_inicio, proxima_execucao, conta_id, categoria_id, usuario_id
) VALUES
('Salário', 5000.00, 'RECEITA', 'MENSAL', '2024-01-01', '2024-02-01', 1, 1, 1),
('Aluguel', 1200.00, 'DESPESA', 'MENSAL', '2024-01-05', '2024-02-05', 1, 2, 1),
('Internet', 89.90, 'DESPESA', 'MENSAL', '2024-01-10', '2024-02-10', 1, 3, 1);
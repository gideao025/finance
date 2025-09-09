CREATE TABLE categorias (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo VARCHAR(50) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO categorias (nome, tipo) VALUES
('Salário', 'RECEITA'),
('Freelance', 'RECEITA'),
('Investimentos', 'RECEITA'),
('Alimentação', 'DESPESA'),
('Transporte', 'DESPESA'),
('Moradia', 'DESPESA'),
('Lazer', 'DESPESA'),
('Saúde', 'DESPESA');
CREATE TABLE usuarios (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    atualizado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usuario_roles (
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    role VARCHAR(50) NOT NULL,
    PRIMARY KEY (usuario_id, role)
);

CREATE INDEX idx_usuarios_email ON usuarios(email);
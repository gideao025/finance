CREATE TABLE notificacoes (
    id BIGSERIAL PRIMARY KEY,
    usuario_id BIGINT NOT NULL REFERENCES usuarios(id),
    mensagem VARCHAR(1000) NOT NULL,
    lida BOOLEAN DEFAULT FALSE,
    tipo VARCHAR(50),
    criado_em TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    data_leitura TIMESTAMP
);

CREATE INDEX idx_notificacoes_usuario ON notificacoes(usuario_id);
CREATE INDEX idx_notificacoes_lida ON notificacoes(lida);
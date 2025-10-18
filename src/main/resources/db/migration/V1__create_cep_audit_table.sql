CREATE TABLE IF NOT EXISTS cep_audit (
    id BIGSERIAL PRIMARY KEY,
    cep VARCHAR(20) NOT NULL,
    request_url TEXT NOT NULL,
    status VARCHAR(50) NOT NULL,
    message TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);

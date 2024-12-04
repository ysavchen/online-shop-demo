--liquibase formatted sql

--changeset ysavchen:004.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS processed_requests
(
    idempotency_key uuid        PRIMARY KEY,
    resource_id     uuid        NOT NULL,
    resource_type   varchar(15) NOT NULL,
    created_at      timestamptz NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE idempotency_keys IS 'Таблица для хранения обработанных запросов';
COMMENT ON COLUMN idempotency_keys.idempotency_key IS 'Ключ идемпотентности запроса';
COMMENT ON COLUMN idempotency_keys.resource_id IS 'ID ресурса';
COMMENT ON COLUMN idempotency_keys.resource_type IS 'Тип ресурса';
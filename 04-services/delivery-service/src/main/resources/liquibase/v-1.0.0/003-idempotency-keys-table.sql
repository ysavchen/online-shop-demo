--liquibase formatted sql

--changeset ysavchen:003.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS idempotency_keys
(
    idempotency_key uuid        NOT NULL,
    resource_id     uuid        NOT NULL,
    resource        varchar(15) NOT NULL,
    created_at      timestamptz NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE idempotency_keys IS 'Таблица для хранения ключей идемпотентности';
COMMENT ON COLUMN idempotency_keys.idempotency_key IS 'Ключ идемпотентности для сообщения';
COMMENT ON COLUMN idempotency_keys.resource_id IS 'ID ресурса';
COMMENT ON COLUMN idempotency_keys.resource IS 'Название ресурса (delivery)';
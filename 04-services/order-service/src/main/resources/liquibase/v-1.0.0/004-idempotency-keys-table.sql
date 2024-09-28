--liquibase formatted sql

--changeset ysavchen:004.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS idempotency_keys
(
    idempotency_key uuid        NOT NULL,
    order_id        uuid,
    created_at      timestamptz NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE idempotency_keys IS 'Таблица для хранения ключей идемпотентности';
COMMENT ON COLUMN idempotency_keys.idempotency_key IS 'Ключ идемпотентности для POST запроса';
COMMENT ON COLUMN idempotency_keys.order_id IS 'ID заказа';
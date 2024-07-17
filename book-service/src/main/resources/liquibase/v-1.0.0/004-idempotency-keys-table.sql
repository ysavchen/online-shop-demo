--liquibase formatted sql

--changeset ysavchen:004.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS idempotency_keys
(
    idempotency_key uuid        NOT NULL,
    book_id         uuid,
    review_id       uuid,
    created_at      timestamptz NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE idempotency_keys IS 'Таблица для хранения ключей идемпотентности';
COMMENT ON COLUMN idempotency_keys.idempotency_key IS 'Ключ идемпонетности для POST запроса';
COMMENT ON COLUMN idempotency_keys.book_id IS 'ID книги';
COMMENT ON COLUMN idempotency_keys.review_id IS 'ID обзора';
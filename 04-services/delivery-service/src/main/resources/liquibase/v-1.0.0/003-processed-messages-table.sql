--liquibase formatted sql

--changeset ysavchen:003.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS processed_messages
(
    message_key   uuid        PRIMARY KEY,
    resource_id   uuid        NOT NULL,
    resource_type varchar(15) NOT NULL,
    created_at    timestamptz NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE processed_messages IS 'Таблица для хранения обработанных сообщений';
COMMENT ON COLUMN processed_messages.message_key IS 'Уникальный ключ сообщения, первичный ключ';
COMMENT ON COLUMN processed_messages.resource_id IS 'ID ресурса';
COMMENT ON COLUMN processed_messages.resource_type IS 'Тип ресурса (delivery)';
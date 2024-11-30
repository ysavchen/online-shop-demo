--liquibase formatted sql

--changeset ysavchen:002.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS deliveries
(
    id         uuid        PRIMARY KEY DEFAULT MD5(RANDOM()::text || CLOCK_TIMESTAMP()::text)::uuid,
    type       varchar(25) NOT NULL,
    status     varchar(15) NOT NULL,
    address    jsonb       NOT NULL,
    order_id   uuid        NOT NULL,
    created_at timestamptz NOT NULL DEFAULT NOW(),
    updated_at timestamptz NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE deliveries IS 'Таблица для хранения информации о доставке заказа';
COMMENT ON COLUMN deliveries.id IS 'ID записи, первичный ключ';
COMMENT ON COLUMN deliveries.type IS 'Тип доставки';
COMMENT ON COLUMN deliveries.status IS 'Статус доставки';
COMMENT ON COLUMN deliveries.address IS 'Адрес доставки';
COMMENT ON COLUMN deliveries.order_id IS 'ID заказа, по которому осуществляется доставка';
COMMENT ON COLUMN deliveries.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN deliveries.updated_at IS 'Дата и время последнего изменения записи';

--changeset ysavchen:002.02 runOnChange:false splitStatements:false runInTransaction:false
DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_catalog.pg_trigger WHERE tgname = 'update_deliveries_trigger') THEN
            BEGIN
                CREATE TRIGGER update_deliveries_trigger
                    BEFORE UPDATE
                    ON deliveries
                    FOR EACH ROW
                EXECUTE PROCEDURE updated_at_column_func();
            END;
        END IF;
    END
$do$;

COMMENT ON TRIGGER update_deliveries_trigger ON deliveries IS 'Триггер на обновление updated_at для записей таблицы deliveries';
--liquibase formatted sql

--changeset ysavchen:002.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS orders
(
    id             uuid        PRIMARY KEY DEFAULT MD5(RANDOM()::text || CLOCK_TIMESTAMP()::text)::uuid,
    items          jsonb       NOT NULL,
    status         varchar(15) NOT NULL,
    user_id        uuid        NOT NULL,
    total_quantity smallint    NOT NULL,
    total_price numeric(12, 2) NOT NULL,
    currency    varchar(3)     NOT NULL,
    created_at  timestamptz    NOT NULL DEFAULT NOW(),
    updated_at  timestamptz    NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE orders IS 'Таблица для хранения заказов';
COMMENT ON COLUMN orders.id IS 'ID записи, первичный ключ';
COMMENT ON COLUMN orders.items IS 'Заказанные товары';
COMMENT ON COLUMN orders.status IS 'Статус заказа';
COMMENT ON COLUMN orders.user_id IS 'ID покупателя';
COMMENT ON COLUMN orders.total_quantity IS 'Общее количество товаров в заказе';
COMMENT ON COLUMN orders.total_price IS 'Общая стоимость заказа';
COMMENT ON COLUMN orders.currency IS 'Валюта заказа';
COMMENT ON COLUMN orders.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN orders.updated_at IS 'Дата и время последнего изменения записи';

--changeset ysavchen:002.02 runOnChange:false splitStatements:false runInTransaction:false
DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_catalog.pg_trigger WHERE tgname = 'update_orders_trigger') THEN
            BEGIN
                CREATE TRIGGER update_orders_trigger
                    BEFORE UPDATE
                    ON orders
                    FOR EACH ROW
                EXECUTE PROCEDURE updated_at_column_func();
            END;
        END IF;
    END
$do$;

COMMENT ON TRIGGER update_orders_trigger ON orders IS 'Триггер на обновление updated_at для записей таблицы orders';
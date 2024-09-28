--liquibase formatted sql

--changeset ysavchen:003.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS order_items
(
    id             uuid           PRIMARY KEY,
    category       varchar(15)    NOT NULL,
    quantity       integer        NOT NULL,
    price          numeric(12, 2) NOT NULL,
    currency       varchar(3)     NOT NULL,
    order_fk       uuid           NOT NULL REFERENCES orders(id),
    created_at     timestamptz    NOT NULL DEFAULT NOW(),
    updated_at     timestamptz    NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE orders IS 'Таблица для хранения заказанных товаров';
COMMENT ON COLUMN order_items.id IS 'ID записи, первичный ключ';
COMMENT ON COLUMN order_items.category IS 'Категория товара (книги, одежда и т.д.)';
COMMENT ON COLUMN order_items.price IS 'Цена товара в момент покупки';
COMMENT ON COLUMN order_items.currency IS 'Валюта цены товара';
COMMENT ON COLUMN order_items.order_fk IS 'Внешний ключ на запись в таблице заказов';

--changeset ysavchen:003.02 runOnChange:false splitStatements:false runInTransaction:false
DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_catalog.pg_trigger WHERE tgname = 'update_order_items_trigger') THEN
            BEGIN
                CREATE TRIGGER update_order_items_trigger
                    BEFORE UPDATE
                    ON order_items
                    FOR EACH ROW
                EXECUTE PROCEDURE updated_at_column_func();
            END;
        END IF;
    END
$do$;

COMMENT ON TRIGGER update_order_items_trigger ON order_items IS 'Триггер на обновление updated_at для записей таблицы order_items';
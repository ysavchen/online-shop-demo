--liquibase formatted sql

--changeset Savchenko Y.A.:002.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS books
(
    id          uuid             PRIMARY KEY DEFAULT MD5(RANDOM()::text || CLOCK_TIMESTAMP()::text)::uuid,
    title       varchar(255)     NOT NULL,
    authors     text[]           NOT NULL DEFAULT '{}',
    description text,
    price       double precision NOT NULL,
    currency    varchar(3)       NOT NULL,
    created_at  timestamptz      NOT NULL DEFAULT NOW(),
    updated_at  timestamptz      NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE books IS 'Таблица для хранения книг';
COMMENT ON COLUMN books.title IS 'Название книги';
COMMENT ON COLUMN books.authors IS 'Авторы книги';
COMMENT ON COLUMN books.description IS 'Описание книги в base64';
COMMENT ON COLUMN books.price IS 'Цена книги';
COMMENT ON COLUMN books.currency IS 'Валюта для цены книги';
COMMENT ON COLUMN books.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN books.updated_at IS 'Дата и время последнего изменения записи';

--changeset Savchenko Y.A.:002.02 runOnChange:false splitStatements:false runInTransaction:false
DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_catalog.pg_trigger WHERE tgname = 'update_books_trigger') THEN
            BEGIN
                CREATE TRIGGER update_books_trigger
                    BEFORE UPDATE
                    ON books
                    FOR EACH ROW
                EXECUTE PROCEDURE updated_at_column_func();
            END;
        END IF;
    END
$do$;

COMMENT ON TRIGGER update_books_trigger ON books IS 'Триггер на обновление updated_at для записей таблицы books';
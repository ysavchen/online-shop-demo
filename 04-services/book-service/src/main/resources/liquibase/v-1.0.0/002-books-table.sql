--liquibase formatted sql

--changeset ysavchen:002.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS books
(
    id           uuid             PRIMARY KEY DEFAULT MD5(RANDOM()::text || CLOCK_TIMESTAMP()::text)::uuid,
    isbn         varchar(17)      NOT NULL UNIQUE,
    title        varchar(150)     NOT NULL,
    authors      text[]           NOT NULL DEFAULT '{}',
    description  text,
    genre        varchar(50)      NOT NULL,
    release_date date,
    quantity     integer          NOT NULL,
    price        numeric(12, 2),
    currency     varchar(3),
    created_at   timestamptz      NOT NULL DEFAULT NOW(),
    updated_at   timestamptz      NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE books IS 'Таблица для хранения книг';
COMMENT ON COLUMN books.id IS 'ID записи, первичный ключ';
COMMENT ON COLUMN books.isbn IS 'ISBN (международный стандартный номер книги)';
COMMENT ON COLUMN books.title IS 'Название книги';
COMMENT ON COLUMN books.authors IS 'Авторы книги';
COMMENT ON COLUMN books.description IS 'Описание книги в base64';
COMMENT ON COLUMN books.genre IS 'Жанр книги';
COMMENT ON COLUMN books.release_date IS 'Дата выхода книги';
COMMENT ON COLUMN books.quantity IS 'Количество книг на складе';
COMMENT ON COLUMN books.price IS 'Цена книги';
COMMENT ON COLUMN books.currency IS 'Валюта цены книги';
COMMENT ON COLUMN books.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN books.updated_at IS 'Дата и время последнего изменения записи';

--changeset ysavchen:002.02 runOnChange:false splitStatements:false runInTransaction:false
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
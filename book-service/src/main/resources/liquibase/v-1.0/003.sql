--liquibase formatted sql

--changeset Savchenko Y.A.:003.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS authors
(
    id         uuid        PRIMARY KEY DEFAULT MD5(RANDOM()::text || CLOCK_TIMESTAMP()::text)::uuid,
    first_name varchar(50) NOT NULL,
    last_name  varchar(50) NOT NULL,
    created_at timestamptz NOT NULL    DEFAULT NOW(),
    updated_at timestamptz NOT NULL    DEFAULT NOW()
);

COMMENT ON TABLE authors IS 'Таблица для хранения авторов книг';
COMMENT ON COLUMN authors.first_name IS 'Имя автора';
COMMENT ON COLUMN authors.last_name IS 'Фамилия автора';
COMMENT ON COLUMN authors.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN authors.updated_at IS 'Дата и время последнего изменения записи';

--changeset Savchenko Y.A.:003.02 runOnChange:false splitStatements:false runInTransaction:false
DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_catalog.pg_trigger WHERE tgname = 'update_authors_trigger') THEN
            BEGIN
                CREATE TRIGGER update_authors_trigger
                    BEFORE UPDATE
                    ON authors
                    FOR EACH ROW
                EXECUTE PROCEDURE updated_at_column_func();
            END;
        END IF;
    END
$do$;

COMMENT ON TRIGGER update_authors_trigger ON authors IS 'Триггер на изменение записи таблицы authors (обновление времени модификации)';
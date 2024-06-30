--liquibase formatted sql

--changeset ysavchen:003.01 runOnChange:false splitStatements:true runInTransaction:false
CREATE TABLE IF NOT EXISTS reviews
(
    id          uuid          PRIMARY KEY DEFAULT MD5(RANDOM()::text || CLOCK_TIMESTAMP()::text)::uuid,
    title       varchar(150)  NULL,
    review_text text          NULL,     --todo: добавить валидацию на base64
    author      varchar(100)  NOT NULL,
    rating      numeric(1, 1) NOT NULL, --todo: добавить валидацию min 1.0 и max 5.0
    book_fk     uuid          NOT NULL,
    created_at  timestamptz   NOT NULL DEFAULT NOW(),
    updated_at  timestamptz   NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE reviews IS 'Таблица для хранения обзоров книг';
COMMENT ON COLUMN reviews.id IS 'Идентификатор записи, первичный ключ';
COMMENT ON COLUMN reviews.title IS 'Заголовок обзора';
COMMENT ON COLUMN reviews.review_text IS 'Текст обзора в base64';
COMMENT ON COLUMN reviews.author IS 'Автор обзора';
COMMENT ON COLUMN reviews.rating IS 'Оценка книги от автора обзора';
COMMENT ON COLUMN reviews.book_fk IS 'Внешний ключ на запись в таблице книг';
COMMENT ON COLUMN reviews.created_at IS 'Дата и время создания записи';
COMMENT ON COLUMN reviews.updated_at IS 'Дата и время последнего изменения записи';

--changeset ysavchen:003.02 runOnChange:false splitStatements:true runInTransaction:false
ALTER TABLE reviews
    ADD CONSTRAINT fk_reviews_to_books FOREIGN KEY (book_fk) REFERENCES books (id);

--changeset ysavchen:003.03 runOnChange:false splitStatements:true runInTransaction:false
CREATE INDEX IF NOT EXISTS book_fk_idx ON reviews (book_fk);

--changeset ysavchen:003.04 runOnChange:false splitStatements:false runInTransaction:false
DO
$do$
    BEGIN
        IF NOT EXISTS(SELECT 1 FROM pg_catalog.pg_trigger WHERE tgname = 'update_reviews_trigger') THEN
            BEGIN
                CREATE TRIGGER update_reviews_trigger
                    BEFORE UPDATE
                    ON reviews
                    FOR EACH ROW
                EXECUTE PROCEDURE updated_at_column_func();
            END;
        END IF;
    END
$do$;

COMMENT ON TRIGGER update_reviews_trigger ON reviews IS 'Триггер на обновление updated_at для записей таблицы reviews';
--liquibase formatted sql

--changeset ysavchen:005.01 runOnChange:false splitStatements:false runInTransaction:false
CREATE OR REPLACE FUNCTION get_book_id(title varchar(150)) RETURNS uuid AS
$$
DECLARE book_id uuid;
BEGIN
    SELECT id INTO STRICT book_id FROM books WHERE books.title = get_book_id.title;
    RETURN book_id;
END;
$$ LANGUAGE 'plpgsql';

COMMENT ON FUNCTION get_book_id(varchar(150)) IS 'Функция для получения book_id по названию книги';
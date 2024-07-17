--liquibase formatted sql

--changeset Savchenko Y.A.:001.01 runOnChange:false splitStatements:false runInTransaction:false
CREATE OR REPLACE FUNCTION updated_at_column_func() RETURNS trigger AS
$$
BEGIN
    new.updated_at = NOW();
    RETURN new;
END;
$$ LANGUAGE 'plpgsql';

COMMENT ON FUNCTION updated_at_column_func() IS 'Функция по обновлению updated_at для записей';
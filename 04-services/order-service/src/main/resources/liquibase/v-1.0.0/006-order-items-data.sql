--liquibase formatted sql

--changeset ysavchen:006.01 runOnChange:false splitStatements:false runInTransaction:false
INSERT INTO order_items (id, category, quantity, price, currency, order_fk)
VALUES
  (
    '01907545-eaf3-7de3-9157-dce3ece084e6',
    'BOOKS',
    1,
    720.00,
    'RUB',
    '0190bd82-13e5-77fe-bb73-0764d5e3efdb'
  ),
  (
    '01907546-9a94-741a-9c7c-be202484542f',
    'BOOKS',
    1,
    250.42,
    'RUB',
    '0190bd82-13e5-77fe-bb73-0764d5e3efdb'
  ),
  (
    '01907548-ca7f-728a-b9e1-ab6430db67c9',
    'BOOKS',
    1,
    178.39,
    'RUB',
    '0190bd82-13e5-77fe-bb73-0764d5e3efdb'
  );
--liquibase formatted sql

--changeset ysavchen:005.01 runOnChange:false splitStatements:false runInTransaction:false
INSERT INTO orders (id, user_id, status, items, total_quantity, total_price, currency)
VALUES
  (
    '0190bd82-13e5-77fe-bb73-0764d5e3efdb',
    '0190bd8a-8d20-72b2-a4a6-46de1cb92e06',
    'CREATED',
    '[{"id": "01907545-eaf3-7de3-9157-dce3ece084e6", "category": "BOOK", "quantity": 1, "price": {"value": 720.00, "currency": "RUB"}}]',
    1,
    720.00,
    'RUB'
  );

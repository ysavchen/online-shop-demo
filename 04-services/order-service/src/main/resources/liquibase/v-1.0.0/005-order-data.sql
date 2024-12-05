--liquibase formatted sql

--changeset ysavchen:005.01 runOnChange:false splitStatements:false runInTransaction:false
INSERT INTO orders (id, user_id, status, total_quantity, total_price, total_currency)
VALUES
  (
    '019251e2-46c8-72b2-9a71-bb14e49a1857',
    '0190bd8a-8d20-72b2-a4a6-46de1cb92e06',
    'CREATED',
    1,
    369.00,
    'RUB'
  ),
  (
    '019251d3-4b85-7696-a9b9-b05c5d8e6a9e',
    '0190bd8a-8d20-72b2-a4a6-46de1cb92e06',
    'IN_PROGRESS',
    2,
    2325.41,
    'RUB'
  ),
  (
    '0190bd82-13e5-77fe-bb73-0764d5e3efdb',
    '0190bd8a-8d20-72b2-a4a6-46de1cb92e06',
    'DELIVERED',
    3,
    1148.81,
    'RUB'
  );
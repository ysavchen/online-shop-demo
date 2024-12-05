--liquibase formatted sql

--changeset ysavchen:004.01 runOnChange:false splitStatements:false runInTransaction:false
INSERT INTO deliveries (id, type, "date", address, status, order_id)
VALUES
  (
    '0193955d-620d-7d0f-a416-7bba32b5d3a6',
    'IN_STORE_PICKUP',
    '2024-12-07',
    '{"country": "England", "city": "London", "street": "Water St.", "building": "4"}',
    'CREATED',
    '019251e2-46c8-72b2-9a71-bb14e49a1857'
  ),
  (
    '01939560-3965-7167-9db5-2cdc951a22a4',
    'IN_STORE_PICKUP',
    '2024-11-04',
    '{"country": "England", "city": "London", "street": "Regent St.", "building": "235"}',
    'IN_PROGRESS',
    '019251d3-4b85-7696-a9b9-b05c5d8e6a9e'
  ),
  (
    '01939560-6ca8-7573-883a-fd846b862f3b',
    'HOME_DELIVERY',
    '2024-09-15',
    '{"country": "England", "city": "London", "street": "Chapel Street", "building": "6"}',
    'DELIVERED',
    '0190bd82-13e5-77fe-bb73-0764d5e3efdb'
  );
insert into orders (id, addressee_name, address_id, phone_id, email, created_at)
values (1, 'John Doe', 1, 1, 'john.doe@test.com', '2020-05-25T10:35:56.879695500');

insert into order_book (order_id, book_id) values (1, 1);
insert into order_book (order_id, book_id) values (1, 2);
insert into order_book (order_id, book_id) values (1, 3);

alter sequence orders_id_seq restart with 2;
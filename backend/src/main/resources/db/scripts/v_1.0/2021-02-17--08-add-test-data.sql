insert into authors(id, full_name) values (1, 'Author One');
insert into authors(id, full_name) values (2, 'Author Two');
alter sequence authors_id_seq restart with 3;

insert into books (id, title, description, author_id, image, price) values (1, 'Book One', 'Description One', 1, '/imageOne', 22.95);
insert into books (id, title, description, author_id, image, price) values (2, 'Book Two', 'Description Two', 2, '/imageTwo', 46.00);
alter sequence books_id_seq restart with 3;

/* userOne@test.com , Start01# */
insert into users (id, first_name, last_name, email, password) values (1, 'Name One', 'Surname One', 'userOne@test.com', '$2a$10$Wfn//hRL3NrA9DG0fYRtYuhzLZc8CLDNKvv4Twcx55XEDsWABlD8K');
alter sequence users_id_seq restart with 2;

insert into addresses (id, address) values (1, 'Address, 1');
alter sequence addresses_id_seq restart with 2;

insert into phones (id, phone) values (1, '+1111 1111');
alter sequence phones_id_seq restart with 2;

insert into orders (id, addressee_name, address_id, phone_id, email, created_at) values (1, 'Name One Surname One', 1, 1, 'userOne@test.com', '2020-05-25T10:35:56.879695500');
insert into order_book (order_id, book_id) values (1, 1);
insert into order_book (order_id, book_id) values (1, 2);
alter sequence orders_id_seq restart with 2;
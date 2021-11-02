create table books
(
    id          bigserial primary key,
    title       varchar(255) not null,
    description text,
    author_id   bigint,
    image       varchar(255),
    price       double precision       not null
);

create table authors
(
    id        bigserial primary key,
    full_name varchar(255) not null
);

create table orders
(
    id        bigserial primary key,
    addressee_name varchar(255) not null,
    email varchar(255),
    created_at timestamp,
    address_id   bigint,
    phone_id   bigint
);

create table order_book (
    order_id bigint not null,
    book_id bigint not null
);

create table addresses
(
    id      bigserial primary key,
    address varchar(255) not null
);

create table phones
(
    id    bigserial primary key,
    phone varchar(255) not null
);

create table users
(
    id         bigserial primary key,
    first_name varchar(255) not null,
    last_name  varchar(255) not null,
    email      varchar(255) not null,
    password   varchar(255) not null
);

alter table books
    add constraint fk_books_authors foreign key (author_id) references authors (id);

alter table orders
    add constraint fk_orders_addresses foreign key (address_id) references addresses (id);

alter table orders
    add constraint fk_orders_phones foreign key (phone_id) references phones (id);

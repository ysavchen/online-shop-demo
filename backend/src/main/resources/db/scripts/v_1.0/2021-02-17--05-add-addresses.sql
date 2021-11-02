insert into addresses (id, address) values (1, 'Madison Ave, 7');
insert into addresses (id, address) values (2, 'Park Ave, 3');

alter sequence addresses_id_seq restart with 3;
/* john.doe@test.com , Start01# */
insert into users (id, first_name, last_name, email, password)
values (1, 'John', 'Doe', 'john.doe@test.com', '$2a$10$Wfn//hRL3NrA9DG0fYRtYuhzLZc8CLDNKvv4Twcx55XEDsWABlD8K');

/* james.franklin@test.com , Start02# */
insert into users (id, first_name, last_name, email, password)
values (2, 'James', 'Franklin', 'james.franklin@test.com',
        '$2a$10$VA1SFGWztAk5iSDMSwYGbuxvR7dNVYsMJDTY.mjzO.8lbRjeNNA9a');

/* eleonora.mckinsey@test.com , Start03# */
insert into users (id, first_name, last_name, email, password)
values (3, 'Eleonora', 'McKinsey', 'eleonora.mckinsey@test.com',
        '$2a$10$PhKTwxvMPEcy75PrHQiW.u5xfcpqW4NtZCV/ewP3hC33g/sgtyOSq');

alter sequence users_id_seq restart with 4;
insert into users
values ('admin@example.com', true, '$2a$12$wuqV4zO0jBAvlkO3RDBQ.edBZ2bYTjpW59xBZLKIHyZhJZ.3WMqe2');
insert into users
values ('user@example.com', true, '$2a$12$.Qw7/YMRMkCKCnMkc3fxh.Exj9IcTl9zQlFRI8EDz0KxUSMC4KJLa');
insert into authority (role, email)
values ('ROLE_ADMIN', 'admin@example.com');
insert into authority (role, email)
values ('ROLE_USER', 'user@example.com');
insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, email)
values ('kavka_address_one', 'kavka_address_two', 'kavka_city', 'kavka', 'USA', 'Kavka', 'Website', '555-777788', 56791, 'Chicago', true, 'admin@example.com');
insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, email)
values ('kavka_address_one_return', 'kavka_address_two_return', 'kavka_city', 'kavka', 'USA', 'Kavka', 'Website', '555-777788', 8916, 'Vegas', false, 'admin@example.com');
insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, email)
values ('user_address_one', 'user_address_two', 'user_city', 'user', 'USA', 'User', 'Website', '790-0006588', 98021, 'Texas', true, 'user@example.com');
insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, email)
values ('user_address_another', 'user_address__another', 'user_city', 'user', 'Canada', 'User', 'Website', '790-0006588', 89023, 'DC', false, 'user@example.com');
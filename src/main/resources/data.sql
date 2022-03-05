insert into users (company, email, enabled, first_name, is_verified, last_name, password)
values (null,'admin@example.com', true, 'admin_fname', false, 'admin_lname','$2a$12$wuqV4zO0jBAvlkO3RDBQ.edBZ2bYTjpW59xBZLKIHyZhJZ.3WMqe2');
insert into users (company, email, enabled, first_name, is_verified, last_name, password)
values (null,'user@example.com', true, 'user_fname', false, 'user_lname','$2a$12$.Qw7/YMRMkCKCnMkc3fxh.Exj9IcTl9zQlFRI8EDz0KxUSMC4KJLa');

insert into authority (role, user_id)
values ('ROLE_ADMIN', 1);
insert into authority (role, user_id)
values ('ROLE_USER', 2);

insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, user_id)
values ('kavka_address_one', 'kavka_address_two', 'kavka_city', 'kavka', 'USA', 'Kavka', 'Website', '555-777788', 56791, 'Chicago', true, 1);
insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, user_id)
values ('kavka_address_one_return', 'kavka_address_two_return', 'kavka_city', 'kavka', 'USA', 'Kavka', 'Website', '555-777788', 8916, 'Vegas', false, 1);
insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, user_id)
values ('user_address_one', 'user_address_two', 'user_city', 'user', 'USA', 'User', 'Website', '790-0006588', 98021, 'Texas', true, 2);
insert into customer (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state, is_default, user_id)
values ('user_address_another', 'user_address__another', 'user_city', 'user', 'Canada', 'User', 'Website', '790-0006588', 89023, 'DC', false, 2);

# insert into product (name, price, code, image) values ('product_name', 100, 'UYTER-IUYHN-INWGT-UIQOP', 'some_dummy_image_url');
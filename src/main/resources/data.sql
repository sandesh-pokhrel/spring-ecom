insert into users (company, email, enabled, first_name, last_name, password)
values ('kavka', 'admin@kavka.com', true, 'admin_fname', 'admin_lname',
        '$2a$12$wuqV4zO0jBAvlkO3RDBQ.edBZ2bYTjpW59xBZLKIHyZhJZ.3WMqe2');
insert into users (company, email, enabled, first_name, last_name, password)
values ('company_one', 'user@kavka.com', true, 'user_fname', 'user_lname',
        '$2a$12$.Qw7/YMRMkCKCnMkc3fxh.Exj9IcTl9zQlFRI8EDz0KxUSMC4KJLa');

insert into authority (role, user_id)
values ('ROLE_ADMIN', 1);
insert into authority (role, user_id)
values ('ROLE_USER', 2);

insert into address (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state,
                     is_default, user_id)
values ('kavka_address_one', 'kavka_address_two', 'kavka_city', 'kavka', 'USA', 'Kavka', 'Website', '555-777788', 56791,
        'Chicago', true, 1);
insert into address (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state,
                     is_default, user_id)
values ('kavka_address_one_return', 'kavka_address_two_return', 'kavka_city', 'kavka', 'USA', 'Kavka', 'Website',
        '555-777788', 8916, 'Vegas', false, 1);
insert into address (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state,
                     is_default, user_id)
values ('user_address_one', 'user_address_two', 'user_city', 'user', 'USA', 'User', 'Website', '790-0006588', 98021,
        'Texas', true, 2);
insert into address (address_one, address_two, city, company, country, first_name, last_name, phone, postal_code, state,
                     is_default, user_id)
values ('user_address_another', 'user_address__another', 'user_city', 'user', 'Canada', 'User', 'Website',
        '790-0006588', 89023, 'DC', false, 2);

insert into product_category (code, enabled, name, thumbnail_image_url)
values ('AD8Y2', true, 'Product category name', 'product_category_thumbnail_url');
insert into product (code, description, image_url, name, product_category_id)
values ('PRCODE', 'Very long product description goes here', 'product_dummy_image_url', 'Product name', 1);
insert into product_detail (code, colors, image_url, name, price, product_id, tier_one_price, tier_two_price, tier_three_price)
values ('RT8YZ-3*3', null, 'product_detail_image_url', 'Product detail name', 175, 1, 5, 10, 15);
insert into product_detail (code, colors, image_url, name, price, product_id, tier_one_price, tier_two_price, tier_three_price)
values ('M6KLY-6*7', null, 'product_detail_image_url_second', 'Product detail name second', 85, 1, 4, 8, 12);

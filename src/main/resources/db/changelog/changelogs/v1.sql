
create table products(
id bigserial primary key not null,
name varchar(40) not null,
quantity int not null,
price numeric not null,
created_at timestamp not null,
updated_at timestamp not null);



-- liquibase formatted sql

-- changeset liquibase:1
create table account (id bigint not null, balance integer not null, number integer not null, addresss_id bigint, primary key (id)) engine=InnoDB;
create table address (id bigint not null, city varchar(255), country varchar(255), number varchar(255), street varchar(255), zip_code integer not null, primary key (id)) engine=InnoDB;
create table customer (id bigint not null, birthdate date not null, customer_id bigint not null, email varchar(255) not null, first_name varchar(255), last_name varchar(255), name_regex tinyblob, address_id bigint, primary key (id)) engine=InnoDB;
create table hibernate_sequence (next_val bigint) engine=InnoDB;
insert into hibernate_sequence values ( 1 );
alter table customer add constraint unique_mail unique (email);
alter table account add constraint account_address_id foreign key (addresss_id) references customer (id) on delete cascade;
alter table customer add constraint customer_address_id foreign key (address_id) references address (id) on delete set null;
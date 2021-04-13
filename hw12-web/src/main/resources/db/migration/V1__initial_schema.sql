create sequence hibernate_sequence;

create table address_data_set
(
    id       int8 not null,
    street   varchar(255),
    clent_id int8,
    primary key (id)
);

create table client
(
    id       int8 not null,
    login    varchar(255),
    name     varchar(255),
    password varchar(255),
    primary key (id)
);

create table phone_data_set
(
    id        int8 not null,
    number    varchar(255),
    client_id int8 not null,
    primary key (id)
);

alter table if exists address_data_set add constraint fk_clent_id_client foreign key (clent_id) references client;

alter table if exists phone_data_set add constraint fk_clent_id_client foreign key (client_id) references client;

insert into client (login, name, password, id) values ('client1', 'Иванов Иван', '111', 1);
insert into phone_data_set (client_id, number, id) values (1, '+7999', 1);
insert into phone_data_set (client_id, number, id) values (1, '+7998', 2);
insert into address_data_set (clent_id, street, id) values (1, 'street1', 1);

SELECT nextval('hibernate_sequence');
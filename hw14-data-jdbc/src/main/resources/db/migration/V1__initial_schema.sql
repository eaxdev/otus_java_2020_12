create table address_data_set
(
    id       bigserial not null primary key,
    street   varchar(255),
    client_id int8
);

create table client
(
    id       bigserial not null primary key,
    login    varchar(255),
    name     varchar(255),
    password varchar(255)
);

create table phone_data_set
(
    id        bigserial not null primary key,
    number    varchar(255),
    client_id int8      not null
);

alter table if exists address_data_set add constraint fk_clent_id_client foreign key (client_id) references client;

alter table if exists phone_data_set add constraint fk_clent_id_client foreign key (client_id) references client;
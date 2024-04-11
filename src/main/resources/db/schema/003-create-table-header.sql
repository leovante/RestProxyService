--liquibase formatted sql
--changeset temnikov_do:002-create-table-header

create sequence header_ordinal_number_seq
    start with 1
    increment by 1
    cache 10;

create table header
(
    id   SERIAL       not null,
    name  varchar(255) not null,
    value varchar(255) not null,
    constraint name_value_pk primary key (name, value)
);

alter sequence header_ordinal_number_seq
    owned by header.id;

comment on table header is 'header';
comment on column header.name is 'название header';
comment on column header.value is 'значение header';

--liquibase formatted sql
--changeset temnikov_do:002-create-table-header

create table header
(
    id          SERIAL       not null
        constraint header_pkey primary key,
    name        varchar(255) not null,
    value       varchar(255) not null,
    response_id bigint       not null,
    constraint header_response_fk
        foreign key (response_id)
            references response (id)
);

comment on table header is 'header';
comment on column header.id is 'идентификатор header';
comment on column header.name is 'название header';
comment on column header.value is 'значение header';
comment on column header.response_id is 'идентификатор response';

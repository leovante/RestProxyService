--liquibase formatted sql
--changeset temnikov_do:001-create-table-endpoint

create sequence endpoint_ordinal_number_seq
    start with 1
    increment by 1
    cache 10;

create table endpoint
(
    id     SERIAL       not null,
    path   varchar(255) not null,
    method varchar(255) not null,
    team   varchar(255) not null,
    wait   bigint,
    constraint path_method_team_pk primary key (path, method, team)
);

alter sequence endpoint_ordinal_number_seq
    owned by endpoint.id;

comment on table endpoint is 'endpoint';
comment on column endpoint.path is 'путь endpoint';
comment on column endpoint.method is 'метод endpoint';
comment on column endpoint.team is 'идентификатор team';
comment on column endpoint.wait is 'время ожидания в миллисекундах';


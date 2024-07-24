--liquibase formatted sql
--changeset temnikov_do:001-create-table-endpoint

create sequence endpoint_ordinal_number_seq
    start with 1
    increment by 1
    cache 10;

create table endpoint
(
    id            SERIAL                      not null,
    path          varchar(255)                not null,
    method        varchar(255)                not null,
    team          varchar(255)                not null,
    wait          bigint,
    is_regex      bool,
    response_stub jsonb                       not null,
    idx           int                         not null,
    created_at    timestamp without time zone not null,
    updated_at    timestamp without time zone not null,
    constraint path_method_team_pk primary key (path, method, team)
);

alter sequence endpoint_ordinal_number_seq
    owned by endpoint.id;

create index endpoint_path_idx
    on endpoint (path);

create index endpoint_method_idx
    on endpoint (method);

create index endpoint_team_idx
    on endpoint (team);

create index endpoint_is_regex_idx
    on endpoint (is_regex);

comment on table endpoint is 'endpoint';
comment on column endpoint.path is 'путь endpoint';
comment on column endpoint.method is 'метод endpoint';
comment on column endpoint.team is 'идентификатор team';
comment on column endpoint.wait is 'время ожидания в миллисекундах';


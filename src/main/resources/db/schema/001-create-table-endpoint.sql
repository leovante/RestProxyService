--liquibase formatted sql
--changeset temnikov_do:001-create-table-team-endpoint

create table endpoint
(
    id      SERIAL       not null
        constraint endpoint_pkey primary key,
    path    varchar(255) not null,
    method  varchar(255) not null,
    wait    bigint,
    team_id bigint       not null,
    constraint endpoint_team_fk
        foreign key (team_id)
            references team (id)

);

comment on table endpoint is 'endpoint';
comment on column endpoint.id is 'уникальный идентификатор';
comment on column endpoint.path is 'путь endpoint';
comment on column endpoint.method is 'метод endpoint';
comment on column endpoint.wait is 'время ожидания в миллисекундах';
comment on column endpoint.team_id is 'идентификатор team';


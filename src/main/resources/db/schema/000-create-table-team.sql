--liquibase formatted sql
--changeset temnikov_do:000-create-table-team

create table team
(
    id   SERIAL       not null
        constraint team_pkey primary key,
    code varchar(255) not null,
    unique (code)
);

create index team_code_idx
    on team (code);

comment on table team is 'команда';
comment on column team.id is 'уникальный идентификатор';
comment on column team.code is 'уникальный идентификатор';

--liquibase formatted sql
--changeset temnikov_do:005-create-table-request-history

create table request_history
(
    id              SERIAL       not null
        constraint request_history_pkey primary key,
    request         varchar(8000),
    endpoint_path   varchar(255) not null,
    endpoint_method varchar(255) not null,
    endpoint_team   varchar(255) not null,
    constraint response_endpoint_fk foreign key (endpoint_path, endpoint_method, endpoint_team)
        references endpoint (path, method, team)
);

comment on table request_history is 'response';
comment on column request_history.id is 'идентификатор response';
comment on column request_history.request is 'request response';
comment on column request_history.endpoint_path is 'путь endpoint';
comment on column request_history.endpoint_method is 'метод endpoint';
comment on column request_history.endpoint_team is 'идентификатор team';
--liquibase formatted sql
--changeset temnikov_do:003-create-table-response

create table response
(
    id              SERIAL                      not null
        constraint response_pkey primary key,
    status          varchar(255)                not null,
    json_body       varchar(100000),
    string_body     varchar(100000),
    index           bigint,
    current_index   bigint,
    created_at      timestamp without time zone not null,
    updated_at      timestamp without time zone not null,
    endpoint_path   varchar(255)                not null,
    endpoint_method varchar(255)                not null,
    endpoint_team   varchar(255)                not null,
    constraint response_endpoint_fk foreign key (endpoint_path, endpoint_method, endpoint_team)
        references endpoint (path, method, team) on delete cascade
);

create index response_created_at_idx
    on response (created_at);

create index response_path_idx
    on response (endpoint_path);

create index response_method_idx
    on response (endpoint_method);

create index response_team_idx
    on response (endpoint_team);

comment on table response is 'response';
comment on column response.id is 'идентификатор response';
comment on column response.status is 'status response';
comment on column response.json_body is 'body_json response';
comment on column response.string_body is 'body_string response';
comment on column response.index is 'index для body response';
comment on column response.current_index is 'current_index для body response';
comment on column response.created_at is 'дата создания';
comment on column response.updated_at is 'дата обновления';
comment on column response.endpoint_path is 'путь endpoint';
comment on column response.endpoint_method is 'метод endpoint';
comment on column response.endpoint_team is 'идентификатор team';

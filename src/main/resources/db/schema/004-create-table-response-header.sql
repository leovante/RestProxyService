--liquibase formatted sql
--changeset temnikov_do:004-create-table-response-header

create table response_header
(
    header_name  varchar(255) not null,
    header_value varchar(255) not null,
    response_id  SERIAL       not null,
    constraint header_name_value_fk foreign key (header_name, header_value)
        references header (name, value) on delete cascade,
    constraint response_id_fk foreign key (response_id)
        references response (id) on delete cascade
);

comment on table response_header is 'response_header';
comment on column response_header.header_name is 'название header_name';
comment on column response_header.header_value is 'значение header_value';
comment on column response_header.response_id is 'значение response_id';

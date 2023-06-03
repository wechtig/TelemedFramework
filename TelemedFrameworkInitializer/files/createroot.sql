create table telemed_role
(
    uid  varchar(80) not null
        primary key,
    role varchar(20) null
);

create table telemed_user
(
    uid        varchar(80)  not null
        primary key,
    email      varchar(20)  null,
    username   varchar(20)  null,
    password   varchar(120) null,
    first_name varchar(20)  null,
    last_name  varchar(20)  null,
    role_uid   varchar(20)  null
);

create index fk_role
    on telemed_user (role_uid);


create table if not exists users
(
    id       serial not null
        constraint user_table_pk
            primary key,
    login    varchar(255)
        constraint uk_ow0gan20590jrb00upg3va2fn
        unique,
    password varchar(255),
    role     varchar(255)
                 );

create table if not exists tasks
(
    id       serial not null
        constraint tasks_table_pk
            primary key,
    comment     varchar(255),
    description varchar(255),
    title       varchar(255),
    author_id   bigint
        constraint fkhods8r8oyyx7tuj3c91ki2sk1
            references users,
    executor_id bigint
        constraint fkbrg922bkqn5m7212jsqjg6ioe
            references users
);

insert into users(login, password, role)
VALUES ('test_user1', '$2a$10$xwf/u8NOJvOgYJEZ68aM6OB3j/fIR7N/YLZAbHckYkfIKJ4Es2eVa', 'ROLE_USER'),
       ('test_admin', '$2a$10$TvcHqk1iquYJ01Lmjpc73eSbt.6YlmdKwRVAfUyKgMwM.jt5NSfoO', 'ROLE_ADMIN');


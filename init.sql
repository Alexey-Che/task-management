create table if not exists users
(
    id       serial not null
        constraint user_table_pk
            primary key,
    email    varchar(255)
        constraint uk_ow0gan20590jrb00upg3va2fn
        unique,
    password varchar(255),
    role     varchar(45)
 );

create table comment
(
    id       serial not null
        constraint comment_table_pk
            primary key,
    text varchar(255)
);

create table if not exists task
(
    id       serial not null
        constraint task_table_pk
            primary key,
    title       varchar(255)  not null,
    description varchar(255),
    status      varchar(45),
    priority    varchar(45),
    author_id   bigint
        constraint fknu4tbtolc8avjgstax6sk0woe
            references users,
    comment_id  bigint
        constraint fk80bu2ev6i4lwcb7p5v5l673ij
            references comment,
    executor_id bigint
        constraint fk60p5enwpfg7w3pnor96m0t931
            references users
);

insert into users(email, password, role)
values ('test_user1@mail.com', '$2a$10$xwf/u8NOJvOgYJEZ68aM6OB3j/fIR7N/YLZAbHckYkfIKJ4Es2eVa', 'ROLE_USER'),
       ('test_admin@mail.com', '$2a$10$TvcHqk1iquYJ01Lmjpc73eSbt.6YlmdKwRVAfUyKgMwM.jt5NSfoO', 'ROLE_ADMIN');

insert into task(title, description, status, priority, author_id)
values ('task_1', 'description_1', 'PENDING', 'LOW', 1),
       ('task_2', 'description_2', 'IN_PROGRESS', 'MEDIUM', 1),
       ('task_3', 'description_3', 'COMPLETE', 'HIGH', 1);


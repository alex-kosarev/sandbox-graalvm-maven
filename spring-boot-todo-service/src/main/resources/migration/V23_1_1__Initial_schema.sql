create schema if not exists todo;

create table todo.t_todo
(
    id             uuid primary key,
    c_completed    boolean   not null default false,
    c_task         varchar   not null check (length(trim(c_task)) > 0 ),
    c_date_created timestamp not null
);
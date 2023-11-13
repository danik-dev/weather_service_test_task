--liquibase formatted sql
--changeset danik-dev:1
--comment initial database schema
create table sensor
(
    enabled    boolean not null,
    id         bigserial
        primary key,
    access_key varchar(255),
    name       varchar(255)
);

create table measurement
(
    raining   boolean not null,
    value     double precision,
    fixed_at  timestamp(6),
    id        bigserial
        primary key,
    sensor_id bigint
        constraint fk4j3sm2q5k6pjafuwg783w7fne
            references sensor
);

alter table measurement
    owner to postgres;
alter table sensor
    owner to postgres;

--rollback truncate TABLE "sensor"; truncate TABLE "measurement";
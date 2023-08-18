DROP TABLE user_role if exists;
DROP TABLE vote if exists;
DROP TABLE users if exists;
DROP TABLE dish if exists;
DROP TABLE restaurant if exists;
DROP SEQUENCE global_seq if exists;

//CREATE SEQUENCE GLOBAL_SEQ AS INTEGER START WITH 100000;

create table USER_ROLE
(
    ID      INTEGER GENERATED BY DEFAULT AS IDENTITY,// SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    USER_ID INTEGER           not null,
    ROLE    CHARACTER VARYING not null,
    CONSTRAINT user_roles_idx UNIQUE (user_id, role),
    constraint "USER_ROLE_pk"
        primary key (ID)
);

create table USERS
(
    ID         INTEGER GENERATED BY DEFAULT AS IDENTITY,// SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    EMAIL      VARCHAR(255)                     NOT NULL
        constraint "USERS_pk2"
            unique,
    NAME       VARCHAR(128)                     NOT NULL,
    PASSWORD   VARCHAR(255),
    ENABLED    BOOLEAN   default TRUE           not null,
    REGISTERED TIMESTAMP default LOCALTIMESTAMP not null,
    constraint "USERS_pk"
        primary key (ID)
);
CREATE UNIQUE INDEX users_unique_email_idx
    ON USERS (email);

-- auto-generated definition
create table RESTAURANT
(
    ID   INTEGER generated always as identity,// SEQUENCE GLOBAL_SEQ,
    NAME CHARACTER VARYING not null
        constraint "RESTAURANT_pk2"
            unique,
    constraint "RESTAURANT_pk"
        primary key (ID)
);

-- auto-generated definition
create table DISH
(
    ID            INTEGER GENERATED BY DEFAULT AS IDENTITY,// SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    NAME          CHARACTER VARYING(128) not null,
    PRICE         DOUBLE PRECISION       not null,
    RESTAURANT_ID INTEGER                not null,
    DATE_MENU     DATE default CURRENT_DATE not null,
    constraint "DISH_pk"
        primary key (ID)
);

 create unique index DISH_RESTAURANT_ID_DATE_MENU_NAME_INDEX
     on DISH (RESTAURANT_ID, DATE_MENU, NAME);

create table VOTE
(
    ID            INTEGER GENERATED BY DEFAULT AS IDENTITY,// SEQUENCE GLOBAL_SEQ PRIMARY KEY,
    USER_ID       INTEGER                   not null,
    RESTAURANT_ID INTEGER                   not null,
    DATE_VOTE     DATE default CURRENT_DATE not null,
    TIME_VOTE     TIME default CURRENT_TIME not null,
    constraint "VOTE_pk"
        primary key (ID)
);

create unique index VOTE_USER_ID_DATE_VOTE_UINDEX
    on VOTE (USER_ID, DATE_VOTE);


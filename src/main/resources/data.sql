DELETE FROM user_role;
DELETE FROM vote;
DELETE FROM users;
DELETE FROM dish;
DELETE FROM RESTAURANT;

ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN',100001),
       ('USER', 100001);

INSERT INTO RESTAURANT (name)
VALUES ('Restaran1'),
       ('Restaran2'),
       ('Restaran3');

INSERT INTO DISH (name,PRICE,RESTAURANT_ID,DATE_MENU)
VALUES ('Dish1.1',600.00,100006,'2023-08-08');
INSERT INTO DISH (name,PRICE,RESTAURANT_ID)
VALUES ('Dish1.1',600.00,100006),
       ('Dish1.2',200.00,100006),
       ('Dish2.1',400.00,100007);


INSERT INTO VOTE (USER_ID,RESTAURANT_ID,DATE_VOTE)
VALUES (10000,100006,'2023-08-08');

INSERT INTO VOTE (USER_ID,RESTAURANT_ID)
VALUES (10000,100006),
       (10001,100007);



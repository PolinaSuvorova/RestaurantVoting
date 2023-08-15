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
VALUES ('USER', 10000),
       ('ADMIN', 10001),
       ('USER', 10001);

INSERT INTO RESTAURANT (name)
VALUES ('Restaran1'),
       ('Restaran2'),
       ('Restaran3');

INSERT INTO DISH (name, price, restaurant_id, date_menu)
VALUES ('dish1_1', 1000,1,'2023-08-08'),
       ('dish1_2', 500,1,'2023-08-08'),
       ('dish1_3', 500,1,'2023-08-07'),
       ('dish2_1', 500,2,'2023-08-08'),
       ('dish2_2', 500,2,'2023-08-08');

INSERT INTO VOTE (USER_ID, RESTAURANT_ID)
VALUES (10000, 10006),
       (10001, 10007);

INSERT INTO VOTE (USER_ID, RESTAURANT_ID,DATE_VOTE)
VALUES (10000, 10008,'2023-08-07'),
       (10001, 10006,'2023-08-07');
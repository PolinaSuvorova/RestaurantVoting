DELETE FROM user_role;
DELETE FROM vote;
DELETE FROM users;
DELETE FROM dish;
DELETE FROM RESTAURANT;

INSERT INTO USERS (name, email, password)
VALUES ('User', 'user@yandex.ru', '{noop}password'),
       ('Admin', 'admin@gmail.com', '{noop}admin'),
       ('Guest', 'guest@gmail.com', '{noop}guest');

INSERT INTO USER_ROLE (role, user_id)
VALUES ('USER', 1),
       ('ADMIN',2),
       ('USER', 2);

INSERT INTO RESTAURANT (name)
VALUES ('Restarant1'),
       ('Restarant2'),
       ('Restarant3');

INSERT INTO DISH (name,PRICE,RESTAURANT_ID)
VALUES ('Dish1.1',600.00,1),
       ('Dish1.2',200.00,1),
       ('Dish2.1',400.00,2);

INSERT INTO DISH (name,PRICE,RESTAURANT_ID,DATE_MENU)
VALUES ('Dish1.1',600.00,1,'2023-08-08');


INSERT INTO VOTE (USER_ID,RESTAURANT_ID,DATE_VOTE)
VALUES (1,1,'2023-08-08');

INSERT INTO VOTE (USER_ID,RESTAURANT_ID)
VALUES (1,1),
       (2,2);



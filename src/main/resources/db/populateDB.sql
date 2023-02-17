DELETE FROM topjava.public.user_role;
DELETE FROM topjava.public.users;
ALTER SEQUENCE topjava.public.global_seq RESTART WITH 100000;

INSERT INTO topjava.public.users (name, email, password)
VALUES ('User', 'user@yandex.ru', 'password'),
       ('Admin', 'admin@gmail.com', 'admin'),
       ('Guest', 'guest@gmail.com', 'guest');

INSERT INTO topjava.public.user_role (role, user_id)
VALUES ('USER', 100000),
       ('ADMIN', 100001);

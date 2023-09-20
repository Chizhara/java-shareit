INSERT INTO users(name, email)
VALUES ('User A', 'mail@mail.ru'),
       ('User B', 'email@mail.ru');

INSERT INTO requests(description, created, requester_id)
VALUES ('Request A', '2023-09-12T12:12:12', 1),
       ('Request B', '2023-09-12T9:9:9', 1),
       ('Request C', '2023-09-12T10:10:10', 2);

INSERT INTO items(name, description, available, owner_id, request_id)
VALUES ('Item A', 'Test Item A', true, 1, 1),
       ('Item B', 'Test Item B', false, 2, null);

INSERT INTO bookings(start_date, end_date, item_id, booker_id, status)
VALUES ('2023-11-12T12:12:12', '2023-12-12T12:12:12', 1, 2, 'WAITING'),
       ('2023-06-12T12:12:12', '2023-11-12T12:12:12', 2, 1, 'REJECTED'),
       ('2023-10-12T12:12:12', '2023-11-12T12:12:12', 2, 1, 'APPROVED'),
       ('2024-06-12T12:12:12', '2024-11-12T12:12:12', 2, 1, 'WAITING'),
       ('2022-11-12T12:12:12', '2023-06-12T12:12:12', 2, 1, 'APPROVED');

INSERT INTO comments(text, item_id, author_id, created)
VALUES ('Comment A', 2, 1, '2023-09-12T12:12:12'),
       ('Comment B', 2, 1, '2023-09-16T12:12:12');
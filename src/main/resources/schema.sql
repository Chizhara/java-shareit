DROP TABLE IF EXISTS comments CASCADE;
DROP TABLE IF EXISTS bookings CASCADE;
DROP TABLE IF EXISTS items CASCADE;
DROP TABLE IF EXISTS requests CASCADE;
DROP TABLE IF EXISTS users CASCADE;

CREATE TABLE IF NOT EXISTS users
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(150) UNIQUE NOT NULL,
    name VARCHAR(90) NOT NULL
);

CREATE TABLE IF NOT EXISTS requests
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    description VARCHAR(120) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    requester_id BIGINT REFERENCES users(id) NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(40) NOT NULL,
    description VARCHAR(120) NOT NULL,
    available BOOLEAN NOT NULL,
    owner_id BIGINT REFERENCES users(id) NOT NULL,
    request_id BIGINT REFERENCES requests(id) NULL
);

CREATE TABLE IF NOT EXISTS bookings
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    start_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_date TIMESTAMP WITHOUT TIME ZONE NULL,
    item_id BIGINT REFERENCES items(id) NOT NULL,
    booker_id BIGINT REFERENCES users(id) NOT NULL,
    status VARCHAR(10) NOT NULL
);

CREATE TABLE IF NOT EXISTS comments
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    text VARCHAR(120) NOT NULL,
    item_id BIGINT REFERENCES items(id) NOT NULL,
    author_id BIGINT REFERENCES users(id) NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL
);
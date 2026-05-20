CREATE TABLE authorized_user
(
    id         BIGSERIAL    NOT NULL PRIMARY KEY,
    email      VARCHAR(255) NOT NULL UNIQUE,
    pfadi_name VARCHAR(255),
    role       VARCHAR(50)  NOT NULL
);

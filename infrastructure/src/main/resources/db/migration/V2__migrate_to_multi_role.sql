CREATE TABLE user_role (
    user_id BIGINT NOT NULL REFERENCES authorized_user(id) ON DELETE CASCADE,
    role    VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id, role)
);
INSERT INTO user_role (user_id, role) SELECT id, role FROM authorized_user WHERE role IS NOT NULL;
ALTER TABLE authorized_user ADD COLUMN profile_photo_url VARCHAR(512);
ALTER TABLE authorized_user DROP COLUMN role;

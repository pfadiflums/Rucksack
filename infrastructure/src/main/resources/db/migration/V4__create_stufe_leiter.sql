CREATE TABLE stufe_leiter (
    id BIGSERIAL PRIMARY KEY,
    stufe_id BIGINT NOT NULL REFERENCES stufe(id) ON DELETE CASCADE,
    authorized_user_id BIGINT NOT NULL REFERENCES authorized_user(id) ON DELETE CASCADE,
    sort_order INT NOT NULL DEFAULT 0,
    UNIQUE (stufe_id, authorized_user_id)
);

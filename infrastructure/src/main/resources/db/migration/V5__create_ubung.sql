CREATE TABLE ubung (
    id BIGSERIAL PRIMARY KEY,
    stufe_id BIGINT NOT NULL REFERENCES stufe(id) ON DELETE CASCADE,
    date DATE NOT NULL,
    antreten_time TIME NOT NULL,
    antreten_location VARCHAR(255) NOT NULL,
    abtreten_time TIME NOT NULL,
    abtreten_location VARCHAR(255) NOT NULL,
    motto VARCHAR(255),
    tenue TEXT,
    mitnehmen TEXT,
    weiteres TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    created_by_id BIGINT NOT NULL REFERENCES authorized_user(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now(),
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT now()
);

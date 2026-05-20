CREATE TABLE stufe (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    slug VARCHAR(50) NOT NULL UNIQUE,
    tagline VARCHAR(255),
    description TEXT,
    primary_color VARCHAR(7) NOT NULL,
    group_photo_url VARCHAR(512),
    google_calendar_iframe_url TEXT
);

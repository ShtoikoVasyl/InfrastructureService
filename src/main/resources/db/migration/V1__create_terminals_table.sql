CREATE TABLE terminals (
                           id BIGSERIAL PRIMARY KEY,
                           name VARCHAR(255) NOT NULL,
                           address VARCHAR(255),
                           password VARCHAR(255),
                           signature VARCHAR(255),
                           last_activity TIMESTAMP WITH TIME ZONE
);
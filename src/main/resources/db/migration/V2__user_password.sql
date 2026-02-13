ALTER TABLE users
ADD COLUMN password_hash TEXT;

ALTER TABLE users
ADD CONSTRAINT users_username_unique UNIQUE (username);
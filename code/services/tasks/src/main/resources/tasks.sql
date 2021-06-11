-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------

CREATE TABLE task (
    id SERIAL UNIQUE PRIMARY KEY NOT NULL,
    task_type_id INTEGER NOT NULL,
    task_status_id INTEGER NOT NULL,
    database_id INTEGER NOT NULL,
    issues INTEGER NOT NULL,
    resolved INTEGER NOT NULL,
    rejected INTEGER NOT NULL,
    note VARCHAR(250) NULL,
    last_updated BIGINT NOT NULL);

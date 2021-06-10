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
    last_updated INTEGER NOT NULL);

-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO task(task_type_id, task_status_id, database_id, issues, resolved, rejected, note, last_updated)
VALUES(1,1,1,1000,100,10,'Note 1', 1623348871);

INSERT INTO task(task_type_id, task_status_id, database_id, issues, resolved, rejected, note, last_updated)
VALUES(2,2,2,2000,200,20,'Note 2', 1623348872);

INSERT INTO task(task_type_id, task_status_id, database_id, issues, resolved, rejected, note, last_updated)
VALUES(3,3,3,3000,300,30,'Note 3', 1623348873);

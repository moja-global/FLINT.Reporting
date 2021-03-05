-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE date_dimension (
    date_dimension_id_pk SERIAL UNIQUE PRIMARY KEY NOT NULL,
    year INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO date_dimension(year) VALUES(1991);
INSERT INTO date_dimension(year) VALUES(1992);
INSERT INTO date_dimension(year) VALUES(1993);

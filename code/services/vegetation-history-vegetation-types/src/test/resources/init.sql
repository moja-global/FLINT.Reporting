-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE veghistory_vegtypeinfo_mapping (
    veghistory_vegtypeinfo_mapping_id_pk SERIAL UNIQUE PRIMARY KEY NOT NULL,
    veghistory_dimension_id_fk INTEGER NOT NULL,
    vegtypeinfo_dimension_id_fk INTEGER NOT NULL,
    itemnumber INTEGER NOT NULL,
    year INTEGER NOT NULL);

-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO veghistory_vegtypeinfo_mapping(veghistory_dimension_id_fk,vegtypeinfo_dimension_id_fk,itemnumber,year)
VALUES(1,1,1,1991);

INSERT INTO veghistory_vegtypeinfo_mapping(veghistory_dimension_id_fk,vegtypeinfo_dimension_id_fk,itemnumber,year)
VALUES(2,2,2,1992);

INSERT INTO veghistory_vegtypeinfo_mapping(veghistory_dimension_id_fk,vegtypeinfo_dimension_id_fk,itemnumber,year)
VALUES(3,3,3,1993);

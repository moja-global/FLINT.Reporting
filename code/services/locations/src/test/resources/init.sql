-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE location (
    location_dimension_id_pk SERIAL UNIQUE PRIMARY KEY NOT NULL,
    countyinfo_dimension_id_fk INTEGER NOT NULL,
    tileinfo_dimension_id_fk INTEGER NOT NULL,
    veghistory_dimension_id_fk INTEGER NOT NULL,
    unitcount INTEGER NOT NULL,
    unitareasum DOUBLE PRECISION NOT NULL);



-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO location(
location_dimension_id_pk,
countyinfo_dimension_id_fk,
tileinfo_dimension_id_fk,
veghistory_dimension_id_fk,
unitcount,
unitareasum)
VALUES(1,1,1,1,1,1.0);

INSERT INTO location(
location_dimension_id_pk,
countyinfo_dimension_id_fk,
tileinfo_dimension_id_fk,
veghistory_dimension_id_fk,
unitcount,
unitareasum)
VALUES(2,2,2,2,2,2.0);

INSERT INTO location(
location_dimension_id_pk,
countyinfo_dimension_id_fk,
tileinfo_dimension_id_fk,
veghistory_dimension_id_fk,
unitcount,
unitareasum)
VALUES(3,3,3,3,3,3.0);

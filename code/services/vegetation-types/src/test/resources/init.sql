-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE vegtypeinfo_dimension (
    vegtypeinfo_dimension_id_pk SERIAL UNIQUE PRIMARY KEY NOT NULL,
    ipcccovertypeinfo_dimension_id_fk INTEGER NOT NULL,
    vegtypename VARCHAR(250) NOT NULL,
    woodtype BOOLEAN NOT NULL,
    naturalsystem BOOLEAN NOT NULL);



-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO vegtypeinfo_dimension(ipcccovertypeinfo_dimension_id_fk,vegtypename,woodtype,naturalsystem) VALUES(1,'First Vegetation Type', FALSE,FALSE);
INSERT INTO vegtypeinfo_dimension(ipcccovertypeinfo_dimension_id_fk,vegtypename,woodtype,naturalsystem) VALUES(2,'Second Vegetation Type', TRUE,FALSE);
INSERT INTO vegtypeinfo_dimension(ipcccovertypeinfo_dimension_id_fk,vegtypename,woodtype,naturalsystem) VALUES(3,'Third Vegetation Type', TRUE,TRUE);

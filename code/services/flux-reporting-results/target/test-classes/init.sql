-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE flux_reporting_results (
    date_dimension_id_fk INTEGER NOT NULL,
    location_dimension_id_fk INTEGER NOT NULL,
    fluxtypeinfo_dimension_id_fk INTEGER NOT NULL,
    source_poolinfo_dimension_id_fk INTEGER NOT NULL,
    sink_poolinfo_dimension_id_fk INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO flux_reporting_results(
date_dimension_id_fk,
location_dimension_id_fk,
fluxtypeinfo_dimension_id_fk,
source_poolinfo_dimension_id_fk,
sink_poolinfo_dimension_id_fk)
VALUES(1,1,1,1,1);

INSERT INTO flux_reporting_results(
date_dimension_id_fk,
location_dimension_id_fk,
fluxtypeinfo_dimension_id_fk,
source_poolinfo_dimension_id_fk,
sink_poolinfo_dimension_id_fk)
VALUES(2,2,2,2,2);

INSERT INTO flux_reporting_results(
date_dimension_id_fk,
location_dimension_id_fk,
fluxtypeinfo_dimension_id_fk,
source_poolinfo_dimension_id_fk,
sink_poolinfo_dimension_id_fk)
VALUES(3,3,3,3,3);

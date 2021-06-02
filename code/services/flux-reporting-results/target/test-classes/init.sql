-- ------------------------------------------------------------
-- Create Table
-- ------------------------------------------------------------
CREATE TABLE flux_reporting_results (
    flux_reporting_results_id_pk INTEGER NOT NULL,
    date_dimension_id_fk INTEGER NOT NULL,
    location_dimension_id_fk INTEGER NOT NULL,
    fluxtypeinfo_dimension_id_fk INTEGER NOT NULL,
    source_poolinfo_dimension_id_fk INTEGER NOT NULL,
    sink_poolinfo_dimension_id_fk INTEGER NOT NULL,
    flux DOUBLE PRECISION NOT NULL,
    itemcount INTEGER NOT NULL);


-- ------------------------------------------------------------
-- Add Some dummy data
-- ------------------------------------------------------------

INSERT INTO flux_reporting_results(
flux_reporting_results_id_pk,
date_dimension_id_fk,
location_dimension_id_fk,
fluxtypeinfo_dimension_id_fk,
source_poolinfo_dimension_id_fk,
sink_poolinfo_dimension_id_fk,
flux,
itemcount)
VALUES(1,1,1,1,1,1,1.0,1);

INSERT INTO flux_reporting_results(
flux_reporting_results_id_pk,
date_dimension_id_fk,
location_dimension_id_fk,
fluxtypeinfo_dimension_id_fk,
source_poolinfo_dimension_id_fk,
sink_poolinfo_dimension_id_fk,
flux,
itemcount)
VALUES(2,2,2,2,2,2,2.0,2);

INSERT INTO flux_reporting_results(
flux_reporting_results_id_pk,
date_dimension_id_fk,
location_dimension_id_fk,
fluxtypeinfo_dimension_id_fk,
source_poolinfo_dimension_id_fk,
sink_poolinfo_dimension_id_fk,
flux,
itemcount)
VALUES(3,3,3,3,3,3,3.0,3);

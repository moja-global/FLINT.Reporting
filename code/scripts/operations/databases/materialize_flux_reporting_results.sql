-- View: public.materialized_flux_reporting_results 

-- DROP VIEW public.materialized_flux_reporting_results ;

CREATE MATERIALIZED VIEW public.materialized_flux_reporting_results AS 
 SELECT row_number() OVER () AS flux_reporting_results_id_pk,
    f.date_dimension_id_fk,
    f.location_dimension_id_fk,
    f.fluxtypeinfo_dimension_id_fk,
    f.source_poolinfo_dimension_id_fk,
    f.sink_poolinfo_dimension_id_fk,
    sum(f.flux) AS flux,
    sum(f.itemcount) AS itemcount
   FROM flux_reporting_results_withlocaldomain f
  GROUP BY f.date_dimension_id_fk, f.location_dimension_id_fk, f.fluxtypeinfo_dimension_id_fk, f.source_poolinfo_dimension_id_fk, f.sink_poolinfo_dimension_id_fk;

ALTER TABLE public.materialized_flux_reporting_results
  OWNER TO postgres;

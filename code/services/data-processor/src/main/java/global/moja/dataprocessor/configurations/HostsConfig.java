
/*
 * Copyright (C) 2020 The Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.configurations;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Configuration
@Getter
public class HostsConfig {

    @Value("${conversion.and.remaining.periods.service.host}/api/v1/conversion_and_remaining_periods")
    private String conversionAndRemainingPeriodsServiceHost;

    @Value("${cover.types.service.host}/api/v1/cover_types")
    private String coverTypesServiceHost;

    @Value("${databases.service.host}/api/v1/databases")
    private String databasesServiceHost;

    @Value("${dates.service.host}/api/v1/dates")
    private String datesServiceHost;

    @Value("${emission.types.service.host}/api/v1/emission_types")
    private String emissionTypesServiceHost;

    @Value("${flux.reporting.results.service.host}/api/v1/flux_reporting_results")
    private String fluxReportingResultsServiceHost;

    @Value("${fluxes.to.reporting.variables.service.host}/api/v1/fluxes_to_reporting_variables")
    private String fluxesToReportingVariablesServiceHost;

    @Value("${land.use.categories.service.host}/api/v1/land_use_categories")
    private String landUseCategoriesServiceHost;

    @Value("${land.uses.flux.types.service.host}/api/v1/land_uses_flux_types")
    private String landUsesFluxTypesServiceHost;

    @Value("${land.uses.flux.types.to.reporting.tables.service.host}/api/v1/land_uses_flux_types_to_reporting_tables")
    private String landUsesFluxTypesToReportingTablesServiceHost;

    @Value("${locations.service.host}/api/v1/locations")
    private String locationsServiceHost;

    @Value("${quantity.observations.service.host}/api/v1/quantity_observations")
    private String quantityObservationsServiceHost;

    @Value("${reporting.tables.service.host}/api/v1/reporting_tables")
    private String reportingTablesServiceHost;

    @Value("${vegetation.history.vegetation.types.service.host}/api/v1/vegetation_history_vegetation_types")
    private String vegetationHistoryVegetationTypesServiceHost;

    @Value("${vegetation.types.service.host}/api/v1/vegetation_types")
    private String vegetationTypesServiceHost;

}

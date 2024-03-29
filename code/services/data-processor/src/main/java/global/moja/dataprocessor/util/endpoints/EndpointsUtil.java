/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataprocessor.util.endpoints;

import global.moja.dataprocessor.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class EndpointsUtil {

    @Autowired
    ConversionAndRemainingPeriodsEndpointsUtil conversionAndRemainingPeriodsEndpointsUtil;

    @Autowired
    CoverTypesEndpointsUtil coverTypesEndpointsUtil;

    @Autowired
    DatabasesEndpointsUtil databasesEndpointsUtil;

    @Autowired
    DatesEndpointsUtil datesEndpointsUtil;

    @Autowired
    EmissionTypesEndpointsUtil emissionTypesEndpointsUtil;

    @Autowired
    FluxReportingResultsEndpointsUtil fluxReportingResultsEndpointsUtil;

    @Autowired
    FluxesToReportingVariablesEndpointsUtil fluxesToReportingVariablesEndpointsUtil;

    @Autowired
    LandUseCategoriesEndpointsUtil landUseCategoriesEndpointsUtil;

    @Autowired
    LandUsesFluxTypesEndpointsUtil landUsesFluxTypesEndpointsUtil;

    @Autowired
    LandUsesFluxTypesToReportingTablesEndpointsUtil landUsesFluxTypesToReportingTablesEndpointsUtil;

    @Autowired
    LocationsEndpointsUtil locationsEndpointsUtil;
    
    @Autowired
    QuantityObservationsEndpointUtil quantityObservationsEndpointUtil;

    @Autowired
    ReportingTablesEndpointsUtil reportingTablesEndpointsUtil;

    @Autowired
    VegetationHistoryVegetationTypesEndpointsUtil vegetationHistoryVegetationTypesEndpointsUtil;

    @Autowired
    VegetationTypesEndpointsUtil vegetationTypesEndpointsUtil;

    public Flux<Long> createQuantityObservations(QuantityObservation[] observations) {
        return quantityObservationsEndpointUtil
                .createQuantityObservations(observations);
    }

    public Flux<ConversionAndRemainingPeriod> retrieveConversionAndRemainingPeriods() {
        return conversionAndRemainingPeriodsEndpointsUtil
                .retrieveConversionAndRemainingPeriods();
    }

    public Flux<CoverType> retrieveCoverTypes() {
        return coverTypesEndpointsUtil
                .retrieveCoverTypes();
    }

    public Mono<Database> retrieveDatabase(Long databaseId) {
        return databasesEndpointsUtil
                .retrieveDatabase(databaseId);
    }

    public Flux<Database> retrieveDatabases() {
        return databasesEndpointsUtil
                .retrieveDatabases();
    }


    public Flux<Date> retrieveDates(Long databaseId) {
        return datesEndpointsUtil
                .retrieveDates(databaseId);
    }

    public Flux<EmissionType> retrieveEmissionTypes() {
        return emissionTypesEndpointsUtil
                .retrieveEmissionTypes();
    }

    public Flux<FluxReportingResult> retrieveFluxReportingResultsByLocation(Long databaseId, Long locationId) {
        return fluxReportingResultsEndpointsUtil
                .retrieveFluxReportingResultsByLocation(databaseId, locationId);
    }

    public Flux<FluxReportingResult> retrieveFluxReportingResultsByParty(Long databaseId, Long partyId) {
        return fluxReportingResultsEndpointsUtil
                .retrieveFluxReportingResultsByParty(databaseId, partyId);
    }

    public Flux<FluxToReportingVariable> retrieveFluxesToReportingVariables() {
        return fluxesToReportingVariablesEndpointsUtil
                .retrieveFluxesToReportingVariables();
    }

    public Flux<LandUseCategory> retrieveLandUseCategories() {
        return landUseCategoriesEndpointsUtil
                .retrieveLandUseCategories();
    }

    public Flux<LandUseFluxType> retrieveLandUsesFluxTypes() {
        return landUsesFluxTypesEndpointsUtil
                .retrieveLandUsesFluxTypes();
    }

    public Flux<LandUseFluxTypeToReportingTable> retrieveLandUsesFluxTypesToReportingTables() {
        return landUsesFluxTypesToReportingTablesEndpointsUtil
                .retrieveLandUsesFluxTypesToReportingTables();
    }

    public Flux<Location> retrieveLocations(Long databaseId, Long partyId) {
        return locationsEndpointsUtil
                .retrieveLocations(databaseId, partyId);
    }

    public Flux<ReportingTable> retrieveReportingTables() {
        return reportingTablesEndpointsUtil
                .retrieveReportingTables();
    }

    public Flux<VegetationHistoryVegetationType> retrieveVegetationHistoryVegetationTypes(
            Long databaseId, Long vegetationHistoryId) {
        return vegetationHistoryVegetationTypesEndpointsUtil
                .retrieveVegetationHistoryVegetationTypes(databaseId, vegetationHistoryId);
    }

    public Flux<VegetationHistoryVegetationType> retrieveVegetationHistoryVegetationTypesByPartyId(
            Long databaseId, Long partyId) {
        return vegetationHistoryVegetationTypesEndpointsUtil
                .retrieveVegetationHistoryVegetationTypesByPartyId(databaseId, partyId);
    }

    public Flux<VegetationType> retrieveVegetationTypes(Long databaseId) {
        return vegetationTypesEndpointsUtil
                .retrieveVegetationTypes(databaseId);
    }

}

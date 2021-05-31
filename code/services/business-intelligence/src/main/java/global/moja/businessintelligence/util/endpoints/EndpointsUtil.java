/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.businessintelligence.util.endpoints;

import global.moja.businessintelligence.models.*;
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
    private ConversionAndRemainingPeriodsEndpointsUtil conversionAndRemainingPeriodsEndpointsUtil;

    @Autowired
    private CoverTypesEndpointsUtil coverTypesEndpointsUtil;

    @Autowired
    private DatabasesEndpointsUtil databasesEndpointsUtil;

    @Autowired
    private LandUseCategoriesEndpointsUtil landUseCategoriesEndpointsUtil;

    @Autowired
    private LocationsEndpointsUtil locationsEndpointsUtil;

    @Autowired
    private ReportingTablesEndpointsUtil reportingTablesEndpointsUtil;

    @Autowired
    private VegetationHistoryVegetationTypesEndpointsUtil vegetationHistoryVegetationTypesEndpointsUtil;

    @Autowired
    private VegetationTypesEndpointsUtil vegetationTypesEndpointsUtil;


    public Flux<ConversionAndRemainingPeriod> retrieveConversionAndRemainingPeriods() {
        return conversionAndRemainingPeriodsEndpointsUtil
                .retrieveConversionAndRemainingPeriods();
    }

    public Flux<CoverType> retrieveCoverTypes() {
        return coverTypesEndpointsUtil
                .retrieveCoverTypes();
    }

    public Flux<Database> retrieveDatabases() {
        return databasesEndpointsUtil
                .retrieveDatabases();
    }

    public Flux<LandUseCategory> retrieveLandUseCategories(){
        return landUseCategoriesEndpointsUtil
                .retrieveLandUseCategories();
    }

    public Flux<ReportingTable> retrieveReportingTables() {
        return reportingTablesEndpointsUtil
                .retrieveReportingTables();
    }

    public Flux<Location> retrieveLocations(Long databaseId, Long partyId) {
        return locationsEndpointsUtil
                .retrieveLocations(databaseId, partyId);
    }

    public Flux<VegetationHistoryVegetationType> retrieveVegetationHistoryVegetationTypes(
            Long databaseId, Long vegetationHistoryId) {
        return vegetationHistoryVegetationTypesEndpointsUtil
                .retrieveVegetationHistoryVegetationTypes(databaseId, vegetationHistoryId);
    }

    public Flux<VegetationType> retrieveVegetationTypes(Long databaseId) {
        return vegetationTypesEndpointsUtil
                .retrieveVegetationTypes(databaseId);
    }

}

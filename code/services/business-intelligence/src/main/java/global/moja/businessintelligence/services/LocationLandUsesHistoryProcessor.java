
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.services;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.CoverTypesHistoricDetail;
import global.moja.businessintelligence.daos.LocationCoverTypesHistory;
import global.moja.businessintelligence.daos.LocationVegetationTypesHistory;
import global.moja.businessintelligence.daos.VegetationTypesHistoricDetail;
import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class LocationCoverTypesHistoryProcessor {

    @Autowired
    EndpointsUtil endpointsUtil;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    public Mono<LocationCoverTypesHistory> processLocationCoverTypesHistory(
            LocationVegetationTypesHistory locationVegetationTypesHistory) {

        log.trace("Entering processLocationCoverTypesHistory()");
        log.debug("Location Vegetation Types History = {}", locationVegetationTypesHistory);

        // Validate the Location Vegetation Types History
        log.trace("Validating the Location Vegetation Types History");
        if (locationVegetationTypesHistory == null) {
            log.error("The Location Vegetation Types History should not be null");
            return Mono.error(new ServerException("The Location Vegetation Types History should not be null"));
        }

        // Validate the Location Vegetation Types History's history
        log.trace("Validating the Location Vegetation Types History's history");
        if (locationVegetationTypesHistory.getHistory() == null) {
            log.error("The Location Vegetation Types History's history should not be null");
            return Mono.error(new ServerException("The Location Vegetation Types History's history should not be null"));
        }

        return

                // 1. Create a Flux from the Vegetation Types Historic Details
                Flux.fromIterable(locationVegetationTypesHistory.getHistory())

                        // 2. Convert each VegetationTypesHistoricDetail to the corresponding CoverTypesHistoricDetail
                        .map(vegetationTypesHistoricDetail ->
                                CoverTypesHistoricDetail
                                        .builder()
                                        .itemNumber(vegetationTypesHistoricDetail.getItemNumber())
                                        .year(vegetationTypesHistoricDetail.getYear())
                                        .coverType(
                                                configurationDataProvider
                                                        .getCoverType(
                                                                vegetationTypesHistoricDetail
                                                                        .getVegetationType()
                                                                        .getCoverTypeId()))
                                        .build())

                        // 3. Collect the Cover Types Historic Detail records
                        .collect(Collectors.toList())

                        // 4. Build and return the Location Cover Types History record
                        .map(records ->
                                LocationCoverTypesHistory
                                        .builder()
                                        .id(locationVegetationTypesHistory.getId())
                                        .partyId(locationVegetationTypesHistory.getPartyId())
                                        .tileId(locationVegetationTypesHistory.getTileId())
                                        .vegetationHistoryId(locationVegetationTypesHistory.getVegetationHistoryId())
                                        .unitCount(locationVegetationTypesHistory.getUnitCount())
                                        .unitAreaSum(locationVegetationTypesHistory.getUnitAreaSum())
                                        .history(records)
                                        .build());
    }


}

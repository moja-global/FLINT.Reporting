
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.services;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.LocationCoverTypesHistory;
import global.moja.businessintelligence.daos.LocationCoverTypesHistories;
import global.moja.businessintelligence.daos.LocationVegetationTypesHistories;
import global.moja.businessintelligence.exceptions.ServerException;
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
    ConfigurationDataProvider configurationDataProvider;

    public Mono<LocationCoverTypesHistories> processLocationCoverTypesHistories(
            LocationVegetationTypesHistories locationVegetationTypesHistories) {

        log.trace("Entering processLocationCoverTypesHistories()");
        log.debug("Location Vegetation Types Histories = {}", locationVegetationTypesHistories);

        // Validate the Location Vegetation Types Histories
        log.trace("Validating the Location Vegetation Types Histories");
        if (locationVegetationTypesHistories == null) {
            log.error("The Location Vegetation Types Histories should not be null");
            return Mono.error(new ServerException("The Location Vegetation Types Histories should not be null"));
        }

        // Validate the Location Vegetation Types Histories' histories
        log.trace("Validating the Location Vegetation Types Histories' histories");
        if (locationVegetationTypesHistories.getHistories() == null) {
            log.error("The Location Vegetation Types Histories' histories should not be null");
            return Mono.error(new ServerException("The Location Vegetation Types Histories' histories should not be null"));
        }

        return

                // 1. Create a Flux from the Vegetation Types Histories' histories
                Flux.fromIterable(locationVegetationTypesHistories.getHistories())

                        // 2. Filter out records with a null Vegetation Type
                        .filter(vegetationTypesHistoricDetail -> vegetationTypesHistoricDetail.getVegetationType() != null)

                        // 3. Convert the Vegetation Types Histories to the corresponding Cover Types Histories
                        .map(vegetationTypesHistoricDetail ->
                                LocationCoverTypesHistory
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

                        // 4. Collate the Cover Types Histories
                        .collect(Collectors.toList())

                        // 5. Build and return the Location Cover Types Histories record
                        .map(records ->
                                LocationCoverTypesHistories
                                        .builder()
                                        .locationId(locationVegetationTypesHistories.getLocationId())
                                        .partyId(locationVegetationTypesHistories.getPartyId())
                                        .tileId(locationVegetationTypesHistories.getTileId())
                                        .vegetationHistoryId(locationVegetationTypesHistories.getVegetationHistoryId())
                                        .unitCount(locationVegetationTypesHistories.getUnitCount())
                                        .unitAreaSum(locationVegetationTypesHistories.getUnitAreaSum())
                                        .histories(records)
                                        .build());
    }


}

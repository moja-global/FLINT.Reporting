
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.LocationCoverTypesHistory;
import global.moja.dataprocessor.daos.LocationCoverTypesHistories;
import global.moja.dataprocessor.daos.LocationVegetationTypesHistories;
import global.moja.dataprocessor.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationCoverTypesService {

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    public Mono<LocationCoverTypesHistories> getLocationCoverTypesHistories(
            LocationVegetationTypesHistories locationVegetationTypesHistories) {

        log.trace("Entering getLocationCoverTypesHistories()");
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

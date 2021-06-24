
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.LocationVegetationTypesHistories;
import global.moja.dataprocessor.daos.LocationVegetationTypesHistory;
import global.moja.dataprocessor.exceptions.ServerException;
import global.moja.dataprocessor.models.Location;
import global.moja.dataprocessor.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationVegetationTypesService {

    @Autowired
    EndpointsUtil endpointsUtil;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    private final String logMessagePrefix = "[Location Vegetation Types Service]";

    public Mono<LocationVegetationTypesHistories> getLocationVegetationTypesHistories
            (Long databaseId, Location location) {

        log.trace("{} - Entering getLocationVegetationTypesHistories()", logMessagePrefix);
        log.debug("{} - Database Id = {}", logMessagePrefix, databaseId);
        log.debug("{} - Location = {}", logMessagePrefix, location);

        // Validate the passed-in arguments
        log.trace("{} - Validating passed-in arguments", logMessagePrefix);

        if (databaseId == null || location == null || location.getId() == null
                || location.getVegetationHistoryId() == null) {

            // Create the error message
            String error =
                    databaseId == null ? "Database Id should not be null" :
                                    location == null ? "Location should not be null" :
                                            location.getId() == null ? "Location Id should not be null" :
                                                    "Vegetation History Id should not be null";

            // Throw the error
            return Mono.error(new ServerException(logMessagePrefix + " - " + error));

        }

        return
                endpointsUtil

                        // 1. Retrieve the Vegetation History Vegetation Types records corresponding to the provided
                        // Database and Location Vegetation History Ids
                        .retrieveVegetationHistoryVegetationTypes(
                                databaseId, location.getVegetationHistoryId())

                        .onErrorMap(e -> new ServerException(logMessagePrefix + " - Vegetation Histories retrieval failed", e))

                        // 2. Convert each Vegetation History Vegetation Type record to the corresponding
                        // Vegetation Types History record
                        .map(vegetationHistoryVegetationType ->
                                LocationVegetationTypesHistory
                                        .builder()
                                        .itemNumber(vegetationHistoryVegetationType.getItemNumber())
                                        .year(vegetationHistoryVegetationType.getYear())
                                        .vegetationType(
                                                configurationDataProvider
                                                        .getVegetationType(
                                                                databaseId,
                                                                vegetationHistoryVegetationType.getVegetationTypeId()))
                                        .build())

                        // 3. Collect the Vegetation Types History records
                        .collect(Collectors.toList())

                        // 4. Build and return the Location Vegetation Types History record
                        .map(records ->
                                LocationVegetationTypesHistories
                                        .builder()
                                        .locationId(location.getId())
                                        .partyId(location.getPartyId())
                                        .tileId(location.getTileId())
                                        .vegetationHistoryId(location.getVegetationHistoryId())
                                        .unitCount(location.getUnitCount())
                                        .unitAreaSum(location.getUnitAreaSum())
                                        .histories(records)
                                        .build());
    }

}

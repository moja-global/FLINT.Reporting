
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.services;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.LocationVegetationTypesHistory;
import global.moja.businessintelligence.daos.VegetationTypeHistoricDetail;
import global.moja.businessintelligence.models.Location;
import global.moja.businessintelligence.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class LocationCoverHistoryProcessor {

    @Autowired
    EndpointsUtil endpointsUtil;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    public Mono<LocationVegetationTypesHistory> process(Long databaseId, Location location) {

        log.trace("Entering Location Vegetation History Processor");

        log.debug("Database id = {}", databaseId);
        log.debug("Location = {}", location);

        // Validate the database id
        log.trace("Validating the database id");
        if (databaseId == null) {
            log.error("The database id should not be null");
            return Mono.empty();
        }

        // Validate the location
        log.trace("Validating the location");
        if (location == null) {
            log.error("The location should not be null");
            return Mono.empty();
        }

        // Validate the location's id
        log.trace("Validating the location's id");
        if (location.getId() == null) {
            log.error("The location's id should not be null");
            return Mono.empty();
        }

        // Validate the location's vegetation history id
        log.trace("Validating the location's vegetation history id");
        if (location.getVegetationHistoryId() == null) {
            log.error("The location's vegetation history id should not be null");
            return Mono.empty();
        }

        return

                endpointsUtil

                        // 1. Retrieve the Vegetation History Vegetation Types records corresponding to the provided
                        // Database Id and Location Vegetation History Id
                        .retrieveVegetationHistoryVegetationTypes(
                                databaseId,
                                location.getVegetationHistoryId())

                        // 2. Convert each Vegetation History Vegetation Type record to the corresponding
                        // Vegetation Type Historic Detail record
                        .map(vegetationHistoryVegetationType ->
                                VegetationTypeHistoricDetail
                                        .builder()
                                        .itemNumber(vegetationHistoryVegetationType.getItemNumber())
                                        .year(vegetationHistoryVegetationType.getYear())
                                        .vegetationType(
                                                configurationDataProvider
                                                        .getVegetationType(
                                                                databaseId,
                                                                vegetationHistoryVegetationType.getVegetationTypeId()))
                                        .build())

                        // 3. Collect the Vegetation Type Historic Detail records
                        .collect(Collectors.toList())

                        // 4. Build and return the Location Vegetation Types History record
                        .map(records ->
                                LocationVegetationTypesHistory
                                        .builder()
                                        .id(location.getId())
                                        .partyId(location.getPartyId())
                                        .tileId(location.getTileId())
                                        .vegetationHistoryId(location.getVegetationHistoryId())
                                        .unitCount(location.getUnitCount())
                                        .unitAreaSum(location.getUnitAreaSum())
                                        .history(records)
                                        .build());
    }


}

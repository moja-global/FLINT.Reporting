
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.services;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.LandUsesFluxesHistoricDetail;
import global.moja.businessintelligence.daos.LocationLandUsesFluxesHistory;
import global.moja.businessintelligence.daos.LocationLandUsesHistory;
import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.models.FluxReportingResult;
import global.moja.businessintelligence.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class LocationLandUsesFluxesHistoryProcessor {

    @Autowired
    EndpointsUtil endpointsUtil;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    public Mono<LocationLandUsesFluxesHistory> processLocationLandUsesFluxesHistory
            (Long databaseId, LocationLandUsesHistory locationLandUsesHistory) {

        log.trace("Entering processLocationLandUsesFluxesHistory()");
        log.debug("Database id = {}", databaseId);
        log.debug("Location Land Uses History = {}", locationLandUsesHistory);

        // Validate the database id
        log.trace("Validating the database id");
        if (databaseId == null) {
            log.error("The database id should not be null");
            return Mono.error(new ServerException("The database id should not be null"));
        }

        // Validate the Location Land Uses history
        log.trace("Validating the Location Land Uses history");
        if (locationLandUsesHistory == null) {
            log.error("The Location Land Uses history should not be null");
            return Mono.error(new ServerException("The Location Land Uses history should not be null"));
        }

        // Validate the Location Land Uses History's history
        log.trace("Validating the Location Land Uses History's history");
        if (locationLandUsesHistory.getHistory() == null) {
            log.error("The Location Land Uses History's history should not be null");
            return Mono.error(new ServerException("The Location Land Uses History's history should not be null"));
        }


        return

                endpointsUtil

                        // 1. Retrieve the Flux Reporting Results records corresponding to the provided
                        // Database Id and Location Id
                        .retrieveFluxReportingResults(
                                databaseId,
                                locationLandUsesHistory.getLocationId())


                        // 2. Collect and map the Flux Reporting Results by the Date Dimension
                        .collectMultimap(FluxReportingResult::getDateId, result -> result)


                        // 3. Convert each Land Use Historic Detail record to the corresponding
                        // Land Use Fluxes Historic Detail record
                        .flatMap(dateMappedFluxReportingResults ->

                                Flux.fromIterable(locationLandUsesHistory.getHistory())
                                        .map(landUsesHistoricDetail ->
                                                LandUsesFluxesHistoricDetail
                                                        .builder()
                                                        .itemNumber(landUsesHistoricDetail.getItemNumber())
                                                        .year(landUsesHistoricDetail.getYear())
                                                        .fluxes(
                                                                new ArrayList<>(
                                                                        dateMappedFluxReportingResults.get(
                                                                                configurationDataProvider
                                                                                        .getDateDimensionId(
                                                                                                databaseId,
                                                                                                landUsesHistoricDetail
                                                                                                        .getYear()))))
                                                        .build())
                                        .collectList()
                        )


                        // 4. Build and return the Location Vegetation Types History record
                        .map(records ->
                                LocationLandUsesFluxesHistory
                                        .builder()
                                        .locationId(locationLandUsesHistory.getLocationId())
                                        .partyId(locationLandUsesHistory.getPartyId())
                                        .tileId(locationLandUsesHistory.getTileId())
                                        .vegetationHistoryId(locationLandUsesHistory.getVegetationHistoryId())
                                        .unitCount(locationLandUsesHistory.getUnitCount())
                                        .unitAreaSum(locationLandUsesHistory.getUnitAreaSum())
                                        .history(records)
                                        .build());
    }


}

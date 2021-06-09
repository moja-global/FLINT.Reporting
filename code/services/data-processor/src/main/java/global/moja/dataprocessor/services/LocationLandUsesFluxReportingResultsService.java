
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.LocationLandUsesFluxReportingResultsHistories;
import global.moja.dataprocessor.daos.LocationLandUsesFluxReportingResultsHistory;
import global.moja.dataprocessor.daos.LocationLandUsesHistories;
import global.moja.dataprocessor.exceptions.ServerException;
import global.moja.dataprocessor.models.FluxReportingResult;
import global.moja.dataprocessor.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationLandUsesFluxReportingResultsService {

    @Autowired
    EndpointsUtil endpointsUtil;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    public Mono<LocationLandUsesFluxReportingResultsHistories> getLocationLandUsesFluxReportingResultsHistories
            (Long databaseId, LocationLandUsesHistories locationLandUsesHistories) {

        log.trace("Entering generateLocationLandUsesFluxesHistory()");
        log.debug("Database id = {}", databaseId);
        log.debug("Location Land Uses History = {}", locationLandUsesHistories);

        // Validate the database id
        log.trace("Validating the database id");
        if (databaseId == null) {
            log.error("The database id should not be null");
            return Mono.error(new ServerException("The database id should not be null"));
        }

        // Validate the Location Land Uses Histories
        log.trace("Validating the Location Land Uses Histories");
        if (locationLandUsesHistories == null) {
            log.error("The Location Land Uses Histories should not be null");
            return Mono.error(new ServerException("The Location Land Uses Histories should not be null"));
        }

        // Validate the Location Land Uses Histories' histories
        log.trace("Validating the Location Land Uses Histories' histories");
        if (locationLandUsesHistories.getHistories() == null) {
            log.error("The Location Land Uses Histories' histories should not be null");
            return Mono.error(new ServerException("The Location Land Uses Histories' histories should not be null"));
        }


        return

                endpointsUtil

                        // 1. Retrieve the Flux Reporting Results records corresponding to the provided
                        // Database and Location ids
                        .retrieveFluxReportingResults(
                                databaseId,
                                locationLandUsesHistories.getLocationId())


                        // 2. Collect and map the Flux Reporting Results by their Date Dimension Id
                        .collectMultimap(FluxReportingResult::getDateId, result -> result)


                        // 3. Convert each Land Uses History record to the corresponding
                        // Land Use Fluxes History record
                        .flatMap(dateMappedFluxReportingResults ->

                                Flux.fromIterable(locationLandUsesHistories.getHistories())
                                        .map(landUsesHistoricDetail ->
                                                LocationLandUsesFluxReportingResultsHistory
                                                        .builder()
                                                        .itemNumber(landUsesHistoricDetail.getItemNumber())
                                                        .year(landUsesHistoricDetail.getYear())
                                                        .landUseCategory(landUsesHistoricDetail.getLandUseCategory())
                                                        .confirmed(landUsesHistoricDetail.getConfirmed())
                                                        .fluxReportingResults(
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


                        // 4. Build and return the Location Vegetation Types Histories record
                        .map(records ->
                                LocationLandUsesFluxReportingResultsHistories
                                        .builder()
                                        .locationId(locationLandUsesHistories.getLocationId())
                                        .partyId(locationLandUsesHistories.getPartyId())
                                        .tileId(locationLandUsesHistories.getTileId())
                                        .vegetationHistoryId(locationLandUsesHistories.getVegetationHistoryId())
                                        .unitCount(locationLandUsesHistories.getUnitCount())
                                        .unitAreaSum(locationLandUsesHistories.getUnitAreaSum())
                                        .histories(records)
                                        .build());
    }


}

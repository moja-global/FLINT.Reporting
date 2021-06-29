
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
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

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

    private final String logMessagePrefix = "[Location Land Uses Flux Reporting Results Service]";

    public Mono<LocationLandUsesFluxReportingResultsHistories> getLocationLandUsesFluxReportingResultsHistories
            (Long databaseId, LocationLandUsesHistories locationLandUsesHistories) {


        log.trace("{} - Entering getLocationLandUsesFluxReportingResultsHistories()", logMessagePrefix);
        log.debug("{} - Database Id = {}", logMessagePrefix, databaseId);
        log.debug("{} - Location Land Uses Histories = {}", logMessagePrefix, locationLandUsesHistories);

        // Validate the passed-in arguments
        log.trace("{} - Validating passed-in arguments", logMessagePrefix);

        if (databaseId == null || locationLandUsesHistories == null || locationLandUsesHistories.getHistories() == null) {

            // Create the error message
            String error =
                    databaseId == null ? "Database Id should not be null" :
                            locationLandUsesHistories == null ? "Location Land Uses Histories should not be null" :
                                    "Location Land Uses Histories' histories should not be null";

            // Throw the error
            return Mono.error(new ServerException(logMessagePrefix + " - " + error));

        }


        return

                endpointsUtil

                        // 1. Retrieve the Flux Reporting Results records corresponding to the provided
                        // Database and Location ids
                        .retrieveFluxReportingResultsByLocation(
                                databaseId,
                                locationLandUsesHistories.getLocationId())

                        .onErrorMap(e -> new ServerException(logMessagePrefix + " - Flux Reporting Results retrieval failed", e))

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
                                                                dateMappedFluxReportingResults
                                                                        .keySet()
                                                                        .stream()
                                                                        .filter(dateDimensionId ->
                                                                                dateDimensionId ==
                                                                                        configurationDataProvider
                                                                                                .getDateDimensionId(
                                                                                                        databaseId,
                                                                                                        landUsesHistoricDetail
                                                                                                                .getYear()))
                                                                        .findAny()
                                                                        .map(dateDimensionId ->
                                                                                dateMappedFluxReportingResults
                                                                                        .get(dateDimensionId) == null ?
                                                                                        new ArrayList<FluxReportingResult>() :
                                                                                        new ArrayList<>(
                                                                                                dateMappedFluxReportingResults
                                                                                                        .get(dateDimensionId)))
                                                                        .orElse(new ArrayList<>()))
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


    Mono<LocationLandUsesFluxReportingResultsHistories> getLocationLandUsesFluxReportingResultsHistories(
            Long databaseId,
            LocationLandUsesHistories locationLandUsesHistories,
            Map<Long, Collection<FluxReportingResult>> fluxReportingResultMap) {

        return
                // 1. Retrieve the Flux Reporting Results records corresponding to the provided Database and Location id
                Flux.fromIterable(fluxReportingResultMap.getOrDefault(locationLandUsesHistories.getLocationId(), new ArrayList<>()))


                        // 2. Collect and map the Flux Reporting Results by their Date Dimension Id
                        .collectMultimap(FluxReportingResult::getDateId, result -> result)

                        .doOnNext(map -> {
                            log.info("Mapped Flux Reporting Results = {}", map);
                        })

                        // 3. Convert each Land Uses History record to the corresponding
                        // Land Use Fluxes History record
                        .flatMap(dateMappedFluxReportingResults ->

                                Flux.fromIterable(locationLandUsesHistories.getHistories())
                                        .doOnNext(l -> {
                                            log.info("Land Use Historic Detail Year = {}", l.getYear());
                                            log.info("Land Use Historic Detail Year Date Dimension = {}", configurationDataProvider
                                                    .getDateDimensionId(databaseId, l.getYear()));
                                        })
                                        .map(landUsesHistoricDetail ->

                                                LocationLandUsesFluxReportingResultsHistory
                                                        .builder()
                                                        .itemNumber(landUsesHistoricDetail.getItemNumber())
                                                        .year(landUsesHistoricDetail.getYear())
                                                        .landUseCategory(landUsesHistoricDetail.getLandUseCategory())
                                                        .confirmed(landUsesHistoricDetail.getConfirmed())
                                                        .fluxReportingResults(
                                                               new ArrayList<>(
                                                                       dateMappedFluxReportingResults.getOrDefault(
                                                                               dateMappedFluxReportingResults
                                                                                       .keySet()
                                                                                       .stream()
                                                                                       .filter(dateDimensionId ->
                                                                                               dateDimensionId ==
                                                                                                       configurationDataProvider
                                                                                                               .getDateDimensionId(
                                                                                                                       databaseId,
                                                                                                                       landUsesHistoricDetail
                                                                                                                               .getYear()))
                                                                                       .findAny()
                                                                                       .orElse(-1L),
                                                                               new ArrayList<FluxReportingResult>()
                                                                       )
                                                               )
                                                             )
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


/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.LocationLandUsesAllocatedFluxReportingResult;
import global.moja.dataprocessor.daos.LocationLandUsesAllocatedFluxReportingResults;
import global.moja.dataprocessor.daos.LocationLandUsesFluxReportingResultsHistories;
import global.moja.dataprocessor.exceptions.ServerException;
import global.moja.dataprocessor.util.FluxReportingResultsAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationLandUsesFluxReportingResultsAllocationService {

    @Autowired
    FluxReportingResultsAllocator fluxReportingResultsAllocator;

    private final String logMessagePrefix = "[Location Land Uses Flux Reporting Results Allocation Service]";

    public Mono<LocationLandUsesAllocatedFluxReportingResults> allocateLocationLandUsesFluxReportingResults
            (LocationLandUsesFluxReportingResultsHistories locationLandUsesFluxReportingResultsHistories) {

        log.trace("{} - Entering allocateLocationLandUsesFluxReportingResults()", logMessagePrefix);
        log.debug("Location Land Uses Flux Reporting Results Histories = {}", locationLandUsesFluxReportingResultsHistories);

        // Validate the passed-in arguments
        log.trace("{} - Validating passed-in arguments", logMessagePrefix);

        if (locationLandUsesFluxReportingResultsHistories == null ||
                locationLandUsesFluxReportingResultsHistories.getHistories() == null) {

            // Create the error message
            String error =
                    locationLandUsesFluxReportingResultsHistories == null ?
                            "Location Land Uses Flux Reporting Results Histories should not be null" :
                            "Location Land Uses Flux Reporting Results Histories' histories should not be null";

            // Throw the error
            return Mono.error(new ServerException(logMessagePrefix + " - " + error));

        }

        return

                // 1. Create a Flux from the Land Uses Flux Reporting Results Histories' histories
                Flux.fromIterable(locationLandUsesFluxReportingResultsHistories.getHistories())

                        .doOnNext(locationLandUsesFluxReportingResultsHistory -> {
                            log.info("");
                            log.info("Land Use Category = {}", locationLandUsesFluxReportingResultsHistory.getLandUseCategory());
                            log.info("Flux Reporting Results = {}", locationLandUsesFluxReportingResultsHistory.getFluxReportingResults());
                        })

                        // 2. Filter out records with a null Land Use Category
                        .filter(locationLandUsesFluxReportingResultsHistory ->
                                locationLandUsesFluxReportingResultsHistory.getLandUseCategory() != null &&
                                        locationLandUsesFluxReportingResultsHistory.getLandUseCategory().getId() != null)

                        // 3. Filter out records with a null or empty flux reporting results
                        .filter(locationLandUsesFluxReportingResultsHistory ->
                                locationLandUsesFluxReportingResultsHistory.getFluxReportingResults() != null &&
                                        !locationLandUsesFluxReportingResultsHistory.getFluxReportingResults().isEmpty())

                        // 4. Sort the records by item number
                        .sort()

                        // 5. Allocate the Flux Reporting Results
                        .flatMap(locationLandUsesFluxReportingResultsHistory ->

                                Flux.fromIterable(locationLandUsesFluxReportingResultsHistory.getFluxReportingResults())

                                        // 5.1. Filter out records without a Flux Type Id
                                        .filter(fluxReportingResult -> fluxReportingResult.getFluxTypeId() != null)

                                        // 5.2. Filter out records without a Source Pool Id
                                        .filter(fluxReportingResult -> fluxReportingResult.getSourcePoolId() != null)

                                        // 5.3. Filter out records without a Sink Pool Id
                                        .filter(fluxReportingResult -> fluxReportingResult.getSinkPoolId() != null)

                                        // 5.4. Sort the records by item number
                                        .sort()

                                        .doOnNext(fluxReportingResult -> {
                                            log.info("");
                                            log.info("Flux Reporting Result = {}", fluxReportingResult);
                                        })

                                        // 5.5. Allocate the Flux Reporting Results
                                        .map(fluxReportingResult ->
                                                fluxReportingResultsAllocator
                                                        .allocateFluxReportingResults(
                                                                locationLandUsesFluxReportingResultsHistory
                                                                        .getLandUseCategory()
                                                                        .getId(),
                                                                fluxReportingResult))

                                        .doOnNext(allocatedFluxReportingResults -> {
                                            log.info("Allocated Flux Reporting Results = {}", allocatedFluxReportingResults);
                                        })

                                        // 5.6. Collate the allocated Flux Reporting Results
                                        .collectList()

                                        // 5.7. Build the Location Land Uses Allocated Flux Reporting Result record
                                        .map(allocatedFluxReportingResults ->
                                                LocationLandUsesAllocatedFluxReportingResult
                                                        .builder()
                                                        .itemNumber(locationLandUsesFluxReportingResultsHistory.getItemNumber())
                                                        .year(locationLandUsesFluxReportingResultsHistory.getYear())
                                                        .landUseCategory(locationLandUsesFluxReportingResultsHistory.getLandUseCategory())
                                                        .confirmed(locationLandUsesFluxReportingResultsHistory.getConfirmed())
                                                        .allocations(
                                                                allocatedFluxReportingResults
                                                                        .stream()
                                                                        .flatMap(Collection::stream)
                                                                        .collect(Collectors.toList()))
                                                        .build()))

                        // 6. Collate the Location Land Uses Allocated Flux Reporting Result records
                        .collect(Collectors.toList())


                        // 7. Build and return the Location Land Uses Allocated Flux Reporting Results records
                        .map(records ->
                                LocationLandUsesAllocatedFluxReportingResults
                                        .builder()
                                        .locationId(locationLandUsesFluxReportingResultsHistories.getLocationId())
                                        .partyId(locationLandUsesFluxReportingResultsHistories.getPartyId())
                                        .tileId(locationLandUsesFluxReportingResultsHistories.getTileId())
                                        .vegetationHistoryId(locationLandUsesFluxReportingResultsHistories.getVegetationHistoryId())
                                        .unitCount(locationLandUsesFluxReportingResultsHistories.getUnitCount())
                                        .unitAreaSum(locationLandUsesFluxReportingResultsHistories.getUnitAreaSum())
                                        .allocations(records)
                                        .build());
    }


}


/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessing.services;

import global.moja.dataprocessing.configurations.ConfigurationDataProvider;
import global.moja.dataprocessing.daos.LocationLandUsesAllocatedFluxReportingResult;
import global.moja.dataprocessing.daos.LocationLandUsesAllocatedFluxReportingResults;
import global.moja.dataprocessing.daos.LocationLandUsesFluxReportingResultsHistories;
import global.moja.dataprocessing.exceptions.ServerException;
import global.moja.dataprocessing.util.FluxReportingResultsAllocator;
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
    ConfigurationDataProvider configurationDataProvider;

    @Autowired
    FluxReportingResultsAllocator fluxReportingResultsAllocator;

    public Mono<LocationLandUsesAllocatedFluxReportingResults> allocateLocationLandUsesFluxReportingResults
            (LocationLandUsesFluxReportingResultsHistories locationLandUsesFluxReportingResultsHistories) {

        log.trace("Entering allocateLocationLandUsesFluxReportingResults()");
        log.debug("Location Land Uses Flux Reporting Results Histories = {}", locationLandUsesFluxReportingResultsHistories);

        // Validate the Location Land Uses Flux Reporting Results Histories
        log.trace("Validating the Location Land Uses Flux Reporting Results Histories");
        if (locationLandUsesFluxReportingResultsHistories == null) {
            log.error("The Location Land Uses Flux Reporting Results Histories should not be null");
            return Mono.error(new ServerException("The Location Land Uses Flux Reporting Results Histories should not be null"));
        }

        // Validate the Location Land Uses Flux Reporting Results Histories' histories
        log.trace("Validating the Location Land Uses Flux Reporting Results Histories' histories");
        if (locationLandUsesFluxReportingResultsHistories.getHistories() == null) {
            log.error("The Location Land Uses Flux Reporting Results Histories' histories should not be null");
            return Mono.error(new ServerException("The Location Land Uses Flux Reporting Results Histories' histories should not be null"));
        }

        return

                // 1. Create a Flux from the Land Uses Flux Reporting Results Histories' histories
                Flux.fromIterable(locationLandUsesFluxReportingResultsHistories.getHistories())

                        // 2. Filter out records with a null Land Use Category
                        .filter(locationLandUsesFluxReportingResultsHistory -> locationLandUsesFluxReportingResultsHistory.getLandUseCategory() != null)

                        // 3. Filter out records with a null or empty flux reporting results
                        .filter(locationLandUsesFluxReportingResultsHistory ->
                                locationLandUsesFluxReportingResultsHistory.getFluxReportingResults() != null &&
                                        !locationLandUsesFluxReportingResultsHistory.getFluxReportingResults().isEmpty())

                        // 5. Sort the records by item number
                        .sort()

                        // 6. Allocate the Flux Reporting Results
                        .flatMap(locationLandUsesFluxReportingResultsHistory ->

                                Flux.fromIterable(locationLandUsesFluxReportingResultsHistory.getFluxReportingResults())

                                        // 6.1 Sort the records by item number
                                        .sort()

                                        // 6.2. Allocate the Flux Reporting Results
                                        .map(fluxReportingResult ->
                                                fluxReportingResultsAllocator
                                                        .allocateFluxReportingResults(
                                                                locationLandUsesFluxReportingResultsHistory
                                                                        .getLandUseCategory()
                                                                        .getId(),
                                                                fluxReportingResult))

                                        // 6.3. Collate the allocated Flux Reporting Results
                                        .collectList()

                                        // 6.4. Build the Location Land Uses Allocated Flux Reporting Result record
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
                                                        .build())
                        )

                        // 4. Collate the Location Land Uses Allocated Flux Reporting Result records
                        .collect(Collectors.toList())

                        // 5. Build and return the Location Land Uses Allocated Flux Reporting Results records
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

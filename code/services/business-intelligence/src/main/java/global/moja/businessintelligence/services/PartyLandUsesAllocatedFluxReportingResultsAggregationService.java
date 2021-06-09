
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.services;

import global.moja.businessintelligence.daos.Aggregation;
import global.moja.businessintelligence.daos.LocationLandUsesAllocatedFluxReportingResults;
import global.moja.businessintelligence.daos.LocationLandUsesAllocatedFluxReportingResultsAggregation;
import global.moja.businessintelligence.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationLandUsesAllocatedFluxReportingResultsAggregationService {

    public Mono<LocationLandUsesAllocatedFluxReportingResultsAggregation> aggregateLocationLandUsesAllocatedFluxReportingResults
            (LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults) {

        log.trace("Entering aggregateLocationLandUsesAllocatedFluxReportingResults()");
        log.debug("Location Land Uses Allocated Flux Reporting Results = {}",
                locationLandUsesAllocatedFluxReportingResults);

        // Validate the Location Land Uses Allocated Flux Reporting Results Results
        log.trace("Validating the Location Land Uses Allocated Flux Reporting Results Results");
        if (locationLandUsesAllocatedFluxReportingResults == null) {
            log.error("The Location Land Uses Allocated Flux Reporting Results should not be null");
            return Mono.error(
                    new ServerException("The Location Land Uses Allocated Flux Reporting Results should not be null"));
        }

        // Validate the Location Land Uses Allocated Flux Reporting Results' results
        log.trace("Validating the Location Land Uses Allocated Flux Reporting Results' results");
        if (locationLandUsesAllocatedFluxReportingResults.getAllocations() == null) {
            log.error("The Location Land Uses Allocated Flux Reporting Results' results should not be null");
            return Mono.error(
                    new ServerException("The Location Land Uses Allocated Flux Reporting Results' results should not be null"));
        }

        // Aggregate the Location Land Uses Allocated Flux Reporting Results' results
        log.trace("Aggregating the Location Land Uses Allocated Flux Reporting Results' results");
        final List<Aggregation> aggregations = new ArrayList<>();

        // 1. Stream the Location Land Uses Allocated Flux Reporting Results'
        locationLandUsesAllocatedFluxReportingResults
                .getAllocations()
                .stream()

                // 2. Filter out records with empty Flux Reporting Results Allocations
                .filter(locationLandUsesAllocatedFluxReportingResult ->
                        !locationLandUsesAllocatedFluxReportingResult.getAllocations().isEmpty())

                // 3. Sort the remaining records
                .sorted()

                // 4. Loop through the remaining records and carry out the aggregation task
                .forEach(locationLandUsesAllocatedFluxReportingResult ->

                        // 4.1. Create a steam of the allocations
                        locationLandUsesAllocatedFluxReportingResult.getAllocations()
                                .stream()

                                // 4.2. Sort the allocations
                                .sorted()

                                // 4.3. Aggregate
                                .forEach(allocation -> {

                                    // Get the previous aggregation - if it exists
                                    Aggregation previousAggregation =
                                            aggregations
                                                    .stream()
                                                    .filter(a -> a.getReportingTableId().equals(allocation.getReportingTableId()))
                                                    .filter(a -> a.getLandUseCategoryId().equals(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId()))
                                                    .filter(a -> a.getReportingVariableId().equals(allocation.getReportingVariableId()))
                                                    .filter(a -> a.getYear().equals(locationLandUsesAllocatedFluxReportingResult.getYear()))
                                                    .findAny()
                                                    .orElse(null);

                                    // Update the previous aggregation if it exists
                                    // Else create totally new aggregation record
                                    if (previousAggregation != null) {
                                        if (allocation.getFlux() != null) {
                                            previousAggregation.setAmount(previousAggregation.getAmount().add(new BigDecimal(allocation.getFlux())));
                                        }
                                    } else {
                                        aggregations.add(
                                                Aggregation
                                                        .builder()
                                                        .reportingTableId(allocation.getReportingTableId())
                                                        .landUseCategoryId(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId())
                                                        .reportingVariableId(allocation.getReportingVariableId())
                                                        .year(locationLandUsesAllocatedFluxReportingResult.getYear())
                                                        .amount(allocation.getFlux() == null ? BigDecimal.ZERO : new BigDecimal(allocation.getFlux()))
                                                        .build());
                                    }




                                }));


        // Sort the aggregations
        Collections.sort(aggregations);

        // Return the aggregated result
        return Mono.just(
                LocationLandUsesAllocatedFluxReportingResultsAggregation
                        .builder()
                        .locationId(locationLandUsesAllocatedFluxReportingResults.getLocationId())
                        .partyId(locationLandUsesAllocatedFluxReportingResults.getPartyId())
                        .tileId(locationLandUsesAllocatedFluxReportingResults.getTileId())
                        .vegetationHistoryId(locationLandUsesAllocatedFluxReportingResults.getVegetationHistoryId())
                        .unitCount(locationLandUsesAllocatedFluxReportingResults.getUnitCount())
                        .unitAreaSum(locationLandUsesAllocatedFluxReportingResults.getUnitAreaSum())
                        .aggregations(aggregations)
                        .build());


    }


}

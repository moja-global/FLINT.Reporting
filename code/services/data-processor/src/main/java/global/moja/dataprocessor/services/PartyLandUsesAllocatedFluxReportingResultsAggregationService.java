
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.daos.LocationLandUsesAllocatedFluxReportingResultsAggregation;
import global.moja.dataprocessor.exceptions.ServerException;
import global.moja.dataprocessor.models.QuantityObservation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class PartyLandUsesAllocatedFluxReportingResultsAggregationService {

    @Value("${area.reporting.variable.id}")
    Long AREA_REPORTING_VARIABLE;

    @Value("${kilohectares.unit.id}")
    Long KILOHECTARES_UNIT_ID;

    @Value("${kilotonnes.unit.id}")
    Long KILOTONNES_UNIT_ID;

    @Value("${processed.observation.id}")
    Long PROCESSED_OBSERVATION_ID;

    public Mono<List<QuantityObservation>> aggregateFluxReportingResultsAggregations(
            Long taskId,
            Long partyId,
            Long databaseId,
            List<LocationLandUsesAllocatedFluxReportingResultsAggregation> locationLandUsesAllocatedFluxReportingResultsAggregations) {

        log.trace("Entering aggregateFluxReportingResultsAggregations()");
        log.debug("Task id = {}", taskId);
        log.debug("Party id = {}", partyId);
        log.debug("Database id = {}", databaseId);
        log.debug("Location Land Uses Allocated Flux Reporting Results Aggregations = {}",
                locationLandUsesAllocatedFluxReportingResultsAggregations);

        // Validate the task id
        log.trace("Validating the task id");
        if (taskId == null) {
            log.error("The task id should not be null");
            return Mono.error(new ServerException("The task id should not be null"));
        }

        // Validate the party id
        log.trace("Validating the party id");
        if (partyId == null) {
            log.error("The party id should not be null");
            return Mono.error(new ServerException("The party id should not be null"));
        }

        // Validate the database id
        log.trace("Validating the database id");
        if (databaseId == null) {
            log.error("The database id should not be null");
            return Mono.error(new ServerException("The database id should not be null"));
        }

        // Validate the Location Land Uses Allocated Flux Reporting Results Aggregations
        log.trace("Validating the Location Land Uses Allocated Flux Reporting Results Aggregations");
        if (locationLandUsesAllocatedFluxReportingResultsAggregations == null) {
            log.error("The Location Land Uses Allocated Flux Reporting Results Aggregations Aggregations should not be null");
            return Mono.error(
                    new ServerException("The Location Land Uses Allocated Flux Reporting Results Aggregations Aggregations should not be null"));
        }


        // Aggregate the Location Land Uses Allocated Flux Reporting Results Aggregations
        log.trace("Aggregating the Location Land Uses Allocated Flux Reporting Results Aggregations");
        final List<QuantityObservation> observations = new ArrayList<>();

        // 1. Create a stream from the Location Land Uses Allocated Flux Reporting Results Aggregations
        locationLandUsesAllocatedFluxReportingResultsAggregations.stream()

                // 2. Sort the stream
                .sorted()

                // 3. Loop through the stream members and carry out the aggregations based on the nested aggregations
                .forEach(locationLandUsesAllocatedFluxReportingResultsAggregation ->

                        // 3.1. Create a stream from the nested Aggregation records
                        locationLandUsesAllocatedFluxReportingResultsAggregation.getAggregations().stream()

                                // 3.2. Sort the stream
                                .sorted()

                                // 3.3. Aggregate
                                .forEach(aggregation -> {

                                    // Get the previous quantity observation - if it exists
                                    QuantityObservation observation =
                                            observations
                                                    .stream()
                                                    .filter(a -> a.getReportingTableId().equals(aggregation.getReportingTableId()))
                                                    .filter(a -> a.getLandUseCategoryId().equals(aggregation.getLandUseCategoryId()))
                                                    .filter(a -> a.getReportingVariableId().equals(aggregation.getReportingVariableId()))
                                                    .filter(a -> a.getYear().equals(aggregation.getYear()))
                                                    .findAny()
                                                    .orElse(null);

                                    // Update the previous aggregation if it exists
                                    // Else create totally new aggregation record
                                    // TODO confirm with Jim / RDL
                                    // All of the data should be divided by 1000.
                                    // This will turn the current Hectares into Kilohectares and Tonnes in Kilotonnes
                                    if (observation != null) {
                                        if (aggregation.getAmount() != null) {
                                            observation.setAmount(observation.getAmount()
                                                    .add(aggregation.getAmount().divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP)));
                                        }
                                    } else {
                                        observations.add(
                                                QuantityObservation
                                                        .builder()
                                                        .observationTypeId(PROCESSED_OBSERVATION_ID)
                                                        .taskId(taskId)
                                                        .partyId(partyId)
                                                        .databaseId(databaseId)
                                                        .reportingTableId(aggregation.getReportingTableId())
                                                        .landUseCategoryId(aggregation.getLandUseCategoryId())
                                                        .reportingVariableId(aggregation.getReportingVariableId())
                                                        .year(aggregation.getYear())
                                                        .amount(aggregation.getAmount() == null ?
                                                                BigDecimal.ZERO :
                                                                aggregation.getAmount().divide(new BigDecimal(1000), 6, RoundingMode.HALF_UP))
                                                        .unitId(aggregation.getReportingVariableId().equals(AREA_REPORTING_VARIABLE) ? KILOHECTARES_UNIT_ID : KILOTONNES_UNIT_ID)
                                                        .build());
                                    }


                                }));


        // Return the aggregated result
        return Mono.just(observations);


    }


}

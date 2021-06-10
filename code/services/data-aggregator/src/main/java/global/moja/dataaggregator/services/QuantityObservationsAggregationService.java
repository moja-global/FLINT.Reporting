
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataaggregator.services;

import global.moja.dataaggregator.exceptions.ServerException;
import global.moja.dataaggregator.models.QuantityObservation;
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
public class QuantityObservationsAggregationService {


    @Value("${aggregated.observation.id}")
    Long AGGREGATED_OBSERVATION_ID;

    public Mono<List<QuantityObservation>> aggregateProcessedQuantityObservations(
            Long taskId,
            Long parentPartyId,
            List<QuantityObservation> processedObservations) {

        log.trace("Entering aggregateFluxReportingResultsAggregations()");
        log.debug("Task Id = {}", taskId);
        log.debug("Parent Party Id = {}", taskId);
        log.debug("Processed Quantity Observations = {}", processedObservations);

        // Validate the task id
        log.trace("Validating the task id");
        if (taskId == null) {
            log.error("The task id should not be null");
            return Mono.error(new ServerException("The task id should not be null"));
        }

        // Validate the Parent Party Id
        log.trace("Validating the Parent Party Id");
        if (parentPartyId == null) {
            log.error("The Parent Party Id should not be null");
            return Mono.error(new ServerException("The Parent Party Id should not be null"));
        }

        // Validate the Processed Quantity Observations
        log.trace("Validating the Quantity Observations");
        if (processedObservations == null) {
            log.error("The Processed Quantity Observations should not be null");
            return Mono.error(new ServerException("The Processed Quantity Observations should not be null"));
        }

        // Aggregate the Processed Quantity Observations
        log.trace("Aggregating the Processed Quantity Observations");
        final List<QuantityObservation> aggregatedObservations = new ArrayList<>();

        // 1. Create a stream from the Processed Quantity Observations
        processedObservations.stream()

                // 2. Sort the stream
                .sorted()

                // 3. Loop through the stream members and carry out the aggregations based on the nested aggregations
                .forEach(quantityObservation -> {

                    // Get the previous quantity observation - if it exists
                    QuantityObservation observation =
                            aggregatedObservations
                                    .stream()
                                    .filter(a -> a.getReportingTableId().equals(quantityObservation.getReportingTableId()))
                                    .filter(a -> a.getLandUseCategoryId().equals(quantityObservation.getLandUseCategoryId()))
                                    .filter(a -> a.getReportingVariableId().equals(quantityObservation.getReportingVariableId()))
                                    .filter(a -> a.getYear().equals(quantityObservation.getYear()))
                                    .findAny()
                                    .orElse(null);

                    // Update the previous aggregation if it exists
                    // Else create totally new aggregation record
                    if (observation != null) {
                        if (quantityObservation.getAmount() != null) {
                            observation.setAmount(observation.getAmount()
                                    .add(quantityObservation.getAmount()));
                        }
                    } else {
                        aggregatedObservations.add(
                                QuantityObservation
                                        .builder()
                                        .observationTypeId(AGGREGATED_OBSERVATION_ID)
                                        .taskId(taskId)
                                        .partyId(parentPartyId)
                                        .databaseId(quantityObservation.getDatabaseId())
                                        .reportingTableId(quantityObservation.getReportingTableId())
                                        .landUseCategoryId(quantityObservation.getLandUseCategoryId())
                                        .reportingVariableId(quantityObservation.getReportingVariableId())
                                        .year(quantityObservation.getYear())
                                        .amount(quantityObservation.getAmount() == null ?
                                                BigDecimal.ZERO : quantityObservation.getAmount())
                                        .unitId(quantityObservation.getUnitId())
                                        .build());
                    }
                });


        // Return the aggregated result
        return Mono.just(aggregatedObservations);


    }


}


/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.RabbitConfig;
import global.moja.dataprocessor.daos.*;
import global.moja.dataprocessor.models.Location;
import global.moja.dataprocessor.models.QuantityObservation;
import global.moja.dataprocessor.util.DataProcessingStatus;
import global.moja.dataprocessor.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static global.moja.dataprocessor.util.DataProcessingStatus.SUCCEEDED;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class DataProcessingService {


    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    LocationVegetationTypesService locationVegetationTypesService;

    @Autowired
    LocationCoverTypesService locationCoverTypesService;

    @Autowired
    LocationLandUsesCategoriesService locationLandUsesCategoriesService;

    @Autowired
    LocationLandUsesFluxReportingResultsService locationLandUsesFluxReportingResultsService;

    @Autowired
    LocationLandUsesFluxReportingResultsAllocationService locationLandUsesFluxReportingResultsAllocationService;

    @Autowired
    LocationLandUsesAllocatedFluxReportingResultsAggregationService locationLandUsesAllocatedFluxReportingResultsAggregationService;

    @Autowired
    PartyLandUsesAllocatedFluxReportingResultsAggregationService partyLandUsesAllocatedFluxReportingResultsAggregationService;

    @Autowired
    EndpointsUtil endpointsUtil;

    @RabbitListener(queues = RabbitConfig.RAW_DATA_PROCESSING_QUEUE)
    public void processData(final DataProcessingRequest request) {

        String prefix = "[Database: " + request.getDatabaseId() + ", Party: " + request.getPartyId() + "]";

        // Log entry message
        log.info("");
        log.info("========================================================================");
        log.info("{} - Entering  Data Processing Service", prefix);
        log.info("========================================================================");
        log.info("");


        // Validate the passed-in arguments
        log.trace("Validating passed-in arguments");
        if (request.getTaskId() == null || request.getDatabaseId() == null || request.getPartyId() == null) {

            // Publish an error status
            rabbitTemplate.convertAndSend(
                    RabbitConfig.RAW_DATA_PROCESSING_RESULTS_QUEUE,
                    DataProcessingResponse
                            .builder()
                            .taskId(request.getTaskId())
                            .databaseId(request.getDatabaseId())
                            .partyId(request.getPartyId())
                            .statusCode(DataProcessingStatus.FAILED.getId())
                            .build());


            // Log an error message and exit
            String error =
                    request.getTaskId() == null ? "Task Id should not be null" :
                            request.getDatabaseId() == null ? "Database Id should not be null" :
                                    "Party Id should not be null";

            log.info("");
            log.info("------------------------------------------------------------------------");
            log.error("{}", error);

        } else {

            // All the required inputs have been passed in
            log.trace("All the required inputs have been passed in");

            // Submit task for processing and get the resultant status code
            log.trace("Submitting task for processing and getting the resultant status code");
            DataProcessingStatus status =
                    endpointsUtil

                            // Retrieve the Locations associated with the Party
                            .retrieveLocations(request.getDatabaseId(), request.getPartyId())

                            .onErrorResume(e -> {
                                log.error("[Locations Retrieval Endpoint] - Locations Retrieval Failed", e);
                                return Mono.just(new Location());})

                            .filter(l -> l.getId() != null)

                            // Log out the location for debugging purposes
                            .doOnNext(location -> {
                                log.info("");
                                log.info("Database: {}, Party: {}, Location: {}", request.getDatabaseId(), request.getPartyId(), location.getId());
                                log.info("------------------------------------------------------------------------");
                                log.info("");
                            })

                            // Retrieve the Vegetation History Vegetation Types records for each location
                            .flatMap(location ->
                                    locationVegetationTypesService
                                            .getLocationVegetationTypesHistories(request.getDatabaseId(), location))

                            .onErrorResume(e -> {
                                log.error(e.getMessage(), e);
                                return Mono.just(new LocationVegetationTypesHistories());})

                            .filter(l -> l.getLocationId() != null)

                            // Convert the Vegetation Types Histories to Cover Types Histories
                            .flatMap(locationVegetationTypesHistories ->
                                    locationCoverTypesService
                                            .getLocationCoverTypesHistories(
                                                    locationVegetationTypesHistories))

                            .onErrorResume(e -> {
                                log.error(e.getMessage(), e);
                                return Mono.just(new LocationCoverTypesHistories());})

                            .filter(l -> l.getLocationId() != null)

                            // Convert Cover Types Histories to Land Use Histories
                            .flatMap(locationCoverTypesHistories ->
                                    locationLandUsesCategoriesService
                                            .getLocationLandUsesCategoriesHistories(
                                                    locationCoverTypesHistories))

                            .onErrorResume(e -> {
                                log.error(e.getMessage(), e);
                                return Mono.just(new LocationLandUsesHistories()); })

                            .filter(l -> l.getLocationId() != null)

                            // Append the Location's Flux Reporting Results
                            .flatMap(locationLandUsesHistories ->
                                    locationLandUsesFluxReportingResultsService
                                            .getLocationLandUsesFluxReportingResultsHistories(
                                                    request.getDatabaseId(), locationLandUsesHistories))
                            .onErrorResume(e -> {
                                log.error(e.getMessage(), e);
                                return Mono.just(new LocationLandUsesFluxReportingResultsHistories());
                            })

                            .filter(l -> l.getLocationId() != null)

                            // Allocate the Location's Flux Reporting Results
                            .flatMap(locationLandUsesFluxReportingResultsHistories ->
                                    locationLandUsesFluxReportingResultsAllocationService
                                            .allocateLocationLandUsesFluxReportingResults(
                                                    locationLandUsesFluxReportingResultsHistories))
                            .onErrorResume(e -> {
                                log.error(e.getMessage(), e);
                                return Mono.just(new LocationLandUsesAllocatedFluxReportingResults());})

                            .filter(l -> l.getLocationId() != null)

                            // Aggregated the allocated the Location's Flux Reporting Results
                            .flatMap(allocatedFluxReportingResults ->
                                    locationLandUsesAllocatedFluxReportingResultsAggregationService
                                            .aggregateLocationLandUsesAllocatedFluxReportingResults(
                                                    allocatedFluxReportingResults))
                            .onErrorResume(e -> {
                                log.error(e.getMessage(), e);
                                return Mono.just(new LocationLandUsesAllocatedFluxReportingResultsAggregation());})

                            .filter(l -> l.getLocationId() != null)

                            // Collect the aggregated Flux Reporting Results
                            .collectList()

                            // Log for debugging purpose
                            .doOnNext(location -> {
                                log.info("");
                                log.info("Database: {}, Party: {}, Location: All", request.getDatabaseId(), request.getPartyId());
                                log.info("------------------------------------------------------------------------");
                                log.info("");
                            })

                            // Aggregate the location results
                            .flatMap(locationLandUsesAllocatedFluxReportingResultsAggregations ->
                                    partyLandUsesAllocatedFluxReportingResultsAggregationService
                                            .aggregateFluxReportingResultsAggregations(
                                                    request.getTaskId(),
                                                    request.getPartyId(),
                                                    request.getDatabaseId(),
                                                    locationLandUsesAllocatedFluxReportingResultsAggregations))
                            .doOnError(e -> {
                                log.error(e.getMessage(), e);
                            })

                            // Save the aggregated results
                            .flatMap(quantityObservations ->
                                    endpointsUtil
                                            .createQuantityObservations(quantityObservations.toArray(QuantityObservation[]::new))
                                            .collectList())
                            .doOnError(e -> {
                                log.error("[Quantity Observations Endpoint] - Aggregated Quantity Observations Saving Failed", e);
                                log.error(e.getMessage(), e);
                            })

                            .map(ids -> SUCCEEDED)
                            .onErrorReturn(DataProcessingStatus.FAILED)
                            .block();

            log.info("Processing Status Code = {}", status.getId());

            // Publish the processing status
            log.trace("Publishing the processing status");
            rabbitTemplate.convertAndSend(
                    RabbitConfig.RAW_DATA_PROCESSING_RESULTS_QUEUE,
                    DataProcessingResponse
                            .builder()
                            .taskId(request.getTaskId())
                            .databaseId(request.getDatabaseId())
                            .partyId(request.getPartyId())
                            .statusCode(status.getId())
                            .build());


            // Log Exit Message
            log.info("");
            log.info("------------------------------------------------------------------------");
            if(status == SUCCEEDED) {
                log.info("Data Processing Succeeded");
            } else {
                log.error("Data Processing Failed");
            }


        }

        log.info("------------------------------------------------------------------------");
        log.info("");
        log.info("========================================================================");
        log.info("{} - Leaving  Data Processing Service", prefix);
        log.info("========================================================================");
        log.info("");


    }
}

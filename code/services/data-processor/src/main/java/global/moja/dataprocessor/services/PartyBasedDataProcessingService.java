
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
import global.moja.dataprocessor.models.FluxReportingResult;
import global.moja.dataprocessor.models.Location;
import global.moja.dataprocessor.models.QuantityObservation;
import global.moja.dataprocessor.models.VegetationHistoryVegetationType;
import global.moja.dataprocessor.util.DataProcessingStatus;
import global.moja.dataprocessor.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static global.moja.dataprocessor.util.DataProcessingStatus.SUCCEEDED;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class PartyBasedDataProcessingService {


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

            final Map<Long, Collection<FluxReportingResult>> fluxReportingResultMap = new HashMap<>();
            final Map<Long, Collection<VegetationHistoryVegetationType>> vegetationHistoryVegetationTypeMap = new HashMap<>();


            DataProcessingStatus status =

                    // Retrieve the Flux Reporting Results associated with the Party

                    endpointsUtil
                            .retrieveFluxReportingResultsByParty(request.getDatabaseId(), request.getPartyId())
                            .doOnError(e -> log.error("[Locations Retrieval Endpoint] - Locations Retrieval Failed", e))
                            .onErrorReturn(new FluxReportingResult())
                            .filter(f -> f.getId() != null)
                            .collectMultimap(FluxReportingResult::getLocationId, result -> result)
                            .doOnNext(fluxReportingResultMap::putAll)


                            // Retrieve the Vegetation-History-Vegetation-Types associated with the Party
                            .flatMap(map ->
                                    endpointsUtil
                                            .retrieveVegetationHistoryVegetationTypesByPartyId(request.getDatabaseId(), request.getPartyId())
                                            .doOnError(e -> log.error("[Vegetation History Vegetation Types Endpoint] - Vegetation History Vegetation Types Retrieval Failed", e))
                                            .onErrorReturn(new VegetationHistoryVegetationType())
                                            .filter(v -> v.getId() != null)
                                            .collectMultimap(VegetationHistoryVegetationType::getVegetationHistoryId, result -> result))
                            .doOnNext(vegetationHistoryVegetationTypeMap::putAll)

                            .flatMap(map ->
                                    endpointsUtil

                                            // Retrieve the Locations associated with the Party
                                            .retrieveLocations(request.getDatabaseId(), request.getPartyId())
                                            .doOnError(e -> log.error("[Flux Reporting Results Retrieval Endpoint] - Flux Reporting Results Retrieval Failed", e))
                                            .onErrorReturn(new Location())
                                            .filter(l -> l.getId() != null)
                                            .doOnNext(location -> {
                                                log.info("");
                                                log.info("Database: {}, Party: {}, Location: {}", request.getDatabaseId(), request.getPartyId(), location.getId());
                                                log.info("------------------------------------------------------------------------");
                                                log.info("{} Location Flux Reporting Results Present", fluxReportingResultMap.get(location.getId()) == null ? 0 : fluxReportingResultMap.get(location.getId()).size());
                                                log.info("{} Vegetation Types Histories Present", vegetationHistoryVegetationTypeMap.get(location.getId()) == null ? 0 : vegetationHistoryVegetationTypeMap.get(location.getId()).size());
                                                log.info("");
                                            })

                                            // Retrieve the Vegetation History Vegetation Types records for each location
                                            .flatMap(location ->
                                                    locationVegetationTypesService
                                                            .getLocationVegetationTypesHistories(
                                                                    request.getDatabaseId(),
                                                                    location,
                                                                    vegetationHistoryVegetationTypeMap))
                                            .doOnError(e -> log.error(e.getMessage(), e))
                                            .onErrorReturn(new LocationVegetationTypesHistories())
                                            .filter(l -> l.getLocationId() != null)
                                            .filter(l -> l.getHistories() != null)
                                            .filter(l -> !l.getHistories().isEmpty())

                                            .doOnNext(l -> log.info("Processed {} Vegetation Types Histories for the location", l.getHistories().size()))

                                            // Convert the Vegetation Types Histories to Cover Types Histories
                                            .flatMap(locationVegetationTypesHistories ->
                                                    locationCoverTypesService
                                                            .getLocationCoverTypesHistories(
                                                                    locationVegetationTypesHistories))
                                            .doOnError(e -> log.error(e.getMessage(), e))
                                            .onErrorReturn(new LocationCoverTypesHistories())
                                            .filter(l -> l.getLocationId() != null)
                                            .filter(l -> l.getHistories() != null)
                                            .filter(l -> !l.getHistories().isEmpty())

                                            .doOnNext(l -> log.info("Processed {} Cover Types Histories for the location", l.getHistories().size()))

                                            // Convert Cover Types Histories to Land Use Histories
                                            .flatMap(locationCoverTypesHistories ->
                                                    locationLandUsesCategoriesService
                                                            .getLocationLandUsesCategoriesHistories(
                                                                    locationCoverTypesHistories))
                                            .doOnError(e -> log.error(e.getMessage(), e))
                                            .onErrorReturn(new LocationLandUsesHistories())
                                            .filter(l -> l.getLocationId() != null)
                                            .filter(l -> l.getHistories() != null)
                                            .filter(l -> !l.getHistories().isEmpty())

                                            .doOnNext(l -> log.info("Processed {} Land Uses Histories for the location", l.getHistories().size()))

                                            // Append the Location's Flux Reporting Results
                                            .flatMap(locationLandUsesHistories ->
                                                    locationLandUsesFluxReportingResultsService
                                                            .getLocationLandUsesFluxReportingResultsHistories(
                                                                    request.getDatabaseId(),
                                                                    locationLandUsesHistories,
                                                                    fluxReportingResultMap))
                                            .doOnError(e -> log.error(e.getMessage(), e))
                                            .onErrorReturn(new LocationLandUsesFluxReportingResultsHistories())
                                            .filter(l -> l.getLocationId() != null)
                                            .filter(l -> l.getHistories() != null)
                                            .filter(l -> !l.getHistories().isEmpty())

                                            .doOnNext(l -> log.info("Processed {} Land Uses Flux Reporting Results Histories for the location", l.getHistories().size()))

                                            // Allocate the Location's Flux Reporting Results
                                            .flatMap(locationLandUsesFluxReportingResultsHistories ->
                                                    locationLandUsesFluxReportingResultsAllocationService
                                                            .allocateLocationLandUsesFluxReportingResults(
                                                                    locationLandUsesFluxReportingResultsHistories))
                                            .doOnError(e -> log.error(e.getMessage(), e))
                                            .onErrorReturn(new LocationLandUsesAllocatedFluxReportingResults())
                                            .filter(l -> l.getLocationId() != null)
                                            .filter(l -> l.getAllocations() != null)
                                            .filter(l -> !l.getAllocations().isEmpty())

                                            .doOnNext(l -> log.info("Processed {} Allocations for the location", l.getAllocations().size()))

                                            // Aggregated the allocated the Location's Flux Reporting Results
                                            .flatMap(allocatedFluxReportingResults ->
                                                    locationLandUsesAllocatedFluxReportingResultsAggregationService
                                                            .aggregateLocationLandUsesAllocatedFluxReportingResults(
                                                                    allocatedFluxReportingResults))
                                            .doOnError(e -> log.error(e.getMessage(), e))
                                            .onErrorReturn(new LocationLandUsesAllocatedFluxReportingResultsAggregation())
                                            .filter(l -> l.getLocationId() != null)
                                            .filter(l -> l.getAggregations() != null)
                                            .filter(l -> !l.getAggregations().isEmpty())

                                            .doOnNext(l -> log.info("Processed {} Aggregations for the location", l.getAggregations().size()))


                                            // Collect the aggregated Flux Reporting Results
                                            .collectList()

                                            // Log for debugging purpose
                                            .doOnNext(locationLandUsesAllocatedFluxReportingResultsAggregations -> {
                                                log.info("");
                                                log.info("Database: {}, Party: {}, Location: All", request.getDatabaseId(), request.getPartyId());
                                                log.info("------------------------------------------------------------------------");
                                                log.info("Aggregating {} locations aggregates", locationLandUsesAllocatedFluxReportingResultsAggregations.size());
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
                                            .doOnError(e -> log.error(e.getMessage(), e))
                                            .doOnNext(l -> log.info("Aggregations = {}", l))


                                            // Save the aggregated results
                                            .flatMap(quantityObservations ->
                                                    endpointsUtil
                                                            .createQuantityObservations(quantityObservations.toArray(QuantityObservation[]::new))
                                                            .collectList())
                                            .doOnError(e -> {
                                                log.error("[Quantity Observations Endpoint] - Aggregated Quantity Observations Saving Failed", e);
                                            })

                                            .map(ids -> SUCCEEDED)
                                            .onErrorReturn(DataProcessingStatus.FAILED))
                            .block();

            log.info("Processing Status Code = {}", status != null ? status.getId() : null);

            // Publish the processing status
            log.trace("Publishing the processing status");
            rabbitTemplate.convertAndSend(
                    RabbitConfig.RAW_DATA_PROCESSING_RESULTS_QUEUE,
                    DataProcessingResponse
                            .builder()
                            .taskId(request.getTaskId())
                            .databaseId(request.getDatabaseId())
                            .partyId(request.getPartyId())
                            .statusCode(status != null ? status.getId() : null)
                            .build());


            // Log Exit Message
            log.info("");
            log.info("------------------------------------------------------------------------");
            if (status == SUCCEEDED) {
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

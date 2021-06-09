
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.RabbitConfig;
import global.moja.dataprocessor.daos.DataProcessingRequest;
import global.moja.dataprocessor.daos.DataProcessingResponse;
import global.moja.dataprocessor.models.QuantityObservation;
import global.moja.dataprocessor.util.DataProcessingStatus;
import global.moja.dataprocessor.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    LocationLandUsesService locationLandUsesService;

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

        log.info("[Database: {}, Party: {}] - Data Processing Commencement",
                request.getDatabaseId(), request.getPartyId());


        // Ascertain that all the required fields have been passed in
        if (request.getTaskId() == null || request.getDatabaseId() == null || request.getPartyId() == null) {

            log.info("[Database: {}, Party: {}] - Data Processing Failed",
                    request.getDatabaseId(), request.getPartyId());

            log.error(
                    request.getTaskId() == null ?
                            "[Database: {}, Party: {}] - Task Id should not be null" :
                            request.getDatabaseId() == null ?
                                    "[Database: {}, Party: {}] - Database Id should not be null" :
                                    "[Database: {}, Party: {}] - Party Id should not be null",
                    request.getDatabaseId(), request.getPartyId());

            rabbitTemplate.convertAndSend(
                    RabbitConfig.RAW_DATA_PROCESSING_RESULTS_QUEUE,
                    DataProcessingResponse
                            .builder()
                            .taskId(request.getTaskId())
                            .databaseId(request.getDatabaseId())
                            .partyId(request.getPartyId())
                            .statusCode(DataProcessingStatus.FAILED.getId())
                            .build());

        } else {

            // Process and get the processing status code
            DataProcessingStatus dataProcessingStatus =
                    endpointsUtil
                            .retrieveLocations(request.getDatabaseId(), request.getPartyId())
                            .flatMap(location ->
                                    locationVegetationTypesService
                                            .getLocationVegetationTypesHistories(
                                                    request.getDatabaseId(), location))
                            .flatMap(locationVegetationTypesHistories ->
                                    locationCoverTypesService
                                            .getLocationCoverTypesHistories(
                                                    locationVegetationTypesHistories))
                            .flatMap(locationCoverTypesHistories ->
                                    locationLandUsesService
                                            .getLocationLandUsesHistories(
                                                    locationCoverTypesHistories))
                            .flatMap(locationLandUsesHistories ->
                                    locationLandUsesFluxReportingResultsService
                                            .getLocationLandUsesFluxReportingResultsHistories(
                                                    request.getDatabaseId(), locationLandUsesHistories))
                            .flatMap(locationLandUsesFluxReportingResultsHistories ->
                                    locationLandUsesFluxReportingResultsAllocationService
                                            .allocateLocationLandUsesFluxReportingResults(
                                                    locationLandUsesFluxReportingResultsHistories))
                            .flatMap(allocatedFluxReportingResults ->
                                    locationLandUsesAllocatedFluxReportingResultsAggregationService
                                            .aggregateLocationLandUsesAllocatedFluxReportingResults(
                                                    allocatedFluxReportingResults))
                            .collectList()
                            .flatMap(locationLandUsesAllocatedFluxReportingResultsAggregations ->
                                    partyLandUsesAllocatedFluxReportingResultsAggregationService
                                            .aggregateFluxReportingResultsAggregations(
                                                    request.getPartyId(),
                                                    request.getDatabaseId(),
                                                    locationLandUsesAllocatedFluxReportingResultsAggregations))
                            .flatMap(quantityObservations -> endpointsUtil.createQuantityObservations(quantityObservations.toArray(QuantityObservation[]::new)))
                            .map(ids -> DataProcessingStatus.SUCCEEDED)
                            .onErrorReturn(DataProcessingStatus.FAILED)
                            .block();


            log.info(
                    dataProcessingStatus == DataProcessingStatus.SUCCEEDED ?
                            "[Database: {}, Party: {}] - Data Processing Succeeded" :
                            "[Database: {}, Party: {}] - Data Processing Failed",
                    request.getDatabaseId(), request.getPartyId());


            // Publish the processing status
            rabbitTemplate.convertAndSend(
                    RabbitConfig.RAW_DATA_PROCESSING_RESULTS_QUEUE,
                    DataProcessingResponse
                            .builder()
                            .taskId(request.getTaskId())
                            .databaseId(request.getDatabaseId())
                            .partyId(request.getPartyId())
                            .statusCode(dataProcessingStatus.getId())
                            .build());


        }


    }
}

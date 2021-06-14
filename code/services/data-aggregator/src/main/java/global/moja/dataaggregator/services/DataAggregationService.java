
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataaggregator.services;

import global.moja.dataaggregator.configurations.RabbitConfig;
import global.moja.dataaggregator.daos.DataAggregationRequest;
import global.moja.dataaggregator.daos.DataAggregationResponse;
import global.moja.dataaggregator.models.QuantityObservation;
import global.moja.dataaggregator.util.DataAggregationStatus;
import global.moja.dataaggregator.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class DataAggregationService {

    @Value("${processed.observation.id}")
    Long PROCESSED_OBSERVATION_ID;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    QuantityObservationsAggregationService quantityObservationsAggregationService;

    @Autowired
    EndpointsUtil endpointsUtil;

    @RabbitListener(queues = RabbitConfig.PROCESSED_DATA_AGGREGATION_QUEUE)
    public void processData(final DataAggregationRequest request) {

        log.info("[Database: {}, Parent Party: {}] - Data Aggregation Commencement",
                request.getDatabaseId(), request.getParentPartyId());


        // Ascertain that all the required fields have been passed in
        if (request.getTaskId() == null ||
                request.getDatabaseId() == null ||
                request.getParentPartyId() == null ||
                request.getAccountabilityTypeId() == null) {

            log.info("[Database: {}, Parent Party: {}] - Data Aggregation Failed",
                    request.getDatabaseId(), request.getParentPartyId());

            log.error(
                    request.getTaskId() == null ?
                            "[Database: {}, Parent Party: {}] - Task Id should not be null" :
                            request.getDatabaseId() == null ?
                                    "[Database: {}, Parent Party: {}] - Database Id should not be null" :
                                    request.getParentPartyId() == null ?
                                            "[Database: {}, Parent Party: {}] - Parent Party Id should not be null" :
                                            "[Database: {}, Parent Party: {}] - Accountability Type Id should not be null",
                    request.getDatabaseId(), request.getParentPartyId());

            rabbitTemplate.convertAndSend(
                    RabbitConfig.PROCESSED_DATA_AGGREGATION_RESULTS_QUEUE,
                    DataAggregationResponse
                            .builder()
                            .taskId(request.getTaskId())
                            .databaseId(request.getDatabaseId())
                            .parentPartyId(request.getParentPartyId())
                            .statusCode(DataAggregationStatus.FAILED.getId())
                            .build());

        } else {

            // Process and get the processing status code
            DataAggregationStatus dataAggregationStatus = endpointsUtil
                    .retrieveAccountabilities(
                            request.getAccountabilityTypeId(),
                            request.getParentPartyId())
                    .map(a -> a.getSubsidiaryPartyId())
                    .collectList()
                    .flatMap(subsidiaryPartiesIds ->
                            endpointsUtil
                                    .retrieveQuantityObservations(
                                            PROCESSED_OBSERVATION_ID,
                                            subsidiaryPartiesIds,
                                            request.getDatabaseId())
                                    .collectList())
                    .flatMap(processedQuantityObservations ->
                            quantityObservationsAggregationService
                                    .aggregateProcessedQuantityObservations(
                                            request.getTaskId(),
                                            request.getParentPartyId(),
                                            processedQuantityObservations))
                    .flatMap(aggregatedQuantityObservations ->
                            endpointsUtil.createQuantityObservations(
                                    aggregatedQuantityObservations.toArray(QuantityObservation[]::new))
                                    .collectList())
                    .map(observations -> DataAggregationStatus.SUCCEEDED)
                    .onErrorReturn(DataAggregationStatus.FAILED)
                    .block();

            log.info(
                    dataAggregationStatus == DataAggregationStatus.SUCCEEDED ?
                            "[Database: {}, Parent Party: {}] - Data Aggregation Succeeded" :
                            "[Database: {}, Parent Party: {}] - Data Aggregation Failed",
                    request.getDatabaseId(), request.getParentPartyId());


            // Publish the processing status
            rabbitTemplate.convertAndSend(
                    RabbitConfig.PROCESSED_DATA_AGGREGATION_RESULTS_QUEUE,
                    DataAggregationResponse
                            .builder()
                            .taskId(request.getTaskId())
                            .databaseId(request.getDatabaseId())
                            .parentPartyId(request.getParentPartyId())
                            .statusCode(dataAggregationStatus.getId())
                            .build());


        }


    }
}

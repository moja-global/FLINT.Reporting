
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.services;

import global.moja.taskmanager.configurations.ConfigurationDataProvider;
import global.moja.taskmanager.configurations.RabbitConfig;
import global.moja.taskmanager.daos.DataAggregationResponse;
import global.moja.taskmanager.daos.DataProcessingRequest;
import global.moja.taskmanager.daos.DataProcessingResponse;
import global.moja.taskmanager.models.Task;
import global.moja.taskmanager.util.DataIntegrationStatus;
import global.moja.taskmanager.util.DataIntegrationTasksUtil;
import global.moja.taskmanager.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.BaseSubscriber;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class DatabaseIntegrationService {

    @Value("${data.processing.task.type.id}")
    Long DATA_PROCESSING_TASK_TYPE_ID;

    @Value("${data.aggregation.task.type.id}")
    Long DATA_AGGREGATION_TASK_TYPE_ID;

    @Value("${open.task.status.id}")
    Long OPEN_TASK_STATUS_ID;

    @Value("${closed.task.status.id}")
    Long CLOSED_TASK_STATUS_ID;

    final EndpointsUtil endpointsUtil;
    final ConfigurationDataProvider configurationDataProvider;
    final RabbitTemplate rabbitTemplate;
    final DataIntegrationTasksUtil dataIntegrationTasksUtil;

    @Autowired
    public DatabaseIntegrationService(
            EndpointsUtil endpointsUtil,
            ConfigurationDataProvider configurationDataProvider,
            RabbitTemplate rabbitTemplate) {

        this.endpointsUtil = endpointsUtil;
        this.configurationDataProvider = configurationDataProvider;
        this.rabbitTemplate = rabbitTemplate;
        this.dataIntegrationTasksUtil = DataIntegrationTasksUtil.instance();
    }

    public void integrateDatabase(Long databaseId) {
        endpointsUtil
                .deleteQuantityObservations(databaseId)
                .then(endpointsUtil.deleteTasks(databaseId))
                .subscribe(new BaseSubscriber<>() {
                    @Override
                    protected void hookOnComplete() {
                        startDataProcessing(databaseId);
                    }
                });
    }

    private void startDataProcessing(Long databaseId) {

        log.trace("Entering startDataProcessing()");
        log.debug("Database Id = {}", databaseId);

        endpointsUtil
                .createTask(
                        Task.builder()
                                .id(null)
                                .taskTypeId(DATA_PROCESSING_TASK_TYPE_ID)
                                .taskStatusId(OPEN_TASK_STATUS_ID)
                                .databaseId(databaseId)
                                .issues(configurationDataProvider.getTotalDataProcessingIssues())
                                .resolved(0)
                                .rejected(0)
                                .note("Initialized Data Processing Task")
                                .build())
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnNext(Task task) {

                        // Keep a local copy of the task for management purpose
                        dataIntegrationTasksUtil.insertTask(task);

                        // Queue the data processing tasks
                        configurationDataProvider
                                .getDataProcessingLevelPartiesIds()
                                .forEach(id -> {
                                    rabbitTemplate.convertAndSend(
                                            RabbitConfig.RAW_DATA_PROCESSING_QUEUE,
                                            DataProcessingRequest
                                                    .builder()
                                                    .taskId(task.getId())
                                                    .databaseId(databaseId)
                                                    .partyId(id)
                                                    .build());
                                });
                    }

                });
    }


    @RabbitListener(queues = RabbitConfig.RAW_DATA_PROCESSING_RESULTS_QUEUE)
    void manageDataProcessing(final DataProcessingResponse response) {

        log.trace("Entering manageDataProcessing()");
        log.debug("Data Processing Response = {}", response);

        // Retrieve the parent task details from the local tasks utility
        Task task = dataIntegrationTasksUtil.retrieveTask(response.getTaskId());

        // Fail early if the task details were not found
        if (task == null) {
            log.error("Could not retrieve the parent task details from the local tasks utility");
            return;
        }

        // Update the task properties in the local utility
        if (response.getStatusCode() == DataIntegrationStatus.SUCCEEDED.getId()) {
            task.setResolved(task.getResolved() + 1);
        } else {
            task.setRejected(task.getRejected() + 1);
        }

        if (task.getIssues() == task.getResolved() + task.getRejected()) {
            task.setTaskStatusId(CLOSED_TASK_STATUS_ID);
        }

            // Update the task properties in the backend
        endpointsUtil.updateTask(task).subscribe();

        // Check whether the data processing is complete
        if (task.getTaskStatusId().equals(CLOSED_TASK_STATUS_ID)) {

            // Start the data aggregation task
            startDataAggregation(task.getDatabaseId());

            // Remove the task details from the local tasks utility
            dataIntegrationTasksUtil.removeTask(task);

        } else {

            // Update the task details in the local tasks utility
            dataIntegrationTasksUtil.updateTask(task);
        }

    }

    private void startDataAggregation(Long databaseId) {

        endpointsUtil
                .createTask(
                        Task.builder()
                                .id(null)
                                .taskTypeId(DATA_AGGREGATION_TASK_TYPE_ID)
                                .taskStatusId(OPEN_TASK_STATUS_ID)
                                .databaseId(databaseId)
                                .issues(configurationDataProvider.getTotalDataAggregationIssues())
                                .resolved(0)
                                .rejected(0)
                                .note("Initialized Data Aggregation Task")
                                .build())
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnNext(Task task) {

                        // Keep a local copy of the task for management purpose
                        dataIntegrationTasksUtil.insertTask(task);

                        // Keep a local copy of the initial data aggregation level for management purpose
                        dataIntegrationTasksUtil.insertTaskLevel(task.getId(), 1);

                        // Keep a local copy of the level's handed issues for management purpose
                        dataIntegrationTasksUtil.initializeTaskLevelHandledIssuesCount(task.getId());

                        // Queue the data aggregation tasks
                        configurationDataProvider
                                .getDataAggregationLevelsPartiesIds()
                                .get(1)
                                .forEach(id -> rabbitTemplate.convertAndSend(
                                        RabbitConfig.PROCESSED_DATA_AGGREGATION_QUEUE,
                                        DataProcessingRequest
                                                .builder()
                                                .taskId(task.getId())
                                                .databaseId(databaseId)
                                                .partyId(id)
                                                .build()));
                    }

                });
    }


    @RabbitListener(queues = RabbitConfig.PROCESSED_DATA_AGGREGATION_RESULTS_QUEUE)
    void manageDataAggregation(final DataAggregationResponse response) {

        log.trace("Entering manageDataAggregation()");
        log.debug("Data Aggregation Response = {}", response);

        // Retrieve the parent task details from the local tasks utility
        Task task = dataIntegrationTasksUtil.retrieveTask(response.getTaskId());

        // Fail early if the task details were not found
        if (task == null) {
            log.error("Could not retrieve the parent task details from the local tasks utility");
            return;
        }

        // Update the task properties in local tasks utility
        if (response.getStatusCode() == DataIntegrationStatus.SUCCEEDED.getId()) {
            task.setResolved(task.getResolved() + 1);
        } else {
            task.setRejected(task.getRejected() + 1);
        }

        if (task.getIssues() == task.getResolved() + task.getRejected()) {
            task.setTaskStatusId(CLOSED_TASK_STATUS_ID);
        }

        dataIntegrationTasksUtil.updateTask(task);
        dataIntegrationTasksUtil.incrementTaskLevelHandledIssuesCount(task.getId());

        // Update the task properties in the backend
        endpointsUtil.updateTask(task).subscribe();

        // Check whether the data processing is complete
        if (task.getTaskStatusId().equals(CLOSED_TASK_STATUS_ID)) {

            // Retrieve and update the target database's details
            endpointsUtil
                    .retrieveDatabase(task.getDatabaseId())
                    .map(database -> {
                        database.setProcessed(true);
                        return database;
                    })
                    .flatMap(endpointsUtil::updateDatabase)
                    .subscribe();

            // Remove the task details from the local tasks utility
            dataIntegrationTasksUtil.removeTask(task);
            dataIntegrationTasksUtil.removeTaskLevel(task.getId());
            dataIntegrationTasksUtil.removeTaskLevelHandledIssuesCount(task.getId());

        } else {

            // Get the current data aggregation level
            Integer level = dataIntegrationTasksUtil.retrieveTaskLevel(task.getId());

            // Get the total number of issues handled at that aggregation level
            Integer handledIssues = dataIntegrationTasksUtil.retrieveTaskLevelHandledIssuesCount(task.getId());

            // Check whether the data aggregation at the current level is complete
            if(handledIssues == configurationDataProvider.getDataAggregationLevelsPartiesIds().get(level).size()) {

                // Increment the aggregation level
                dataIntegrationTasksUtil.incrementTaskLevel(task.getId());

                // Reset the handled issues
                dataIntegrationTasksUtil.initializeTaskLevelHandledIssuesCount(task.getId());

                // Queue the next data aggregation tasks
                configurationDataProvider
                        .getDataAggregationLevelsPartiesIds()
                        .get(level + 1)
                        .forEach(id -> rabbitTemplate.convertAndSend(
                                RabbitConfig.PROCESSED_DATA_AGGREGATION_QUEUE,
                                DataProcessingRequest
                                        .builder()
                                        .taskId(task.getId())
                                        .databaseId(task.getDatabaseId())
                                        .partyId(id)
                                        .build()));


            }

        }

    }



}


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
                .flatMap(count -> endpointsUtil.deleteTasks(databaseId))
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

        log.trace("Creating a Data Processing Task record)");
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
                                .note("Processing Database")
                                .build())
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnNext(Task task) {

                        log.debug("Data Processing Task = {}", task);

                        // Keep a local copy of the Data Processing Task record for management purpose
                        log.trace("Keeping a local copy of the Data Processing Task record for management purpose");
                        dataIntegrationTasksUtil.insertTask(task);

                        // Queue Data Processing requests
                        log.trace("Queueing Data Processing requests");
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
    private void manageDataProcessing(final DataProcessingResponse response) {

        log.trace("Entering manageDataProcessing()");
        log.debug("Data Processing Response = {}", response);

        // Retrieve the parent task details from the local tasks utility
        log.trace("Retrieving the parent task details from the local tasks utility");
        Task task = dataIntegrationTasksUtil.retrieveTask(response.getTaskId());
        log.debug("Task = {}", task);

        // Fail early if the task details were not found
        if (task == null) {
            log.error("Could not find the parent task details in the local tasks utility");
            return;
        }

        // Update the task details
        log.trace("Updating the task details");

        if (response.getStatusCode() == DataIntegrationStatus.SUCCEEDED.getId()) {
            task.setResolved(task.getResolved() + 1);
        } else {
            task.setRejected(task.getRejected() + 1);
        }

        if (task.getIssues() == task.getResolved() + task.getRejected()) {
            task.setTaskStatusId(CLOSED_TASK_STATUS_ID);
            task.setNote("Done Processing Database");
        }

        log.debug("Updated Task = {}", task);

        // Save the updated task in the local utility
        log.trace("Saving the updated task in the local utility");
        dataIntegrationTasksUtil.updateTask(task);

        // Save the updated task in the database
        log.trace("Saving the updated task in the database");
        endpointsUtil.updateTask(task).subscribe();

        // Check whether the data processing is complete
        log.trace("Checking whether the data processing is complete");
        if (task.getTaskStatusId().equals(CLOSED_TASK_STATUS_ID)) {

            // The data processing is complete
            log.trace("The data processing is complete");

            // Clear all local data processing details
            log.trace("Clearing all local data processing details");
            dataIntegrationTasksUtil.removeTask(task);

            // Start the data aggregation task
            log.trace("Starting the data aggregation task");
            startDataAggregation(task.getDatabaseId());

        } else {

            // The data processing is not complete
            log.trace("The data processing is not complete");

        }

    }

    private void startDataAggregation(Long databaseId) {

        log.trace("Entering startDataAggregation()");
        log.debug("Database Id = {}", databaseId);

        log.trace("Creating a Data Aggregation Task record)");
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
                                .note("Aggregating Database")
                                .build())
                .subscribe(new BaseSubscriber<>() {

                    @Override
                    protected void hookOnNext(Task task) {

                        log.debug("Data Aggregation Task = {}", task);

                        // Keep a local copy of the Data Aggregation Task record for management purpose
                        log.trace("Keeping a local copy of the Data Aggregation Task record for management purpose");
                        dataIntegrationTasksUtil.insertTask(task);

                        // Keep a local copy of the initial data aggregation level for management purpose
                        log.trace("Keeping a local copy of the initial data aggregation level for management purpose");
                        dataIntegrationTasksUtil.initializeTaskLevel(task.getId());

                        // Keep a local copy of the data aggregation level's handled issues for management purpose
                        log.trace("Keeping a local copy of the data aggregation level's handled issues for management purpose");
                        dataIntegrationTasksUtil.initializeTaskLevelHandledIssuesCount(task.getId());

                        // Queue the initial data aggregation tasks
                        log.trace("Queueing the initial data aggregation tasks");
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
    private void manageDataAggregation(final DataAggregationResponse response) {

        log.trace("Entering manageDataAggregation()");
        log.debug("Data Aggregation Response = {}", response);

        // Retrieve the parent task details from the local tasks utility
        log.trace("Retrieving the parent task details from the local tasks utility");
        Task task = dataIntegrationTasksUtil.retrieveTask(response.getTaskId());

        // Fail early if the task details were not found
        if (task == null) {
            log.error("Could not retrieve the parent task details from the local tasks utility");
            return;
        }

        // Update the task details
        log.trace("Updating the task details");

        if (response.getStatusCode() == DataIntegrationStatus.SUCCEEDED.getId()) {
            task.setResolved(task.getResolved() + 1);
        } else {
            task.setRejected(task.getRejected() + 1);
        }

        if (task.getIssues() == task.getResolved() + task.getRejected()) {
            task.setTaskStatusId(CLOSED_TASK_STATUS_ID);
            task.setNote("Done Aggregating Database");
        }

        log.debug("Updated Task = {}", task);

        // Save the updated task in the local utility
        log.trace("Saving the updated task in the local utility");
        dataIntegrationTasksUtil.updateTask(task);
        dataIntegrationTasksUtil.incrementTaskLevelHandledIssuesCount(task.getId());

        // Save the updated task in the database
        log.trace("Saving the updated task in the database");
        endpointsUtil.updateTask(task).subscribe();

        // Check whether the data aggregation is complete
        log.trace("Checking whether the data aggregation is complete");
        if (task.getTaskStatusId().equals(CLOSED_TASK_STATUS_ID)) {

            // Retrieve and update the target database's details
            log.trace("Retrieving and updating the target database's 'processed' status");
            endpointsUtil
                    .retrieveDatabase(task.getDatabaseId())
                    .map(database -> {
                        database.setProcessed(true);
                        return database;
                    })
                    .flatMap(endpointsUtil::updateDatabase)
                    .subscribe();

            // Clear all local data aggregation details
            log.trace("Clearing all local data aggregation details");
            dataIntegrationTasksUtil.removeTask(task);
            dataIntegrationTasksUtil.removeTaskLevel(task.getId());
            dataIntegrationTasksUtil.removeTaskLevelHandledIssuesCount(task.getId());

        } else {

            // Get the current data aggregation level
            log.trace("Getting the current data aggregation level");
            Integer level = dataIntegrationTasksUtil.retrieveTaskLevel(task.getId());

            // Get the total number of issues handled at that current aggregation level
            log.trace("Get the total number of issues handled at that current aggregation level");
            Integer handledIssues = dataIntegrationTasksUtil.retrieveTaskLevelHandledIssuesCount(task.getId());

            // Check whether the level's data aggregation is complete
            log.trace("Checking whether the level's data aggregation is complete");
            if (handledIssues == configurationDataProvider.getDataAggregationLevelsPartiesIds().get(level).size()) {

                // The level's data aggregation is complete
                log.trace("The level's data aggregation is complete");

                // Move to the next aggregation level
                log.trace("Moving to the next aggregation level");
                dataIntegrationTasksUtil.incrementTaskLevel(task.getId());

                // Reset the total number of handled issues
                log.trace("Resetting the total number of handled issues");
                dataIntegrationTasksUtil.initializeTaskLevelHandledIssuesCount(task.getId());

                // Queue the next set of Data Aggregation requests
                log.trace("Queueing the next set of Data Aggregation requests");
                configurationDataProvider
                        .getDataAggregationLevelsPartiesIds()
                        .get(dataIntegrationTasksUtil.retrieveTaskLevel(task.getId()))
                        .forEach(id -> rabbitTemplate.convertAndSend(
                                RabbitConfig.PROCESSED_DATA_AGGREGATION_QUEUE,
                                DataProcessingRequest
                                        .builder()
                                        .taskId(task.getId())
                                        .databaseId(task.getDatabaseId())
                                        .partyId(id)
                                        .build()));


            } else {

                // The level's data aggregation is not complete
                log.trace("The level's data aggregation is not complete");
            }

        }

    }


}

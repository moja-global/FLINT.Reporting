/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.handlers.get;

import global.moja.tasks.models.Task;
import global.moja.tasks.util.builders.QueryParametersBuilder;
import global.moja.tasks.exceptions.ServerException;
import global.moja.tasks.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class RetrieveTasksHandler {

    @Autowired
    TasksRepository repository;

    /**
     * Retrieves all Tasks or specific Tasks if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Tasks records to be retrieved
     * @return the stream of responses containing the details of the retrieved Tasks records
     */
    public Mono<ServerResponse> retrieveTasks(ServerRequest request) {

        log.trace("Entering retrieveTasks()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveTasksConsideringQueryParameters(request),
                                Task.class)
                        .onErrorMap(e -> new ServerException("Tasks retrieval failed", e));
    }

    private Flux<Task> retrieveTasksConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectTasks(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .taskTypeId(request)
                                        .taskStatusId(request)
                                        .databaseId(request)
                                        .build());
    }


}

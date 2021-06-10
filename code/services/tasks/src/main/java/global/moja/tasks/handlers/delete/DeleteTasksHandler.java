/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.handlers.delete;

import global.moja.tasks.exceptions.ServerException;
import global.moja.tasks.util.builders.QueryParametersBuilder;
import global.moja.tasks.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class DeleteTasksHandler {

    @Autowired
    TasksRepository repository;

    /**
     * Deletes all Tasks or specific Tasks records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Tasks records to be deleted
     * @return the response containing the number of Tasks records deleted
     */
    public Mono<ServerResponse> deleteTasks(ServerRequest request) {

        log.trace("Entering deleteTasks()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteTasksConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Tasks deletion failed", e));
    }


    private Mono<Integer> deleteTasksConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteTasks(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .taskTypeId(request)
                                        .taskStatusId(request)
                                        .databaseId(request)
                                        .build());
    }


}

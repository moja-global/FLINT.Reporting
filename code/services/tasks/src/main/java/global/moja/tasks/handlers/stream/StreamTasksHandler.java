/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.handlers.stream;

import global.moja.tasks.exceptions.ServerException;
import global.moja.tasks.models.Task;
import global.moja.tasks.repository.TasksRepository;
import global.moja.tasks.util.builders.QueryParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class StreamTasksHandler {

    @Autowired
    TasksRepository repository;

    /**
     * Returns the target task, as is, at first, then only pushes the task downstream when its been updated
     *
     * @param request the request
     * @return the stream of responses containing the details of the retrieved Tasks records
     */
    public Mono<ServerResponse> streamTasks(ServerRequest request) {

        log.trace("Entering retrieveTasks()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_NDJSON)
                        .body(streamTasks(), Task.class)
                        .onErrorMap(e -> new ServerException("Tasks retrieval failed", e));
    }

    private Flux<Task> streamTasks() {

        return Flux.interval(Duration.ofSeconds(1))
                .flatMap(tick -> tick == 0 ? getAllTasks() : getUpdatedTasks());
    }

    private Flux<Task> getAllTasks() {
        return repository.selectTasks(new QueryParametersBuilder().build());
    }

    private Flux<Task> getUpdatedTasks() {
        return
                repository
                        .selectTasks(
                                new QueryParametersBuilder()
                                        .updatedAfter(System.currentTimeMillis() - 1000)
                                        .build());
    }





}

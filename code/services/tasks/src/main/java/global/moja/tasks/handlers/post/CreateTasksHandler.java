/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.handlers.post;

import global.moja.tasks.exceptions.ServerException;
import global.moja.tasks.models.Task;
import global.moja.tasks.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateTasksHandler {

	@Autowired
    TasksRepository repository;
	

	/**
	 * Recursively creates Tasks records
	 *
	 * @param request the request containing the details of the Tasks records to be created
	 * @return the stream of responses containing the details of the newly created Tasks records
	 */
	public Mono<ServerResponse> createTasks(ServerRequest request) {

		log.trace("Entering createTasks()");

		return 
			request
				.bodyToMono(Task[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createTasks(units), Task.class))
				.onErrorMap(e -> new ServerException("Tasks creation failed", e));
	}
	
	private Flux<Task> createTasks(Task[] tasks) {
		
		return
			repository
				.insertTasks(tasks)
				.flatMap(id -> repository.selectTask(id));
			
	}



}

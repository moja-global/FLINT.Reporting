/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.handlers.post;

import global.moja.tasks.models.Task;
import global.moja.tasks.exceptions.ServerException;
import global.moja.tasks.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateTaskHandler {

	@Autowired
    TasksRepository repository;
	
	/**
	 * Creates a Task record
	 *
	 * @param request the request containing the details of the Task record to be created
	 * @return the response containing the details of the newly created Task record
	 */
	public Mono<ServerResponse> createTask(ServerRequest request) {

		log.trace("Entering createTask()");

		return 
			request
				.bodyToMono(Task.class)
				.flatMap(task ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createTask(task), Task.class))
				.onErrorMap(e -> new ServerException("Task creation failed", e));
	}
	
	
	private Mono<Task> createTask(Task task){
		
		return 
			repository
				.insertTask(task)
				.flatMap(id -> repository.selectTask(id));
		  
	}

}

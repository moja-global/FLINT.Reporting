/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.handlers.put;

import global.moja.tasks.models.Task;
import global.moja.tasks.exceptions.ServerException;
import global.moja.tasks.repository.TasksRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UpdateTaskHandler {

	@Autowired
    TasksRepository repository;
	
	/**
	 * Updates a Task record
	 * @param request the request containing the details of the Task record to be updated
	 * @return the response containing the details of the newly updated Task record
	 */
	public Mono<ServerResponse> updateTask(ServerRequest request) {

		log.trace("Entering updateTask()");

		return 
			request
				.bodyToMono(Task.class)
					.flatMap(task ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateTask(task), Task.class))
					.onErrorMap(e -> new ServerException("Task update failed", e));
	}
	
	
	private Mono<Task> updateTask(Task task){
		
		return 
			repository
				.updateTask(task)
				.flatMap(count -> repository.selectTask(task.getId()));
	}
}

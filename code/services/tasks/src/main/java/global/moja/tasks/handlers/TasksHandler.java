/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.handlers;

import global.moja.tasks.handlers.delete.DeleteTaskHandler;
import global.moja.tasks.handlers.delete.DeleteTasksHandler;
import global.moja.tasks.handlers.get.RetrieveTasksHandler;
import global.moja.tasks.handlers.put.UpdateTaskHandler;
import global.moja.tasks.handlers.put.UpdateTasksHandler;
import global.moja.tasks.handlers.post.CreateTasksHandler;
import global.moja.tasks.handlers.get.RetrieveTaskHandler;
import global.moja.tasks.handlers.post.CreateTaskHandler;
import global.moja.tasks.handlers.stream.StreamTasksHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class TasksHandler {

	// POST HANDLERS
	@Autowired
	CreateTaskHandler createTaskHandler;

	@Autowired
	CreateTasksHandler createTasksHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveTaskHandler retrieveTaskByIdHandler;
	
	@Autowired
	RetrieveTasksHandler retrieveTasksHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateTaskHandler updateTaskHandler;
	
	@Autowired
	UpdateTasksHandler updateTasksHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteTaskHandler deleteTaskByIdHandler;
	
	@Autowired
	DeleteTasksHandler deleteTasksHandler;


	// STREAM HANDLERS
	@Autowired
	StreamTasksHandler streamTasksHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createTask(ServerRequest request) {
		return this.createTaskHandler.createTask(request);
	}
	
	public Mono<ServerResponse> createTasks(ServerRequest request) {
		return createTasksHandler.createTasks(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveTask(ServerRequest request) {
		return this.retrieveTaskByIdHandler.retrieveTask(request);
	}
	
	public Mono<ServerResponse> retrieveTasks(ServerRequest request) {
		return this.retrieveTasksHandler.retrieveTasks(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateTask(ServerRequest request) {
		return this.updateTaskHandler.updateTask(request);
	}
	
	public Mono<ServerResponse> updateTasks(ServerRequest request) {
		return this.updateTasksHandler.updateTasks(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteTask(ServerRequest request) {
		return this.deleteTaskByIdHandler.deleteTask(request);
	}
	
	public Mono<ServerResponse> deleteTasks(ServerRequest request) {
		return this.deleteTasksHandler.deleteTasks(request);
	}	

	// </editor-fold>


	// <editor-fold desc="STREAM">
	public Mono<ServerResponse> streamTasks(ServerRequest request) {
		return this.streamTasksHandler.streamTasks(request);
	}

	// </editor-fold>

}

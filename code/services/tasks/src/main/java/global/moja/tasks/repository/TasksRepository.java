/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.repository;

import global.moja.tasks.models.Task;
import global.moja.tasks.repository.deletion.DeleteTaskQuery;
import global.moja.tasks.repository.deletion.DeleteTasksQuery;
import global.moja.tasks.daos.QueryParameters;
import global.moja.tasks.repository.updation.UpdateTaskQuery;
import global.moja.tasks.repository.updation.UpdateTasksQuery;
import global.moja.tasks.repository.selection.SelectTasksQuery;
import global.moja.tasks.repository.insertion.InsertTaskQuery;
import global.moja.tasks.repository.insertion.InsertTasksQuery;
import global.moja.tasks.repository.selection.SelectTaskQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class TasksRepository {

	
	@Autowired
	InsertTaskQuery insertTaskQuery;
	
	@Autowired
	InsertTasksQuery insertTasksQuery;
	
	@Autowired
	SelectTaskQuery selectTaskQuery;
	
	@Autowired
	SelectTasksQuery selectTasksQuery;

	@Autowired
	UpdateTaskQuery updateTaskQuery;
	
	@Autowired
	UpdateTasksQuery updateTasksQuery;
	
	@Autowired
	DeleteTaskQuery deleteTaskQuery;
	
	@Autowired
	DeleteTasksQuery deleteTasksQuery;


	public Mono<Long> insertTask(Task task) {
		return insertTaskQuery.insertTask(task);
	}
	
	public Flux<Long> insertTasks(Task[] tasks) {
		return insertTasksQuery.insertTasks(tasks);
	}

	public Mono<Task> selectTask(Long id) {
		return selectTaskQuery.selectTask(id);
	}
	
	public Flux<Task> selectTasks(QueryParameters parameters) {
		return selectTasksQuery.selectTasks(parameters);
	}

	public Mono<Integer> updateTask(Task task) {
		return updateTaskQuery.updateTask(task);
	}
	
	public Flux<Integer> updateTasks(Task[] tasks) {
		return updateTasksQuery.updateTasks(tasks);
	}	
	
	public Mono<Integer> deleteTaskById(Long id) {
		return deleteTaskQuery.deleteTask(id);
	}
	
	public Mono<Integer> deleteTasks(QueryParameters parameters) {
		return deleteTasksQuery.deleteTasks(parameters);
	}


}

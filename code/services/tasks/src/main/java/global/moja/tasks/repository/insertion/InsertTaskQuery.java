/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.repository.insertion;

import global.moja.tasks.models.Task;
import global.moja.tasks.configurations.DatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class InsertTaskQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Task record into the database
	 * @param task a bean containing the Task record details
	 * @return the unique identifier of the newly inserted Task record
	 */
	public Mono<Long> insertTask(Task task){
		
		log.trace("Entering insertTask()");

		String query = "INSERT INTO task(task_type_id,task_status_id,database_id,issues,resolved,rejected,note,last_updated) VALUES(?,?,?,?,?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							task.getTaskTypeId(),
							task.getTaskStatusId(),
							task.getDatabaseId(),
							task.getIssues(),
							task.getResolved(),
							task.getRejected(),
							task.getNote(),
							task.getLastUpdated())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.repository.selection;

import global.moja.tasks.configurations.DatabaseConfig;
import global.moja.tasks.models.Task;
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
public class SelectTaskQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects a Task record from the database given its unique identifier
	 * @param id the unique identifier of the Task record to be selected
	 * @return the Task record with the given id if found
	 */
	public Mono<Task> selectTask(Long id) {

		log.trace("Entering selectTask()");

		String query = "SELECT * FROM task WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							Task.builder()
									.id(rs.getLong("id"))
									.taskTypeId(rs.getLong("task_type_id"))
									.taskStatusId(rs.getLong("task_status_id"))
									.databaseId(rs.getLong("database_id"))
									.issues(rs.getInt("issues"))
									.resolved(rs.getInt("resolved"))
									.rejected(rs.getInt("rejected"))
									.note(rs.getString("note"))
									.lastUpdated(rs.getLong("last_updated"))
									.build()));
	}

}

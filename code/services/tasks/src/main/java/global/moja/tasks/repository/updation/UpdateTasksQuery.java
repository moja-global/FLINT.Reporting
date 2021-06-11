/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.tasks.repository.updation;

import global.moja.tasks.models.Task;
import io.reactivex.Flowable;
import global.moja.tasks.configurations.DatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class UpdateTasksQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Tasks records
     *
     * @param tasks an array of beans containing the Tasks records details
     * @return the number of Tasks records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateTasks(Task[] tasks) {

        log.trace("Entering updateTasks()");

        String query =
                "UPDATE task SET " +
                        "task_type_id = ?, " +
                        "task_status_id = ?, " +
                        "database_id = ?, " +
                        "issues = ?, " +
                        "resolved = ?, " +
                        "rejected = ?, " +
                        "note = ?, " +
                        "last_updated = ? " +
                        "WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(tasks))
                                .counts());
    }

    private Flowable getParametersListStream(Task[] tasks) {

        List<List> temp = new ArrayList<>();

        for (Task task : tasks) {
            temp.add(Arrays.asList(
                    task.getTaskTypeId(),
                    task.getTaskStatusId(),
                    task.getDatabaseId(),
                    task.getIssues(),
                    task.getResolved(),
                    task.getRejected(),
                    task.getNote(),
                    System.currentTimeMillis(),
                    task.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

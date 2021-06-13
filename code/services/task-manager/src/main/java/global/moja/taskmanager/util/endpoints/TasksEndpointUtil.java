/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.util.endpoints;

import global.moja.taskmanager.models.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class TasksEndpointUtil {

    @Autowired
    TasksCreationEndpointUtil tasksCreationEndpointUtil;

    @Autowired
    TasksDeletionEndpointUtil tasksDeletionEndpointUtil;

    @Autowired
    TasksUpdationEndpointUtil tasksUpdationEndpointUtil;

    public Mono<Task> createTasks(Task task) {
        return tasksCreationEndpointUtil.createTasks(task);
    }

    public Mono<Integer> deleteTasks(Long databaseId) {
        return tasksDeletionEndpointUtil.deleteTasks(databaseId);
    }

    public Mono<Task> updateTask(Task task) {
        return tasksUpdationEndpointUtil.updateTask(task);
    }
}

/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.taskmanager.util.endpoints;

import global.moja.taskmanager.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class EndpointsUtil {

    @Autowired
    AccountabilitiesEndpointUtil accountabilitiesEndpointUtil;

    @Autowired
    AccountabilityRulesEndpointUtil accountabilityRulesEndpointUtil;

    @Autowired
    DatabasesEndpointUtil databasesEndpointUtil;

    @Autowired
    QuantityObservationsEndpointUtil quantityObservationsEndpointUtil;

    @Autowired
    TasksEndpointUtil tasksEndpointUtil;


    // Accountabilities

    public Flux<Accountability> retrieveAccountabilities(Long accountabilityRuleId){
        return accountabilitiesEndpointUtil.retrieveAccountabilities(accountabilityRuleId);
    }

    public Flux<AccountabilityRule> retrieveAccountabilityRules(Long accountabilityTypeId) {
        return accountabilityRulesEndpointUtil.retrieveAccountabilityRules(accountabilityTypeId);
    }

    // Databases

    public Mono<Database> retrieveDatabase(Long databaseId) {
        return databasesEndpointUtil.retrieveDatabase(databaseId);
    }

    public Mono<Database> updateDatabase(Database database) {
        return databasesEndpointUtil.updateDatabase(database);
    }


    // Quantity Observations

    public Mono<Integer> deleteQuantityObservations(Long databaseId) {
        return quantityObservationsEndpointUtil.deleteQuantityObservations(databaseId);
    }


    // Tasks
    public Mono<Task> createTask(Task task) {
        return tasksEndpointUtil.createTasks(task);
    }

    public Mono<Integer> deleteTasks(Long databaseId) {
        return tasksEndpointUtil.deleteTasks(databaseId);
    }

    public Mono<Task> updateTask(Task task) {
        return tasksEndpointUtil.updateTask(task);
    }

}

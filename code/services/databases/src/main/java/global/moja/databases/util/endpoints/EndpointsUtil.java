/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.databases.util.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
public class EndpointsUtil {

    @Autowired
    QuantityObservationsEndpointUtil quantityObservationsEndpointUtil;

    @Autowired
    TasksEndpointUtil tasksEndpointUtil;

    @Autowired
    TaskManagerEndpointUtil taskManagerEndpointUtil;

    public Mono<Integer> deleteQuantityObservations(Long databaseId) {
        return quantityObservationsEndpointUtil.deleteQuantityObservations(databaseId);
    }

    public Mono<Integer> deleteTasks(Long databaseId) {
        return tasksEndpointUtil.deleteTasks(databaseId);
    }

    public Mono<Void> integrateDatabase(Long databaseId) {
        return taskManagerEndpointUtil.integrateDatabase(databaseId);
    }


}

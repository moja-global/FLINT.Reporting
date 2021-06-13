/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.util.endpoints;


import global.moja.taskmanager.models.Database;
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
public class DatabasesEndpointUtil {

    @Autowired
    DatabasesRetrievalEndpointUtil databasesRetrievalEndpointUtil;

    @Autowired
    DatabasesUpdationEndpointUtil databasesUpdationEndpointUtil;

    public Mono<Database> retrieveDatabase(Long databaseId) {
        return databasesRetrievalEndpointUtil.retrieveDatabase(databaseId);
    }

    public Mono<Database> updateDatabase(Database database) {
        return databasesUpdationEndpointUtil.updateDatabase(database);
    }
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository.deletion;

import global.moja.databases.configurations.DatabaseConfig;
import global.moja.databases.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class DeleteDatabaseQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    @Autowired
    EndpointsUtil endpointsUtil;

    /**
     * Deletes a Database record from the database
     *
     * @param id the unique identifier of the Database record to be deleted
     * @return the number of Databases records affected by the query i.e deleted
     */
    public Mono<Integer> deleteDatabase(Long id) {

        log.trace("Entering deleteDatabase");

        String query = "DELETE FROM database WHERE id = ?";

        return endpointsUtil
                .deleteQuantityObservations(id)
                .flatMap(count -> endpointsUtil.deleteTasks(id))
                .flatMap(count ->
                        Mono.from(databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameters(id)
                                .counts()));
    }

}

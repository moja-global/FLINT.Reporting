/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository.updation;

import io.reactivex.Flowable;
import global.moja.databases.configurations.DatabaseConfig;
import global.moja.databases.models.Database;
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
public class UpdateDatabasesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Databases records
     *
     * @param databases an array of beans containing the Databases records details
     * @return the number of Databases records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateDatabases(Database[] databases) {

        log.trace("Entering updateDatabases()");

        String query =
                "UPDATE database SET " +
                        "label = ?, " +
                        "description  = ?, " +
                        "url = ?, " +
                        "start_year = ?, " +
                        "end_year = ?, " +
                        "processed = ?, " +
                        "published = ?, " +
                        "archived = ?, " +
                        "WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(databases))
                                .counts());
    }

    private Flowable getParametersListStream(Database[] databases) {

        List<List> temp = new ArrayList<>();

        for (Database database : databases) {
            temp.add(Arrays.asList(
                    database.getLabel(),
                    database.getDescription(),
                    database.getUrl(),
                    database.getStartYear(),
                    database.getEndYear(),
                    database.getProcessed(),
                    database.getPublished(),
                    database.getArchived(),
                    database.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

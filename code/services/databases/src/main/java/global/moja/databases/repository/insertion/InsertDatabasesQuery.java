/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository.insertion;

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
public class InsertDatabasesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Databases records into the database
     *
     * @param databases an array of beans containing the Databases records details
     * @return the unique identifiers of the newly inserted Databases records
     */
    public Flux<Long> insertDatabases(Database[] databases) {

        log.trace("Entering insertDatabases");

        String query =
                "INSERT INTO database(" +
                        "label," +
                        "description," +
                        "url," +
                        "start_year," +
                        "end_year," +
                        "processed," +
                        "published," +
                        "archived) " +
                        "VALUES(?,?,?,?,?,?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(databases))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
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
                    database.getArchived()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers.post;

import global.moja.databases.exceptions.ServerException;
import global.moja.databases.models.Database;
import global.moja.databases.repository.DatabasesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class CreateDatabaseHandler {

    @Autowired
    DatabasesRepository repository;

    /**
     * Creates a Database record
     *
     * @param request the request containing the details of the Database record to be created
     * @return the response containing the details of the newly created Database record
     */
    public Mono<ServerResponse> createDatabase(ServerRequest request) {

        log.trace("Entering createDatabase()");

        return
                request
                        .bodyToMono(Database.class)
                        .flatMap(database ->
                                ServerResponse
                                        .status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(createDatabase(database), Database.class))
                        .onErrorMap(e -> new ServerException("Database creation failed", e));
    }


    private Mono<Database> createDatabase(Database database) {

        return
                repository
                        .insertDatabase(database)
                        .flatMap(id -> repository.selectDatabase(id));

    }




}

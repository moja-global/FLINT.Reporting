/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers.get;

import global.moja.databases.exceptions.ServerException;
import global.moja.databases.repository.DatabasesRepository;
import global.moja.databases.models.Database;
import global.moja.databases.util.builders.QueryParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class RetrieveDatabasesHandler {

    @Autowired
    DatabasesRepository repository;

    /**
     * Retrieves all Databases or specific Databases if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Databases records to be retrieved
     * @return the stream of responses containing the details of the retrieved Databases records
     */
    public Mono<ServerResponse> retrieveDatabases(ServerRequest request) {

        log.trace("Entering retrieveDatabases()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveDatabasesConsideringQueryParameters(request),
                                Database.class)
                        .onErrorMap(e -> new ServerException("Databases retrieval failed", e));
    }

    private Flux<Database> retrieveDatabasesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectDatabases(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .label(request)
                                        .startYear(request)
                                        .endYear(request)
                                        .processed(request)
                                        .published(request)
                                        .archived(request)
                                        .build());
    }


}

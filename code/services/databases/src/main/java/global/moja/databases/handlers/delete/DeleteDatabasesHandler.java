/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers.delete;

import global.moja.databases.exceptions.ServerException;
import global.moja.databases.repository.DatabasesRepository;
import global.moja.databases.util.builders.QueryParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class DeleteDatabasesHandler {

    @Autowired
    DatabasesRepository repository;

    /**
     * Deletes all Databases or specific Databases records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Databases records to be deleted
     * @return the response containing the number of Databases records deleted
     */
    public Mono<ServerResponse> deleteDatabases(ServerRequest request) {

        log.trace("Entering deleteDatabases()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteDatabasesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Databases deletion failed", e));
    }


    private Mono<Integer> deleteDatabasesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteDatabases(
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

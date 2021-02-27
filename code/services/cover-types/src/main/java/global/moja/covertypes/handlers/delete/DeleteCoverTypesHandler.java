/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.handlers.delete;

import global.moja.covertypes.exceptions.ServerException;
import global.moja.covertypes.repository.CoverTypesRepository;
import global.moja.covertypes.util.builders.QueryParametersBuilder;
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
public class DeleteCoverTypesHandler {

    @Autowired
    CoverTypesRepository repository;

    /**
     * Deletes all Cover Types or specific Cover Types records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Cover Types records to be deleted
     * @return the response containing the number of Cover Types records deleted
     */
    public Mono<ServerResponse> deleteCoverTypes(ServerRequest request) {

        log.trace("Entering deleteCoverTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteCoverTypesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Cover Types deletion failed", e));
    }


    private Mono<Integer> deleteCoverTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteCoverTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .code(request)
                                        .build());
    }


}

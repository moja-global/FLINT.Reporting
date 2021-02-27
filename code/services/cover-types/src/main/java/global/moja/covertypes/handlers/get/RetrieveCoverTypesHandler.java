/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.handlers.get;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.exceptions.ServerException;
import global.moja.covertypes.repository.CoverTypesRepository;
import global.moja.covertypes.util.builders.QueryParametersBuilder;
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
public class RetrieveCoverTypesHandler {

    @Autowired
    CoverTypesRepository repository;

    /**
     * Retrieves all Cover Types or specific Cover Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Cover Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Cover Types records
     */
    public Mono<ServerResponse> retrieveCoverTypes(ServerRequest request) {

        log.trace("Entering retrieveCoverTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveCoverTypesConsideringQueryParameters(request),
                                CoverType.class)
                        .onErrorMap(e -> new ServerException("Cover Types retrieval failed", e));
    }

    private Flux<CoverType> retrieveCoverTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectCoverTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .code(request)
                                        .build());
    }


}

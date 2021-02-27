/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes.handlers.get;

import global.moja.fluxtypes.models.FluxType;
import global.moja.fluxtypes.util.builders.QueryParametersBuilder;
import global.moja.fluxtypes.exceptions.ServerException;
import global.moja.fluxtypes.repository.FluxTypesRepository;
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
public class RetrieveFluxTypesHandler {

    @Autowired
    FluxTypesRepository repository;

    /**
     * Retrieves all Flux Types or specific Flux Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Flux Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Flux Types records
     */
    public Mono<ServerResponse> retrieveFluxTypes(ServerRequest request) {

        log.trace("Entering retrieveFluxTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveFluxTypesConsideringQueryParameters(request),
                                FluxType.class)
                        .onErrorMap(e -> new ServerException("Flux Types retrieval failed", e));
    }

    private Flux<FluxType> retrieveFluxTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectFluxTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .build());
    }


}

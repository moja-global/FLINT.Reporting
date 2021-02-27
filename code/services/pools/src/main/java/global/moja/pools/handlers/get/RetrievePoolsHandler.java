/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.handlers.get;

import global.moja.pools.exceptions.ServerException;
import global.moja.pools.models.Pool;
import global.moja.pools.repository.PoolsRepository;
import global.moja.pools.util.builders.QueryParametersBuilder;
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
public class RetrievePoolsHandler {

    @Autowired
    PoolsRepository repository;

    /**
     * Retrieves all Pools or specific Pools if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Pools records to be retrieved
     * @return the stream of responses containing the details of the retrieved Pools records
     */
    public Mono<ServerResponse> retrievePools(ServerRequest request) {

        log.trace("Entering retrievePools()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrievePoolsConsideringQueryParameters(request),
                                Pool.class)
                        .onErrorMap(e -> new ServerException("Pools retrieval failed", e));
    }

    private Flux<Pool> retrievePoolsConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectPools(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .build());
    }


}

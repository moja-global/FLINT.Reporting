/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.handlers.delete;

import global.moja.pools.exceptions.ServerException;
import global.moja.pools.repository.PoolsRepository;
import global.moja.pools.util.builders.QueryParametersBuilder;
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
public class DeletePoolsHandler {

    @Autowired
    PoolsRepository repository;

    /**
     * Deletes all Pools or specific Pools records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Pools records to be deleted
     * @return the response containing the number of Pools records deleted
     */
    public Mono<ServerResponse> deletePools(ServerRequest request) {

        log.trace("Entering deletePools()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deletePoolsConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Pools deletion failed", e));
    }


    private Mono<Integer> deletePoolsConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deletePools(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .build());
    }


}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.handlers.delete;

import global.moja.landusesfluxtypes.repository.LandUsesFluxTypesRepository;
import global.moja.landusesfluxtypes.util.builders.QueryParametersBuilder;
import global.moja.landusesfluxtypes.exceptions.ServerException;
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
public class DeleteLandUsesFluxTypesHandler {

    @Autowired
    LandUsesFluxTypesRepository repository;

    /**
     * Deletes all Land Uses Flux Types or specific Land Uses Flux Types records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Land Uses Flux Types records to be deleted
     * @return the response containing the number of Land Uses Flux Types records deleted
     */
    public Mono<ServerResponse> deleteLandUsesFluxTypes(ServerRequest request) {

        log.trace("Entering deleteLandUsesFluxTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteLandUsesFluxTypesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Land Uses Flux Types deletion failed", e));
    }


    private Mono<Integer> deleteLandUsesFluxTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteLandUsesFluxTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .landUseCategoryId(request)
                                        .fluxTypeId(request)
                                        .build());
    }


}

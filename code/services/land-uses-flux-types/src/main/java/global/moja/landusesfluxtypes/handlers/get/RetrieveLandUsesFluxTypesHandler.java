/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.handlers.get;

import global.moja.landusesfluxtypes.exceptions.ServerException;
import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.repository.LandUsesFluxTypesRepository;
import global.moja.landusesfluxtypes.util.builders.QueryParametersBuilder;
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
public class RetrieveLandUsesFluxTypesHandler {

    @Autowired
    LandUsesFluxTypesRepository repository;

    /**
     * Retrieves all Land Uses Flux Types or specific Land Uses Flux Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Land Uses Flux Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Land Uses Flux Types records
     */
    public Mono<ServerResponse> retrieveLandUsesFluxTypes(ServerRequest request) {

        log.trace("Entering retrieveLandUsesFluxTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveLandUsesFluxTypesConsideringQueryParameters(request),
                                LandUseFluxType.class)
                        .onErrorMap(e -> new ServerException("Land Uses Flux Types retrieval failed", e));
    }

    private Flux<LandUseFluxType> retrieveLandUsesFluxTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectLandUsesFluxTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .landUseCategoryId(request)
                                        .fluxTypeId(request)
                                        .build());
    }


}

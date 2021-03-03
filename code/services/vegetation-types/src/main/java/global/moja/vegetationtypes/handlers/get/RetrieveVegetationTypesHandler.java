/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.handlers.get;

import global.moja.vegetationtypes.models.VegetationType;
import global.moja.vegetationtypes.exceptions.ServerException;
import global.moja.vegetationtypes.repository.VegetationTypesRepository;
import global.moja.vegetationtypes.util.builders.QueryParametersBuilder;
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
public class RetrieveVegetationTypesHandler {

    @Autowired
    VegetationTypesRepository repository;

    /**
     * Retrieves all Vegetation Types or specific Vegetation Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Vegetation Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Vegetation Types records
     */
    public Mono<ServerResponse> retrieveVegetationTypes(ServerRequest request) {

        log.trace("Entering retrieveVegetationTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveVegetationTypesConsideringQueryParameters(request),
                                VegetationType.class)
                        .onErrorMap(e -> new ServerException("Vegetation Types retrieval failed", e));
    }

    private Flux<VegetationType> retrieveVegetationTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectVegetationTypes(
                                Long.parseLong(request.pathVariable("databaseId")),
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .coverTypeId(request)
                                        .name(request)
                                        .woodyType(request)
                                        .naturalSystem(request)
                                        .build());
    }


}

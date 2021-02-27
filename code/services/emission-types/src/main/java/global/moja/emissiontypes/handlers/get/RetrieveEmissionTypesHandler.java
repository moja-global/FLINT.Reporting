/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.handlers.get;

import global.moja.emissiontypes.models.EmissionType;
import global.moja.emissiontypes.util.builders.QueryParametersBuilder;
import global.moja.emissiontypes.exceptions.ServerException;
import global.moja.emissiontypes.repository.EmissionTypesRepository;
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
public class RetrieveEmissionTypesHandler {

    @Autowired
    EmissionTypesRepository repository;

    /**
     * Retrieves all Emission Types or specific Emission Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Emission Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Emission Types records
     */
    public Mono<ServerResponse> retrieveEmissionTypes(ServerRequest request) {

        log.trace("Entering retrieveEmissionTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveEmissionTypesConsideringQueryParameters(request),
                                EmissionType.class)
                        .onErrorMap(e -> new ServerException("Emission Types retrieval failed", e));
    }

    private Flux<EmissionType> retrieveEmissionTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectEmissionTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .abbreviation(request)
                                        .build());
    }


}

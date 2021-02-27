/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.handlers.delete;

import global.moja.emissiontypes.util.builders.QueryParametersBuilder;
import global.moja.emissiontypes.exceptions.ServerException;
import global.moja.emissiontypes.repository.EmissionTypesRepository;
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
public class DeleteEmissionTypesHandler {

    @Autowired
    EmissionTypesRepository repository;

    /**
     * Deletes all Emission Types or specific Emission Types records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Emission Types records to be deleted
     * @return the response containing the number of Emission Types records deleted
     */
    public Mono<ServerResponse> deleteEmissionTypes(ServerRequest request) {

        log.trace("Entering deleteEmissionTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteEmissionTypesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Emission Types deletion failed", e));
    }


    private Mono<Integer> deleteEmissionTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteEmissionTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .abbreviation(request)
                                        .build());
    }


}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.handlers.delete;

import lombok.extern.slf4j.Slf4j;
import moja.global.fluxestounfcccvariables.exceptions.ServerException;
import moja.global.fluxestounfcccvariables.repository.FluxesToUnfcccVariablesRepository;
import moja.global.fluxestounfcccvariables.util.builders.QueryParametersBuilder;
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
public class DeleteFluxesToUnfcccVariablesHandler {

    @Autowired
    FluxesToUnfcccVariablesRepository repository;

    /**
     * Deletes all fluxes to UNFCCC variable records or specific fluxes to UNFCCC variable records if given their
     * unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the fluxes to UNFCCC variable
     *                records to be deleted
     * @return the response containing the number of fluxes to UNFCCC variable records deleted
     */
    public Mono<ServerResponse> deleteFluxesToUnfcccVariables(ServerRequest request) {

        log.trace("Entering deleteFluxesToUnfcccVariables()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteFluxesToUnfcccVariablesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Fluxes To UNFCCC Variable records deletion failed", e));
    }


    private Mono<Integer> deleteFluxesToUnfcccVariablesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteFluxesToUnfcccVariables(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .startPoolId(request)
                                        .endPoolId(request)
                                        .unfcccVariableId(request)
                                        .rule(request)
                                        .build());
    }


}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxestounfcccvariables.handlers.get;

import moja.global.fluxestounfcccvariables.exceptions.ServerException;
import moja.global.fluxestounfcccvariables.models.FluxToUnfcccVariable;
import moja.global.fluxestounfcccvariables.repository.FluxesToUnfcccVariablesRepository;
import lombok.extern.slf4j.Slf4j;
import moja.global.fluxestounfcccvariables.util.builders.QueryParametersBuilder;
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
public class RetrieveFluxesToUnfcccVariablesHandler {

    @Autowired
    FluxesToUnfcccVariablesRepository repository;

    /**
     * Retrieves all fluxes to UNFCCC variable records or specific fluxes to UNFCCC variable records if given their
     * unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the fluxes to UNFCCC variable
     *                records to be retrieved
     * @return the stream of responses containing the details of the retrieved fluxes to UNFCCC variable records
     */
    public Mono<ServerResponse> retrieveFluxesToUnfcccVariables(ServerRequest request) {

        log.trace("Entering retrieveFluxesToUnfcccVariables()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveFluxesToUnfcccVariablesConsideringQueryParameters(request),
                                FluxToUnfcccVariable.class)
                        .onErrorMap(e -> new ServerException("Fluxes To UNFCCC Variable records retrieval failed", e));
    }

    private Flux<FluxToUnfcccVariable> retrieveFluxesToUnfcccVariablesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectFluxesToUnfcccVariables(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .startPoolId(request)
                                        .endPoolId(request)
                                        .unfcccVariableId(request)
                                        .rule(request)
                                        .build());
    }


}

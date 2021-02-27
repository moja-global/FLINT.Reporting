/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.handlers.get;

import global.moja.fluxestoreportingvariables.models.FluxToReportingVariable;
import global.moja.fluxestoreportingvariables.repository.FluxesToReportingVariablesRepository;
import global.moja.fluxestoreportingvariables.util.builders.QueryParametersBuilder;
import global.moja.fluxestoreportingvariables.exceptions.ServerException;
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
public class RetrieveFluxesToReportingVariablesHandler {

    @Autowired
    FluxesToReportingVariablesRepository repository;

    /**
     * Retrieves all fluxes to reporting variable records or specific fluxes to reporting variable records if given their
     * unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the fluxes to reporting variable
     *                records to be retrieved
     * @return the stream of responses containing the details of the retrieved fluxes to reporting variable records
     */
    public Mono<ServerResponse> retrieveFluxesToReportingVariables(ServerRequest request) {

        log.trace("Entering retrieveFluxesToReportingVariables()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveFluxesToReportingVariablesConsideringQueryParameters(request),
                                FluxToReportingVariable.class)
                        .onErrorMap(e -> new ServerException("Fluxes To Reporting Variable records retrieval failed", e));
    }

    private Flux<FluxToReportingVariable> retrieveFluxesToReportingVariablesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectFluxesToReportingVariables(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .startPoolId(request)
                                        .endPoolId(request)
                                        .reportingVariableId(request)
                                        .rule(request)
                                        .build());
    }


}

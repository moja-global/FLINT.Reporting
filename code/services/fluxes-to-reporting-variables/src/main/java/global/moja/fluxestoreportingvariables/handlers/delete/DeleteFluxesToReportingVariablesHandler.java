/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxestoreportingvariables.handlers.delete;

import global.moja.fluxestoreportingvariables.util.builders.QueryParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import global.moja.fluxestoreportingvariables.exceptions.ServerException;
import global.moja.fluxestoreportingvariables.repository.FluxesToReportingVariablesRepository;
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
public class DeleteFluxesToReportingVariablesHandler {

    @Autowired
    FluxesToReportingVariablesRepository repository;

    /**
     * Deletes all fluxes to reporting variable records or specific fluxes to reporting variable records if given their
     * unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the fluxes to reporting variable
     *                records to be deleted
     * @return the response containing the number of fluxes to reporting variable records deleted
     */
    public Mono<ServerResponse> deleteFluxesToReportingVariables(ServerRequest request) {

        log.trace("Entering deleteFluxesToReportingVariables()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteFluxesToReportingVariablesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Fluxes To Reporting Variable records deletion failed", e));
    }


    private Mono<Integer> deleteFluxesToReportingVariablesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteFluxesToReportingVariables(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .startPoolId(request)
                                        .endPoolId(request)
                                        .reportingVariableId(request)
                                        .rule(request)
                                        .build());
    }


}

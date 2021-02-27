/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.handlers.delete;

import global.moja.reportingvariables.exceptions.ServerException;
import global.moja.reportingvariables.repository.ReportingVariablesRepository;
import global.moja.reportingvariables.util.builders.QueryParametersBuilder;
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
public class DeleteReportingVariablesHandler {

    @Autowired
    ReportingVariablesRepository repository;

    /**
     * Deletes all Reporting Variables or specific Reporting Variables records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Reporting Variables records to be deleted
     * @return the response containing the number of Reporting Variables records deleted
     */
    public Mono<ServerResponse> deleteReportingVariables(ServerRequest request) {

        log.trace("Entering deleteReportingVariables()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteReportingVariablesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Reporting Variables deletion failed", e));
    }


    private Mono<Integer> deleteReportingVariablesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteReportingVariables(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .reportingFrameworkId(request)
                                        .name(request)
                                        .build());
    }


}

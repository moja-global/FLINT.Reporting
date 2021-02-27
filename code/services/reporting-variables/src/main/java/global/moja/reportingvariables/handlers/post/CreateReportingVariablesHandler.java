/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.handlers.post;

import global.moja.reportingvariables.exceptions.ServerException;
import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.repository.ReportingVariablesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class CreateReportingVariablesHandler {

    @Autowired
    ReportingVariablesRepository repository;


    /**
     * Recursively creates Reporting Variables records
     *
     * @param request the request containing the details of the Reporting Variables records to be created
     * @return the stream of responses containing the details of the newly created Reporting Variables records
     */
    public Mono<ServerResponse> createReportingVariables(ServerRequest request) {

        log.trace("Entering createReportingVariables()");

        return
                request
                        .bodyToMono(ReportingVariable[].class)
                        .flatMap(units ->
                                ServerResponse
                                        .status(HttpStatus.CREATED)
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(createReportingVariables(units), ReportingVariable.class))
                        .onErrorMap(e -> new ServerException("Reporting Variables creation failed", e));
    }

    private Flux<ReportingVariable> createReportingVariables(ReportingVariable[] reportingVariables) {

        return
                repository
                        .insertReportingVariables(
                                Arrays.stream(reportingVariables)
                                        .sorted()
                                        .toArray(ReportingVariable[]::new))
                        .flatMap(id -> repository.selectReportingVariable(id));

    }


}

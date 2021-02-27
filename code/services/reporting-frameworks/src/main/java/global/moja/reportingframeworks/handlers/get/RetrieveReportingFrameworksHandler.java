/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframeworks.handlers.get;

import global.moja.reportingframeworks.repository.ReportingFrameworksRepository;
import global.moja.reportingframeworks.exceptions.ServerException;
import global.moja.reportingframeworks.models.ReportingFramework;
import global.moja.reportingframeworks.util.builders.QueryParametersBuilder;
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
public class RetrieveReportingFrameworksHandler {

    @Autowired
    ReportingFrameworksRepository repository;

    /**
     * Retrieves all Reporting Frameworks or specific Reporting Frameworks if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Reporting Frameworks records to be retrieved
     * @return the stream of responses containing the details of the retrieved Reporting Frameworks records
     */
    public Mono<ServerResponse> retrieveReportingFrameworks(ServerRequest request) {

        log.trace("Entering retrieveReportingFrameworks()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveReportingFrameworksConsideringQueryParameters(request),
                                ReportingFramework.class)
                        .onErrorMap(e -> new ServerException("Reporting Frameworks retrieval failed", e));
    }

    private Flux<ReportingFramework> retrieveReportingFrameworksConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectReportingFrameworks(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .build());
    }


}

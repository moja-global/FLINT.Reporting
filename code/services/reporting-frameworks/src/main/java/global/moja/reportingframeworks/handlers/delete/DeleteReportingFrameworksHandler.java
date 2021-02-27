/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframeworks.handlers.delete;

import global.moja.reportingframeworks.repository.ReportingFrameworksRepository;
import global.moja.reportingframeworks.util.builders.QueryParametersBuilder;
import global.moja.reportingframeworks.exceptions.ServerException;
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
public class DeleteReportingFrameworksHandler {

    @Autowired
    ReportingFrameworksRepository repository;

    /**
     * Deletes all Reporting Frameworks or specific Reporting Frameworks records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Reporting Frameworks records to be deleted
     * @return the response containing the number of Reporting Frameworks records deleted
     */
    public Mono<ServerResponse> deleteReportingFrameworks(ServerRequest request) {

        log.trace("Entering deleteReportingFrameworks()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteReportingFrameworksConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Reporting Frameworks deletion failed", e));
    }


    private Mono<Integer> deleteReportingFrameworksConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteReportingFrameworks(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .build());
    }


}

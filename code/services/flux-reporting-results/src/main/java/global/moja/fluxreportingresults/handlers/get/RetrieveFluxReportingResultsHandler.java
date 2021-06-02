/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.handlers.get;

import global.moja.fluxreportingresults.exceptions.ServerException;
import global.moja.fluxreportingresults.models.FluxReportingResult;
import global.moja.fluxreportingresults.repository.FluxReportingResultsRepository;
import global.moja.fluxreportingresults.util.builders.QueryParametersBuilder;
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
public class RetrieveFluxReportingResultsHandler {

    @Autowired
    FluxReportingResultsRepository repository;

    /**
     * Retrieves all Flux Reporting Results or specific Flux Reporting Results if given query filters
     *
     * @param request the request, optionally containing the unique identifiers of the Flux Reporting Results records to be retrieved
     * @return the stream of responses containing the details of the retrieved Flux Reporting Results records
     */
    public Mono<ServerResponse> retrieveFluxReportingResults(ServerRequest request) {

        log.trace("Entering retrieveFluxReportingResults()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveFluxReportingResultsConsideringQueryParameters(request),
                                FluxReportingResult.class)
                        .onErrorMap(e -> new ServerException("Flux Reporting Results retrieval failed", e));
    }

    private Flux<FluxReportingResult> retrieveFluxReportingResultsConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectFluxReportingResults(
                                Long.parseLong(request.pathVariable("databaseId")),
                                new QueryParametersBuilder()
                                        .id(request)
                                        .dateId(request)
                                        .locationId(request)
                                        .fluxTypeId(request)
                                        .sourcePoolId(request)
                                        .sinkPoolId(request)
                                        .itemCount(request)
                                        .build());
    }


}

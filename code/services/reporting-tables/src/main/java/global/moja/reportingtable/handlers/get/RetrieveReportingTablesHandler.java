/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.handlers.get;

import global.moja.reportingtable.models.ReportingTable;
import global.moja.reportingtable.util.builders.QueryParametersBuilder;
import global.moja.reportingtable.exceptions.ServerException;
import global.moja.reportingtable.repository.ReportingTablesRepository;
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
public class RetrieveReportingTablesHandler {

    @Autowired
    ReportingTablesRepository repository;

    /**
     * Retrieves all Reporting Tables or specific Reporting Tables if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Reporting Tables records to be retrieved
     * @return the stream of responses containing the details of the retrieved Reporting Tables records
     */
    public Mono<ServerResponse> retrieveReportingTables(ServerRequest request) {

        log.trace("Entering retrieveReportingTables()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveReportingTablesConsideringQueryParameters(request),
                                ReportingTable.class)
                        .onErrorMap(e -> new ServerException("Reporting Tables retrieval failed", e));
    }

    private Flux<ReportingTable> retrieveReportingTablesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectReportingTables(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .reportingFrameworkId(request)
                                        .number(request)
                                        .name(request)
                                        .build());
    }


}

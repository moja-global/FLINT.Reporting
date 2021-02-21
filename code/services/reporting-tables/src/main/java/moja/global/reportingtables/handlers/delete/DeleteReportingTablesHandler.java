/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.handlers.delete;

import moja.global.reportingtables.exceptions.ServerException;
import moja.global.reportingtables.repository.ReportingTablesRepository;
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
public class DeleteReportingTablesHandler {

    @Autowired
    ReportingTablesRepository repository;

    /**
     * Deletes all reporting table records or specific reporting table records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the reporting table records to be deleted
     * @return the response containing the number of reporting table records deleted
     */
    public Mono<ServerResponse> deleteReportingTables(ServerRequest request) {

        log.trace("Entering deleteReportingTables()");

        return
                Mono.just(getIds(request))
                        .flatMap(ids ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(ids.length == 0 ? deleteAllReportingTables() : deleteReportingTablesByIds(ids), Integer.class))
                        .onErrorMap(e -> new ServerException("Reporting Table records deletion failed", e));
    }


    private Mono<Integer> deleteAllReportingTables() {

        return repository.deleteAllReportingTables();
    }

    private Mono<Integer> deleteReportingTablesByIds(Long[] ids) {

        return repository.deleteReportingTablesByIds(ids);
    }

    private Long[] getIds(ServerRequest request) {

        return
                request.queryParams().get("ids") == null ? new Long[]{}:
                        request.queryParams()
                                .get("ids")
                                .stream()
                                .sorted()
                                .map(Long::parseLong)
                                .toArray(Long[]::new);

    }


}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.handlers.get;

import global.moja.dates.exceptions.ServerException;
import global.moja.dates.models.Date;
import global.moja.dates.repository.DatesRepository;
import global.moja.dates.util.builders.QueryParametersBuilder;
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
public class RetrieveDatesHandler {

    @Autowired
    DatesRepository repository;

    /**
     * Retrieves all Dates or specific Dates if given query filters
     *
     * @param request the request, optionally containing the unique identifiers of the Dates records to be retrieved
     * @return the stream of responses containing the details of the retrieved Dates records
     */
    public Mono<ServerResponse> retrieveDates(ServerRequest request) {

        log.trace("Entering retrieveDates()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveDatesConsideringQueryParameters(request),
                                Date.class)
                        .onErrorMap(e -> new ServerException("Dates retrieval failed", e));
    }

    private Flux<Date> retrieveDatesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectDates(
                                Long.parseLong(request.pathVariable("databaseId")),
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .year(request)
                                        .build());
    }


}

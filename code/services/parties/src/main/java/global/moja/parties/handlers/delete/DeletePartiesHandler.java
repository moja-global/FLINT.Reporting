/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.handlers.delete;

import global.moja.parties.exceptions.ServerException;
import global.moja.parties.repository.PartiesRepository;
import global.moja.parties.util.builders.QueryParametersBuilder;
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
public class DeletePartiesHandler {

    @Autowired
    PartiesRepository repository;

    /**
     * Deletes all Parties or specific Parties records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Parties records to be deleted
     * @return the response containing the number of Parties records deleted
     */
    public Mono<ServerResponse> deleteParties(ServerRequest request) {

        log.trace("Entering deleteParties()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deletePartiesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Parties deletion failed", e));
    }


    private Mono<Integer> deletePartiesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteParties(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .partyTypeId(request)
                                        .name(request)
                                        .build());
    }


}

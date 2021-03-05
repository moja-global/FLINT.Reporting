/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.handlers.get;

import global.moja.parties.exceptions.ServerException;
import global.moja.parties.models.Party;
import global.moja.parties.repository.PartiesRepository;
import global.moja.parties.util.builders.QueryParametersBuilder;
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
public class RetrievePartiesHandler {

    @Autowired
    PartiesRepository repository;

    /**
     * Retrieves all Parties or specific Parties if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Parties records to be retrieved
     * @return the stream of responses containing the details of the retrieved Parties records
     */
    public Mono<ServerResponse> retrieveParties(ServerRequest request) {

        log.trace("Entering retrieveParties()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrievePartiesConsideringQueryParameters(request),
                                Party.class)
                        .onErrorMap(e -> new ServerException("Parties retrieval failed", e));
    }

    private Flux<Party> retrievePartiesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectParties(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .partyTypeId(request)
                                        .name(request)
                                        .build());
    }


}

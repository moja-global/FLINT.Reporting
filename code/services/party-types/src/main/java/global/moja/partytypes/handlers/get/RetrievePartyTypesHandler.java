/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.handlers.get;

import global.moja.partytypes.exceptions.ServerException;
import global.moja.partytypes.models.PartyType;
import global.moja.partytypes.repository.PartyTypesRepository;
import global.moja.partytypes.util.builders.QueryParametersBuilder;
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
public class RetrievePartyTypesHandler {

    @Autowired
    PartyTypesRepository repository;

    /**
     * Retrieves all Party Types or specific Party Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Party Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Party Types records
     */
    public Mono<ServerResponse> retrievePartyTypes(ServerRequest request) {

        log.trace("Entering retrievePartyTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrievePartyTypesConsideringQueryParameters(request),
                                PartyType.class)
                        .onErrorMap(e -> new ServerException("Party Types retrieval failed", e));
    }

    private Flux<PartyType> retrievePartyTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectPartyTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .parentPartyTypeId(request)
                                        .name(request)
                                        .build());
    }


}

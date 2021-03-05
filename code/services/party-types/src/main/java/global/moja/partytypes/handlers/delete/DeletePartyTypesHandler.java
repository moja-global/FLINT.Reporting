/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.handlers.delete;

import global.moja.partytypes.exceptions.ServerException;
import global.moja.partytypes.repository.PartyTypesRepository;
import global.moja.partytypes.util.builders.QueryParametersBuilder;
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
public class DeletePartyTypesHandler {

    @Autowired
    PartyTypesRepository repository;

    /**
     * Deletes all Party Types or specific Party Types records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Party Types records to be deleted
     * @return the response containing the number of Party Types records deleted
     */
    public Mono<ServerResponse> deletePartyTypes(ServerRequest request) {

        log.trace("Entering deletePartyTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deletePartyTypesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Party Types deletion failed", e));
    }


    private Mono<Integer> deletePartyTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deletePartyTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .parentPartyTypeId(request)
                                        .name(request)
                                        .build());
    }


}

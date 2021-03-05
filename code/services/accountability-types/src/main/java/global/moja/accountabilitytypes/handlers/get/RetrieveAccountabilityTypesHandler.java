/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.handlers.get;

import global.moja.accountabilitytypes.exceptions.ServerException;
import global.moja.accountabilitytypes.models.AccountabilityType;
import global.moja.accountabilitytypes.repository.AccountabilityTypesRepository;
import global.moja.accountabilitytypes.util.builders.QueryParametersBuilder;
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
public class RetrieveAccountabilityTypesHandler {

    @Autowired
    AccountabilityTypesRepository repository;

    /**
     * Retrieves all Accountability Types or specific Accountability Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Accountability Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Accountability Types records
     */
    public Mono<ServerResponse> retrieveAccountabilityTypes(ServerRequest request) {

        log.trace("Entering retrieveAccountabilityTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveAccountabilityTypesConsideringQueryParameters(request),
                                AccountabilityType.class)
                        .onErrorMap(e -> new ServerException("Accountability Types retrieval failed", e));
    }

    private Flux<AccountabilityType> retrieveAccountabilityTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectAccountabilityTypes(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .name(request)
                                        .build());
    }


}

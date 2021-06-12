/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.handlers.delete;

import global.moja.accountabilities.exceptions.ServerException;
import global.moja.accountabilities.repository.AccountabilitiesRepository;
import global.moja.accountabilities.util.builders.QueryParametersBuilder;
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
public class DeleteAccountabilitiesHandler {

    @Autowired
    AccountabilitiesRepository repository;

    /**
     * Deletes all Accountabilities or specific Accountabilities records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Accountabilities records to be deleted
     * @return the response containing the number of Accountabilities records deleted
     */
    public Mono<ServerResponse> deleteAccountabilities(ServerRequest request) {

        log.trace("Entering deleteAccountabilities()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteAccountabilitiesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Accountabilities deletion failed", e));
    }


    private Mono<Integer> deleteAccountabilitiesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteAccountabilities(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .accountabilityTypeId(request)
                                        .accountabilityRuleId(request)
                                        .parentPartyId(request)
                                        .subsidiaryPartyId(request)
                                        .build());
    }


}

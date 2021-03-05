/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.handlers.delete;

import global.moja.accountabilityrules.repository.AccountabilityRulesRepository;
import global.moja.accountabilityrules.util.builders.QueryParametersBuilder;
import global.moja.accountabilityrules.exceptions.ServerException;
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
public class DeleteAccountabilityRulesHandler {

    @Autowired
    AccountabilityRulesRepository repository;

    /**
     * Deletes all Accountability Rules or specific Accountability Rules records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Accountability Rules records to be deleted
     * @return the response containing the number of Accountability Rules records deleted
     */
    public Mono<ServerResponse> deleteAccountabilityRules(ServerRequest request) {

        log.trace("Entering deleteAccountabilityRules()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteAccountabilityRulesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Accountability Rules deletion failed", e));
    }


    private Mono<Integer> deleteAccountabilityRulesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteAccountabilityRules(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .accountabilityTypeId(request)
                                        .parentPartyTypeId(request)
                                        .subsidiaryPartyTypeId(request)
                                        .build());
    }


}

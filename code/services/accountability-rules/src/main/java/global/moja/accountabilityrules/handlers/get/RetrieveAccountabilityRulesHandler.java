/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.handlers.get;

import global.moja.accountabilityrules.models.AccountabilityRule;
import global.moja.accountabilityrules.repository.AccountabilityRulesRepository;
import global.moja.accountabilityrules.util.builders.QueryParametersBuilder;
import global.moja.accountabilityrules.exceptions.ServerException;
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
public class RetrieveAccountabilityRulesHandler {

    @Autowired
    AccountabilityRulesRepository repository;

    /**
     * Retrieves all Accountability Rules or specific Accountability Rules if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Accountability Rules records to be retrieved
     * @return the stream of responses containing the details of the retrieved Accountability Rules records
     */
    public Mono<ServerResponse> retrieveAccountabilityRules(ServerRequest request) {

        log.trace("Entering retrieveAccountabilityRules()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveAccountabilityRulesConsideringQueryParameters(request),
                                AccountabilityRule.class)
                        .onErrorMap(e -> new ServerException("Accountability Rules retrieval failed", e));
    }

    private Flux<AccountabilityRule> retrieveAccountabilityRulesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectAccountabilityRules(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .accountabilityTypeId(request)
                                        .parentPartyTypeId(request)
                                        .subsidiaryPartyTypeId(request)
                                        .build());
    }


}

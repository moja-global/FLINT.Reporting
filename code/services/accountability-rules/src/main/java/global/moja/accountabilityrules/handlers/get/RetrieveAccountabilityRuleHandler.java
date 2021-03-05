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
import global.moja.accountabilityrules.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class RetrieveAccountabilityRuleHandler {

	@Autowired
    AccountabilityRulesRepository repository;

	/**
	 * Retrieves a Accountability Rule record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Accountability Rule record to be retrieved
	 * @return the response containing the details of the retrieved Accountability Rule record
	 */
	public Mono<ServerResponse> retrieveAccountabilityRule(ServerRequest request) {

		log.trace("Entering retrieveAccountabilityRule()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveAccountabilityRuleById(Long.parseLong(request.pathVariable("id"))),
                        AccountabilityRule.class)
				.onErrorMap(e -> new ServerException("Accountability Rule retrieval failed", e));
	}


	private Mono<AccountabilityRule> retrieveAccountabilityRuleById(Long id) {

		return
			repository
				.selectAccountabilityRule(id);
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.handlers.post;

import global.moja.accountabilityrules.models.AccountabilityRule;
import global.moja.accountabilityrules.repository.AccountabilityRulesRepository;
import global.moja.accountabilityrules.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateAccountabilityRulesHandler {

	@Autowired
    AccountabilityRulesRepository repository;
	

	/**
	 * Recursively creates Accountability Rules records
	 *
	 * @param request the request containing the details of the Accountability Rules records to be created
	 * @return the stream of responses containing the details of the newly created Accountability Rules records
	 */
	public Mono<ServerResponse> createAccountabilityRules(ServerRequest request) {

		log.trace("Entering createAccountabilityRules()");

		return 
			request
				.bodyToMono(AccountabilityRule[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createAccountabilityRules(units), AccountabilityRule.class))
				.onErrorMap(e -> new ServerException("Accountability Rules creation failed", e));
	}
	
	private Flux<AccountabilityRule> createAccountabilityRules(AccountabilityRule[] accountabilityRules) {
		
		return
			repository
				.insertAccountabilityRules(accountabilityRules)
				.flatMap(id -> repository.selectAccountabilityRule(id));
			
	}



}

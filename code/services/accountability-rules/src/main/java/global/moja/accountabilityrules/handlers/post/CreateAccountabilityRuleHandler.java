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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateAccountabilityRuleHandler {

	@Autowired
    AccountabilityRulesRepository repository;
	
	/**
	 * Creates a Accountability Rule record
	 *
	 * @param request the request containing the details of the Accountability Rule record to be created
	 * @return the response containing the details of the newly created Accountability Rule record
	 */
	public Mono<ServerResponse> createAccountabilityRule(ServerRequest request) {

		log.trace("Entering createAccountabilityRule()");

		return 
			request
				.bodyToMono(AccountabilityRule.class)
				.flatMap(accountabilityRule ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createAccountabilityRule(accountabilityRule), AccountabilityRule.class))
				.onErrorMap(e -> new ServerException("Accountability Rule creation failed", e));
	}
	
	
	private Mono<AccountabilityRule> createAccountabilityRule(AccountabilityRule accountabilityRule){
		
		return 
			repository
				.insertAccountabilityRule(accountabilityRule)
				.flatMap(id -> repository.selectAccountabilityRule(id));
		  
	}

}

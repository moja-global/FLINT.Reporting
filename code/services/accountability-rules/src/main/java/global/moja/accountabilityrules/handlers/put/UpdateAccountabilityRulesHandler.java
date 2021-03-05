/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.handlers.put;

import global.moja.accountabilityrules.models.AccountabilityRule;
import global.moja.accountabilityrules.repository.AccountabilityRulesRepository;
import global.moja.accountabilityrules.exceptions.ServerException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateAccountabilityRulesHandler {

	@Autowired
    AccountabilityRulesRepository repository;
	
	/**
	 * Updates Accountability Rules records
	 * @param request the request containing the details of the Accountability Rules records to be updated
	 * @return the response containing the details of the newly updated Accountability Rules records
	 */
	public Mono<ServerResponse> updateAccountabilityRules(ServerRequest request) {

		log.trace("Entering updateAccountabilityRules()");

		return 
			request
					.bodyToMono(AccountabilityRule[].class)
					.flatMap(accountabilityRules->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateAccountabilityRules(accountabilityRules), AccountabilityRule.class))
					.onErrorMap(e -> new ServerException("Accountability Rules update failed", e));
	}
	
	private Flux<AccountabilityRule> updateAccountabilityRules(AccountabilityRule[] accountabilityRules) {
		return 
			Flux.fromStream(Arrays.stream(accountabilityRules).sorted())
				.flatMap(unit -> 
					repository
						.updateAccountabilityRule(unit)
						.flatMap(count -> repository.selectAccountabilityRule(unit.getId())));

	}

	


}

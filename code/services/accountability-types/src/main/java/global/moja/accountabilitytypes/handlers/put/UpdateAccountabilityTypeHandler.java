/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.handlers.put;

import global.moja.accountabilitytypes.exceptions.ServerException;
import global.moja.accountabilitytypes.models.AccountabilityType;
import global.moja.accountabilitytypes.repository.AccountabilityTypesRepository;
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
public class UpdateAccountabilityTypeHandler {

	@Autowired
    AccountabilityTypesRepository repository;
	
	/**
	 * Updates a Accountability Type record
	 * @param request the request containing the details of the Accountability Type record to be updated
	 * @return the response containing the details of the newly updated Accountability Type record
	 */
	public Mono<ServerResponse> updateAccountabilityType(ServerRequest request) {

		log.trace("Entering updateAccountabilityType()");

		return 
			request
				.bodyToMono(AccountabilityType.class)
					.flatMap(accountabilityType ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateAccountabilityType(accountabilityType), AccountabilityType.class))
					.onErrorMap(e -> new ServerException("Accountability Type update failed", e));
	}
	
	
	private Mono<AccountabilityType> updateAccountabilityType(AccountabilityType accountabilityType){
		
		return 
			repository
				.updateAccountabilityType(accountabilityType)
				.flatMap(count -> repository.selectAccountabilityType(accountabilityType.getId()));
	}
}

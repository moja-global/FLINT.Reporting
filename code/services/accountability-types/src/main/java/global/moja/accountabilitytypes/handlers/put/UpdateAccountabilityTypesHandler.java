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
public class UpdateAccountabilityTypesHandler {

	@Autowired
	AccountabilityTypesRepository repository;
	
	/**
	 * Updates Accountability Types records
	 * @param request the request containing the details of the Accountability Types records to be updated
	 * @return the response containing the details of the newly updated Accountability Types records
	 */
	public Mono<ServerResponse> updateAccountabilityTypes(ServerRequest request) {

		log.trace("Entering updateAccountabilityTypes()");

		return 
			request
					.bodyToMono(AccountabilityType[].class)
					.flatMap(accountabilityTypes->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateAccountabilityTypes(accountabilityTypes), AccountabilityType.class))
					.onErrorMap(e -> new ServerException("Accountability Types update failed", e));
	}
	
	private Flux<AccountabilityType> updateAccountabilityTypes(AccountabilityType[] accountabilityTypes) {
		return 
			Flux.fromStream(Arrays.stream(accountabilityTypes).sorted())
				.flatMap(unit -> 
					repository
						.updateAccountabilityType(unit)
						.flatMap(count -> repository.selectAccountabilityType(unit.getId())));

	}

	


}

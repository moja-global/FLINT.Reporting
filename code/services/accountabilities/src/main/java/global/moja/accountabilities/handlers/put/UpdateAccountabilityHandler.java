/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.handlers.put;

import global.moja.accountabilities.models.Accountability;
import global.moja.accountabilities.repository.AccountabilitiesRepository;
import global.moja.accountabilities.exceptions.ServerException;
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
public class UpdateAccountabilityHandler {

	@Autowired
    AccountabilitiesRepository repository;
	
	/**
	 * Updates a Accountability record
	 * @param request the request containing the details of the Accountability record to be updated
	 * @return the response containing the details of the newly updated Accountability record
	 */
	public Mono<ServerResponse> updateAccountability(ServerRequest request) {

		log.trace("Entering updateAccountability()");

		return 
			request
				.bodyToMono(Accountability.class)
					.flatMap(accountability ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateAccountability(accountability), Accountability.class))
					.onErrorMap(e -> new ServerException("Accountability update failed", e));
	}
	
	
	private Mono<Accountability> updateAccountability(Accountability accountability){
		
		return 
			repository
				.updateAccountability(accountability)
				.flatMap(count -> repository.selectAccountability(accountability.getId()));
	}
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.handlers.post;

import global.moja.accountabilities.models.Accountability;
import global.moja.accountabilities.repository.AccountabilitiesRepository;
import global.moja.accountabilities.exceptions.ServerException;
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
public class CreateAccountabilityHandler {

	@Autowired
    AccountabilitiesRepository repository;
	
	/**
	 * Creates a Accountability record
	 *
	 * @param request the request containing the details of the Accountability record to be created
	 * @return the response containing the details of the newly created Accountability record
	 */
	public Mono<ServerResponse> createAccountability(ServerRequest request) {

		log.trace("Entering createAccountability()");

		return 
			request
				.bodyToMono(Accountability.class)
				.flatMap(accountability ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createAccountability(accountability), Accountability.class))
				.onErrorMap(e -> new ServerException("Accountability creation failed", e));
	}
	
	
	private Mono<Accountability> createAccountability(Accountability accountability){
		
		return 
			repository
				.insertAccountability(accountability)
				.flatMap(id -> repository.selectAccountability(id));
		  
	}

}

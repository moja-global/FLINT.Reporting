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
public class UpdateAccountabilitiesHandler {

	@Autowired
    AccountabilitiesRepository repository;
	
	/**
	 * Updates Accountabilities records
	 * @param request the request containing the details of the Accountabilities records to be updated
	 * @return the response containing the details of the newly updated Accountabilities records
	 */
	public Mono<ServerResponse> updateAccountabilities(ServerRequest request) {

		log.trace("Entering updateAccountabilities()");

		return 
			request
					.bodyToMono(Accountability[].class)
					.flatMap(accountabilities->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateAccountabilities(accountabilities), Accountability.class))
					.onErrorMap(e -> new ServerException("Accountabilities update failed", e));
	}
	
	private Flux<Accountability> updateAccountabilities(Accountability[] accountabilities) {
		return 
			Flux.fromStream(Arrays.stream(accountabilities).sorted())
				.flatMap(unit -> 
					repository
						.updateAccountability(unit)
						.flatMap(count -> repository.selectAccountability(unit.getId())));

	}

	


}

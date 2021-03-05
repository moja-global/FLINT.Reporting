/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.handlers.get;

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
public class RetrieveAccountabilityHandler {

	@Autowired
    AccountabilitiesRepository repository;

	/**
	 * Retrieves a Accountability record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Accountability record to be retrieved
	 * @return the response containing the details of the retrieved Accountability record
	 */
	public Mono<ServerResponse> retrieveAccountability(ServerRequest request) {

		log.trace("Entering retrieveAccountability()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveAccountabilityById(Long.parseLong(request.pathVariable("id"))),
                        Accountability.class)
				.onErrorMap(e -> new ServerException("Accountability retrieval failed", e));
	}


	private Mono<Accountability> retrieveAccountabilityById(Long id) {

		return
			repository
				.selectAccountability(id);
	}

}

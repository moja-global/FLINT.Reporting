/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers.get;

import global.moja.databases.daos.DatabaseValidationStatus;
import global.moja.databases.exceptions.ServerException;
import global.moja.databases.services.DatabaseValidityTestService;
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
public class RetrieveDatabaseValidationResultsHandler {

	@Autowired
	DatabaseValidityTestService service;

	/**
	 * Tests the validity of a database given its url
	 *
	 * @param request the request containing the url of the database to be tested
	 * @return the response containing the database components validation status
	 */
	public Mono<ServerResponse> retrieveDatabaseValidationResults(ServerRequest request) {

		log.trace("Entering validateDatabase()");

		return
				request
						.bodyToMono(String.class)
						.flatMap(url ->
								ServerResponse
										.status(HttpStatus.OK)
										.contentType(MediaType.APPLICATION_JSON)
										.body(service.validateDatabase(url), DatabaseValidationStatus.class))
						.onErrorMap(e -> new ServerException("Database validation failed", e));
	}

}

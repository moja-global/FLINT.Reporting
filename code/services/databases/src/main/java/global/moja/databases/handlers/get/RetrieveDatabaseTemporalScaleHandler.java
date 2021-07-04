/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers.get;

import global.moja.databases.daos.DatabaseTemporalScale;
import global.moja.databases.exceptions.ServerException;
import global.moja.databases.services.DatabaseTemporalCoverageService;
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
public class RetrieveDatabaseTemporalScaleHandler {

	@Autowired
	DatabaseTemporalCoverageService service;

	/**
	 * Retrieves the temporal scale of a database (in terms of years) given its url
	 *
	 * @param request the request containing the url of the target database
	 * @return the response containing the year range
	 */
	public Mono<ServerResponse> retrieveDatabaseTemporalScale(ServerRequest request) {

		log.trace("Entering retrieveDatabaseTemporalCoverage()");

		return
				request
						.bodyToMono(String.class)
						.flatMap(url ->
								ServerResponse
										.status(HttpStatus.OK)
										.contentType(MediaType.APPLICATION_JSON)
										.body(service.retrieveDatabaseTemporalScale(url), DatabaseTemporalScale.class))
						.onErrorMap(e -> new ServerException("Database Temporal Scale retrieval failed", e));
	}

}

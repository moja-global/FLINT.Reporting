/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.handlers.post;

import global.moja.taskmanager.services.DatabaseIntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PostDatabaseIntegrationTaskHandler {

	@Autowired
	DatabaseIntegrationService databaseIntegrationService;

	/**
	 * Submits the Database Integration request
	 *
	 * @param request the request containing the unique identifier of the Database to be integrated
	 * @return the response indication whether the background task was successfully submitted
	 */
	public Mono<ServerResponse> postDatabaseIntegrationTask(ServerRequest request) {

		log.trace("Entering postDatabaseIntegrationTask()");

		databaseIntegrationService.integrateDatabase(Long.parseLong(request.pathVariable("databaseId")));

		return ServerResponse.ok().build();
	}


}

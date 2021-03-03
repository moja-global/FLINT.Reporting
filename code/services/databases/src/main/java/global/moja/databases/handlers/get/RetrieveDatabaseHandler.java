/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers.get;

import global.moja.databases.exceptions.ServerException;
import global.moja.databases.repository.DatabasesRepository;
import global.moja.databases.models.Database;
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
public class RetrieveDatabaseHandler {

	@Autowired
    DatabasesRepository repository;

	/**
	 * Retrieves a Database record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Database record to be retrieved
	 * @return the response containing the details of the retrieved Database record
	 */
	public Mono<ServerResponse> retrieveDatabase(ServerRequest request) {

		log.trace("Entering retrieveDatabase()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveDatabaseById(Long.parseLong(request.pathVariable("id"))),
                        Database.class)
				.onErrorMap(e -> new ServerException("Database retrieval failed", e));
	}


	private Mono<Database> retrieveDatabaseById(Long id) {

		return
			repository
				.selectDatabase(id);
	}

}

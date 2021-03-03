/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.handlers.put;

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
public class UpdateDatabaseHandler {

	@Autowired
    DatabasesRepository repository;
	
	/**
	 * Updates a Database record
	 * @param request the request containing the details of the Database record to be updated
	 * @return the response containing the details of the newly updated Database record
	 */
	public Mono<ServerResponse> updateDatabase(ServerRequest request) {

		log.trace("Entering updateDatabase()");

		return 
			request
				.bodyToMono(Database.class)
					.flatMap(database ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateDatabase(database), Database.class))
					.onErrorMap(e -> new ServerException("Database update failed", e));
	}


	private Mono<Database> updateDatabase(Database database){

		return
			repository
				.updateDatabase(database)
				.flatMap(count -> repository.selectDatabase(database.getId()));
	}



}

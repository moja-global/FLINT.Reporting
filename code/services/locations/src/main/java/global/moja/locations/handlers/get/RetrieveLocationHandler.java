/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.handlers.get;

import global.moja.locations.exceptions.ServerException;
import global.moja.locations.models.Location;
import global.moja.locations.repository.LocationsRepository;
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
public class RetrieveLocationHandler {

	@Autowired
    LocationsRepository repository;

	/**
	 * Retrieves a Location record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Location record to be retrieved
	 * @return the response containing the details of the retrieved Location record
	 */
	public Mono<ServerResponse> retrieveLocation(ServerRequest request) {

		log.trace("Entering retrieveLocation()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveLocationById(request),
                        Location.class)
				.onErrorMap(e -> new ServerException("Location retrieval failed", e));
	}


	private Mono<Location> retrieveLocationById(ServerRequest request) {

		return
			repository
				.selectLocation(
						Long.parseLong(request.pathVariable("databaseId")),
						Long.parseLong(request.pathVariable("id"))
				);
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.handlers.get;

import global.moja.quantityobservations.exceptions.ServerException;
import global.moja.quantityobservations.repository.QuantityObservationsRepository;
import global.moja.quantityobservations.models.QuantityObservation;
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
public class RetrieveQuantityObservationHandler {

	@Autowired
    QuantityObservationsRepository repository;

	/**
	 * Retrieves a Quantity Observation record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Quantity Observation record to be retrieved
	 * @return the response containing the details of the retrieved Quantity Observation record
	 */
	public Mono<ServerResponse> retrieveQuantityObservation(ServerRequest request) {

		log.trace("Entering retrieveQuantityObservation()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveQuantityObservationById(Long.parseLong(request.pathVariable("id"))),
                        QuantityObservation.class)
				.onErrorMap(e -> new ServerException("Quantity Observation retrieval failed", e));
	}


	private Mono<QuantityObservation> retrieveQuantityObservationById(Long id) {

		return
			repository
				.selectQuantityObservation(id);
	}

}

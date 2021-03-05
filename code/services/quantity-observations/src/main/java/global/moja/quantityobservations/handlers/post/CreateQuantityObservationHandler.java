/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.handlers.post;

import global.moja.quantityobservations.exceptions.ServerException;
import global.moja.quantityobservations.models.QuantityObservation;
import global.moja.quantityobservations.repository.QuantityObservationsRepository;
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
public class CreateQuantityObservationHandler {

	@Autowired
    QuantityObservationsRepository repository;
	
	/**
	 * Creates a Quantity Observation record
	 *
	 * @param request the request containing the details of the Quantity Observation record to be created
	 * @return the response containing the details of the newly created Quantity Observation record
	 */
	public Mono<ServerResponse> createQuantityObservation(ServerRequest request) {

		log.trace("Entering createQuantityObservation()");

		return 
			request
				.bodyToMono(QuantityObservation.class)
				.flatMap(quantityObservation ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createQuantityObservation(quantityObservation), QuantityObservation.class))
				.onErrorMap(e -> new ServerException("Quantity Observation creation failed", e));
	}
	
	
	private Mono<QuantityObservation> createQuantityObservation(QuantityObservation quantityObservation){
		
		return 
			repository
				.insertQuantityObservation(quantityObservation)
				.flatMap(id -> repository.selectQuantityObservation(id));
		  
	}

}

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateQuantityObservationsHandler {

	@Autowired
    QuantityObservationsRepository repository;
	

	/**
	 * Recursively creates Quantity Observations records
	 *
	 * @param request the request containing the details of the Quantity Observations records to be created
	 * @return the stream of responses containing the details of the newly created Quantity Observations records
	 */
	public Mono<ServerResponse> createQuantityObservations(ServerRequest request) {

		log.trace("Entering createQuantityObservations()");

		return 
			request
				.bodyToMono(QuantityObservation[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createQuantityObservations(units), QuantityObservation.class))
				.onErrorMap(e -> new ServerException("Quantity Observations creation failed", e));
	}
	
	private Flux<QuantityObservation> createQuantityObservations(QuantityObservation[] quantityObservations) {
		
		return
			repository
				.insertQuantityObservations(quantityObservations)
				.flatMap(id -> repository.selectQuantityObservation(id));
			
	}



}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.handlers.put;

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
public class UpdateQuantityObservationHandler {

	@Autowired
    QuantityObservationsRepository repository;
	
	/**
	 * Updates a Quantity Observation record
	 * @param request the request containing the details of the Quantity Observation record to be updated
	 * @return the response containing the details of the newly updated Quantity Observation record
	 */
	public Mono<ServerResponse> updateQuantityObservation(ServerRequest request) {

		log.trace("Entering updateQuantityObservation()");

		return 
			request
				.bodyToMono(QuantityObservation.class)
					.flatMap(quantityObservation ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateQuantityObservation(quantityObservation), QuantityObservation.class))
					.onErrorMap(e -> new ServerException("Quantity Observation update failed", e));
	}
	
	
	private Mono<QuantityObservation> updateQuantityObservation(QuantityObservation quantityObservation){
		
		return 
			repository
				.updateQuantityObservation(quantityObservation)
				.flatMap(count -> repository.selectQuantityObservation(quantityObservation.getId()));
	}
}

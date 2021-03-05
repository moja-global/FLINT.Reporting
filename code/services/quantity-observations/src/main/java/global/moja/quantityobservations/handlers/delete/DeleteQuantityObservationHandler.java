/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.handlers.delete;

import global.moja.quantityobservations.exceptions.ServerException;
import global.moja.quantityobservations.repository.QuantityObservationsRepository;
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
public class DeleteQuantityObservationHandler {

	@Autowired
    QuantityObservationsRepository repository;
	
	/**
	 * Deletes a Quantity Observation record
	 *
	 * @param request the request containing the details of the Quantity Observation record to be deleted
	 * @return the response containing the number of Quantity Observations records deleted
	 */
	public Mono<ServerResponse> deleteQuantityObservation(ServerRequest request) {

		log.trace("Entering deleteQuantityObservation()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteQuantityObservationById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Quantity Observation deletion failed", e));

	}
	
	private Mono<Integer> deleteQuantityObservationById(Long id){
		
		return 
			repository
				.deleteQuantityObservationById(id);
	}

}

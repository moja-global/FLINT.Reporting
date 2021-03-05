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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateQuantityObservationsHandler {

	@Autowired
    QuantityObservationsRepository repository;
	
	/**
	 * Updates Quantity Observations records
	 * @param request the request containing the details of the Quantity Observations records to be updated
	 * @return the response containing the details of the newly updated Quantity Observations records
	 */
	public Mono<ServerResponse> updateQuantityObservations(ServerRequest request) {

		log.trace("Entering updateQuantityObservations()");

		return 
			request
					.bodyToMono(QuantityObservation[].class)
					.flatMap(quantityObservations->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateQuantityObservations(quantityObservations), QuantityObservation.class))
					.onErrorMap(e -> new ServerException("Quantity Observations update failed", e));
	}
	
	private Flux<QuantityObservation> updateQuantityObservations(QuantityObservation[] quantityObservations) {
		return 
			Flux.fromStream(Arrays.stream(quantityObservations).sorted())
				.flatMap(unit -> 
					repository
						.updateQuantityObservation(unit)
						.flatMap(count -> repository.selectQuantityObservation(unit.getId())));

	}

	


}

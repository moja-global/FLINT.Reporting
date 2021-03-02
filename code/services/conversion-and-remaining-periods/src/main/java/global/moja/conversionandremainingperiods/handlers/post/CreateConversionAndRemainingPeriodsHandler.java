/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.handlers.post;

import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import global.moja.conversionandremainingperiods.exceptions.ServerException;
import global.moja.conversionandremainingperiods.repository.ConversionAndRemainingPeriodsRepository;
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
public class CreateConversionAndRemainingPeriodsHandler {

	@Autowired
    ConversionAndRemainingPeriodsRepository repository;
	

	/**
	 * Recursively creates Conversion and Remaining Periods records
	 *
	 * @param request the request containing the details of the Conversion and Remaining Periods records to be created
	 * @return the stream of responses containing the details of the newly created Conversion and Remaining Periods records
	 */
	public Mono<ServerResponse> createConversionAndRemainingPeriods(ServerRequest request) {

		log.trace("Entering createConversionAndRemainingPeriods()");

		return 
			request
				.bodyToMono(ConversionAndRemainingPeriod[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createConversionAndRemainingPeriods(units), ConversionAndRemainingPeriod.class))
				.onErrorMap(e -> new ServerException("Conversion and Remaining Periods creation failed", e));
	}
	
	private Flux<ConversionAndRemainingPeriod> createConversionAndRemainingPeriods(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {
		
		return
			repository
				.insertConversionAndRemainingPeriods(conversionAndRemainingPeriods)
				.flatMap(id -> repository.selectConversionAndRemainingPeriod(id));
			
	}



}

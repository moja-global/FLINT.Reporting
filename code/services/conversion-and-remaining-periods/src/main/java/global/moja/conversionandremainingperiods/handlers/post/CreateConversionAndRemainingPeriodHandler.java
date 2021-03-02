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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateConversionAndRemainingPeriodHandler {

	@Autowired
    ConversionAndRemainingPeriodsRepository repository;
	
	/**
	 * Creates a Conversion and Remaining Period record
	 *
	 * @param request the request containing the details of the Conversion and Remaining Period record to be created
	 * @return the response containing the details of the newly created Conversion and Remaining Period record
	 */
	public Mono<ServerResponse> createConversionAndRemainingPeriod(ServerRequest request) {

		log.trace("Entering createConversionAndRemainingPeriod()");

		return 
			request
				.bodyToMono(ConversionAndRemainingPeriod.class)
				.flatMap(conversionAndRemainingPeriod ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createConversionAndRemainingPeriod(conversionAndRemainingPeriod), ConversionAndRemainingPeriod.class))
				.onErrorMap(e -> new ServerException("Conversion and Remaining Period creation failed", e));
	}
	
	
	private Mono<ConversionAndRemainingPeriod> createConversionAndRemainingPeriod(ConversionAndRemainingPeriod conversionAndRemainingPeriod){
		
		return 
			repository
				.insertConversionAndRemainingPeriod(conversionAndRemainingPeriod)
				.flatMap(id -> repository.selectConversionAndRemainingPeriod(id));
		  
	}

}

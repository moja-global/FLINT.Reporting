/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.handlers.put;

import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import global.moja.conversionandremainingperiods.exceptions.ServerException;
import global.moja.conversionandremainingperiods.repository.ConversionAndRemainingPeriodsRepository;
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
public class UpdateConversionAndRemainingPeriodHandler {

	@Autowired
    ConversionAndRemainingPeriodsRepository repository;
	
	/**
	 * Updates a Conversion and Remaining Period record
	 * @param request the request containing the details of the Conversion and Remaining Period record to be updated
	 * @return the response containing the details of the newly updated Conversion and Remaining Period record
	 */
	public Mono<ServerResponse> updateConversionAndRemainingPeriod(ServerRequest request) {

		log.trace("Entering updateConversionAndRemainingPeriod()");

		return 
			request
				.bodyToMono(ConversionAndRemainingPeriod.class)
					.flatMap(conversionAndRemainingPeriod ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateConversionAndRemainingPeriod(conversionAndRemainingPeriod), ConversionAndRemainingPeriod.class))
					.onErrorMap(e -> new ServerException("Conversion and Remaining Period update failed", e));
	}
	
	
	private Mono<ConversionAndRemainingPeriod> updateConversionAndRemainingPeriod(ConversionAndRemainingPeriod conversionAndRemainingPeriod){
		
		return 
			repository
				.updateConversionAndRemainingPeriod(conversionAndRemainingPeriod)
				.flatMap(count -> repository.selectConversionAndRemainingPeriod(conversionAndRemainingPeriod.getId()));
	}
}

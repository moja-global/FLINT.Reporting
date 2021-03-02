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
public class UpdateConversionAndRemainingPeriodsHandler {

	@Autowired
    ConversionAndRemainingPeriodsRepository repository;
	
	/**
	 * Updates Conversion and Remaining Periods records
	 * @param request the request containing the details of the Conversion and Remaining Periods records to be updated
	 * @return the response containing the details of the newly updated Conversion and Remaining Periods records
	 */
	public Mono<ServerResponse> updateConversionAndRemainingPeriods(ServerRequest request) {

		log.trace("Entering updateConversionAndRemainingPeriods()");

		return 
			request
					.bodyToMono(ConversionAndRemainingPeriod[].class)
					.flatMap(conversionAndRemainingPeriods->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateConversionAndRemainingPeriods(conversionAndRemainingPeriods), ConversionAndRemainingPeriod.class))
					.onErrorMap(e -> new ServerException("Conversion and Remaining Periods update failed", e));
	}
	
	private Flux<ConversionAndRemainingPeriod> updateConversionAndRemainingPeriods(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {
		return 
			Flux.fromStream(Arrays.stream(conversionAndRemainingPeriods).sorted())
				.flatMap(unit -> 
					repository
						.updateConversionAndRemainingPeriod(unit)
						.flatMap(count -> repository.selectConversionAndRemainingPeriod(unit.getId())));

	}

	


}

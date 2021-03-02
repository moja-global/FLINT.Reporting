/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.handlers.delete;

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
public class DeleteConversionAndRemainingPeriodHandler {

	@Autowired
    ConversionAndRemainingPeriodsRepository repository;
	
	/**
	 * Deletes a Conversion and Remaining Period record
	 *
	 * @param request the request containing the details of the Conversion and Remaining Period record to be deleted
	 * @return the response containing the number of Conversion and Remaining Periods records deleted
	 */
	public Mono<ServerResponse> deleteConversionAndRemainingPeriod(ServerRequest request) {

		log.trace("Entering deleteConversionAndRemainingPeriod()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteConversionAndRemainingPeriodById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Conversion and Remaining Period deletion failed", e));

	}
	
	private Mono<Integer> deleteConversionAndRemainingPeriodById(Long id){
		
		return 
			repository
				.deleteConversionAndRemainingPeriodById(id);
	}

}

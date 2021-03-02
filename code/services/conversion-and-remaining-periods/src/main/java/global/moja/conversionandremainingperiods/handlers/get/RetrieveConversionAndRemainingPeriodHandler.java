/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.handlers.get;

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
public class RetrieveConversionAndRemainingPeriodHandler {

	@Autowired
    ConversionAndRemainingPeriodsRepository repository;

	/**
	 * Retrieves a Conversion and Remaining Period record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Conversion and Remaining Period record to be retrieved
	 * @return the response containing the details of the retrieved Conversion and Remaining Period record
	 */
	public Mono<ServerResponse> retrieveConversionAndRemainingPeriod(ServerRequest request) {

		log.trace("Entering retrieveConversionAndRemainingPeriod()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveConversionAndRemainingPeriodById(Long.parseLong(request.pathVariable("id"))),
                        ConversionAndRemainingPeriod.class)
				.onErrorMap(e -> new ServerException("Conversion and Remaining Period retrieval failed", e));
	}


	private Mono<ConversionAndRemainingPeriod> retrieveConversionAndRemainingPeriodById(Long id) {

		return
			repository
				.selectConversionAndRemainingPeriod(id);
	}

}

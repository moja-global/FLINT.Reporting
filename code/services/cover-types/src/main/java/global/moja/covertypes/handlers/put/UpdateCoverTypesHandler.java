/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.handlers.put;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.exceptions.ServerException;
import global.moja.covertypes.repository.CoverTypesRepository;
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
public class UpdateCoverTypesHandler {

	@Autowired
    CoverTypesRepository repository;
	
	/**
	 * Updates Cover Types records
	 * @param request the request containing the details of the Cover Types records to be updated
	 * @return the response containing the details of the newly updated Cover Types records
	 */
	public Mono<ServerResponse> updateCoverTypes(ServerRequest request) {

		log.trace("Entering updateCoverTypes()");

		return 
			request
					.bodyToMono(CoverType[].class)
					.flatMap(coverTypes->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateCoverTypes(coverTypes), CoverType.class))
					.onErrorMap(e -> new ServerException("Cover Types update failed", e));
	}
	
	private Flux<CoverType> updateCoverTypes(CoverType[] coverTypes) {
		return 
			Flux.fromStream(Arrays.stream(coverTypes).sorted())
				.flatMap(unit -> 
					repository
						.updateCoverType(unit)
						.flatMap(count -> repository.selectCoverType(unit.getId())));

	}

	


}

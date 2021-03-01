/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.handlers.put;

import global.moja.landusesfluxtypes.exceptions.ServerException;
import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.repository.LandUsesFluxTypesRepository;
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
public class UpdateLandUseFluxTypeHandler {

	@Autowired
    LandUsesFluxTypesRepository repository;
	
	/**
	 * Updates a Land Use Flux Type record
	 * @param request the request containing the details of the Land Use Flux Type record to be updated
	 * @return the response containing the details of the newly updated Land Use Flux Type record
	 */
	public Mono<ServerResponse> updateLandUseFluxType(ServerRequest request) {

		log.trace("Entering updateLandUseFluxType()");

		return 
			request
				.bodyToMono(LandUseFluxType.class)
					.flatMap(landUseFluxType ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateLandUseFluxType(landUseFluxType), LandUseFluxType.class))
					.onErrorMap(e -> new ServerException("Land Use Flux Type update failed", e));
	}
	
	
	private Mono<LandUseFluxType> updateLandUseFluxType(LandUseFluxType landUseFluxType){
		
		return 
			repository
				.updateLandUseFluxType(landUseFluxType)
				.flatMap(count -> repository.selectLandUseFluxType(landUseFluxType.getId()));
	}
}

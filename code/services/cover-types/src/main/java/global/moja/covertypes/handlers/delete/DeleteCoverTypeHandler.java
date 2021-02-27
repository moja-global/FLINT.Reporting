/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.handlers.delete;

import global.moja.covertypes.exceptions.ServerException;
import global.moja.covertypes.repository.CoverTypesRepository;
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
public class DeleteCoverTypeHandler {

	@Autowired
    CoverTypesRepository repository;
	
	/**
	 * Deletes a Cover Type record
	 *
	 * @param request the request containing the details of the Cover Type record to be deleted
	 * @return the response containing the number of Cover Types records deleted
	 */
	public Mono<ServerResponse> deleteCoverType(ServerRequest request) {

		log.trace("Entering deleteCoverType()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteCoverTypeById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("Cover Type deletion failed", e));

	}
	
	private Mono<Integer> deleteCoverTypeById(Long id){
		
		return 
			repository
				.deleteCoverTypeById(id);
	}

}

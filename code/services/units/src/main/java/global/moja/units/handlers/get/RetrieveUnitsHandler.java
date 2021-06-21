/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.handlers.get;

import global.moja.units.models.Unit;
import global.moja.units.exceptions.ServerException;
import global.moja.units.repository.UnitsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class RetrieveUnitsHandler {

	@Autowired
    UnitsRepository repository;
		
	/**
	 * Retrieves all unit records or specific unit records if given their unique identifiers
	 * @param request the request, optionally containing the unique identifiers of the unit records to be retrieved
	 * @return the stream of responses containing the details of the retrieved unit records
	 */
	public Mono<ServerResponse> retrieveUnits(ServerRequest request) {

		log.trace("Entering retrieveUnits()");

		return
			Mono.just(getIds(request))
				.flatMap(ids ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(ids.length == 0 ? retrieveAllUnits() : retrieveUnitsByIds(ids), Unit.class))
				.onErrorMap(e -> new ServerException("Unit records retrieval failed", e));
	}
	
	private Flux<Unit> retrieveAllUnits() {
		return 
			repository
				.selectAllUnits();
	}		

	private Flux<Unit> retrieveUnitsByIds(Long[] ids) {
		
		return 
			repository
				.selectUnitsByIds(ids);
	}

	private Long[] getIds(ServerRequest request) {

		return
				request.queryParams().get("ids") == null ? new Long[]{}:
						request.queryParams()
								.get("ids")
								.stream()
								.map(Long::parseLong)
								.sorted()
								.toArray(Long[]::new);

	}

}

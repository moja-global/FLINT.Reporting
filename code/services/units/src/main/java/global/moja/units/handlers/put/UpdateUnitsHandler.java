/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.handlers.put;

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

import java.util.Arrays;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateUnitsHandler {

	@Autowired
    UnitsRepository repository;
	
	/**
	 * Updates unit records
	 * @param request the request containing the details of the unit records to be updated
	 * @return the response containing the details of the newly updated unit records
	 */
	public Mono<ServerResponse> updateUnits(ServerRequest request) {

		log.trace("Entering updateUnits()");

		return 
			request
					.bodyToMono(Unit[].class)
					.flatMap(units ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateUnits(units), Unit.class))
					.onErrorMap(e -> new ServerException("Unit records update failed", e));
	}
	
	private Flux<Unit> updateUnits(Unit[] units) {
		return 
			Flux.fromStream(Arrays.stream(units).sorted())
				.flatMap(unit -> 
					repository
						.updateUnit(unit)
						.flatMap(count -> repository.selectUnitById(unit.getId())));

	}

	


}

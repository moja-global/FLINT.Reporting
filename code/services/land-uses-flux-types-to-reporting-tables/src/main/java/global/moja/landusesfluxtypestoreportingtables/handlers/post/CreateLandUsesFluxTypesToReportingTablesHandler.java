/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.handlers.post;

import global.moja.landusesfluxtypestoreportingtables.exceptions.ServerException;
import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
import global.moja.landusesfluxtypestoreportingtables.repository.LandUsesFluxTypesToReportingTablesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
public class CreateLandUsesFluxTypesToReportingTablesHandler {

	@Autowired
    LandUsesFluxTypesToReportingTablesRepository repository;
	

	/**
	 * Recursively creates Land Uses Flux Types To Reporting Tables records
	 *
	 * @param request the request containing the details of the Land Uses Flux Types To Reporting Tables records to be created
	 * @return the stream of responses containing the details of the newly created Land Uses Flux Types To Reporting Tables records
	 */
	public Mono<ServerResponse> createLandUsesFluxTypesToReportingTables(ServerRequest request) {

		log.trace("Entering createLandUsesFluxTypesToReportingTables()");

		return 
			request
				.bodyToMono(LandUseFluxTypeToReportingTable[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createLandUsesFluxTypesToReportingTables(units), LandUseFluxTypeToReportingTable.class))
				.onErrorMap(e -> new ServerException("Land Uses Flux Types To Reporting Tables creation failed", e));
	}
	
	private Flux<LandUseFluxTypeToReportingTable> createLandUsesFluxTypesToReportingTables(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {
		
		return
			repository
				.insertLandUsesFluxTypesToReportingTables(landUsesFluxTypesToReportingTables)
				.flatMap(id -> repository.selectLandUseFluxTypeToReportingTable(id));
			
	}



}

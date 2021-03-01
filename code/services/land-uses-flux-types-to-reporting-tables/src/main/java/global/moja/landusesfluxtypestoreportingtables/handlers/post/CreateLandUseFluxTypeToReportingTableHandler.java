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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateLandUseFluxTypeToReportingTableHandler {

	@Autowired
    LandUsesFluxTypesToReportingTablesRepository repository;
	
	/**
	 * Creates a LandUseFluxTypeToReportingTable record
	 *
	 * @param request the request containing the details of the LandUseFluxTypeToReportingTable record to be created
	 * @return the response containing the details of the newly created LandUseFluxTypeToReportingTable record
	 */
	public Mono<ServerResponse> createLandUseFluxTypeToReportingTable(ServerRequest request) {

		log.trace("Entering createLandUseFluxTypeToReportingTable()");

		return 
			request
				.bodyToMono(LandUseFluxTypeToReportingTable.class)
				.flatMap(landUseFluxTypeToReportingTable ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createLandUseFluxTypeToReportingTable(landUseFluxTypeToReportingTable), LandUseFluxTypeToReportingTable.class))
				.onErrorMap(e -> new ServerException("LandUseFluxTypeToReportingTable creation failed", e));
	}
	
	
	private Mono<LandUseFluxTypeToReportingTable> createLandUseFluxTypeToReportingTable(LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable){
		
		return 
			repository
				.insertLandUseFluxTypeToReportingTable(landUseFluxTypeToReportingTable)
				.flatMap(id -> repository.selectLandUseFluxTypeToReportingTable(id));
		  
	}

}

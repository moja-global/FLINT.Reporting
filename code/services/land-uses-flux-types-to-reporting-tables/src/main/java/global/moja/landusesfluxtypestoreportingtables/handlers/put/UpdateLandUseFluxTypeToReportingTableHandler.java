/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.handlers.put;

import global.moja.landusesfluxtypestoreportingtables.exceptions.ServerException;
import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
import global.moja.landusesfluxtypestoreportingtables.repository.LandUsesFluxTypesToReportingTablesRepository;
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
public class UpdateLandUseFluxTypeToReportingTableHandler {

	@Autowired
    LandUsesFluxTypesToReportingTablesRepository repository;
	
	/**
	 * Updates a LandUseFluxTypeToReportingTable record
	 * @param request the request containing the details of the LandUseFluxTypeToReportingTable record to be updated
	 * @return the response containing the details of the newly updated LandUseFluxTypeToReportingTable record
	 */
	public Mono<ServerResponse> updateLandUseFluxTypeToReportingTable(ServerRequest request) {

		log.trace("Entering updateLandUseFluxTypeToReportingTable()");

		return 
			request
				.bodyToMono(LandUseFluxTypeToReportingTable.class)
					.flatMap(landUseFluxTypeToReportingTable ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateLandUseFluxTypeToReportingTable(landUseFluxTypeToReportingTable), LandUseFluxTypeToReportingTable.class))
					.onErrorMap(e -> new ServerException("LandUseFluxTypeToReportingTable update failed", e));
	}
	
	
	private Mono<LandUseFluxTypeToReportingTable> updateLandUseFluxTypeToReportingTable(LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable){
		
		return 
			repository
				.updateLandUseFluxTypeToReportingTable(landUseFluxTypeToReportingTable)
				.flatMap(count -> repository.selectLandUseFluxTypeToReportingTable(landUseFluxTypeToReportingTable.getId()));
	}
}

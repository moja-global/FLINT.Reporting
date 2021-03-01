/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.handlers.delete;

import global.moja.landusesfluxtypestoreportingtables.exceptions.ServerException;
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
public class DeleteLandUseFluxTypeToReportingTableHandler {

	@Autowired
    LandUsesFluxTypesToReportingTablesRepository repository;
	
	/**
	 * Deletes a LandUseFluxTypeToReportingTable record
	 *
	 * @param request the request containing the details of the LandUseFluxTypeToReportingTable record to be deleted
	 * @return the response containing the number of Land Uses Flux Types To Reporting Tables records deleted
	 */
	public Mono<ServerResponse> deleteLandUseFluxTypeToReportingTable(ServerRequest request) {

		log.trace("Entering deleteLandUseFluxTypeToReportingTable()");
		
		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(deleteLandUseFluxTypeToReportingTableById(Long.parseLong(request.pathVariable("id"))),Integer.class)
				.onErrorMap(e -> new ServerException("LandUseFluxTypeToReportingTable deletion failed", e));

	}
	
	private Mono<Integer> deleteLandUseFluxTypeToReportingTableById(Long id){
		
		return 
			repository
				.deleteLandUseFluxTypeToReportingTableById(id);
	}

}

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
public class UpdateLandUsesFluxTypesToReportingTablesHandler {

	@Autowired
    LandUsesFluxTypesToReportingTablesRepository repository;
	
	/**
	 * Updates Land Uses Flux Types To Reporting Tables records
	 * @param request the request containing the details of the Land Uses Flux Types To Reporting Tables records to be updated
	 * @return the response containing the details of the newly updated Land Uses Flux Types To Reporting Tables records
	 */
	public Mono<ServerResponse> updateLandUsesFluxTypesToReportingTables(ServerRequest request) {

		log.trace("Entering updateLandUsesFluxTypesToReportingTables()");

		return 
			request
					.bodyToMono(LandUseFluxTypeToReportingTable[].class)
					.flatMap(landUsesFluxTypesToReportingTables->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateLandUsesFluxTypesToReportingTables(landUsesFluxTypesToReportingTables), LandUseFluxTypeToReportingTable.class))
					.onErrorMap(e -> new ServerException("Land Uses Flux Types To Reporting Tables update failed", e));
	}
	
	private Flux<LandUseFluxTypeToReportingTable> updateLandUsesFluxTypesToReportingTables(LandUseFluxTypeToReportingTable[] landUsesFluxTypesToReportingTables) {
		return 
			Flux.fromStream(Arrays.stream(landUsesFluxTypesToReportingTables).sorted())
				.flatMap(unit -> 
					repository
						.updateLandUseFluxTypeToReportingTable(unit)
						.flatMap(count -> repository.selectLandUseFluxTypeToReportingTable(unit.getId())));

	}

	


}

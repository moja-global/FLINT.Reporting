/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.handlers.get;

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
public class RetrieveLandUseFluxTypeToReportingTableHandler {

	@Autowired
    LandUsesFluxTypesToReportingTablesRepository repository;

	/**
	 * Retrieves a LandUseFluxTypeToReportingTable record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the LandUseFluxTypeToReportingTable record to be retrieved
	 * @return the response containing the details of the retrieved LandUseFluxTypeToReportingTable record
	 */
	public Mono<ServerResponse> retrieveLandUseFluxTypeToReportingTable(ServerRequest request) {

		log.trace("Entering retrieveLandUseFluxTypeToReportingTable()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveLandUseFluxTypeToReportingTableById(Long.parseLong(request.pathVariable("id"))),
                        LandUseFluxTypeToReportingTable.class)
				.onErrorMap(e -> new ServerException("LandUseFluxTypeToReportingTable retrieval failed", e));
	}


	private Mono<LandUseFluxTypeToReportingTable> retrieveLandUseFluxTypeToReportingTableById(Long id) {

		return
			repository
				.selectLandUseFluxTypeToReportingTable(id);
	}

}

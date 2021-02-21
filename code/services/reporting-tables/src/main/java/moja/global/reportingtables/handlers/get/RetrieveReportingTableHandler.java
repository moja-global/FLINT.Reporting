/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.handlers.get;

import moja.global.reportingtables.exceptions.ServerException;
import moja.global.reportingtables.models.ReportingTable;
import moja.global.reportingtables.repository.ReportingTablesRepository;
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
public class RetrieveReportingTableHandler {

	@Autowired
	ReportingTablesRepository repository;
	
	/**
	 * Retrieves an reporting table record given its unique identifier
	 * @param request the request containing the unique identifier of the reporting table record to be retrieved
	 * @return the response containing the details of the retrieved reporting table record
	 */
	public Mono<ServerResponse> retrieveReportingTable(ServerRequest request) {

		log.trace("Entering retrieveReportingTableById()");

		return 
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveReportingTableById(Long.parseLong(request.pathVariable("id"))), ReportingTable.class)
				.onErrorMap(e -> new ServerException("Reporting Table record retrieval failed", e));
	}
	
	
	private Mono<ReportingTable> retrieveReportingTableById(Long id) {
		
		return 
			repository
				.selectReportingTableById(id);
	}

}

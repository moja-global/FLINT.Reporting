/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.handlers.post;

import global.moja.reportingtable.models.ReportingTable;
import global.moja.reportingtable.exceptions.ServerException;
import global.moja.reportingtable.repository.ReportingTablesRepository;
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
public class CreateReportingTablesHandler {

	@Autowired
    ReportingTablesRepository repository;
	

	/**
	 * Recursively creates Reporting Tables records
	 *
	 * @param request the request containing the details of the Reporting Tables records to be created
	 * @return the stream of responses containing the details of the newly created Reporting Tables records
	 */
	public Mono<ServerResponse> createReportingTables(ServerRequest request) {

		log.trace("Entering createReportingTables()");

		return 
			request
				.bodyToMono(ReportingTable[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createReportingTables(units), ReportingTable.class))
				.onErrorMap(e -> new ServerException("Reporting Tables creation failed", e));
	}
	
	private Flux<ReportingTable> createReportingTables(ReportingTable[] reportingTables) {
		
		return
			repository
				.insertReportingTables(reportingTables)
				.flatMap(id -> repository.selectReportingTable(id));
			
	}



}

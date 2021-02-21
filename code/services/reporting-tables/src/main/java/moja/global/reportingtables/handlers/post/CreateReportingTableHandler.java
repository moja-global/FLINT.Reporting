/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.handlers.post;

import moja.global.reportingtables.exceptions.ServerException;
import moja.global.reportingtables.models.ReportingTable;
import moja.global.reportingtables.repository.ReportingTablesRepository;
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
public class CreateReportingTableHandler {

	@Autowired
	ReportingTablesRepository repository;
	
	/**
	 * Creates an reporting table record
	 * @param request the request containing the details of the reporting table record to be created
	 * @return the response containing the details of the newly created reporting table record
	 */
	public Mono<ServerResponse> createReportingTable(ServerRequest request) {

		log.trace("Entering createReportingTable()");

		return 
			request
				.bodyToMono(ReportingTable.class)
				.flatMap(unit -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createReportingTable(unit), ReportingTable.class))
				.onErrorMap(e -> new ServerException("Reporting Table record creation failed", e));
	}
	
	
	private Mono<ReportingTable> createReportingTable(ReportingTable reportingTable){
		
		return 
			repository
				.insertReportingTable(reportingTable)
				.flatMap(id -> repository.selectReportingTableById(id));
		  
	}

}

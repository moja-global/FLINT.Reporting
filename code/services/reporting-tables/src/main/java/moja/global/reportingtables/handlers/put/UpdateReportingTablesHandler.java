/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.handlers.put;

import moja.global.reportingtables.models.ReportingTable;
import moja.global.reportingtables.exceptions.ServerException;
import moja.global.reportingtables.repository.ReportingTablesRepository;
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
public class UpdateReportingTablesHandler {

	@Autowired
	ReportingTablesRepository repository;
	
	/**
	 * Updates reporting table records
	 * @param request the request containing the details of the reporting table records to be updated
	 * @return the response containing the details of the newly updated reporting table records
	 */
	public Mono<ServerResponse> updateReportingTables(ServerRequest request) {

		log.trace("Entering updateReportingTables()");

		return 
			request
					.bodyToMono(ReportingTable[].class)
					.flatMap(units ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateReportingTables(units), ReportingTable.class))
					.onErrorMap(e -> new ServerException("Reporting Table records update failed", e));
	}
	
	private Flux<ReportingTable> updateReportingTables(ReportingTable[] reportingTables) {
		return 
			Flux.fromStream(Arrays.stream(reportingTables).sorted())
				.flatMap(unit -> 
					repository
						.updateReportingTable(unit)
						.flatMap(count -> repository.selectReportingTableById(unit.getId())));

	}

	


}

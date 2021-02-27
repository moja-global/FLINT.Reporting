/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.handlers.put;

import global.moja.reportingtable.models.ReportingTable;
import global.moja.reportingtable.exceptions.ServerException;
import global.moja.reportingtable.repository.ReportingTablesRepository;
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
public class UpdateReportingTableHandler {

	@Autowired
    ReportingTablesRepository repository;
	
	/**
	 * Updates a Reporting Table record
	 * @param request the request containing the details of the Reporting Table record to be updated
	 * @return the response containing the details of the newly updated Reporting Table record
	 */
	public Mono<ServerResponse> updateReportingTable(ServerRequest request) {

		log.trace("Entering updateReportingTable()");

		return 
			request
				.bodyToMono(ReportingTable.class)
					.flatMap(reportingTable ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateReportingTable(reportingTable), ReportingTable.class))
					.onErrorMap(e -> new ServerException("Reporting Table update failed", e));
	}
	
	
	private Mono<ReportingTable> updateReportingTable(ReportingTable reportingTable){
		
		return 
			repository
				.updateReportingTable(reportingTable)
				.flatMap(count -> repository.selectReportingTable(reportingTable.getId()));
	}
}

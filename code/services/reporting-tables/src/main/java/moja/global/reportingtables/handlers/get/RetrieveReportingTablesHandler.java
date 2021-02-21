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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class RetrieveReportingTablesHandler {

	@Autowired
	ReportingTablesRepository repository;
		
	/**
	 * Retrieves all reporting table records or specific reporting table records if given their unique identifiers
	 * @param request the request, optionally containing the unique identifiers of the reporting table records to be retrieved
	 * @return the stream of responses containing the details of the retrieved reporting table records
	 */
	public Mono<ServerResponse> retrieveReportingTables(ServerRequest request) {

		log.trace("Entering retrieveReportingTables()");

		return
			Mono.just(getIds(request))
				.flatMap(ids ->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(ids.length == 0 ? retrieveAllReportingTables() : retrieveReportingTablesByIds(ids), ReportingTable.class))
				.onErrorMap(e -> new ServerException("Reporting Table records retrieval failed", e));
	}
	
	private Flux<ReportingTable> retrieveAllReportingTables() {
		return 
			repository
				.selectAllReportingTables();
	}		

	private Flux<ReportingTable> retrieveReportingTablesByIds(Long[] ids) {
		
		return 
			repository
				.selectReportingTablesByIds(ids);
	}

	private Long[] getIds(ServerRequest request) {

		return
				request.queryParams().get("ids") == null ? new Long[]{}:
						request.queryParams()
								.get("ids")
								.stream()
								.map(Long::parseLong)
								.sorted()
								.toArray(Long[]::new);

	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.handlers.get;


import global.moja.crftables.daos.Report;
import global.moja.crftables.services.ReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
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
public class RetrieveCRFTablesHandler {

	@Autowired
	ReportService reportService;

	public Mono<ServerResponse> retrieveCRFTables(ServerRequest request) {

		return generateReport(request)
				.flatMap(
						report -> ServerResponse
								.ok()
								.header("Content-Type", "application/vnd.ms-excel;")
								.header("Content-length", String.valueOf(report.getOutput().length()))
								.header("Content-Disposition", "attachment; filename=" + report.getParty().getName() + ".xlsx")
								.body(Mono.just(new FileSystemResource(report.getOutput())), Resource.class)
				).onErrorResume(
						e -> {
							log.error("Error retrieving CRF Tables report", e);
							return ServerResponse
									.status(HttpStatus.SERVICE_UNAVAILABLE)
									.build();
						}
				);

	}

	private Mono<Report> generateReport(ServerRequest request) {

		return
				reportService
						.generateReport(
								Long.parseLong(request.pathVariable("partyId")),
								Long.parseLong(request.pathVariable("databaseId")),
								Integer.parseInt(request.pathVariable("minYear")),
								Integer.parseInt(request.pathVariable("maxYear")));
	}

}

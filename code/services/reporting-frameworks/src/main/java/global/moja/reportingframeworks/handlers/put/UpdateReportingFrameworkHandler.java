/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframeworks.handlers.put;

import global.moja.reportingframeworks.repository.ReportingFrameworksRepository;
import global.moja.reportingframeworks.exceptions.ServerException;
import global.moja.reportingframeworks.models.ReportingFramework;
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
public class UpdateReportingFrameworkHandler {

	@Autowired
	ReportingFrameworksRepository repository;
	
	/**
	 * Updates a Reporting Framework record
	 * @param request the request containing the details of the Reporting Framework record to be updated
	 * @return the response containing the details of the newly updated Reporting Framework record
	 */
	public Mono<ServerResponse> updateReportingFramework(ServerRequest request) {

		log.trace("Entering updateReportingFramework()");

		return 
			request
				.bodyToMono(ReportingFramework.class)
					.flatMap(reportingFramework ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateReportingFramework(reportingFramework), ReportingFramework.class))
					.onErrorMap(e -> new ServerException("Reporting Framework update failed", e));
	}
	
	
	private Mono<ReportingFramework> updateReportingFramework(ReportingFramework reportingFramework){
		
		return 
			repository
				.updateReportingFramework(reportingFramework)
				.flatMap(count -> repository.selectReportingFramework(reportingFramework.getId()));
	}
}

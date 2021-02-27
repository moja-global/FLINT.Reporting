/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework.handlers.post;

import global.moja.reportingframework.repository.ReportingFrameworksRepository;
import global.moja.reportingframework.exceptions.ServerException;
import global.moja.reportingframework.models.ReportingFramework;
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
public class CreateReportingFrameworkHandler {

	@Autowired
	ReportingFrameworksRepository repository;
	
	/**
	 * Creates a Reporting Framework record
	 *
	 * @param request the request containing the details of the Reporting Framework record to be created
	 * @return the response containing the details of the newly created Reporting Framework record
	 */
	public Mono<ServerResponse> createReportingFramework(ServerRequest request) {

		log.trace("Entering createReportingFramework()");

		return 
			request
				.bodyToMono(ReportingFramework.class)
				.flatMap(reportingFramework ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createReportingFramework(reportingFramework), ReportingFramework.class))
				.onErrorMap(e -> new ServerException("Reporting Framework creation failed", e));
	}
	
	
	private Mono<ReportingFramework> createReportingFramework(ReportingFramework reportingFramework){
		
		return 
			repository
				.insertReportingFramework(reportingFramework)
				.flatMap(id -> repository.selectReportingFramework(id));
		  
	}

}

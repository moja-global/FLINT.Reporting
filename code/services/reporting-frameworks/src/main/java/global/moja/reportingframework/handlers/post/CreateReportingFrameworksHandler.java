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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class CreateReportingFrameworksHandler {

	@Autowired
	ReportingFrameworksRepository repository;
	

	/**
	 * Recursively creates Reporting Frameworks records
	 *
	 * @param request the request containing the details of the Reporting Frameworks records to be created
	 * @return the stream of responses containing the details of the newly created Reporting Frameworks records
	 */
	public Mono<ServerResponse> createReportingFrameworks(ServerRequest request) {

		log.trace("Entering createReportingFrameworks()");

		return 
			request
				.bodyToMono(ReportingFramework[].class)
				.flatMap(units -> 
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createReportingFrameworks(units), ReportingFramework.class))
				.onErrorMap(e -> new ServerException("Reporting Frameworks creation failed", e));
	}
	
	private Flux<ReportingFramework> createReportingFrameworks(ReportingFramework[] reportingFrameworks) {
		
		return
			repository
				.insertReportingFrameworks(reportingFrameworks)
				.flatMap(id -> repository.selectReportingFramework(id));
			
	}



}

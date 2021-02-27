/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.handlers.post;

import global.moja.reportingvariables.exceptions.ServerException;
import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.repository.ReportingVariablesRepository;
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
public class CreateReportingVariableHandler {

	@Autowired
    ReportingVariablesRepository repository;
	
	/**
	 * Creates a Reporting Variable record
	 *
	 * @param request the request containing the details of the Reporting Variable record to be created
	 * @return the response containing the details of the newly created Reporting Variable record
	 */
	public Mono<ServerResponse> createReportingVariable(ServerRequest request) {

		log.trace("Entering createReportingVariable()");

		return 
			request
				.bodyToMono(ReportingVariable.class)
				.flatMap(reportingVariable ->
					ServerResponse
						.status(HttpStatus.CREATED)
						.contentType(MediaType.APPLICATION_JSON)
						.body(createReportingVariable(reportingVariable), ReportingVariable.class))
				.onErrorMap(e -> new ServerException("Reporting Variable creation failed", e));
	}
	
	
	private Mono<ReportingVariable> createReportingVariable(ReportingVariable reportingVariable){
		
		return 
			repository
				.insertReportingVariable(reportingVariable)
				.flatMap(id -> repository.selectReportingVariable(id));
		  
	}

}

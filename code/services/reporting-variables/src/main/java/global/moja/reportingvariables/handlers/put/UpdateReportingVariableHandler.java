/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.handlers.put;

import global.moja.reportingvariables.exceptions.ServerException;
import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.repository.ReportingVariablesRepository;
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
public class UpdateReportingVariableHandler {

	@Autowired
    ReportingVariablesRepository repository;
	
	/**
	 * Updates a Reporting Variable record
	 * @param request the request containing the details of the Reporting Variable record to be updated
	 * @return the response containing the details of the newly updated Reporting Variable record
	 */
	public Mono<ServerResponse> updateReportingVariable(ServerRequest request) {

		log.trace("Entering updateReportingVariable()");

		return 
			request
				.bodyToMono(ReportingVariable.class)
					.flatMap(reportingVariable ->
						ServerResponse
								.ok()
								.contentType(MediaType.APPLICATION_JSON)
								.body(updateReportingVariable(reportingVariable), ReportingVariable.class))
					.onErrorMap(e -> new ServerException("Reporting Variable update failed", e));
	}
	
	
	private Mono<ReportingVariable> updateReportingVariable(ReportingVariable reportingVariable){
		
		return 
			repository
				.updateReportingVariable(reportingVariable)
				.flatMap(count -> repository.selectReportingVariable(reportingVariable.getId()));
	}
}

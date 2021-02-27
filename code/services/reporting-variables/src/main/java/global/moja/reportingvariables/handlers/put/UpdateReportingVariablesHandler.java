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
public class UpdateReportingVariablesHandler {

	@Autowired
    ReportingVariablesRepository repository;
	
	/**
	 * Updates Reporting Variables records
	 * @param request the request containing the details of the Reporting Variables records to be updated
	 * @return the response containing the details of the newly updated Reporting Variables records
	 */
	public Mono<ServerResponse> updateReportingVariables(ServerRequest request) {

		log.trace("Entering updateReportingVariables()");

		return 
			request
					.bodyToMono(ReportingVariable[].class)
					.flatMap(reportingVariables->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateReportingVariables(reportingVariables), ReportingVariable.class))
					.onErrorMap(e -> new ServerException("Reporting Variables update failed", e));
	}
	
	private Flux<ReportingVariable> updateReportingVariables(ReportingVariable[] reportingVariables) {
		return 
			Flux.just(
					Arrays.stream(reportingVariables)
					.sorted()
					.toArray(ReportingVariable[]::new))
				.flatMap(unit -> 
					repository
						.updateReportingVariable(unit)
						.flatMap(count -> repository.selectReportingVariable(unit.getId())));

	}

	


}

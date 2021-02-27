/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.handlers.get;

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
public class RetrieveReportingVariableHandler {

	@Autowired
    ReportingVariablesRepository repository;

	/**
	 * Retrieves a Reporting Variable record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Reporting Variable record to be retrieved
	 * @return the response containing the details of the retrieved Reporting Variable record
	 */
	public Mono<ServerResponse> retrieveReportingVariable(ServerRequest request) {

		log.trace("Entering retrieveReportingVariable()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveReportingVariableById(Long.parseLong(request.pathVariable("id"))),
                        ReportingVariable.class)
				.onErrorMap(e -> new ServerException("Reporting Variable retrieval failed", e));
	}


	private Mono<ReportingVariable> retrieveReportingVariableById(Long id) {

		return
			repository
				.selectReportingVariable(id);
	}

}

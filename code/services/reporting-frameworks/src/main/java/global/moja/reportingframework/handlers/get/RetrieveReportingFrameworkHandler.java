/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework.handlers.get;

import global.moja.reportingframework.repository.ReportingFrameworksRepository;
import global.moja.reportingframework.exceptions.ServerException;
import global.moja.reportingframework.models.ReportingFramework;
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
public class RetrieveReportingFrameworkHandler {

	@Autowired
    ReportingFrameworksRepository repository;

	/**
	 * Retrieves a Reporting Framework record given its unique identifier
	 *
	 * @param request the request containing the unique identifier of the Reporting Framework record to be retrieved
	 * @return the response containing the details of the retrieved Reporting Framework record
	 */
	public Mono<ServerResponse> retrieveReportingFramework(ServerRequest request) {

		log.trace("Entering retrieveReportingFramework()");

		return
			ServerResponse
				.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(retrieveReportingFrameworkById(Long.parseLong(request.pathVariable("id"))),
                        ReportingFramework.class)
				.onErrorMap(e -> new ServerException("Reporting Framework retrieval failed", e));
	}


	private Mono<ReportingFramework> retrieveReportingFrameworkById(Long id) {

		return
			repository
				.selectReportingFramework(id);
	}

}

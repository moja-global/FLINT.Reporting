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
public class UpdateReportingFrameworksHandler {

	@Autowired
	ReportingFrameworksRepository repository;
	
	/**
	 * Updates Reporting Frameworks records
	 * @param request the request containing the details of the Reporting Frameworks records to be updated
	 * @return the response containing the details of the newly updated Reporting Frameworks records
	 */
	public Mono<ServerResponse> updateReportingFrameworks(ServerRequest request) {

		log.trace("Entering updateReportingFrameworks()");

		return 
			request
					.bodyToMono(ReportingFramework[].class)
					.flatMap(reportingFrameworks->
						ServerResponse
							.ok()
							.contentType(MediaType.APPLICATION_JSON)
							.body(updateReportingFrameworks(reportingFrameworks), ReportingFramework.class))
					.onErrorMap(e -> new ServerException("Reporting Frameworks update failed", e));
	}
	
	private Flux<ReportingFramework> updateReportingFrameworks(ReportingFramework[] reportingFrameworks) {
		return 
			Flux.fromStream(Arrays.stream(reportingFrameworks).sorted())
				.flatMap(unit -> 
					repository
						.updateReportingFramework(unit)
						.flatMap(count -> repository.selectReportingFramework(unit.getId())));

	}

	


}

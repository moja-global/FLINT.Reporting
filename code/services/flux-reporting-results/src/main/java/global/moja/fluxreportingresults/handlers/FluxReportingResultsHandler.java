/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.handlers;

import global.moja.fluxreportingresults.handlers.get.RetrieveFluxReportingResultsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class FluxReportingResultsHandler {

	@Autowired
	RetrieveFluxReportingResultsHandler retrieveFluxReportingResultsHandler;


	// <editor-fold desc="GET">
	
	public Mono<ServerResponse> retrieveFluxReportingResults(ServerRequest request) {
		return this.retrieveFluxReportingResultsHandler.retrieveFluxReportingResults(request);
	}
	

	// </editor-fold>	

}

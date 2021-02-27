/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframeworks.handlers;

import global.moja.reportingframeworks.handlers.delete.DeleteReportingFrameworkHandler;
import global.moja.reportingframeworks.handlers.delete.DeleteReportingFrameworksHandler;
import global.moja.reportingframeworks.handlers.get.RetrieveReportingFrameworkHandler;
import global.moja.reportingframeworks.handlers.get.RetrieveReportingFrameworksHandler;
import global.moja.reportingframeworks.handlers.post.CreateReportingFrameworkHandler;
import global.moja.reportingframeworks.handlers.post.CreateReportingFrameworksHandler;
import global.moja.reportingframeworks.handlers.put.UpdateReportingFrameworkHandler;
import global.moja.reportingframeworks.handlers.put.UpdateReportingFrameworksHandler;
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
public class ReportingFrameworksHandler {

	// POST HANDLERS
	@Autowired
	CreateReportingFrameworkHandler createReportingFrameworkHandler;

	@Autowired
	CreateReportingFrameworksHandler createReportingFrameworksHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveReportingFrameworkHandler retrieveReportingFrameworkByIdHandler;
	
	@Autowired
	RetrieveReportingFrameworksHandler retrieveReportingFrameworksHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateReportingFrameworkHandler updateReportingFrameworkHandler;
	
	@Autowired
	UpdateReportingFrameworksHandler updateReportingFrameworksHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteReportingFrameworkHandler deleteReportingFrameworkByIdHandler;
	
	@Autowired
	DeleteReportingFrameworksHandler deleteReportingFrameworksHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createReportingFramework(ServerRequest request) {
		return this.createReportingFrameworkHandler.createReportingFramework(request);
	}
	
	public Mono<ServerResponse> createReportingFrameworks(ServerRequest request) {
		return createReportingFrameworksHandler.createReportingFrameworks(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveReportingFramework(ServerRequest request) {
		return this.retrieveReportingFrameworkByIdHandler.retrieveReportingFramework(request);
	}
	
	public Mono<ServerResponse> retrieveReportingFrameworks(ServerRequest request) {
		return this.retrieveReportingFrameworksHandler.retrieveReportingFrameworks(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateReportingFramework(ServerRequest request) {
		return this.updateReportingFrameworkHandler.updateReportingFramework(request);
	}
	
	public Mono<ServerResponse> updateReportingFrameworks(ServerRequest request) {
		return this.updateReportingFrameworksHandler.updateReportingFrameworks(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteReportingFramework(ServerRequest request) {
		return this.deleteReportingFrameworkByIdHandler.deleteReportingFramework(request);
	}
	
	public Mono<ServerResponse> deleteReportingFrameworks(ServerRequest request) {
		return this.deleteReportingFrameworksHandler.deleteReportingFrameworks(request);
	}	

	// </editor-fold>	

}

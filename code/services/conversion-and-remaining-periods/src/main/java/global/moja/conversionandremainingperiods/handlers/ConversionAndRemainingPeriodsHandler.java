/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.handlers;

import global.moja.conversionandremainingperiods.handlers.delete.DeleteConversionAndRemainingPeriodHandler;
import global.moja.conversionandremainingperiods.handlers.delete.DeleteConversionAndRemainingPeriodsHandler;
import global.moja.conversionandremainingperiods.handlers.get.RetrieveConversionAndRemainingPeriodsHandler;
import global.moja.conversionandremainingperiods.handlers.put.UpdateConversionAndRemainingPeriodHandler;
import global.moja.conversionandremainingperiods.handlers.put.UpdateConversionAndRemainingPeriodsHandler;
import global.moja.conversionandremainingperiods.handlers.post.CreateConversionAndRemainingPeriodsHandler;
import global.moja.conversionandremainingperiods.handlers.get.RetrieveConversionAndRemainingPeriodHandler;
import global.moja.conversionandremainingperiods.handlers.post.CreateConversionAndRemainingPeriodHandler;
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
public class ConversionAndRemainingPeriodsHandler {

	// POST HANDLERS
	@Autowired
	CreateConversionAndRemainingPeriodHandler createConversionAndRemainingPeriodHandler;

	@Autowired
	CreateConversionAndRemainingPeriodsHandler createConversionAndRemainingPeriodsHandler;

	
	// GET HANDLERS
	@Autowired
	RetrieveConversionAndRemainingPeriodHandler retrieveConversionAndRemainingPeriodByIdHandler;
	
	@Autowired
	RetrieveConversionAndRemainingPeriodsHandler retrieveConversionAndRemainingPeriodsHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateConversionAndRemainingPeriodHandler updateConversionAndRemainingPeriodHandler;
	
	@Autowired
	UpdateConversionAndRemainingPeriodsHandler updateConversionAndRemainingPeriodsHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteConversionAndRemainingPeriodHandler deleteConversionAndRemainingPeriodByIdHandler;
	
	@Autowired
	DeleteConversionAndRemainingPeriodsHandler deleteConversionAndRemainingPeriodsHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createConversionAndRemainingPeriod(ServerRequest request) {
		return this.createConversionAndRemainingPeriodHandler.createConversionAndRemainingPeriod(request);
	}
	
	public Mono<ServerResponse> createConversionAndRemainingPeriods(ServerRequest request) {
		return createConversionAndRemainingPeriodsHandler.createConversionAndRemainingPeriods(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveConversionAndRemainingPeriod(ServerRequest request) {
		return this.retrieveConversionAndRemainingPeriodByIdHandler.retrieveConversionAndRemainingPeriod(request);
	}
	
	public Mono<ServerResponse> retrieveConversionAndRemainingPeriods(ServerRequest request) {
		return this.retrieveConversionAndRemainingPeriodsHandler.retrieveConversionAndRemainingPeriods(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateConversionAndRemainingPeriod(ServerRequest request) {
		return this.updateConversionAndRemainingPeriodHandler.updateConversionAndRemainingPeriod(request);
	}
	
	public Mono<ServerResponse> updateConversionAndRemainingPeriods(ServerRequest request) {
		return this.updateConversionAndRemainingPeriodsHandler.updateConversionAndRemainingPeriods(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteConversionAndRemainingPeriod(ServerRequest request) {
		return this.deleteConversionAndRemainingPeriodByIdHandler.deleteConversionAndRemainingPeriod(request);
	}
	
	public Mono<ServerResponse> deleteConversionAndRemainingPeriods(ServerRequest request) {
		return this.deleteConversionAndRemainingPeriodsHandler.deleteConversionAndRemainingPeriods(request);
	}	

	// </editor-fold>	

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.handlers;

import global.moja.quantityobservations.handlers.delete.DeleteQuantityObservationsHandler;
import global.moja.quantityobservations.handlers.get.RetrieveQuantityObservationHandler;
import global.moja.quantityobservations.handlers.post.CreateQuantityObservationHandler;
import global.moja.quantityobservations.handlers.post.CreateQuantityObservationsHandler;
import global.moja.quantityobservations.handlers.put.UpdateQuantityObservationsHandler;
import global.moja.quantityobservations.handlers.delete.DeleteQuantityObservationHandler;
import global.moja.quantityobservations.handlers.get.RetrieveQuantityObservationsHandler;
import global.moja.quantityobservations.handlers.put.UpdateQuantityObservationHandler;
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
public class QuantityObservationsHandler {

	// POST HANDLERS
	@Autowired
    CreateQuantityObservationHandler createQuantityObservationHandler;

	@Autowired
    CreateQuantityObservationsHandler createQuantityObservationsHandler;

	
	// GET HANDLERS
	@Autowired
    RetrieveQuantityObservationHandler retrieveQuantityObservationByIdHandler;
	
	@Autowired
	RetrieveQuantityObservationsHandler retrieveQuantityObservationsHandler;

	
	// PUT HANDLERS
	@Autowired
	UpdateQuantityObservationHandler updateQuantityObservationHandler;
	
	@Autowired
    UpdateQuantityObservationsHandler updateQuantityObservationsHandler;

	
	// DELETE HANDLERS
	@Autowired
	DeleteQuantityObservationHandler deleteQuantityObservationByIdHandler;
	
	@Autowired
    DeleteQuantityObservationsHandler deleteQuantityObservationsHandler;

	
	// <editor-fold desc="POST">
	public Mono<ServerResponse> createQuantityObservation(ServerRequest request) {
		return this.createQuantityObservationHandler.createQuantityObservation(request);
	}
	
	public Mono<ServerResponse> createQuantityObservations(ServerRequest request) {
		return createQuantityObservationsHandler.createQuantityObservations(request);
	}

	// </editor-fold>

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveQuantityObservation(ServerRequest request) {
		return this.retrieveQuantityObservationByIdHandler.retrieveQuantityObservation(request);
	}
	
	public Mono<ServerResponse> retrieveQuantityObservations(ServerRequest request) {
		return this.retrieveQuantityObservationsHandler.retrieveQuantityObservations(request);
	}
	

	// </editor-fold>	

	// <editor-fold desc="PUT">
	public Mono<ServerResponse> updateQuantityObservation(ServerRequest request) {
		return this.updateQuantityObservationHandler.updateQuantityObservation(request);
	}
	
	public Mono<ServerResponse> updateQuantityObservations(ServerRequest request) {
		return this.updateQuantityObservationsHandler.updateQuantityObservations(request);
	}	

	// </editor-fold>	
	
	// <editor-fold desc="DELETE">
	public Mono<ServerResponse> deleteQuantityObservation(ServerRequest request) {
		return this.deleteQuantityObservationByIdHandler.deleteQuantityObservation(request);
	}
	
	public Mono<ServerResponse> deleteQuantityObservations(ServerRequest request) {
		return this.deleteQuantityObservationsHandler.deleteQuantityObservations(request);
	}	

	// </editor-fold>	

}

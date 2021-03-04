/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.handlers;

import global.moja.locations.handlers.get.RetrieveLocationHandler;
import global.moja.locations.handlers.get.RetrieveLocationsHandler;
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
public class LocationsHandler {

	// GET HANDLERS
	@Autowired
    RetrieveLocationHandler retrieveLocationByIdHandler;
	
	@Autowired
    RetrieveLocationsHandler retrieveLocationsHandler;

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveLocation(ServerRequest request) {
		return this.retrieveLocationByIdHandler.retrieveLocation(request);
	}
	
	public Mono<ServerResponse> retrieveLocations(ServerRequest request) {
		return this.retrieveLocationsHandler.retrieveLocations(request);
	}
	

	// </editor-fold>	

}

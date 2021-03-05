/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.handlers;

import global.moja.dates.handlers.get.RetrieveDateHandler;
import global.moja.dates.handlers.get.RetrieveDatesHandler;
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
public class DatesHandler {

	// GET HANDLERS
	@Autowired
    RetrieveDateHandler retrieveDateByIdHandler;
	
	@Autowired
    RetrieveDatesHandler retrieveDatesHandler;

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveDate(ServerRequest request) {
		return this.retrieveDateByIdHandler.retrieveDate(request);
	}
	
	public Mono<ServerResponse> retrieveDates(ServerRequest request) {
		return this.retrieveDatesHandler.retrieveDates(request);
	}
	

	// </editor-fold>	

}

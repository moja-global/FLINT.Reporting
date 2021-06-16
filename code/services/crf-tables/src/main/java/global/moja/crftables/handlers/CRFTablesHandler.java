/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.handlers;


import global.moja.crftables.handlers.get.RetrieveCRFTablesHandler;
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
public class CRFTablesHandler {


	// GET HANDLERS
	@Autowired
	RetrieveCRFTablesHandler retrieveCRFTablesHandler;


	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveCRFTables(ServerRequest request) {
		return retrieveCRFTablesHandler.retrieveCRFTables(request);
	}
	

	// </editor-fold>	

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes.handlers;

import global.moja.vegetationhistoryvegetationtypes.handlers.get.RetrieveVegetationHistoryVegetationTypeHandler;
import global.moja.vegetationhistoryvegetationtypes.handlers.get.RetrieveVegetationHistoryVegetationTypesHandler;
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
public class VegetationHistoryVegetationTypesHandler {

	
	// GET HANDLERS
	@Autowired
	RetrieveVegetationHistoryVegetationTypeHandler retrieveVegetationHistoryVegetationTypeByIdHandler;
	
	@Autowired
	RetrieveVegetationHistoryVegetationTypesHandler retrieveVegetationHistoryVegetationTypesHandler;

	// <editor-fold desc="GET">

	public Mono<ServerResponse> retrieveVegetationHistoryVegetationType(ServerRequest request) {
		return this.retrieveVegetationHistoryVegetationTypeByIdHandler.retrieveVegetationHistoryVegetationType(request);
	}
	
	public Mono<ServerResponse> retrieveVegetationHistoryVegetationTypes(ServerRequest request) {
		return this.retrieveVegetationHistoryVegetationTypesHandler.retrieveVegetationHistoryVegetationTypes(request);
	}
	

	// </editor-fold>	

}

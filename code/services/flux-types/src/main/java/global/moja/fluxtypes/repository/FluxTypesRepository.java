/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxtypes.repository;

import global.moja.fluxtypes.daos.QueryParameters;
import global.moja.fluxtypes.models.FluxType;
import global.moja.fluxtypes.repository.deletion.DeleteFluxTypeQuery;
import global.moja.fluxtypes.repository.deletion.DeleteFluxTypesQuery;
import global.moja.fluxtypes.repository.updation.UpdateFluxTypeQuery;
import global.moja.fluxtypes.repository.updation.UpdateFluxTypesQuery;
import global.moja.fluxtypes.repository.selection.SelectFluxTypesQuery;
import global.moja.fluxtypes.repository.insertion.InsertFluxTypeQuery;
import global.moja.fluxtypes.repository.insertion.InsertFluxTypesQuery;
import global.moja.fluxtypes.repository.selection.SelectFluxTypeQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class FluxTypesRepository {

	
	@Autowired
	InsertFluxTypeQuery insertFluxTypeQuery;
	
	@Autowired
	InsertFluxTypesQuery insertFluxTypesQuery;
	
	@Autowired
	SelectFluxTypeQuery selectFluxTypeQuery;
	
	@Autowired
	SelectFluxTypesQuery selectFluxTypesQuery;

	@Autowired
	UpdateFluxTypeQuery updateFluxTypeQuery;
	
	@Autowired
	UpdateFluxTypesQuery updateFluxTypesQuery;
	
	@Autowired
    DeleteFluxTypeQuery deleteFluxTypeQuery;
	
	@Autowired
    DeleteFluxTypesQuery deleteFluxTypesQuery;


	public Mono<Long> insertFluxType(FluxType fluxType) {
		return insertFluxTypeQuery.insertFluxType(fluxType);
	}
	
	public Flux<Long> insertFluxTypes(FluxType[] fluxTypes) {
		return insertFluxTypesQuery.insertFluxTypes(fluxTypes);
	}

	public Mono<FluxType> selectFluxType(Long id) {
		return selectFluxTypeQuery.selectFluxType(id);
	}
	
	public Flux<FluxType> selectFluxTypes(QueryParameters parameters) {
		return selectFluxTypesQuery.selectFluxTypes(parameters);
	}

	public Mono<Integer> updateFluxType(FluxType fluxType) {
		return updateFluxTypeQuery.updateFluxType(fluxType);
	}
	
	public Flux<Integer> updateFluxTypes(FluxType[] fluxTypes) {
		return updateFluxTypesQuery.updateFluxTypes(fluxTypes);
	}	
	
	public Mono<Integer> deleteFluxTypeById(Long id) {
		return deleteFluxTypeQuery.deleteFluxType(id);
	}
	
	public Mono<Integer> deleteFluxTypes(QueryParameters parameters) {
		return deleteFluxTypesQuery.deleteFluxTypes(parameters);
	}


}

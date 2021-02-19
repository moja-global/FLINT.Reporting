/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.fluxtypes.repository;

import ke.co.miles.fluxtypes.models.FluxType;
import ke.co.miles.fluxtypes.repository.deletion.DeleteAllFluxTypesQuery;
import ke.co.miles.fluxtypes.repository.deletion.DeleteFluxTypeByIdQuery;
import ke.co.miles.fluxtypes.repository.updation.UpdateFluxTypeQuery;
import ke.co.miles.fluxtypes.repository.deletion.DeleteFluxTypesByIdsQuery;
import ke.co.miles.fluxtypes.repository.insertion.InsertFluxTypeQuery;
import ke.co.miles.fluxtypes.repository.insertion.InsertFluxTypesQuery;
import ke.co.miles.fluxtypes.repository.selection.SelectAllFluxTypesQuery;
import ke.co.miles.fluxtypes.repository.selection.SelectFluxTypeByIdQuery;
import ke.co.miles.fluxtypes.repository.selection.SelectFluxTypesByIdsQuery;
import ke.co.miles.fluxtypes.repository.updation.UpdateFluxTypesQuery;
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
	SelectFluxTypeByIdQuery selectFluxTypeByIdQuery;
	
	@Autowired
	SelectFluxTypesByIdsQuery selectFluxTypesByIdsQuery;
	
	@Autowired
	SelectAllFluxTypesQuery selectAllFluxTypesQuery;

	@Autowired
	UpdateFluxTypeQuery updateFluxTypeQuery;
	
	@Autowired
	UpdateFluxTypesQuery updateFluxTypesQuery;
	
	@Autowired
	DeleteFluxTypeByIdQuery deleteFluxTypeByIdQuery;
	
	@Autowired
	DeleteFluxTypesByIdsQuery deleteFluxTypesByIdsQuery;
	
	@Autowired
	DeleteAllFluxTypesQuery deleteAllFluxTypesQuery;


	public Mono<Long> insertFluxType(FluxType fluxType) {
		return insertFluxTypeQuery.insertFluxType(fluxType);
	}
	
	public Flux<Long> insertFluxTypes(FluxType[] fluxTypes) {
		return insertFluxTypesQuery.insertFluxTypes(fluxTypes);
	}

	public Mono<FluxType> selectFluxTypeById(Long id) {
		return selectFluxTypeByIdQuery.selectFluxTypeById(id);
	}
	
	public Flux<FluxType> selectFluxTypesByIds(Long[] ids) {
		return selectFluxTypesByIdsQuery.selectFluxTypesByIds(ids);
	}

	public Flux<FluxType> selectAllFluxTypes() {
		return selectAllFluxTypesQuery.selectAllFluxTypes();
	}	

	public Mono<Integer> updateFluxType(FluxType fluxType) {
		return updateFluxTypeQuery.updateFluxType(fluxType);
	}
	
	public Flux<Integer> updateFluxTypes(FluxType[] fluxTypes) {
		return updateFluxTypesQuery.updateFluxTypes(fluxTypes);
	}	
	
	public Mono<Integer> deleteFluxTypeById(Long id) {
		return deleteFluxTypeByIdQuery.deleteFluxTypeById(id);
	}
	
	public Mono<Integer> deleteFluxTypesByIds(Long[] ids) {
		return deleteFluxTypesByIdsQuery.deleteFluxTypesByIds(ids);
	}

	public Mono<Integer> deleteAllFluxTypes() {
		return deleteAllFluxTypesQuery.deleteAllFluxTypes();
	}	

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.repository;

import global.moja.emissiontypes.daos.QueryParameters;
import global.moja.emissiontypes.models.EmissionType;
import global.moja.emissiontypes.repository.insertion.InsertEmissionTypeQuery;
import global.moja.emissiontypes.repository.insertion.InsertEmissionTypesQuery;
import global.moja.emissiontypes.repository.selection.SelectEmissionTypeQuery;
import global.moja.emissiontypes.repository.selection.SelectEmissionTypesQuery;
import global.moja.emissiontypes.repository.updation.UpdateEmissionTypeQuery;
import global.moja.emissiontypes.repository.updation.UpdateEmissionTypesQuery;
import global.moja.emissiontypes.repository.deletion.DeleteEmissionTypeQuery;
import global.moja.emissiontypes.repository.deletion.DeleteEmissionTypesQuery;
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
public class EmissionTypesRepository {

	
	@Autowired
    InsertEmissionTypeQuery insertEmissionTypeQuery;
	
	@Autowired
    InsertEmissionTypesQuery insertEmissionTypesQuery;
	
	@Autowired
    SelectEmissionTypeQuery selectEmissionTypeQuery;
	
	@Autowired
    SelectEmissionTypesQuery selectEmissionTypesQuery;

	@Autowired
	UpdateEmissionTypeQuery updateEmissionTypeQuery;
	
	@Autowired
	UpdateEmissionTypesQuery updateEmissionTypesQuery;
	
	@Autowired
	DeleteEmissionTypeQuery deleteEmissionTypeQuery;
	
	@Autowired
    DeleteEmissionTypesQuery deleteEmissionTypesQuery;


	public Mono<Long> insertEmissionType(EmissionType emissionType) {
		return insertEmissionTypeQuery.insertEmissionType(emissionType);
	}
	
	public Flux<Long> insertEmissionTypes(EmissionType[] emissionTypes) {
		return insertEmissionTypesQuery.insertEmissionTypes(emissionTypes);
	}

	public Mono<EmissionType> selectEmissionType(Long id) {
		return selectEmissionTypeQuery.selectEmissionType(id);
	}
	
	public Flux<EmissionType> selectEmissionTypes(QueryParameters parameters) {
		return selectEmissionTypesQuery.selectEmissionTypes(parameters);
	}

	public Mono<Integer> updateEmissionType(EmissionType emissionType) {
		return updateEmissionTypeQuery.updateEmissionType(emissionType);
	}
	
	public Flux<Integer> updateEmissionTypes(EmissionType[] emissionTypes) {
		return updateEmissionTypesQuery.updateEmissionTypes(emissionTypes);
	}	
	
	public Mono<Integer> deleteEmissionTypeById(Long id) {
		return deleteEmissionTypeQuery.deleteEmissionType(id);
	}
	
	public Mono<Integer> deleteEmissionTypes(QueryParameters parameters) {
		return deleteEmissionTypesQuery.deleteEmissionTypes(parameters);
	}


}

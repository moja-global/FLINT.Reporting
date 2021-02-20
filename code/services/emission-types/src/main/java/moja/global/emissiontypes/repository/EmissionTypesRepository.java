/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.repository;

import moja.global.emissiontypes.models.EmissionType;
import moja.global.emissiontypes.repository.deletion.DeleteAllEmissionTypesQuery;
import moja.global.emissiontypes.repository.deletion.DeleteEmissionTypeByIdQuery;
import moja.global.emissiontypes.repository.updation.UpdateEmissionTypeQuery;
import moja.global.emissiontypes.repository.deletion.DeleteEmissionTypesByIdsQuery;
import moja.global.emissiontypes.repository.insertion.InsertEmissionTypeQuery;
import moja.global.emissiontypes.repository.insertion.InsertEmissionTypesQuery;
import moja.global.emissiontypes.repository.selection.SelectAllEmissionTypesQuery;
import moja.global.emissiontypes.repository.selection.SelectEmissionTypeByIdQuery;
import moja.global.emissiontypes.repository.selection.SelectEmissionTypesByIdsQuery;
import moja.global.emissiontypes.repository.updation.UpdateEmissionTypesQuery;
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
	SelectEmissionTypeByIdQuery selectEmissionTypeByIdQuery;
	
	@Autowired
	SelectEmissionTypesByIdsQuery selectEmissionTypesByIdsQuery;
	
	@Autowired
	SelectAllEmissionTypesQuery selectAllEmissionTypesQuery;

	@Autowired
	UpdateEmissionTypeQuery updateEmissionTypeQuery;
	
	@Autowired
	UpdateEmissionTypesQuery updateEmissionTypesQuery;
	
	@Autowired
	DeleteEmissionTypeByIdQuery deleteEmissionTypeByIdQuery;
	
	@Autowired
	DeleteEmissionTypesByIdsQuery deleteEmissionTypesByIdsQuery;
	
	@Autowired
	DeleteAllEmissionTypesQuery deleteAllEmissionTypesQuery;


	public Mono<Long> insertEmissionType(EmissionType emissionType) {
		return insertEmissionTypeQuery.insertEmissionType(emissionType);
	}
	
	public Flux<Long> insertEmissionTypes(EmissionType[] emissionTypes) {
		return insertEmissionTypesQuery.insertEmissionTypes(emissionTypes);
	}

	public Mono<EmissionType> selectEmissionTypeById(Long id) {
		return selectEmissionTypeByIdQuery.selectEmissionTypeById(id);
	}
	
	public Flux<EmissionType> selectEmissionTypesByIds(Long[] ids) {
		return selectEmissionTypesByIdsQuery.selectEmissionTypesByIds(ids);
	}

	public Flux<EmissionType> selectAllEmissionTypes() {
		return selectAllEmissionTypesQuery.selectAllEmissionTypes();
	}	

	public Mono<Integer> updateEmissionType(EmissionType emissionType) {
		return updateEmissionTypeQuery.updateEmissionType(emissionType);
	}
	
	public Flux<Integer> updateEmissionTypes(EmissionType[] emissionTypes) {
		return updateEmissionTypesQuery.updateEmissionTypes(emissionTypes);
	}	
	
	public Mono<Integer> deleteEmissionTypeById(Long id) {
		return deleteEmissionTypeByIdQuery.deleteEmissionTypeById(id);
	}
	
	public Mono<Integer> deleteEmissionTypesByIds(Long[] ids) {
		return deleteEmissionTypesByIdsQuery.deleteEmissionTypesByIds(ids);
	}

	public Mono<Integer> deleteAllEmissionTypes() {
		return deleteAllEmissionTypesQuery.deleteAllEmissionTypes();
	}	

}

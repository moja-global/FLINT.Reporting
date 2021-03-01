/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.repository;

import global.moja.landusesfluxtypes.daos.QueryParameters;
import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.repository.deletion.DeleteLandUseFluxTypeQuery;
import global.moja.landusesfluxtypes.repository.deletion.DeleteLandUsesFluxTypesQuery;
import global.moja.landusesfluxtypes.repository.insertion.InsertLandUseFluxTypeQuery;
import global.moja.landusesfluxtypes.repository.insertion.InsertLandUsesFluxTypesQuery;
import global.moja.landusesfluxtypes.repository.selection.SelectLandUseFluxTypeQuery;
import global.moja.landusesfluxtypes.repository.selection.SelectLandUsesFluxTypesQuery;
import global.moja.landusesfluxtypes.repository.updation.UpdateLandUseFluxTypeQuery;
import global.moja.landusesfluxtypes.repository.updation.UpdateLandUsesFluxTypesQuery;
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
public class LandUsesFluxTypesRepository {

	
	@Autowired
    InsertLandUseFluxTypeQuery insertLandUseFluxTypeQuery;
	
	@Autowired
    InsertLandUsesFluxTypesQuery insertLandUsesFluxTypesQuery;
	
	@Autowired
    SelectLandUseFluxTypeQuery selectLandUseFluxTypeQuery;
	
	@Autowired
    SelectLandUsesFluxTypesQuery selectLandUsesFluxTypesQuery;

	@Autowired
    UpdateLandUseFluxTypeQuery updateLandUseFluxTypeQuery;
	
	@Autowired
    UpdateLandUsesFluxTypesQuery updateLandUsesFluxTypesQuery;
	
	@Autowired
    DeleteLandUseFluxTypeQuery deleteLandUseFluxTypeQuery;
	
	@Autowired
    DeleteLandUsesFluxTypesQuery deleteLandUsesFluxTypesQuery;


	public Mono<Long> insertLandUseFluxType(LandUseFluxType landUseFluxType) {
		return insertLandUseFluxTypeQuery.insertLandUseFluxType(landUseFluxType);
	}
	
	public Flux<Long> insertLandUsesFluxTypes(LandUseFluxType[] landUsesFluxTypes) {
		return insertLandUsesFluxTypesQuery.insertLandUsesFluxTypes(landUsesFluxTypes);
	}

	public Mono<LandUseFluxType> selectLandUseFluxType(Long id) {
		return selectLandUseFluxTypeQuery.selectLandUseFluxType(id);
	}
	
	public Flux<LandUseFluxType> selectLandUsesFluxTypes(QueryParameters parameters) {
		return selectLandUsesFluxTypesQuery.selectLandUsesFluxTypes(parameters);
	}

	public Mono<Integer> updateLandUseFluxType(LandUseFluxType landUseFluxType) {
		return updateLandUseFluxTypeQuery.updateLandUseFluxType(landUseFluxType);
	}
	
	public Flux<Integer> updateLandUsesFluxTypes(LandUseFluxType[] landUsesFluxTypes) {
		return updateLandUsesFluxTypesQuery.updateLandUsesFluxTypes(landUsesFluxTypes);
	}	
	
	public Mono<Integer> deleteLandUseFluxTypeById(Long id) {
		return deleteLandUseFluxTypeQuery.deleteLandUseFluxType(id);
	}
	
	public Mono<Integer> deleteLandUsesFluxTypes(QueryParameters parameters) {
		return deleteLandUsesFluxTypesQuery.deleteLandUsesFluxTypes(parameters);
	}


}

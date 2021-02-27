/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.repository;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.repository.deletion.DeleteCoverTypesQuery;
import global.moja.covertypes.daos.QueryParameters;
import global.moja.covertypes.repository.updation.UpdateCoverTypeQuery;
import global.moja.covertypes.repository.updation.UpdateCoverTypesQuery;
import global.moja.covertypes.repository.deletion.DeleteCoverTypeQuery;
import global.moja.covertypes.repository.selection.SelectCoverTypesQuery;
import global.moja.covertypes.repository.insertion.InsertCoverTypeQuery;
import global.moja.covertypes.repository.insertion.InsertCoverTypesQuery;
import global.moja.covertypes.repository.selection.SelectCoverTypeQuery;
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
public class CoverTypesRepository {

	
	@Autowired
	InsertCoverTypeQuery insertCoverTypeQuery;
	
	@Autowired
	InsertCoverTypesQuery insertCoverTypesQuery;
	
	@Autowired
	SelectCoverTypeQuery selectCoverTypeQuery;
	
	@Autowired
	SelectCoverTypesQuery selectCoverTypesQuery;

	@Autowired
	UpdateCoverTypeQuery updateCoverTypeQuery;
	
	@Autowired
	UpdateCoverTypesQuery updateCoverTypesQuery;
	
	@Autowired
	DeleteCoverTypeQuery deleteCoverTypeQuery;
	
	@Autowired
    DeleteCoverTypesQuery deleteCoverTypesQuery;


	public Mono<Long> insertCoverType(CoverType coverType) {
		return insertCoverTypeQuery.insertCoverType(coverType);
	}
	
	public Flux<Long> insertCoverTypes(CoverType[] coverTypes) {
		return insertCoverTypesQuery.insertCoverTypes(coverTypes);
	}

	public Mono<CoverType> selectCoverType(Long id) {
		return selectCoverTypeQuery.selectCoverType(id);
	}
	
	public Flux<CoverType> selectCoverTypes(QueryParameters parameters) {
		return selectCoverTypesQuery.selectCoverTypes(parameters);
	}

	public Mono<Integer> updateCoverType(CoverType coverType) {
		return updateCoverTypeQuery.updateCoverType(coverType);
	}
	
	public Flux<Integer> updateCoverTypes(CoverType[] coverTypes) {
		return updateCoverTypesQuery.updateCoverTypes(coverTypes);
	}	
	
	public Mono<Integer> deleteCoverTypeById(Long id) {
		return deleteCoverTypeQuery.deleteCoverType(id);
	}
	
	public Mono<Integer> deleteCoverTypes(QueryParameters parameters) {
		return deleteCoverTypesQuery.deleteCoverTypes(parameters);
	}


}

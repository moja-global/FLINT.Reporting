/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository;

import global.moja.landusecategories.repository.deletion.DeleteLandUseCategoryQuery;
import global.moja.landusecategories.repository.insertion.InsertLandUseCategoryQuery;
import global.moja.landusecategories.repository.updation.UpdateLandUseCategoriesQuery;
import global.moja.landusecategories.repository.updation.UpdateLandUseCategoryQuery;
import global.moja.landusecategories.daos.QueryParameters;
import global.moja.landusecategories.models.LandUseCategory;
import global.moja.landusecategories.repository.deletion.DeleteLandUseCategoriesQuery;
import global.moja.landusecategories.repository.selection.SelectLandUseCategoriesQuery;
import global.moja.landusecategories.repository.insertion.InsertLandUseCategoriesQuery;
import global.moja.landusecategories.repository.selection.SelectLandUseCategoryQuery;
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
public class LandUseCategoriesRepository {

	
	@Autowired
    InsertLandUseCategoryQuery insertLandUseCategoryQuery;
	
	@Autowired
	InsertLandUseCategoriesQuery insertLandUseCategoriesQuery;
	
	@Autowired
	SelectLandUseCategoryQuery selectLandUseCategoryQuery;
	
	@Autowired
	SelectLandUseCategoriesQuery selectLandUseCategoriesQuery;

	@Autowired
    UpdateLandUseCategoryQuery updateLandUseCategoryQuery;
	
	@Autowired
    UpdateLandUseCategoriesQuery updateLandUseCategoriesQuery;
	
	@Autowired
    DeleteLandUseCategoryQuery deleteLandUseCategoryQuery;
	
	@Autowired
    DeleteLandUseCategoriesQuery deleteLandUseCategoriesQuery;


	public Mono<Long> insertLandUseCategory(LandUseCategory landUseCategory) {
		return insertLandUseCategoryQuery.insertLandUseCategory(landUseCategory);
	}
	
	public Flux<Long> insertLandUseCategories(LandUseCategory[] landUseCategories) {
		return insertLandUseCategoriesQuery.insertLandUseCategories(landUseCategories);
	}

	public Mono<LandUseCategory> selectLandUseCategory(Long id) {
		return selectLandUseCategoryQuery.selectLandUseCategory(id);
	}
	
	public Flux<LandUseCategory> selectLandUseCategories(QueryParameters parameters) {
		return selectLandUseCategoriesQuery.selectLandUseCategories(parameters);
	}

	public Mono<Integer> updateLandUseCategory(LandUseCategory landUseCategory) {
		return updateLandUseCategoryQuery.updateLandUseCategory(landUseCategory);
	}
	
	public Flux<Integer> updateLandUseCategories(LandUseCategory[] landUseCategories) {
		return updateLandUseCategoriesQuery.updateLandUseCategories(landUseCategories);
	}	
	
	public Mono<Integer> deleteLandUseCategoryById(Long id) {
		return deleteLandUseCategoryQuery.deleteLandUseCategory(id);
	}
	
	public Mono<Integer> deleteLandUseCategories(QueryParameters parameters) {
		return deleteLandUseCategoriesQuery.deleteLandUseCategories(parameters);
	}


}

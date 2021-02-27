/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.repository;

import moja.global.unitcategories.models.UnitCategory;
import moja.global.unitcategories.repository.deletion.DeleteAllUnitCategoriesQuery;
import moja.global.unitcategories.repository.deletion.DeleteUnitCategoryByIdQuery;
import moja.global.unitcategories.repository.updation.UpdateUnitCategoryQuery;
import moja.global.unitcategories.repository.deletion.DeleteUnitCategoriesByIdsQuery;
import moja.global.unitcategories.repository.insertion.InsertUnitCategoryQuery;
import moja.global.unitcategories.repository.insertion.InsertUnitCategoriesQuery;
import moja.global.unitcategories.repository.selection.SelectAllUnitCategoriesQuery;
import moja.global.unitcategories.repository.selection.SelectUnitCategoryByIdQuery;
import moja.global.unitcategories.repository.selection.SelectUnitCategoriesByIdsQuery;
import moja.global.unitcategories.repository.updation.UpdateUnitCategoriesQuery;
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
public class UnitCategoriesRepository {

	
	@Autowired
	InsertUnitCategoryQuery insertUnitCategoryQuery;
	
	@Autowired
	InsertUnitCategoriesQuery insertUnitCategoriesQuery;
	
	@Autowired
	SelectUnitCategoryByIdQuery selectUnitCategoryByIdQuery;
	
	@Autowired
	SelectUnitCategoriesByIdsQuery selectUnitCategoriesByIdsQuery;
	
	@Autowired
	SelectAllUnitCategoriesQuery selectAllUnitCategoriesQuery;

	@Autowired
	UpdateUnitCategoryQuery updateUnitCategoryQuery;
	
	@Autowired
	UpdateUnitCategoriesQuery updateUnitCategoriesQuery;
	
	@Autowired
	DeleteUnitCategoryByIdQuery deleteUnitCategoryByIdQuery;
	
	@Autowired
	DeleteUnitCategoriesByIdsQuery deleteUnitCategoriesByIdsQuery;
	
	@Autowired
	DeleteAllUnitCategoriesQuery deleteAllUnitCategoriesQuery;


	public Mono<Long> insertUnitCategory(UnitCategory unitCategory) {
		return insertUnitCategoryQuery.insertUnitCategory(unitCategory);
	}
	
	public Flux<Long> insertUnitCategories(UnitCategory[] unitCategories) {
		return insertUnitCategoriesQuery.insertUnitCategories(unitCategories);
	}

	public Mono<UnitCategory> selectUnitCategoryById(Long id) {
		return selectUnitCategoryByIdQuery.selectUnitCategoryById(id);
	}
	
	public Flux<UnitCategory> selectUnitCategoriesByIds(Long[] ids) {
		return selectUnitCategoriesByIdsQuery.selectUnitCategoriesByIds(ids);
	}

	public Flux<UnitCategory> selectAllUnitCategories() {
		return selectAllUnitCategoriesQuery.selectAllUnitCategories();
	}	

	public Mono<Integer> updateUnitCategory(UnitCategory unitCategory) {
		return updateUnitCategoryQuery.updateUnitCategory(unitCategory);
	}
	
	public Flux<Integer> updateUnitCategories(UnitCategory[] unitCategories) {
		return updateUnitCategoriesQuery.updateUnitCategories(unitCategories);
	}	
	
	public Mono<Integer> deleteUnitCategoryById(Long id) {
		return deleteUnitCategoryByIdQuery.deleteUnitCategoryById(id);
	}
	
	public Mono<Integer> deleteUnitCategoriesByIds(Long[] ids) {
		return deleteUnitCategoriesByIdsQuery.deleteUnitCategoriesByIds(ids);
	}

	public Mono<Integer> deleteAllUnitCategories() {
		return deleteAllUnitCategoriesQuery.deleteAllUnitCategories();
	}	

}

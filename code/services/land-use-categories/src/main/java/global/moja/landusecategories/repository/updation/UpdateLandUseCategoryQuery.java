/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository.updation;

import global.moja.landusecategories.configurations.DatabaseConfig;
import global.moja.landusecategories.models.LandUseCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateLandUseCategoryQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Land Use Category record
	 * @param landUseCategory a bean containing the Land Use Category record details
	 * @return the number of Land Use Categories records affected by the query i.e updated
	 */
	public Mono<Integer> updateLandUseCategory(LandUseCategory landUseCategory){
		
		log.trace("Entering updateLandUseCategory()");

		String query = "UPDATE land_use_category SET reporting_framework_id = ?, parent_land_use_category_id  = ?, cover_type_id = ?, name = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							landUseCategory.getReportingFrameworkId(),
							landUseCategory.getParentLandUseCategoryId(),
							landUseCategory.getCoverTypeId(),
							landUseCategory.getName(),
							landUseCategory.getId())
					.counts());
	}
}

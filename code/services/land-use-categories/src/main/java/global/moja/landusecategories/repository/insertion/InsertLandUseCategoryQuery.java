/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository.insertion;

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
public class InsertLandUseCategoryQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Land Use Category record into the database
	 * @param landUseCategory a bean containing the Land Use Category record details
	 * @return the unique identifier of the newly inserted Land Use Category record
	 */
	public Mono<Long> insertLandUseCategory(LandUseCategory landUseCategory){
		
		log.trace("Entering insertLandUseCategory()");

		String query = "INSERT INTO land_use_category(reporting_framework_id,parent_land_use_category_id,cover_type_id,name) VALUES(?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							landUseCategory.getReportingFrameworkId(),
							landUseCategory.getParentLandUseCategoryId(),
							landUseCategory.getCoverTypeId(),
							landUseCategory.getName())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

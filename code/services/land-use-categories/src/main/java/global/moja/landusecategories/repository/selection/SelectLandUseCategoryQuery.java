/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository.selection;

import global.moja.landusecategories.configurations.DatabaseConfig;
import global.moja.landusecategories.models.LandUseCategory;
import global.moja.landusecategories.util.builders.LandUseCategoryBuilder;
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
public class SelectLandUseCategoryQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Land Use Category record from the database given its unique identifier
	 * @param id the unique identifier of the Land Use Category record to be selected
	 * @return the Land Use Category record with the given id if found
	 */
	public Mono<LandUseCategory> selectLandUseCategory(Long id) {

		log.trace("Entering selectLandUseCategory()");

		String query = "SELECT * FROM land_use_category WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new LandUseCategoryBuilder()
									.id(rs.getLong("id"))
									.reportingFrameworkId(rs.getLong("reporting_framework_id"))
									.parentLandUseCategoryId(rs.getLong("parent_land_use_category_id"))
									.coverTypeId(rs.getLong("cover_type_id"))
									.name(rs.getString("name"))
									.version(rs.getInt("version"))
									.build()));
	}

}

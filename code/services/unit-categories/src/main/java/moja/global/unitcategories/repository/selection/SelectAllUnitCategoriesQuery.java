/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.repository.selection;

import moja.global.unitcategories.configurations.DatabaseConfig;
import moja.global.unitcategories.models.UnitCategory;
import moja.global.unitcategories.util.builders.UnitCategoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class SelectAllUnitCategoriesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects all unit category records from the database
	 * @return a list of unit category records if found
	 */
	public Flux<UnitCategory> selectAllUnitCategories() {

		String SELECTION_QUERY = "SELECT * FROM unit_category";
		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.select(SELECTION_QUERY)
					.get(rs -> 
						new UnitCategoryBuilder()
							.id(rs.getLong("id"))
							.name(rs.getString("name"))
							.version(rs.getInt("version"))
							.build()));
	}

}

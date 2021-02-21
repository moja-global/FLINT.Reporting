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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class SelectUnitCategoryByIdQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects a unit category record from the database given its unique identifier
	 * @param id the unique identifier of the record to be selected
	 * @return the record with the given id if found
	 */
	public Mono<UnitCategory> selectUnitCategoryById(Long id) {

		String query = "SELECT * FROM unit_category WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs -> 
						new UnitCategoryBuilder()
							.id(rs.getLong("id"))
							.name(rs.getString("name"))
							.version(rs.getInt("version"))
							.build()));
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.repository.insertion;

import moja.global.unitcategories.configurations.DatabaseConfig;
import moja.global.unitcategories.models.UnitCategory;
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
public class InsertUnitCategoryQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts a new unitCategory record into the database
	 * @param unitCategory a bean containing the record's details
	 * @return the unique identifier of the newly inserted unitCategory record
	 */
	public Mono<Long> insertUnitCategory(UnitCategory unitCategory){
		
		log.trace("Entering insertUnitCategory()");

		String query = "INSERT INTO unit_category(name) VALUES(?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(unitCategory.getName())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.repository.insertion;

import global.moja.units.configurations.DatabaseConfig;
import global.moja.units.models.Unit;
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
public class InsertUnitQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new unit record into the database
	 * @param unit a bean containing the record's details
	 * @return the unique identifier of the newly inserted unit record
	 */
	public Mono<Long> insertUnit(Unit unit){
		
		log.trace("Entering insertUnit()");

		String query = "INSERT INTO unit(unit_category_id, name, plural, symbol, scale_factor) VALUES(?,?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							unit.getUnitCategoryId(),
							unit.getName(),
							unit.getPlural(),
							unit.getSymbol(),
							unit.getScaleFactor())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

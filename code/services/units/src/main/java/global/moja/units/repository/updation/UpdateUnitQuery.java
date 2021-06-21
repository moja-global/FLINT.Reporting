/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.repository.updation;

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
public class UpdateUnitQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a unit record
	 * @param unit a bean containing the record's details
	 * @return the number of records affected by the query i.e updated
	 */
	public Mono<Integer> updateUnit(Unit unit){
		
		log.trace("Entering updateUnit()");

		String query = "UPDATE unit SET unit_category_id = ?, name = ?, plural = ?, symbol = ?, scale_factor = ? WHERE id = ?";

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
							unit.getScaleFactor(),
							unit.getId())
					.counts());
	}
}

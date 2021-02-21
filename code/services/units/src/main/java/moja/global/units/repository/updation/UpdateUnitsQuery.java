/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.units.repository.updation;

import io.reactivex.Flowable;
import moja.global.units.configurations.DatabaseConfig;
import moja.global.units.models.Unit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateUnitsQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Recursively Updates unit records
	 * @param units an array of beans containing the records' details
	 * @return the number of records affected by each recursive query i.e updated
	 */	
	public Flux<Integer> updateUnits(Unit[] units) {
		
		log.trace("Entering updateUnits()");

		String query = "UPDATE unit SET unit_category_id = ?, name = ?, plural = ?, symbol = ?, scale_factor = ? WHERE id = ?";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(units))
					.counts());
	}

	private Flowable getParametersListStream(Unit[] units) {
		
		List<List> parameters = new ArrayList<>();
		
		for (Unit p : units) {
			parameters.add(Arrays.asList(
					p.getUnitCategoryId(),
					p.getName(),
					p.getPlural(),
					p.getSymbol(),
					p.getScaleFactor(),
					p.getId()));
		}

		return Flowable.fromIterable(parameters);
	}
	


}

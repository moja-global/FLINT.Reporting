/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.repository.updation;

import io.reactivex.Flowable;
import moja.global.unitcategories.configurations.DatabaseConfig;
import moja.global.unitcategories.models.UnitCategory;
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
public class UpdateUnitCategoriesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Recursively Updates unit category records
	 * @param unitCategories an array of beans containing the records' details
	 * @return the number of records affected by each recursive query i.e updated
	 */	
	public Flux<Integer> updateUnitCategories(UnitCategory[] unitCategories) {
		
		log.trace("Entering updateUnitCategories()");

		String query = "UPDATE unit_category SET name = ? WHERE id = ?";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(unitCategories))
					.counts());
	}

	private Flowable getParametersListStream(UnitCategory[] unitCategories) {
		
		List<List> parameters = new ArrayList<>();
		
		for (UnitCategory p : unitCategories) {
			parameters.add(Arrays.asList(p.getName(), p.getId()));
		}

		return Flowable.fromIterable(parameters);
	}
	


}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.repository.insertion;

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
public class InsertUnitCategoriesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts new unit category records into the database
	 * @param unitCategories an array of beans containing the records' details
	 * @return the unique identifiers of the newly inserted unit category records
	 */	
	public Flux<Long> insertUnitCategories(UnitCategory[] unitCategories) {

		String query = "INSERT INTO unit_category(name) VALUES(?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(unitCategories))
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

	private Flowable getParametersListStream(UnitCategory[] unitCategories) {
		
		List<List> parameters = new ArrayList<>();
		
		for (UnitCategory p : unitCategories) {
			parameters.add(Arrays.asList(p.getName()));
		}

		return Flowable.fromIterable(parameters);
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.repository.updation;

import io.reactivex.Flowable;
import moja.global.unfcccvariables.configurations.DatabaseConfig;
import moja.global.unfcccvariables.models.UnfcccVariable;
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
public class UpdateUnfcccVariablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Recursively Updates UNFCCC variable records
	 * @param unfcccVariables an array of beans containing the records' details
	 * @return the number of records affected by each recursive query i.e updated
	 */	
	public Flux<Integer> updateUnfcccVariables(UnfcccVariable[] unfcccVariables) {
		
		log.trace("Entering updateUnfcccVariables()");

		String query = "UPDATE unfccc_variable SET name = ?, measure = ?, abbreviation = ?, unit_id = ? WHERE id = ?";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(unfcccVariables))
					.counts());
	}

	private Flowable getParametersListStream(UnfcccVariable[] unfcccVariables) {
		
		List<List> parameters = new ArrayList<>();
		
		for (UnfcccVariable p : unfcccVariables) {
			parameters.add(Arrays.asList(p.getName(), p.getMeasure(), p.getAbbreviation(), p.getUnitId(), p.getId()));
		}

		return Flowable.fromIterable(parameters);
	}
	


}

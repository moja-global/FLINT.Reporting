/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.repository.insertion;

import io.reactivex.Flowable;
import moja.global.emissiontypes.configurations.DatabaseConfig;
import moja.global.emissiontypes.models.EmissionType;
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
public class InsertEmissionTypesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts new emission type records into the database
	 * @param emissionTypes an array of beans containing the records' details
	 * @return the unique identifiers of the newly inserted emission type records
	 */	
	public Flux<Long> insertEmissionTypes(EmissionType[] emissionTypes) {

		String query = "INSERT INTO emission_type(name, abbreviation, description) VALUES(?,?, ?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(emissionTypes))
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

	private Flowable getParametersListStream(EmissionType[] emissionTypes) {
		
		List<List> parameters = new ArrayList<>();
		
		for (EmissionType p : emissionTypes) {
			parameters.add(Arrays.asList(p.getName(), p.getAbbreviation(), p.getDescription()));
		}

		return Flowable.fromIterable(parameters);
	}

}

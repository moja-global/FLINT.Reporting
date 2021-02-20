/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxtypes.repository.insertion;

import io.reactivex.Flowable;
import moja.global.fluxtypes.configurations.DatabaseConfig;
import moja.global.fluxtypes.models.FluxType;
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
public class InsertFluxTypesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts new flux type records into the database
	 * @param fluxTypes an array of beans containing the records' details
	 * @return the unique identifiers of the newly inserted flux type records
	 */	
	public Flux<Long> insertFluxTypes(FluxType[] fluxTypes) {

		String query = "INSERT INTO flux_type(name, description) VALUES(?,?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(fluxTypes))
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

	private Flowable getParametersListStream(FluxType[] fluxTypes) {
		
		List<List> parameters = new ArrayList<>();
		
		for (FluxType p : fluxTypes) {
			parameters.add(Arrays.asList(p.getName(), p.getDescription()));
		}

		return Flowable.fromIterable(parameters);
	}

}

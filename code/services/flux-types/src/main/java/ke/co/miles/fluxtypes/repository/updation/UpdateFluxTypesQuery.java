/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.fluxtypes.repository.updation;

import io.reactivex.Flowable;
import ke.co.miles.fluxtypes.configurations.DatabaseConfig;
import ke.co.miles.fluxtypes.models.FluxType;
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
public class UpdateFluxTypesQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Recursively Updates flux type records
	 * @param fluxTypes an array of beans containing the records' details
	 * @return the number of records affected by each recursive query i.e updated
	 */	
	public Flux<Integer> updateFluxTypes(FluxType[] fluxTypes) {
		
		log.trace("Entering updateFluxTypes()");

		String query = "UPDATE flux_type SET name = ?, description = ? WHERE id = ?";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(fluxTypes))
					.counts());
	}

	private Flowable getParametersListStream(FluxType[] fluxTypes) {
		
		List<List> parameters = new ArrayList<>();
		
		for (FluxType p : fluxTypes) {
			parameters.add(Arrays.asList(p.getName(), p.getDescription(), p.getId()));
		}

		return Flowable.fromIterable(parameters);
	}
	


}

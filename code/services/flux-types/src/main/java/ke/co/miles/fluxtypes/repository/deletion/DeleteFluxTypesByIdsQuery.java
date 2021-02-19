/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.fluxtypes.repository.deletion;

import ke.co.miles.fluxtypes.configurations.DatabaseConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class DeleteFluxTypesByIdsQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes specific flux type records from the database given their unique identifiers
	 * @param ids the unique identifiers of the records
	 * @return the number of records affected by the query i.e deleted
	 */
	public Mono<Integer> deleteFluxTypesByIds(Long[] ids) {

		String query = "DELETE FROM flux_type WHERE id in (?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameter(new HashSet<>(Arrays.asList(ids)))
					.counts());	
	}
}

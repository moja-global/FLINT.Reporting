/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.repository.deletion;

import moja.global.emissiontypes.configurations.DatabaseConfig;
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
public class DeleteEmissionTypeByIdQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Deletes an emission type record from the database
	 * @param id the unique identifier of the emission type record to be deleted
	 * @return the number of records affected by the query i.e deleted
	 */	
	public Mono<Integer> deleteEmissionTypeById(Long id){

		String query = "DELETE FROM emission_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(id)
					.counts());
	}

}

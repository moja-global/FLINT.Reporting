/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.repository.selection;

import global.moja.emissiontypes.configurations.DatabaseConfig;
import global.moja.emissiontypes.models.EmissionType;
import global.moja.emissiontypes.util.builders.EmissionTypeBuilder;
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
public class SelectEmissionTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Emission Type record from the database given its unique identifier
	 * @param id the unique identifier of the Emission Type record to be selected
	 * @return the Emission Type record with the given id if found
	 */
	public Mono<EmissionType> selectEmissionType(Long id) {

		log.trace("Entering selectEmissionType()");

		String query = "SELECT * FROM emission_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new EmissionTypeBuilder()
									.id(rs.getLong("id"))
									.name(rs.getString("name"))
									.abbreviation(rs.getString("abbreviation"))
									.description(rs.getString("description"))
									.version(rs.getInt("version"))
									.build()));
	}

}

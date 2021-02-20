/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.repository.selection;

import moja.global.emissiontypes.configurations.DatabaseConfig;
import moja.global.emissiontypes.models.EmissionType;
import moja.global.emissiontypes.util.builders.EmissionTypeBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class SelectAllEmissionTypesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects all emission type records from the database
	 * @return a list of emission type records if found
	 */
	public Flux<EmissionType> selectAllEmissionTypes() {

		String SELECTION_QUERY = "SELECT * FROM emission_type";
		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.select(SELECTION_QUERY)
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

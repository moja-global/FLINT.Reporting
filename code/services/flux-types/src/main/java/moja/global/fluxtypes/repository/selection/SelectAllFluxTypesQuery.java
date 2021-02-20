/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.fluxtypes.repository.selection;

import moja.global.fluxtypes.configurations.DatabaseConfig;
import moja.global.fluxtypes.models.FluxType;
import moja.global.fluxtypes.util.builders.FluxTypeBuilder;
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
public class SelectAllFluxTypesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects all flux type records from the database
	 * @return a list of flux type records if found
	 */
	public Flux<FluxType> selectAllFluxTypes() {

		String SELECTION_QUERY = "SELECT * FROM flux_type";
		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.select(SELECTION_QUERY)
					.get(rs -> 
						new FluxTypeBuilder()
							.id(rs.getLong("id"))
							.name(rs.getString("name"))
							.description(rs.getString("description"))
							.version(rs.getInt("version"))
							.build()));
	}

}

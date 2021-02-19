/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package ke.co.miles.fluxtypes.repository.selection;

import ke.co.miles.fluxtypes.configurations.DatabaseConfig;
import ke.co.miles.fluxtypes.models.FluxType;
import ke.co.miles.fluxtypes.util.builders.FluxTypeBuilder;
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
public class SelectFluxTypeByIdQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a flux type record from the database given its unique identifier
	 * @param id the unique identifier of the record to be selected
	 * @return the record with the given id if found
	 */
	public Mono<FluxType> selectFluxTypeById(Long id) {

		String query = "SELECT * FROM flux_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs -> 
						new FluxTypeBuilder()
							.id(rs.getLong("id"))
							.name(rs.getString("name"))
							.description(rs.getString("description"))
							.version(rs.getInt("version"))
							.build()));
	}

}

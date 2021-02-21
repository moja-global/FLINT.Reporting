/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unfcccvariables.repository.selection;

import moja.global.unfcccvariables.configurations.DatabaseConfig;
import moja.global.unfcccvariables.models.UnfcccVariable;
import moja.global.unfcccvariables.util.builders.UnfcccVariableBuilder;
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
public class SelectAllUnfcccVariablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects all UNFCCC variable records from the database
	 * @return a list of UNFCCC variable records if found
	 */
	public Flux<UnfcccVariable> selectAllUnfcccVariables() {

		String SELECTION_QUERY = "SELECT * FROM unfccc_variable";
		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.select(SELECTION_QUERY)
					.get(rs -> 
						new UnfcccVariableBuilder()
								.id(rs.getLong("id"))
								.name(rs.getString("name"))
								.measure(rs.getString("measure"))
								.abbreviation(rs.getString("abbreviation"))
								.unitId(rs.getLong("unit_id"))
								.version(rs.getInt("version"))
								.build()));
	}

}

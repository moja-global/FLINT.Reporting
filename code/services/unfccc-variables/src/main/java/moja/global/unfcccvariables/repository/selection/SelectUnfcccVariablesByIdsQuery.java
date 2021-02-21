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

import java.util.Arrays;
import java.util.HashSet;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class SelectUnfcccVariablesByIdsQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects specific UNFCCC variable records from the database given their unique identified
	 * @param ids the unique identifiers of the records
	 * @return a list of UNFCCC variable records corresponding to the identifiers if found
	 */
	public Flux<UnfcccVariable> selectUnfcccVariablesByIds(Long[] ids) {

		String query = "SELECT * FROM unfccc_variable WHERE id in (?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameter(new HashSet<>(Arrays.asList(ids)))
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

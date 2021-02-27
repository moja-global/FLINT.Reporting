/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.repository.selection;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.configurations.DatabaseConfig;
import global.moja.covertypes.util.builders.CoverTypeBuilder;
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
public class SelectCoverTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Cover Type record from the database given its unique identifier
	 * @param id the unique identifier of the Cover Type record to be selected
	 * @return the Cover Type record with the given id if found
	 */
	public Mono<CoverType> selectCoverType(Long id) {

		log.trace("Entering selectCoverType()");

		String query = "SELECT * FROM cover_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new CoverTypeBuilder()
									.id(rs.getLong("id"))
									.code(rs.getString("code"))
									.description(rs.getString("description"))
									.version(rs.getInt("version"))
									.build()));
	}

}

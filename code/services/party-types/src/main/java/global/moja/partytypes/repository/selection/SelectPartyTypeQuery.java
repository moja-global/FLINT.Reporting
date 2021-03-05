/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.repository.selection;

import global.moja.partytypes.configurations.DatabaseConfig;
import global.moja.partytypes.models.PartyType;
import global.moja.partytypes.util.builders.PartyTypeBuilder;
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
public class SelectPartyTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Party Type record from the database given its unique identifier
	 * @param id the unique identifier of the Party Type record to be selected
	 * @return the Party Type record with the given id if found
	 */
	public Mono<PartyType> selectPartyType(Long id) {

		log.trace("Entering selectPartyType()");

		String query = "SELECT * FROM party_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new PartyTypeBuilder()
									.id(rs.getLong("id"))
									.parentPartyTypeId(rs.getLong("parent_party_type_id"))
									.name(rs.getString("name"))
									.version(rs.getInt("version"))
									.build()));
	}

}

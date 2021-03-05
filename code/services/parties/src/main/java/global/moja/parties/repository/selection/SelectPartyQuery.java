/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.repository.selection;

import global.moja.parties.models.Party;
import global.moja.parties.util.builders.PartyBuilder;
import global.moja.parties.configurations.DatabaseConfig;
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
public class SelectPartyQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Party record from the database given its unique identifier
	 * @param id the unique identifier of the Party record to be selected
	 * @return the Party record with the given id if found
	 */
	public Mono<Party> selectParty(Long id) {

		log.trace("Entering selectParty()");

		String query = "SELECT * FROM party WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new PartyBuilder()
									.id(rs.getLong("id"))
									.partyTypeId(rs.getLong("party_type_id"))
									.name(rs.getString("name"))
									.version(rs.getInt("version"))
									.build()));
	}

}

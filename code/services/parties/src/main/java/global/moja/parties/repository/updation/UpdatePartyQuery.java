/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.repository.updation;

import global.moja.parties.models.Party;
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
public class UpdatePartyQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Party record
	 * @param party a bean containing the Party record details
	 * @return the number of Parties records affected by the query i.e updated
	 */
	public Mono<Integer> updateParty(Party party){
		
		log.trace("Entering updateParty()");

		String query = "UPDATE party SET party_type_id = ?, name = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							party.getPartyTypeId(),
							party.getName(),
							party.getId())
					.counts());
	}
}

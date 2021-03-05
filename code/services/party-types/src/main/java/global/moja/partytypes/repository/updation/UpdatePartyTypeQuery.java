/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.repository.updation;

import global.moja.partytypes.configurations.DatabaseConfig;
import global.moja.partytypes.models.PartyType;
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
public class UpdatePartyTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Party Type record
	 * @param partyType a bean containing the Party Type record details
	 * @return the number of Party Types records affected by the query i.e updated
	 */
	public Mono<Integer> updatePartyType(PartyType partyType){
		
		log.trace("Entering updatePartyType()");

		String query = "UPDATE party_type SET parent_party_type_id = ?, name = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							partyType.getParentPartyTypeId(),
							partyType.getName(),
							partyType.getId())
					.counts());
	}
}

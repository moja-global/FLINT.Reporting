/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.repository.updation;

import global.moja.accountabilities.models.Accountability;
import global.moja.accountabilities.configurations.DatabaseConfig;
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
public class UpdateAccountabilityQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Accountability record
	 * @param accountability a bean containing the Accountability record details
	 * @return the number of Accountabilities records affected by the query i.e updated
	 */
	public Mono<Integer> updateAccountability(Accountability accountability){
		
		log.trace("Entering updateAccountability()");

		String query = "UPDATE accountability SET accountability_type_id = ?, parent_party_id  = ?, subsidiary_party_id = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							accountability.getAccountabilityTypeId(),
							accountability.getParentPartyId(),
							accountability.getSubsidiaryPartyId(),
							accountability.getId())
					.counts());
	}
}

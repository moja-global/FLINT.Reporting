/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.repository.updation;

import global.moja.accountabilitytypes.configurations.DatabaseConfig;
import global.moja.accountabilitytypes.models.AccountabilityType;
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
public class UpdateAccountabilityTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Accountability Type record
	 * @param accountabilityType a bean containing the Accountability Type record details
	 * @return the number of Accountability Types records affected by the query i.e updated
	 */
	public Mono<Integer> updateAccountabilityType(AccountabilityType accountabilityType){
		
		log.trace("Entering updateAccountabilityType()");

		String query = "UPDATE accountability_type SET name = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							accountabilityType.getName(),
							accountabilityType.getId())
					.counts());
	}
}

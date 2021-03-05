/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.repository.deletion;

import global.moja.accountabilitytypes.configurations.DatabaseConfig;
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
public class DeleteAccountabilityTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Deletes a Accountability Type record from the database
	 * @param id the unique identifier of the Accountability Type record to be deleted
	 * @return the number of Accountability Types records affected by the query i.e deleted
	 */	
	public Mono<Integer> deleteAccountabilityType(Long id){

		log.trace("Entering deleteAccountabilityType");

		String query = "DELETE FROM accountability_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(id)
					.counts());
	}

}

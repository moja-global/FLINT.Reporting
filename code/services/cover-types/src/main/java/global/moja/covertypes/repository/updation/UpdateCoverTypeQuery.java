/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.repository.updation;

import global.moja.covertypes.models.CoverType;
import global.moja.covertypes.configurations.DatabaseConfig;
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
public class UpdateCoverTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Cover Type record
	 * @param coverType a bean containing the Cover Type record details
	 * @return the number of Cover Types records affected by the query i.e updated
	 */
	public Mono<Integer> updateCoverType(CoverType coverType){
		
		log.trace("Entering updateCoverType()");

		String query = "UPDATE cover_type SET code = ?, description = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							coverType.getCode(),
							coverType.getDescription(),
							coverType.getId())
					.counts());
	}
}

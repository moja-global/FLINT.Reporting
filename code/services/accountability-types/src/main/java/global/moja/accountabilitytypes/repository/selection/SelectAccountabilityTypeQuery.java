/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.repository.selection;

import global.moja.accountabilitytypes.models.AccountabilityType;
import global.moja.accountabilitytypes.configurations.DatabaseConfig;
import global.moja.accountabilitytypes.util.builders.AccountabilityTypeBuilder;
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
public class SelectAccountabilityTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Accountability Type record from the database given its unique identifier
	 * @param id the unique identifier of the Accountability Type record to be selected
	 * @return the Accountability Type record with the given id if found
	 */
	public Mono<AccountabilityType> selectAccountabilityType(Long id) {

		log.trace("Entering selectAccountabilityType()");

		String query = "SELECT * FROM accountability_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new AccountabilityTypeBuilder()
									.id(rs.getLong("id"))
									.name(rs.getString("name"))
									.version(rs.getInt("version"))
									.build()));
	}

}

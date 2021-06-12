/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.repository.selection;

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
public class SelectAccountabilityQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Accountability record from the database given its unique identifier
	 * @param id the unique identifier of the Accountability record to be selected
	 * @return the Accountability record with the given id if found
	 */
	public Mono<Accountability> selectAccountability(Long id) {

		log.trace("Entering selectAccountability()");

		String query = "SELECT * FROM accountability WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							Accountability.builder()
									.id(rs.getLong("id"))
									.accountabilityTypeId(rs.getLong("accountability_type_id"))
									.accountabilityRuleId(rs.getLong("accountability_rule_id"))
									.parentPartyId(rs.getLong("parent_party_id"))
									.subsidiaryPartyId(rs.getLong("subsidiary_party_id"))
									.version(rs.getInt("version"))
									.build()));
	}

}

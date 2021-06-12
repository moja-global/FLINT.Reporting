/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.repository.selection;

import global.moja.accountabilityrules.configurations.DatabaseConfig;
import global.moja.accountabilityrules.models.AccountabilityRule;
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
public class SelectAccountabilityRuleQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Accountability Rule record from the database given its unique identifier
	 * @param id the unique identifier of the Accountability Rule record to be selected
	 * @return the Accountability Rule record with the given id if found
	 */
	public Mono<AccountabilityRule> selectAccountabilityRule(Long id) {

		log.trace("Entering selectAccountabilityRule()");

		String query = "SELECT * FROM accountability_rule WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							AccountabilityRule.builder()
									.id(rs.getLong("id"))
									.accountabilityTypeId(rs.getLong("accountability_type_id"))
									.parentPartyTypeId(rs.getLong("parent_party_type_id"))
									.subsidiaryPartyTypeId(rs.getLong("subsidiary_party_type_id"))
									.version(rs.getInt("version"))
									.build()));
	}

}

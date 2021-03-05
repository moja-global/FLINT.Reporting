/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.repository.updation;

import global.moja.accountabilityrules.models.AccountabilityRule;
import global.moja.accountabilityrules.configurations.DatabaseConfig;
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
public class UpdateAccountabilityRuleQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Accountability Rule record
	 * @param accountabilityRule a bean containing the Accountability Rule record details
	 * @return the number of Accountability Rules records affected by the query i.e updated
	 */
	public Mono<Integer> updateAccountabilityRule(AccountabilityRule accountabilityRule){
		
		log.trace("Entering updateAccountabilityRule()");

		String query = "UPDATE accountability_rule SET accountability_type_id = ?, parent_party_type_id  = ?, subsidiary_party_type_id = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							accountabilityRule.getAccountabilityTypeId(),
							accountabilityRule.getParentPartyTypeId(),
							accountabilityRule.getSubsidiaryPartyTypeId(),
							accountabilityRule.getId())
					.counts());
	}
}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.repository.insertion;

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
public class InsertAccountabilityQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Accountability record into the database
	 * @param accountability a bean containing the Accountability record details
	 * @return the unique identifier of the newly inserted Accountability record
	 */
	public Mono<Long> insertAccountability(Accountability accountability){
		
		log.trace("Entering insertAccountability()");

		String query = "INSERT INTO accountability(accountability_type_id,accountability_rule_id,parent_party_id,subsidiary_party_id) VALUES(?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							accountability.getAccountabilityTypeId(),
							accountability.getAccountabilityRuleId(),
							accountability.getParentPartyId(),
							accountability.getSubsidiaryPartyId())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

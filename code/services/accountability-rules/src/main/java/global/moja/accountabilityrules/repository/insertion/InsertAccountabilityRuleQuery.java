/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.repository.insertion;

import global.moja.accountabilityrules.configurations.DatabaseConfig;
import global.moja.accountabilityrules.models.AccountabilityRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class InsertAccountabilityRuleQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts a new Accountability Rule record into the database
     *
     * @param accountabilityRule a bean containing the Accountability Rule record details
     * @return the unique identifier of the newly inserted Accountability Rule record
     */
    public Mono<Long> insertAccountabilityRule(AccountabilityRule accountabilityRule) {

        log.trace("Entering insertAccountabilityRule()");

        String query = "INSERT INTO accountability_rule(accountability_type_id,parent_party_type_id,subsidiary_party_type_id) VALUES(?,?,?)";

        return
                Mono.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameters(
                                        accountabilityRule.getAccountabilityTypeId(),
                                        accountabilityRule.getParentPartyTypeId(),
                                        accountabilityRule.getSubsidiaryPartyTypeId()
                                        )
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

}

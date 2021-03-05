/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.repository.selection;

import global.moja.accountabilityrules.configurations.DatabaseConfig;
import global.moja.accountabilityrules.daos.QueryParameters;
import global.moja.accountabilityrules.models.AccountabilityRule;
import global.moja.accountabilityrules.util.builders.QueryWhereClauseBuilder;
import global.moja.accountabilityrules.util.builders.AccountabilityRuleBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class SelectAccountabilityRulesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Accountability Rules records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Accountability Rules records if found
     */
    public Flux<AccountabilityRule> selectAccountabilityRules(QueryParameters parameters) {

        log.trace("Entering selectAccountabilityRules()");

        String query =
                "SELECT * FROM accountability_rule" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new AccountabilityRuleBuilder()
                                                .id(rs.getLong("id"))
                                                .accountabilityTypeId(rs.getLong("accountability_type_id"))
                                                .parentPartyTypeId(rs.getLong("parent_party_type_id"))
                                                .subsidiaryPartyTypeId(rs.getLong("subsidiary_party_type_id"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}

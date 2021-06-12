/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.repository.selection;

import global.moja.accountabilities.configurations.DatabaseConfig;
import global.moja.accountabilities.daos.QueryParameters;
import global.moja.accountabilities.models.Accountability;
import global.moja.accountabilities.util.builders.QueryWhereClauseBuilder;
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
public class SelectAccountabilitiesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Accountabilities records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Accountabilities records if found
     */
    public Flux<Accountability> selectAccountabilities(QueryParameters parameters) {

        log.trace("Entering selectAccountabilities()");

        String query =
                "SELECT * FROM accountability" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        Accountability
                                                .builder()
                                                .id(rs.getLong("id"))
                                                .accountabilityTypeId(rs.getLong("accountability_type_id"))
                                                .accountabilityRuleId(rs.getLong("accountability_rule_id"))
                                                .parentPartyId(rs.getLong("parent_party_id"))
                                                .subsidiaryPartyId(rs.getLong("subsidiary_party_id"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}

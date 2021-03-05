/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.repository.selection;

import global.moja.accountabilitytypes.daos.QueryParameters;
import global.moja.accountabilitytypes.models.AccountabilityType;
import global.moja.accountabilitytypes.util.builders.QueryWhereClauseBuilder;
import global.moja.accountabilitytypes.configurations.DatabaseConfig;
import global.moja.accountabilitytypes.util.builders.AccountabilityTypeBuilder;
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
public class SelectAccountabilityTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Accountability Types records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Accountability Types records if found
     */
    public Flux<AccountabilityType> selectAccountabilityTypes(QueryParameters parameters) {

        log.trace("Entering selectAccountabilityTypes()");

        String query =
                "SELECT * FROM accountability_type" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new AccountabilityTypeBuilder()
                                                .id(rs.getLong("id"))
                                                .name(rs.getString("name"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.repository.selection;

import global.moja.partytypes.configurations.DatabaseConfig;
import global.moja.partytypes.daos.QueryParameters;
import global.moja.partytypes.models.PartyType;
import global.moja.partytypes.util.builders.PartyTypeBuilder;
import global.moja.partytypes.util.builders.QueryWhereClauseBuilder;
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
public class SelectPartyTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Party Types records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Party Types records if found
     */
    public Flux<PartyType> selectPartyTypes(QueryParameters parameters) {

        log.trace("Entering selectPartyTypes()");

        String query =
                "SELECT * FROM party_type" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new PartyTypeBuilder()
                                                .id(rs.getLong("id"))
                                                .parentPartyTypeId(rs.getLong("parent_party_type_id"))
                                                .name(rs.getString("name"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}

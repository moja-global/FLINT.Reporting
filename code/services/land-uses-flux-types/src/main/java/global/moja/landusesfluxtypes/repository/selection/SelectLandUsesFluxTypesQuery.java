/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.repository.selection;

import global.moja.landusesfluxtypes.daos.QueryParameters;
import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.util.builders.LandUseFluxTypeBuilder;
import global.moja.landusesfluxtypes.util.builders.QueryWhereClauseBuilder;
import global.moja.landusesfluxtypes.configurations.DatabaseConfig;
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
public class SelectLandUsesFluxTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Land Uses Flux Types records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Land Uses Flux Types records if found
     */
    public Flux<LandUseFluxType> selectLandUsesFluxTypes(QueryParameters parameters) {

        log.trace("Entering selectLandUsesFluxTypes()");

        String query =
                "SELECT * FROM land_use_flux_type" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new LandUseFluxTypeBuilder()
                                                .id(rs.getLong("id"))
                                                .landUseCategoryId(rs.getLong("land_use_category_id"))
                                                .fluxTypeId(rs.getLong("flux_type_id"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}

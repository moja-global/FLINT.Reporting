/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.repository.selection;

import global.moja.vegetationtypes.models.VegetationType;
import global.moja.vegetationtypes.configurations.DatabaseConfig;
import global.moja.vegetationtypes.daos.QueryParameters;
import global.moja.vegetationtypes.util.builders.VegetationTypeBuilder;
import global.moja.vegetationtypes.util.builders.QueryWhereClauseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class SelectVegetationTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Vegetation Types records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Vegetation Types records if found
     */
    public Flux<VegetationType> selectVegetationTypes(Long databaseId, QueryParameters parameters) {

        log.trace("Entering selectVegetationTypes()");

        String query =
                "SELECT * FROM vegtypeinfo_dimension" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux
                        .from(databaseConfig.getDatabase(databaseId))
                        .flatMap(database ->
                                Flux.from(
                                        database
                                                .select(query)
                                                .get(rs ->
                                                        new VegetationTypeBuilder()
                                                                .id(rs.getLong("vegtypeinfo_dimension_id_pk"))
                                                                .coverTypeId(rs.getLong("ipcccovertypeinfo_dimension_id_fk"))
                                                                .name(rs.getString("vegtypename"))
                                                                .woodyType(rs.getBoolean("woodtype"))
                                                                .naturalSystem(rs.getBoolean("naturalsystem"))
                                                                .build())));


    }

}

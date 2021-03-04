/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes.repository.selection;

import global.moja.vegetationhistoryvegetationtypes.configurations.DatabaseConfig;
import global.moja.vegetationhistoryvegetationtypes.daos.QueryParameters;
import global.moja.vegetationhistoryvegetationtypes.util.builders.QueryWhereClauseBuilder;
import global.moja.vegetationhistoryvegetationtypes.util.builders.VegetationHistoryVegetationTypeBuilder;
import global.moja.vegetationhistoryvegetationtypes.models.VegetationHistoryVegetationType;
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
public class SelectVegetationHistoryVegetationTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Vegetation History Vegetation Types records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Vegetation History Vegetation Types records if found
     */
    public Flux<VegetationHistoryVegetationType> selectVegetationHistoryVegetationTypes(Long databaseId, QueryParameters parameters) {

        log.trace("Entering selectVegetationHistoryVegetationTypes()");

        String query =
                "SELECT * FROM vegetation_history_vegetation_type" +
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
                                                        new VegetationHistoryVegetationTypeBuilder()
                                                                .id(rs.getLong("veghistory_vegtypeinfo_mapping_id_pk"))
                                                                .vegetationHistoryId(rs.getLong("veghistory_dimension_id_fk"))
                                                                .vegetationTypeId(rs.getLong("vegtypeinfo_dimension_id_fk"))
                                                                .itemNumber(rs.getLong("itemnumber"))
                                                                .year(rs.getInt("year"))
                                                                .build())));

    }

}

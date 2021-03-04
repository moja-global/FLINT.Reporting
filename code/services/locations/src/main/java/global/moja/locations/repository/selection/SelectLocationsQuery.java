/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.repository.selection;

import global.moja.locations.configurations.DatabaseConfig;
import global.moja.locations.daos.QueryParameters;
import global.moja.locations.util.builders.LocationBuilder;
import global.moja.locations.util.builders.QueryWhereClauseBuilder;
import global.moja.locations.models.Location;
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
public class SelectLocationsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Locations records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Locations records if found
     */
    public Flux<Location> selectLocations(Long databaseId, QueryParameters parameters) {

        log.trace("Entering selectLocations()");

        String query =
                "SELECT * FROM location_dimension" +
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
                                                        new LocationBuilder()
                                                                .id(rs.getLong("location_dimension_id_pk"))
                                                                .partyId(rs.getLong("countyinfo_dimension_id_fk"))
                                                                .tileId(rs.getLong("tileinfo_dimension_id_fk"))
                                                                .vegetationHistoryId(rs.getLong("veghistory_dimension_id_fk"))
                                                                .unitCount(rs.getLong("unitcount"))
                                                                .unitAreaSum(rs.getDouble("unitareasum"))
                                                                .build())));
    }

}

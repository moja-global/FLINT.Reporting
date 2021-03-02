/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.repository.selection;

import global.moja.conversionandremainingperiods.configurations.DatabaseConfig;
import global.moja.conversionandremainingperiods.daos.QueryParameters;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import global.moja.conversionandremainingperiods.util.builders.ConversionAndRemainingPeriodBuilder;
import global.moja.conversionandremainingperiods.util.builders.QueryWhereClauseBuilder;
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
public class SelectConversionAndRemainingPeriodsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Conversion and Remaining Periods records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Conversion and Remaining Periods records if found
     */
    public Flux<ConversionAndRemainingPeriod> selectConversionAndRemainingPeriods(QueryParameters parameters) {

        log.trace("Entering selectConversionAndRemainingPeriods()");

        String query =
                "SELECT * FROM conversion_and_remaining_period" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new ConversionAndRemainingPeriodBuilder()
                                                .id(rs.getLong("id"))
                                                .previousLandCoverId(rs.getLong("previous_land_cover_id"))
                                                .currentLandCoverId(rs.getLong("current_land_cover_id"))
                                                .conversionPeriod(rs.getInt("conversion_period"))
                                                .remainingPeriod(rs.getInt("remaining_period"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}

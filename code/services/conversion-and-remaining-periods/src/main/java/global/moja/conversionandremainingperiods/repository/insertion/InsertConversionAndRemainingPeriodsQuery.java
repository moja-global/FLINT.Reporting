/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.repository.insertion;

import io.reactivex.Flowable;
import global.moja.conversionandremainingperiods.configurations.DatabaseConfig;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class InsertConversionAndRemainingPeriodsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Conversion and Remaining Periods records into the database
     *
     * @param conversionAndRemainingPeriods an array of beans containing the Conversion and Remaining Periods records details
     * @return the unique identifiers of the newly inserted Conversion and Remaining Periods records
     */
    public Flux<Long> insertConversionAndRemainingPeriods(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {

        log.trace("Entering insertConversionAndRemainingPeriods");

        String query = "INSERT INTO conversion_and_remaining_period(previous_land_cover_id,current_land_cover_id,conversion_period,remaining_period) VALUES(?,?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(conversionAndRemainingPeriods))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {

        List<List> temp = new ArrayList<>();

        for (ConversionAndRemainingPeriod conversionAndRemainingPeriod : conversionAndRemainingPeriods) {
            temp.add(Arrays.asList(
                    conversionAndRemainingPeriod.getPreviousLandCoverId(),
                    conversionAndRemainingPeriod.getCurrentLandCoverId(),
                    conversionAndRemainingPeriod.getConversionPeriod(),
                    conversionAndRemainingPeriod.getRemainingPeriod()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

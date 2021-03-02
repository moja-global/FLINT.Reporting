/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.repository.updation;

import global.moja.conversionandremainingperiods.configurations.DatabaseConfig;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import io.reactivex.Flowable;
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
public class UpdateConversionAndRemainingPeriodsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Conversion and Remaining Periods records
     *
     * @param conversionAndRemainingPeriods an array of beans containing the Conversion and Remaining Periods records details
     * @return the number of Conversion and Remaining Periods records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateConversionAndRemainingPeriods(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {

        log.trace("Entering updateConversionAndRemainingPeriods()");

        String query = "UPDATE conversion_and_remaining_period SET previous_land_cover_id = ?, current_land_cover_id  = ?, conversion_period = ?, remaining_period = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(conversionAndRemainingPeriods))
                                .counts());
    }

    private Flowable getParametersListStream(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {

        List<List> temp = new ArrayList<>();

        for (ConversionAndRemainingPeriod conversionAndRemainingPeriod : conversionAndRemainingPeriods) {
            temp.add(Arrays.asList(
                    conversionAndRemainingPeriod.getPreviousLandCoverId(),
                    conversionAndRemainingPeriod.getCurrentLandCoverId(),
                    conversionAndRemainingPeriod.getConversionPeriod(),
                    conversionAndRemainingPeriod.getRemainingPeriod(),
                    conversionAndRemainingPeriod.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

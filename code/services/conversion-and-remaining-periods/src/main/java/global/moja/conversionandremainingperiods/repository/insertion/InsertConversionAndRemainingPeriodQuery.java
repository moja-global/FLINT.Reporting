/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.repository.insertion;

import global.moja.conversionandremainingperiods.configurations.DatabaseConfig;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class InsertConversionAndRemainingPeriodQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Conversion and Remaining Period record into the database
	 * @param conversionAndRemainingPeriod a bean containing the Conversion and Remaining Period record details
	 * @return the unique identifier of the newly inserted Conversion and Remaining Period record
	 */
	public Mono<Long> insertConversionAndRemainingPeriod(ConversionAndRemainingPeriod conversionAndRemainingPeriod){
		
		log.trace("Entering insertConversionAndRemainingPeriod()");

		String query = "INSERT INTO conversion_and_remaining_period(previous_land_cover_id,current_land_cover_id,conversion_period,remaining_period) VALUES(?,?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							conversionAndRemainingPeriod.getPreviousLandCoverId(),
							conversionAndRemainingPeriod.getCurrentLandCoverId(),
							conversionAndRemainingPeriod.getConversionPeriod(),
							conversionAndRemainingPeriod.getRemainingPeriod())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

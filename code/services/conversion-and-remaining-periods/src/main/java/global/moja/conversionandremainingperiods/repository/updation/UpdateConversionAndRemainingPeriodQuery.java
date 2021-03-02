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
public class UpdateConversionAndRemainingPeriodQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Conversion and Remaining Period record
	 * @param conversionAndRemainingPeriod a bean containing the Conversion and Remaining Period record details
	 * @return the number of Conversion and Remaining Periods records affected by the query i.e updated
	 */
	public Mono<Integer> updateConversionAndRemainingPeriod(ConversionAndRemainingPeriod conversionAndRemainingPeriod){
		
		log.trace("Entering updateConversionAndRemainingPeriod()");

		String query = "UPDATE conversion_and_remaining_period SET previous_land_cover_id = ?, current_land_cover_id  = ?, conversion_period = ?, remaining_period = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							conversionAndRemainingPeriod.getPreviousLandCoverId(),
							conversionAndRemainingPeriod.getCurrentLandCoverId(),
							conversionAndRemainingPeriod.getConversionPeriod(),
							conversionAndRemainingPeriod.getRemainingPeriod(),
							conversionAndRemainingPeriod.getId())
					.counts());
	}
}

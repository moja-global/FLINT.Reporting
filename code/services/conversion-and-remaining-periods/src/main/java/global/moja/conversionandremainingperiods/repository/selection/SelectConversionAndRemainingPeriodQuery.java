/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.repository.selection;

import global.moja.conversionandremainingperiods.configurations.DatabaseConfig;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import global.moja.conversionandremainingperiods.util.builders.ConversionAndRemainingPeriodBuilder;
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
public class SelectConversionAndRemainingPeriodQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Conversion and Remaining Period record from the database given its unique identifier
	 * @param id the unique identifier of the Conversion and Remaining Period record to be selected
	 * @return the Conversion and Remaining Period record with the given id if found
	 */
	public Mono<ConversionAndRemainingPeriod> selectConversionAndRemainingPeriod(Long id) {

		log.trace("Entering selectConversionAndRemainingPeriod()");

		String query = "SELECT * FROM conversion_and_remaining_period WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
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

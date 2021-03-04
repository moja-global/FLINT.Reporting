/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.repository.selection;

import global.moja.locations.configurations.DatabaseConfig;
import global.moja.locations.util.builders.LocationBuilder;
import global.moja.locations.models.Location;
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
public class SelectLocationQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Location record from the database given its unique identifier
	 * @param id the unique identifier of the Location record to be selected
	 * @return the Location record with the given id if found
	 */
	public Mono<Location> selectLocation(Long databaseId, Long id) {

		log.trace("Entering selectLocation()");

		String query = "SELECT * FROM location WHERE location_dimension_id_pk = ?";


		return
				databaseConfig
						.getDatabase(databaseId)
						.flatMap(database ->
								Mono.from(
										database
												.select(query)
												.parameters(id)
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

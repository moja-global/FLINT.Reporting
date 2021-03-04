/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes.repository.selection;

import global.moja.vegetationhistoryvegetationtypes.util.builders.VegetationHistoryVegetationTypeBuilder;
import global.moja.vegetationhistoryvegetationtypes.configurations.DatabaseConfig;
import global.moja.vegetationhistoryvegetationtypes.models.VegetationHistoryVegetationType;
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
public class SelectVegetationHistoryVegetationTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Vegetation History Vegetation Type record from the database given its unique identifier
	 * @param id the unique identifier of the Vegetation History Vegetation Type record to be selected
	 * @return the Vegetation History Vegetation Type record with the given id if found
	 */
	public Mono<VegetationHistoryVegetationType> selectVegetationHistoryVegetationType(Long databaseId, Long id) {

		log.trace("Entering selectVegetationHistoryVegetationType()");

		String query = "SELECT * FROM vegetation_history_vegetation_type WHERE veghistory_vegtypeinfo_mapping_id_pk = ?";

		return
				databaseConfig
						.getDatabase(databaseId)
						.flatMap(database ->
								Mono.from(
										database
												.select(query)
												.parameters(id)
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

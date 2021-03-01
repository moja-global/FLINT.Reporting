/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.repository.updation;

import global.moja.landusesfluxtypestoreportingtables.configurations.DatabaseConfig;
import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
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
public class UpdateLandUseFluxTypeToReportingTableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a LandUseFluxTypeToReportingTable record
	 * @param landUseFluxTypeToReportingTable a bean containing the LandUseFluxTypeToReportingTable record details
	 * @return the number of Land Uses Flux Types To Reporting Tables records affected by the query i.e updated
	 */
	public Mono<Integer> updateLandUseFluxTypeToReportingTable(LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable){
		
		log.trace("Entering updateLandUseFluxTypeToReportingTable()");

		String query = "UPDATE land_use_flux_type_to_reporting_table SET land_use_flux_type_id = ?, emission_type_id  = ?, reporting_table_id = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							landUseFluxTypeToReportingTable.getLandUseFluxTypeId(),
							landUseFluxTypeToReportingTable.getEmissionTypeId(),
							landUseFluxTypeToReportingTable.getReportingTableId(),
							landUseFluxTypeToReportingTable.getId())
					.counts());
	}
}

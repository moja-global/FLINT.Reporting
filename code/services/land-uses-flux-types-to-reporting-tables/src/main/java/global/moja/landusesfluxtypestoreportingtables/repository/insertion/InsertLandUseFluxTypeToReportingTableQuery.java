/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.repository.insertion;

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
public class InsertLandUseFluxTypeToReportingTableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new LandUseFluxTypeToReportingTable record into the database
	 * @param landUseFluxTypeToReportingTable a bean containing the LandUseFluxTypeToReportingTable record details
	 * @return the unique identifier of the newly inserted LandUseFluxTypeToReportingTable record
	 */
	public Mono<Long> insertLandUseFluxTypeToReportingTable(LandUseFluxTypeToReportingTable landUseFluxTypeToReportingTable){
		
		log.trace("Entering insertLandUseFluxTypeToReportingTable()");

		String query = "INSERT INTO land_use_flux_type_to_reporting_table(land_use_flux_type_id,emission_type_id,reporting_table_id) VALUES(?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							landUseFluxTypeToReportingTable.getLandUseFluxTypeId(),
							landUseFluxTypeToReportingTable.getEmissionTypeId(),
							landUseFluxTypeToReportingTable.getReportingTableId())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

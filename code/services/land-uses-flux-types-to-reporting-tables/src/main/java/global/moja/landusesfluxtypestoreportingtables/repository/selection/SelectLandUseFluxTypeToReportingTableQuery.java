/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.repository.selection;

import global.moja.landusesfluxtypestoreportingtables.util.builders.LandUseFluxTypeToReportingTableBuilder;
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
public class SelectLandUseFluxTypeToReportingTableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a LandUseFluxTypeToReportingTable record from the database given its unique identifier
	 * @param id the unique identifier of the LandUseFluxTypeToReportingTable record to be selected
	 * @return the LandUseFluxTypeToReportingTable record with the given id if found
	 */
	public Mono<LandUseFluxTypeToReportingTable> selectLandUseFluxTypeToReportingTable(Long id) {

		log.trace("Entering selectLandUseFluxTypeToReportingTable()");

		String query = "SELECT * FROM land_use_flux_type_to_reporting_table WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new LandUseFluxTypeToReportingTableBuilder()
									.id(rs.getLong("id"))
									.landUseFluxTypeId(rs.getLong("land_use_flux_type_id"))
									.emissionTypeId(rs.getLong("emission_type_id"))
									.reportingTableId(rs.getLong("reporting_table_id"))
									.version(rs.getInt("version"))
									.build()));
	}

}

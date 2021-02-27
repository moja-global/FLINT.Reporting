/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.repository.updation;

import global.moja.reportingtable.configurations.DatabaseConfig;
import global.moja.reportingtable.models.ReportingTable;
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
public class UpdateReportingTableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Reporting Table record
	 * @param reportingTable a bean containing the Reporting Table record details
	 * @return the number of Reporting Tables records affected by the query i.e updated
	 */
	public Mono<Integer> updateReportingTable(ReportingTable reportingTable){
		
		log.trace("Entering updateReportingTable()");

		String query = "UPDATE reporting_table SET reporting_framework_id = ?, number = ?, name = ?, description = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							reportingTable.getReportingFrameworkId(),
							reportingTable.getNumber(),
							reportingTable.getName(),
							reportingTable.getDescription(),
							reportingTable.getId())
					.counts());
	}
}

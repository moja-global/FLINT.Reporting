/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.repository.insertion;

import moja.global.reportingtables.configurations.DatabaseConfig;
import moja.global.reportingtables.models.ReportingTable;
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
public class InsertReportingTableQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts a new reporting table record into the database
	 * @param reportingTable a bean containing the record's details
	 * @return the unique identifier of the newly inserted reportingTable record
	 */
	public Mono<Long> insertReportingTable(ReportingTable reportingTable){
		
		log.trace("Entering insertReportingTable()");

		String query = "INSERT INTO reporting_table(number,name,description) VALUES(?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(reportingTable.getNumber(), reportingTable.getName(), reportingTable.getDescription())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.repository.deletion;

import moja.global.reportingtables.configurations.DatabaseConfig;
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
public class DeleteAllReportingTablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Deletes all reporting table records from the database
	 * @return the number of records affected by the query i.e deleted
	 */		
	public Mono<Integer> deleteAllReportingTables(){

		String query = "DELETE FROM reporting_table";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.counts());
	}	

}

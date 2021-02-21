/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.repository.selection;

import moja.global.reportingtables.configurations.DatabaseConfig;
import moja.global.reportingtables.models.ReportingTable;
import moja.global.reportingtables.util.builders.ReportingTableBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class SelectAllReportingTablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects all reporting table records from the database
	 * @return a list of reporting table records if found
	 */
	public Flux<ReportingTable> selectAllReportingTables() {

		String SELECTION_QUERY = "SELECT * FROM reporting_table";
		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.select(SELECTION_QUERY)
					.get(rs -> 
						new ReportingTableBuilder()
								.id(rs.getLong("id"))
								.number(rs.getString("number"))
								.name(rs.getString("name"))
								.description(rs.getString("description"))
								.version(rs.getInt("version"))
								.build()));
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingtable.repository.selection;

import global.moja.reportingtable.configurations.DatabaseConfig;
import global.moja.reportingtable.models.ReportingTable;
import global.moja.reportingtable.util.builders.ReportingTableBuilder;
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
public class SelectReportingTableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Reporting Table record from the database given its unique identifier
	 * @param id the unique identifier of the Reporting Table record to be selected
	 * @return the Reporting Table record with the given id if found
	 */
	public Mono<ReportingTable> selectReportingTable(Long id) {

		log.trace("Entering selectReportingTable()");

		String query = "SELECT * FROM reporting_table WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new ReportingTableBuilder()
									.id(rs.getLong("id"))
									.reportingFrameworkId(rs.getLong("reporting_framework_id"))
									.number(rs.getString("number"))
									.name(rs.getString("name"))
									.description(rs.getString("description"))
									.version(rs.getInt("version"))
									.build()));
	}

}

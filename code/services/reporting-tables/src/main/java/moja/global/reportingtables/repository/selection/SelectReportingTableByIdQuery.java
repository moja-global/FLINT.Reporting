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
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class SelectReportingTableByIdQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects an reporting table record from the database given its unique identifier
	 * @param id the unique identifier of the record to be selected
	 * @return the record with the given id if found
	 */
	public Mono<ReportingTable> selectReportingTableById(Long id) {

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
									.number(rs.getString("number"))
									.name(rs.getString("name"))
									.description(rs.getString("description"))
									.version(rs.getInt("version"))
									.build()));
	}

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.repository.selection;

import global.moja.reportingvariables.configurations.DatabaseConfig;
import global.moja.reportingvariables.models.ReportingVariable;
import global.moja.reportingvariables.util.builders.ReportingVariableBuilder;
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
public class SelectReportingVariableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Reporting Variable record from the database given its unique identifier
	 * @param id the unique identifier of the Reporting Variable record to be selected
	 * @return the Reporting Variable record with the given id if found
	 */
	public Mono<ReportingVariable> selectReportingVariable(Long id) {

		log.trace("Entering selectReportingVariable()");

		String query = "SELECT * FROM reporting_variable WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new ReportingVariableBuilder()
									.id(rs.getLong("id"))
									.reportingFrameworkId(rs.getLong("reporting_framework_id"))
									.name(rs.getString("name"))
									.description(rs.getString("description"))
									.version(rs.getInt("version"))
									.build()));
	}

}

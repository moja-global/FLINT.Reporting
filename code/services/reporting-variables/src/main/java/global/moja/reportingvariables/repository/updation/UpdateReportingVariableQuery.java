/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.repository.updation;

import global.moja.reportingvariables.configurations.DatabaseConfig;
import global.moja.reportingvariables.models.ReportingVariable;
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
public class UpdateReportingVariableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Reporting Variable record
	 * @param reportingVariable a bean containing the Reporting Variable record details
	 * @return the number of Reporting Variables records affected by the query i.e updated
	 */
	public Mono<Integer> updateReportingVariable(ReportingVariable reportingVariable){
		
		log.trace("Entering updateReportingVariable()");

		String query = "UPDATE reporting_variable SET reporting_framework_id = ?, name = ?, description = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							reportingVariable.getReportingFrameworkId(),
							reportingVariable.getName(),
							reportingVariable.getDescription(),
							reportingVariable.getId())
					.counts());
	}
}

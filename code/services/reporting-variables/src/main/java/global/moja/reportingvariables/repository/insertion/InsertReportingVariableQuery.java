/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingvariables.repository.insertion;

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
public class InsertReportingVariableQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Reporting Variable record into the database
	 * @param reportingVariable a bean containing the Reporting Variable record details
	 * @return the unique identifier of the newly inserted Reporting Variable record
	 */
	public Mono<Long> insertReportingVariable(ReportingVariable reportingVariable){
		
		log.trace("Entering insertReportingVariable()");

		String query = "INSERT INTO reporting_variable(reporting_framework_id,name,description) VALUES(?,?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							reportingVariable.getReportingFrameworkId(),
							reportingVariable.getName(),
							reportingVariable.getDescription())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

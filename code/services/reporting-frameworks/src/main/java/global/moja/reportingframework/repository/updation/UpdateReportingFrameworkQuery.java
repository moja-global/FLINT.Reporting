/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.reportingframework.repository.updation;

import global.moja.reportingframework.configurations.DatabaseConfig;
import global.moja.reportingframework.models.ReportingFramework;
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
public class UpdateReportingFrameworkQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Reporting Framework record
	 * @param reportingFramework a bean containing the Reporting Framework record details
	 * @return the number of Reporting Frameworks records affected by the query i.e updated
	 */
	public Mono<Integer> updateReportingFramework(ReportingFramework reportingFramework){
		
		log.trace("Entering updateReportingFramework()");

		String query = "UPDATE reporting_framework SET name = ?, description = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							reportingFramework.getName(),
							reportingFramework.getDescription(),
							reportingFramework.getId())
					.counts());
	}
}

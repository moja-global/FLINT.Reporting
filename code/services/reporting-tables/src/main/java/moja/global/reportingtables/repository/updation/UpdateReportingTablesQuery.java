/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.repository.updation;

import io.reactivex.Flowable;
import moja.global.reportingtables.configurations.DatabaseConfig;
import moja.global.reportingtables.models.ReportingTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateReportingTablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Recursively Updates reporting table records
	 * @param reportingTables an array of beans containing the records' details
	 * @return the number of records affected by each recursive query i.e updated
	 */	
	public Flux<Integer> updateReportingTables(ReportingTable[] reportingTables) {
		
		log.trace("Entering updateReportingTables()");

		String query = "UPDATE reporting_table SET number = ?, name = ?, description = ? WHERE id = ?";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(reportingTables))
					.counts());
	}

	private Flowable getParametersListStream(ReportingTable[] reportingTables) {
		
		List<List> parameters = new ArrayList<>();
		
		for (ReportingTable p : reportingTables) {
			parameters.add(Arrays.asList(p.getNumber(), p.getName(), p.getDescription(), p.getId()));
		}

		return Flowable.fromIterable(parameters);
	}
	


}

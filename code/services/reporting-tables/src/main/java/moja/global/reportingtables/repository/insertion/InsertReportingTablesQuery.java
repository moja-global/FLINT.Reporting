/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.reportingtables.repository.insertion;

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
public class InsertReportingTablesQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Inserts new reporting table records into the database
	 * @param reportingTables an array of beans containing the records' details
	 * @return the unique identifiers of the newly inserted reporting table records
	 */	
	public Flux<Long> insertReportingTables(ReportingTable[] reportingTables) {

		String query = "INSERT INTO reporting_table(number,name,description) VALUES(?,?, ?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameterListStream(getParametersListStream(reportingTables))
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

	private Flowable getParametersListStream(ReportingTable[] reportingTables) {
		
		List<List> parameters = new ArrayList<>();
		
		for (ReportingTable p : reportingTables) {
			parameters.add(Arrays.asList(p.getNumber(), p.getName(), p.getDescription()));
		}

		return Flowable.fromIterable(parameters);
	}

}

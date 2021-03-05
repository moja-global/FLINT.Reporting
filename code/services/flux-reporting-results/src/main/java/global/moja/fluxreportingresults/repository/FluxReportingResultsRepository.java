/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.fluxreportingresults.repository;

import global.moja.fluxreportingresults.daos.QueryParameters;
import global.moja.fluxreportingresults.models.FluxReportingResult;
import global.moja.fluxreportingresults.repository.selection.SelectFluxReportingResultsQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class FluxReportingResultsRepository {
	
	@Autowired
	SelectFluxReportingResultsQuery selectFluxReportingResultsQuery;

	public Flux<FluxReportingResult> selectFluxReportingResults(Long databaseId, QueryParameters parameters) {
		return selectFluxReportingResultsQuery.selectFluxReportingResults(databaseId, parameters);
	}

}

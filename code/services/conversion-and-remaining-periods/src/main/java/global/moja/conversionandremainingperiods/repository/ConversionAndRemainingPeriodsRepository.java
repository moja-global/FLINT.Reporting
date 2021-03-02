/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.repository;

import global.moja.conversionandremainingperiods.daos.QueryParameters;
import global.moja.conversionandremainingperiods.models.ConversionAndRemainingPeriod;
import global.moja.conversionandremainingperiods.repository.insertion.InsertConversionAndRemainingPeriodQuery;
import global.moja.conversionandremainingperiods.repository.insertion.InsertConversionAndRemainingPeriodsQuery;
import global.moja.conversionandremainingperiods.repository.selection.SelectConversionAndRemainingPeriodQuery;
import global.moja.conversionandremainingperiods.repository.selection.SelectConversionAndRemainingPeriodsQuery;
import global.moja.conversionandremainingperiods.repository.updation.UpdateConversionAndRemainingPeriodQuery;
import global.moja.conversionandremainingperiods.repository.updation.UpdateConversionAndRemainingPeriodsQuery;
import global.moja.conversionandremainingperiods.repository.deletion.DeleteConversionAndRemainingPeriodQuery;
import global.moja.conversionandremainingperiods.repository.deletion.DeleteConversionAndRemainingPeriodsQuery;
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
public class ConversionAndRemainingPeriodsRepository {

	
	@Autowired
    InsertConversionAndRemainingPeriodQuery insertConversionAndRemainingPeriodQuery;
	
	@Autowired
    InsertConversionAndRemainingPeriodsQuery insertConversionAndRemainingPeriodsQuery;
	
	@Autowired
    SelectConversionAndRemainingPeriodQuery selectConversionAndRemainingPeriodQuery;
	
	@Autowired
    SelectConversionAndRemainingPeriodsQuery selectConversionAndRemainingPeriodsQuery;

	@Autowired
	UpdateConversionAndRemainingPeriodQuery updateConversionAndRemainingPeriodQuery;
	
	@Autowired
	UpdateConversionAndRemainingPeriodsQuery updateConversionAndRemainingPeriodsQuery;
	
	@Autowired
	DeleteConversionAndRemainingPeriodQuery deleteConversionAndRemainingPeriodQuery;
	
	@Autowired
    DeleteConversionAndRemainingPeriodsQuery deleteConversionAndRemainingPeriodsQuery;


	public Mono<Long> insertConversionAndRemainingPeriod(ConversionAndRemainingPeriod conversionAndRemainingPeriod) {
		return insertConversionAndRemainingPeriodQuery.insertConversionAndRemainingPeriod(conversionAndRemainingPeriod);
	}
	
	public Flux<Long> insertConversionAndRemainingPeriods(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {
		return insertConversionAndRemainingPeriodsQuery.insertConversionAndRemainingPeriods(conversionAndRemainingPeriods);
	}

	public Mono<ConversionAndRemainingPeriod> selectConversionAndRemainingPeriod(Long id) {
		return selectConversionAndRemainingPeriodQuery.selectConversionAndRemainingPeriod(id);
	}
	
	public Flux<ConversionAndRemainingPeriod> selectConversionAndRemainingPeriods(QueryParameters parameters) {
		return selectConversionAndRemainingPeriodsQuery.selectConversionAndRemainingPeriods(parameters);
	}

	public Mono<Integer> updateConversionAndRemainingPeriod(ConversionAndRemainingPeriod conversionAndRemainingPeriod) {
		return updateConversionAndRemainingPeriodQuery.updateConversionAndRemainingPeriod(conversionAndRemainingPeriod);
	}
	
	public Flux<Integer> updateConversionAndRemainingPeriods(ConversionAndRemainingPeriod[] conversionAndRemainingPeriods) {
		return updateConversionAndRemainingPeriodsQuery.updateConversionAndRemainingPeriods(conversionAndRemainingPeriods);
	}	
	
	public Mono<Integer> deleteConversionAndRemainingPeriodById(Long id) {
		return deleteConversionAndRemainingPeriodQuery.deleteConversionAndRemainingPeriod(id);
	}
	
	public Mono<Integer> deleteConversionAndRemainingPeriods(QueryParameters parameters) {
		return deleteConversionAndRemainingPeriodsQuery.deleteConversionAndRemainingPeriods(parameters);
	}


}

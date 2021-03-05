/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dates.repository;

import global.moja.dates.daos.QueryParameters;
import global.moja.dates.models.Date;
import global.moja.dates.repository.selection.SelectDateQuery;
import global.moja.dates.repository.selection.SelectDatesQuery;
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
public class DatesRepository {

	@Autowired
    SelectDateQuery selectDateQuery;
	
	@Autowired
    SelectDatesQuery selectDatesQuery;

	public Mono<Date> selectDate(Long databaseId, Long id) {
		return selectDateQuery.selectDate(databaseId,id);
	}
	
	public Flux<Date> selectDates(Long databaseId, QueryParameters parameters) {
		return selectDatesQuery.selectDates(databaseId,parameters);
	}

}

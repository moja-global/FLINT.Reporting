/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.repository;

import global.moja.databases.daos.QueryParameters;
import global.moja.databases.models.Database;
import global.moja.databases.repository.updation.UpdateDatabaseQuery;
import global.moja.databases.repository.deletion.DeleteDatabaseQuery;
import global.moja.databases.repository.selection.SelectDatabasesQuery;
import global.moja.databases.repository.insertion.InsertDatabaseQuery;
import global.moja.databases.repository.selection.SelectDatabaseQuery;
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
public class DatabasesRepository {

	
	@Autowired
	InsertDatabaseQuery insertDatabaseQuery;

	@Autowired
	SelectDatabaseQuery selectDatabaseQuery;
	
	@Autowired
	SelectDatabasesQuery selectDatabasesQuery;

	@Autowired
	UpdateDatabaseQuery updateDatabaseQuery;
	
	@Autowired
	DeleteDatabaseQuery deleteDatabaseQuery;

	public Mono<Long> insertDatabase(Database database) {
		return insertDatabaseQuery.insertDatabase(database);
	}

	public Mono<Database> selectDatabase(Long id) {
		return selectDatabaseQuery.selectDatabase(id);
	}
	
	public Flux<Database> selectDatabases(QueryParameters parameters) {
		return selectDatabasesQuery.selectDatabases(parameters);
	}

	public Mono<Integer> updateDatabase(Database database) {
		return updateDatabaseQuery.updateDatabase(database);
	}

	public Mono<Integer> deleteDatabaseById(Long id) {
		return deleteDatabaseQuery.deleteDatabase(id);
	}

}

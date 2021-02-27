/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.repository;

import global.moja.pools.daos.QueryParameters;
import global.moja.pools.models.Pool;
import global.moja.pools.repository.updation.UpdatePoolQuery;
import global.moja.pools.repository.updation.UpdatePoolsQuery;
import global.moja.pools.repository.deletion.DeletePoolQuery;
import global.moja.pools.repository.deletion.DeletePoolsQuery;
import global.moja.pools.repository.selection.SelectPoolsQuery;
import global.moja.pools.repository.insertion.InsertPoolQuery;
import global.moja.pools.repository.insertion.InsertPoolsQuery;
import global.moja.pools.repository.selection.SelectPoolQuery;
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
public class PoolsRepository {

	
	@Autowired
	InsertPoolQuery insertPoolQuery;
	
	@Autowired
	InsertPoolsQuery insertPoolsQuery;
	
	@Autowired
	SelectPoolQuery selectPoolQuery;
	
	@Autowired
	SelectPoolsQuery selectPoolsQuery;

	@Autowired
	UpdatePoolQuery updatePoolQuery;
	
	@Autowired
	UpdatePoolsQuery updatePoolsQuery;
	
	@Autowired
	DeletePoolQuery deletePoolQuery;
	
	@Autowired
    DeletePoolsQuery deletePoolsQuery;


	public Mono<Long> insertPool(Pool pool) {
		return insertPoolQuery.insertPool(pool);
	}
	
	public Flux<Long> insertPools(Pool[] pools) {
		return insertPoolsQuery.insertPools(pools);
	}

	public Mono<Pool> selectPool(Long id) {
		return selectPoolQuery.selectPool(id);
	}
	
	public Flux<Pool> selectPools(QueryParameters parameters) {
		return selectPoolsQuery.selectPools(parameters);
	}

	public Mono<Integer> updatePool(Pool pool) {
		return updatePoolQuery.updatePool(pool);
	}
	
	public Flux<Integer> updatePools(Pool[] pools) {
		return updatePoolsQuery.updatePools(pools);
	}	
	
	public Mono<Integer> deletePoolById(Long id) {
		return deletePoolQuery.deletePool(id);
	}
	
	public Mono<Integer> deletePools(QueryParameters parameters) {
		return deletePoolsQuery.deletePools(parameters);
	}


}

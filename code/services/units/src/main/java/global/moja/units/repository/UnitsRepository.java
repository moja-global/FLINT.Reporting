/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.repository;

import global.moja.units.models.Unit;
import global.moja.units.repository.insertion.InsertUnitsQuery;
import global.moja.units.repository.selection.SelectAllUnitsQuery;
import global.moja.units.repository.selection.SelectUnitByIdQuery;
import global.moja.units.repository.selection.SelectUnitsByIdsQuery;
import global.moja.units.repository.deletion.DeleteAllUnitsQuery;
import global.moja.units.repository.deletion.DeleteUnitByIdQuery;
import global.moja.units.repository.deletion.DeleteUnitsByIdsQuery;
import global.moja.units.repository.insertion.InsertUnitQuery;
import global.moja.units.repository.updation.UpdateUnitQuery;
import global.moja.units.repository.updation.UpdateUnitsQuery;
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
public class UnitsRepository {

	
	@Autowired
    InsertUnitQuery insertUnitQuery;
	
	@Autowired
    InsertUnitsQuery insertUnitsQuery;
	
	@Autowired
    SelectUnitByIdQuery selectUnitByIdQuery;
	
	@Autowired
    SelectUnitsByIdsQuery selectUnitsByIdsQuery;
	
	@Autowired
    SelectAllUnitsQuery selectAllUnitsQuery;

	@Autowired
    UpdateUnitQuery updateUnitQuery;
	
	@Autowired
    UpdateUnitsQuery updateUnitsQuery;
	
	@Autowired
    DeleteUnitByIdQuery deleteUnitByIdQuery;
	
	@Autowired
    DeleteUnitsByIdsQuery deleteUnitsByIdsQuery;
	
	@Autowired
    DeleteAllUnitsQuery deleteAllUnitsQuery;


	public Mono<Long> insertUnit(Unit unit) {
		return insertUnitQuery.insertUnit(unit);
	}
	
	public Flux<Long> insertUnits(Unit[] units) {
		return insertUnitsQuery.insertUnits(units);
	}

	public Mono<Unit> selectUnitById(Long id) {
		return selectUnitByIdQuery.selectUnitById(id);
	}
	
	public Flux<Unit> selectUnitsByIds(Long[] ids) {
		return selectUnitsByIdsQuery.selectUnitsByIds(ids);
	}

	public Flux<Unit> selectAllUnits() {
		return selectAllUnitsQuery.selectAllUnits();
	}	

	public Mono<Integer> updateUnit(Unit unit) {
		return updateUnitQuery.updateUnit(unit);
	}
	
	public Flux<Integer> updateUnits(Unit[] units) {
		return updateUnitsQuery.updateUnits(units);
	}	
	
	public Mono<Integer> deleteUnitById(Long id) {
		return deleteUnitByIdQuery.deleteUnitById(id);
	}
	
	public Mono<Integer> deleteUnitsByIds(Long[] ids) {
		return deleteUnitsByIdsQuery.deleteUnitsByIds(ids);
	}

	public Mono<Integer> deleteAllUnits() {
		return deleteAllUnitsQuery.deleteAllUnits();
	}	

}

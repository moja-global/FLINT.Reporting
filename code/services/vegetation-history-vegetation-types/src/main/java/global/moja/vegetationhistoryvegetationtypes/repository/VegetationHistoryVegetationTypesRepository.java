/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes.repository;

import global.moja.vegetationhistoryvegetationtypes.daos.QueryParameters;
import global.moja.vegetationhistoryvegetationtypes.models.VegetationHistoryVegetationType;
import global.moja.vegetationhistoryvegetationtypes.repository.selection.SelectVegetationHistoryVegetationTypeQuery;
import global.moja.vegetationhistoryvegetationtypes.repository.selection.SelectVegetationHistoryVegetationTypesQuery;
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
public class VegetationHistoryVegetationTypesRepository {

	@Autowired
	SelectVegetationHistoryVegetationTypeQuery selectVegetationHistoryVegetationTypeQuery;
	
	@Autowired
	SelectVegetationHistoryVegetationTypesQuery selectVegetationHistoryVegetationTypesQuery;


	public Mono<VegetationHistoryVegetationType> selectVegetationHistoryVegetationType(Long databaseId,Long id) {
		return selectVegetationHistoryVegetationTypeQuery.selectVegetationHistoryVegetationType(databaseId,id);
	}
	
	public Flux<VegetationHistoryVegetationType> selectVegetationHistoryVegetationTypes(Long databaseId,QueryParameters parameters) {
		return selectVegetationHistoryVegetationTypesQuery.selectVegetationHistoryVegetationTypes(databaseId,parameters);
	}


}

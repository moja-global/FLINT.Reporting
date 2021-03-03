/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.repository;

import global.moja.vegetationtypes.daos.QueryParameters;
import global.moja.vegetationtypes.models.VegetationType;
import global.moja.vegetationtypes.repository.selection.SelectVegetationTypeQuery;
import global.moja.vegetationtypes.repository.selection.SelectVegetationTypesQuery;
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
public class VegetationTypesRepository {

	@Autowired
    SelectVegetationTypeQuery selectVegetationTypeQuery;
	
	@Autowired
    SelectVegetationTypesQuery selectVegetationTypesQuery;


	public Mono<VegetationType> selectVegetationType(Long databaseId, Long id) {
		return selectVegetationTypeQuery.selectVegetationType(databaseId, id);
	}
	
	public Flux<VegetationType> selectVegetationTypes(Long databaseId, QueryParameters parameters) {
		return selectVegetationTypesQuery.selectVegetationTypes(databaseId, parameters);
	}


}

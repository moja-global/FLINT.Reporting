/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.repository.updation;

import global.moja.landusesfluxtypes.configurations.DatabaseConfig;
import global.moja.landusesfluxtypes.models.LandUseFluxType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class UpdateLandUseFluxTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Updates a Land Use Flux Type record
	 * @param landUseFluxType a bean containing the Land Use Flux Type record details
	 * @return the number of Land Uses Flux Types records affected by the query i.e updated
	 */
	public Mono<Integer> updateLandUseFluxType(LandUseFluxType landUseFluxType){
		
		log.trace("Entering updateLandUseFluxType()");

		String query = "UPDATE land_use_flux_type SET land_use_category_id  = ?, flux_type_id = ? WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							landUseFluxType.getLandUseCategoryId(),
							landUseFluxType.getFluxTypeId(),
							landUseFluxType.getId())
					.counts());
	}
}

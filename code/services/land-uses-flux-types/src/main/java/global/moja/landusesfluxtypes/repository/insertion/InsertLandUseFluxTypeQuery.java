/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.repository.insertion;

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
public class InsertLandUseFluxTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Inserts a new Land Use Flux Type record into the database
	 * @param landUseFluxType a bean containing the Land Use Flux Type record details
	 * @return the unique identifier of the newly inserted Land Use Flux Type record
	 */
	public Mono<Long> insertLandUseFluxType(LandUseFluxType landUseFluxType){
		
		log.trace("Entering insertLandUseFluxType()");

		String query = "INSERT INTO land_use_flux_type(land_use_category_id,flux_type_id) VALUES(?,?)";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.update(query)
					.parameters(
							landUseFluxType.getLandUseCategoryId(),
							landUseFluxType.getFluxTypeId())
					.returnGeneratedKeys()
					.getAs(Long.class));
	}

}

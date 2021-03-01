/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypes.repository.selection;

import global.moja.landusesfluxtypes.models.LandUseFluxType;
import global.moja.landusesfluxtypes.util.builders.LandUseFluxTypeBuilder;
import global.moja.landusesfluxtypes.configurations.DatabaseConfig;
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
public class SelectLandUseFluxTypeQuery {

	@Autowired
    DatabaseConfig databaseConfig;

	/**
	 * Selects a Land Use Flux Type record from the database given its unique identifier
	 * @param id the unique identifier of the Land Use Flux Type record to be selected
	 * @return the Land Use Flux Type record with the given id if found
	 */
	public Mono<LandUseFluxType> selectLandUseFluxType(Long id) {

		log.trace("Entering selectLandUseFluxType()");

		String query = "SELECT * FROM land_use_flux_type WHERE id = ?";

		return
			Mono.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameters(id)
					.get(rs ->
							new LandUseFluxTypeBuilder()
									.id(rs.getLong("id"))
									.landUseCategoryId(rs.getLong("land_use_category_id"))
									.fluxTypeId(rs.getLong("flux_type_id"))
									.version(rs.getInt("version"))
									.build()));
	}

}

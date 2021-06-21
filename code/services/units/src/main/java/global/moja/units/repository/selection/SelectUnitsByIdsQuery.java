/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.units.repository.selection;

import global.moja.units.configurations.DatabaseConfig;
import global.moja.units.models.Unit;
import global.moja.units.util.builders.UnitBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashSet;

/**
 * @since 1.0
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class SelectUnitsByIdsQuery {

	@Autowired
	DatabaseConfig databaseConfig;

	/**
	 * Selects specific unit records from the database given their unique identifier
	 * @param ids the unique identifiers of the records
	 * @return a list of unit records corresponding to the identifiers if found
	 */
	public Flux<Unit> selectUnitsByIds(Long[] ids) {

		String query = "SELECT * FROM unit WHERE id in (?)";

		return
			Flux.from(
				databaseConfig
					.getDatabase()
					.select(query)
					.parameter(new HashSet<>(Arrays.asList(ids)))
					.get(rs ->
							new UnitBuilder()
									.id(rs.getLong("id"))
									.unitCategoryId(rs.getLong("unit_category_id"))
									.name(rs.getString("name"))
									.plural(rs.getString("plural"))
									.symbol(rs.getString("symbol"))
									.scaleFactor(rs.getDouble("scale_factor"))
									.version(rs.getInt("version"))
									.build()));
	}
}

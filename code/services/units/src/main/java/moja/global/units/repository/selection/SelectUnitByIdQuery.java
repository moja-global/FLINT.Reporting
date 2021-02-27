/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.units.repository.selection;

import moja.global.units.configurations.DatabaseConfig;
import moja.global.units.models.Unit;
import moja.global.units.util.builders.UnitBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class SelectUnitByIdQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects a unit record from the database given its unique identifier
     *
     * @param id the unique identifier of the record to be selected
     * @return the record with the given id if found
     */
    public Mono<Unit> selectUnitById(Long id) {

        String query = "SELECT * FROM unit WHERE id = ?";

        return
                Mono.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .parameters(id)
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

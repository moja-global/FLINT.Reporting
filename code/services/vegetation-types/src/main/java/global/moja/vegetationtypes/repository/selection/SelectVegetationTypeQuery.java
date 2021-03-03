/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationtypes.repository.selection;

import global.moja.vegetationtypes.configurations.DatabaseConfig;
import global.moja.vegetationtypes.models.VegetationType;
import global.moja.vegetationtypes.util.builders.VegetationTypeBuilder;
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
public class SelectVegetationTypeQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects a Vegetation Type record from the database given its unique identifier
     *
     * @param id the unique identifier of the Vegetation Type record to be selected
     * @return the Vegetation Type record with the given id if found
     */
    public Mono<VegetationType> selectVegetationType(Long databaseId, Long id) {

        log.trace("Entering selectVegetationType()");

        String query = "SELECT * FROM vegetation_type WHERE id = ?";

        return
                databaseConfig
                        .getDatabase(databaseId)
                        .flatMap(database ->
                                Mono.from(
                                        database
                                                .select(query)
                                                .parameters(id)
                                                .get(rs ->
                                                        new VegetationTypeBuilder()
                                                                .id(rs.getLong("id"))
                                                                .coverTypeId(rs.getLong("cover_type_id"))
                                                                .name(rs.getString("name"))
                                                                .woodyType(rs.getBoolean("woody_type"))
                                                                .naturalSystem(rs.getBoolean("natural_system"))
                                                                .version(rs.getInt("version"))
                                                                .build())));


    }

}

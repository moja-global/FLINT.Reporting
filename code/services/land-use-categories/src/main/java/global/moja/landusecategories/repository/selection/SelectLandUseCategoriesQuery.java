/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository.selection;

import global.moja.landusecategories.configurations.DatabaseConfig;
import global.moja.landusecategories.daos.QueryParameters;
import global.moja.landusecategories.models.LandUseCategory;
import global.moja.landusecategories.util.builders.LandUseCategoryBuilder;
import global.moja.landusecategories.util.builders.QueryWhereClauseBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class SelectLandUseCategoriesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Selects all or specific Land Use Categories records from the database depending on whether or not
     * query parameters were supplied as part of the query
     *
     * @return a list of Land Use Categories records if found
     */
    public Flux<LandUseCategory> selectLandUseCategories(QueryParameters parameters) {

        log.trace("Entering selectLandUseCategories()");

        String query =
                "SELECT * FROM land_use_category" +
                        new QueryWhereClauseBuilder()
                                .queryParameters(parameters)
                                .build();

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .select(query)
                                .get(rs ->
                                        new LandUseCategoryBuilder()
                                                .id(rs.getLong("id"))
                                                .reportingFrameworkId(rs.getLong("reporting_framework_id"))
                                                .parentLandUseCategoryId(rs.getLong("parent_land_use_category_id"))
                                                .coverTypeId(rs.getLong("cover_type_id"))
                                                .name(rs.getString("name"))
                                                .version(rs.getInt("version"))
                                                .build()));
    }

}

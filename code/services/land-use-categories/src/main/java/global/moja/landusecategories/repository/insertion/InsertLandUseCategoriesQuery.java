/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository.insertion;

import global.moja.landusecategories.configurations.DatabaseConfig;
import global.moja.landusecategories.models.LandUseCategory;
import io.reactivex.Flowable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class InsertLandUseCategoriesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Land Use Categories records into the database
     *
     * @param landUseCategories an array of beans containing the Land Use Categories records details
     * @return the unique identifiers of the newly inserted Land Use Categories records
     */
    public Flux<Long> insertLandUseCategories(LandUseCategory[] landUseCategories) {

        log.trace("Entering insertLandUseCategories");

        String query = "INSERT INTO land_use_category(reporting_framework_id,parent_land_use_category_id,cover_type_id,name) VALUES(?,?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(landUseCategories))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(LandUseCategory[] landUseCategories) {

        List<List> temp = new ArrayList<>();

        for (LandUseCategory landUseCategory : landUseCategories) {
            temp.add(Arrays.asList(
                    landUseCategory.getReportingFrameworkId(),
                    landUseCategory.getParentLandUseCategoryId(),
                    landUseCategory.getCoverTypeId(),
                    landUseCategory.getName()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

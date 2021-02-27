/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.repository.updation;

import io.reactivex.Flowable;
import global.moja.landusecategories.configurations.DatabaseConfig;
import global.moja.landusecategories.models.LandUseCategory;
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
public class UpdateLandUseCategoriesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Land Use Categories records
     *
     * @param landUseCategories an array of beans containing the Land Use Categories records details
     * @return the number of Land Use Categories records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateLandUseCategories(LandUseCategory[] landUseCategories) {

        log.trace("Entering updateLandUseCategories()");

        String query = "UPDATE land_use_category SET reporting_framework_id = ?, parent_land_use_category_id  = ?, cover_type_id = ?, name = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(landUseCategories))
                                .counts());
    }

    private Flowable getParametersListStream(LandUseCategory[] landUseCategories) {

        List<List> temp = new ArrayList<>();

        for (LandUseCategory landUseCategory : landUseCategories) {
            temp.add(Arrays.asList(
                    landUseCategory.getReportingFrameworkId(),
                    landUseCategory.getParentLandUseCategoryId(),
                    landUseCategory.getCoverTypeId(),
                    landUseCategory.getName(),
                    landUseCategory.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

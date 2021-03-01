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
public class UpdateLandUsesFluxTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Land Uses Flux Types records
     *
     * @param landUsesFluxTypes an array of beans containing the Land Uses Flux Types records details
     * @return the number of Land Uses Flux Types records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateLandUsesFluxTypes(LandUseFluxType[] landUsesFluxTypes) {

        log.trace("Entering updateLandUsesFluxTypes()");

        String query = "UPDATE land_use_flux_type SET land_use_category_id  = ?, flux_type_id = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(landUsesFluxTypes))
                                .counts());
    }

    private Flowable getParametersListStream(LandUseFluxType[] landUsesFluxTypes) {

        List<List> temp = new ArrayList<>();

        for (LandUseFluxType landUseFluxType : landUsesFluxTypes) {
            temp.add(Arrays.asList(
                    landUseFluxType.getLandUseCategoryId(),
                    landUseFluxType.getFluxTypeId(),
                    landUseFluxType.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

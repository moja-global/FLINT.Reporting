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
public class InsertLandUsesFluxTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Land Uses Flux Types records into the database
     *
     * @param landUsesFluxTypes an array of beans containing the Land Uses Flux Types records details
     * @return the unique identifiers of the newly inserted Land Uses Flux Types records
     */
    public Flux<Long> insertLandUsesFluxTypes(LandUseFluxType[] landUsesFluxTypes) {

        log.trace("Entering insertLandUsesFluxTypes");

        String query = "INSERT INTO land_use_flux_type(land_use_category_id,flux_type_id) VALUES(?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(landUsesFluxTypes))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(LandUseFluxType[] landUsesFluxTypes) {

        List<List> temp = new ArrayList<>();

        for (LandUseFluxType landUseFluxType : landUsesFluxTypes) {
            temp.add(Arrays.asList(
                    landUseFluxType.getLandUseCategoryId(),
                    landUseFluxType.getFluxTypeId()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

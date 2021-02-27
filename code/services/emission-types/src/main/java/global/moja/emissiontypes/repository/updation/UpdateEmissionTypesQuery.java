/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.emissiontypes.repository.updation;

import global.moja.emissiontypes.configurations.DatabaseConfig;
import global.moja.emissiontypes.models.EmissionType;
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
public class UpdateEmissionTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Emission Types records
     *
     * @param emissionTypes an array of beans containing the Emission Types records details
     * @return the number of Emission Types records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateEmissionTypes(EmissionType[] emissionTypes) {

        log.trace("Entering updateEmissionTypes()");

        String query = "UPDATE emission_type SET name = ?, abbreviation = ?, description = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(emissionTypes))
                                .counts());
    }

    private Flowable getParametersListStream(EmissionType[] emissionTypes) {

        List<List> temp = new ArrayList<>();

        for (EmissionType emissionType : emissionTypes) {
            temp.add(Arrays.asList(
                    emissionType.getName(),
                    emissionType.getAbbreviation(),
                    emissionType.getDescription(),
                    emissionType.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

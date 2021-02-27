/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.covertypes.repository.updation;

import global.moja.covertypes.models.CoverType;
import io.reactivex.Flowable;
import global.moja.covertypes.configurations.DatabaseConfig;
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
public class UpdateCoverTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Cover Types records
     *
     * @param coverTypes an array of beans containing the Cover Types records details
     * @return the number of Cover Types records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateCoverTypes(CoverType[] coverTypes) {

        log.trace("Entering updateCoverTypes()");

        String query = "UPDATE cover_type SET code = ?, description = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(coverTypes))
                                .counts());
    }

    private Flowable getParametersListStream(CoverType[] coverTypes) {

        List<List> temp = new ArrayList<>();

        for (CoverType coverType : coverTypes) {
            temp.add(Arrays.asList(
                    coverType.getCode(),
                    coverType.getDescription(),
                    coverType.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

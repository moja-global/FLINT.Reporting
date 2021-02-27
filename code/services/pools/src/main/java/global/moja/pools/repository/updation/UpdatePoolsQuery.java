/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.pools.repository.updation;

import global.moja.pools.models.Pool;
import io.reactivex.Flowable;
import global.moja.pools.configurations.DatabaseConfig;
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
public class UpdatePoolsQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Pools records
     *
     * @param pools an array of beans containing the Pools records details
     * @return the number of Pools records affected by each recursive query i.e updated
     */
    public Flux<Integer> updatePools(Pool[] pools) {

        log.trace("Entering updatePools()");

        String query = "UPDATE pool SET name = ?, description = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(pools))
                                .counts());
    }

    private Flowable getParametersListStream(Pool[] pools) {

        List<List> temp = new ArrayList<>();

        for (Pool pool : pools) {
            temp.add(Arrays.asList(
                    pool.getName(),
                    pool.getDescription(),
                    pool.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

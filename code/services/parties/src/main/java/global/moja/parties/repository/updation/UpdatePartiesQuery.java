/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.repository.updation;

import global.moja.parties.configurations.DatabaseConfig;
import global.moja.parties.models.Party;
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
public class UpdatePartiesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Parties records
     *
     * @param parties an array of beans containing the Parties records details
     * @return the number of Parties records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateParties(Party[] parties) {

        log.trace("Entering updateParties()");

        String query = "UPDATE party SET party_type_id = ?, name = ?, WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(parties))
                                .counts());
    }

    private Flowable getParametersListStream(Party[] parties) {

        List<List> temp = new ArrayList<>();

        for (Party party : parties) {
            temp.add(Arrays.asList(
                    party.getPartyTypeId(),
                    party.getName(),
                    party.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.repository.updation;

import global.moja.accountabilities.models.Accountability;
import io.reactivex.Flowable;
import global.moja.accountabilities.configurations.DatabaseConfig;
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
public class UpdateAccountabilitiesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Recursively Updates Accountabilities records
     *
     * @param accountabilities an array of beans containing the Accountabilities records details
     * @return the number of Accountabilities records affected by each recursive query i.e updated
     */
    public Flux<Integer> updateAccountabilities(Accountability[] accountabilities) {

        log.trace("Entering updateAccountabilities()");

        String query = "UPDATE accountability SET accountability_type_id = ?, parent_party_id  = ?, subsidiary_party_id = ? WHERE id = ?";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(accountabilities))
                                .counts());
    }

    private Flowable getParametersListStream(Accountability[] accountabilities) {

        List<List> temp = new ArrayList<>();

        for (Accountability accountability : accountabilities) {
            temp.add(Arrays.asList(
                    accountability.getAccountabilityTypeId(),
                    accountability.getParentPartyId(),
                    accountability.getSubsidiaryPartyId(),
                    accountability.getId()
            ));
        }

        return Flowable.fromIterable(temp);
    }


}

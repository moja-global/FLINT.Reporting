/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilities.repository.insertion;

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
public class InsertAccountabilitiesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Accountabilities records into the database
     *
     * @param accountabilities an array of beans containing the Accountabilities records details
     * @return the unique identifiers of the newly inserted Accountabilities records
     */
    public Flux<Long> insertAccountabilities(Accountability[] accountabilities) {

        log.trace("Entering insertAccountabilities");

        String query = "INSERT INTO accountability(accountability_type_id,accountability_rule_id,parent_party_id,subsidiary_party_id) VALUES(?,?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(accountabilities))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(Accountability[] accountabilities) {

        List<List> temp = new ArrayList<>();

        for (Accountability accountability : accountabilities) {
            temp.add(Arrays.asList(
                    accountability.getAccountabilityTypeId(),
                    accountability.getAccountabilityRuleId(),
                    accountability.getParentPartyId(),
                    accountability.getSubsidiaryPartyId()

            ));
        }

        return Flowable.fromIterable(temp);
    }

}

/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilityrules.repository.insertion;

import global.moja.accountabilityrules.configurations.DatabaseConfig;
import global.moja.accountabilityrules.models.AccountabilityRule;
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
public class InsertAccountabilityRulesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Accountability Rules records into the database
     *
     * @param accountabilityRules an array of beans containing the Accountability Rules records details
     * @return the unique identifiers of the newly inserted Accountability Rules records
     */
    public Flux<Long> insertAccountabilityRules(AccountabilityRule[] accountabilityRules) {

        log.trace("Entering insertAccountabilityRules");

        String query = "INSERT INTO accountability_rule(accountability_type_id,parent_party_type_id,subsidiary_party_type_id) VALUES(?,?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(accountabilityRules))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(AccountabilityRule[] accountabilityRules) {

        List<List> temp = new ArrayList<>();

        for (AccountabilityRule accountabilityRule : accountabilityRules) {
            temp.add(Arrays.asList(
                    accountabilityRule.getAccountabilityTypeId(),
                    accountabilityRule.getParentPartyTypeId(),
                    accountabilityRule.getSubsidiaryPartyTypeId()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

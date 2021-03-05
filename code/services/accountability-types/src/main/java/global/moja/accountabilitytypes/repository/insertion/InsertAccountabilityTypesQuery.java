/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.accountabilitytypes.repository.insertion;

import global.moja.accountabilitytypes.configurations.DatabaseConfig;
import global.moja.accountabilitytypes.models.AccountabilityType;
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
public class InsertAccountabilityTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Accountability Types records into the database
     *
     * @param accountabilityTypes an array of beans containing the Accountability Types records details
     * @return the unique identifiers of the newly inserted Accountability Types records
     */
    public Flux<Long> insertAccountabilityTypes(AccountabilityType[] accountabilityTypes) {

        log.trace("Entering insertAccountabilityTypes");

        String query = "INSERT INTO accountability_type(name) VALUES(?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(accountabilityTypes))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(AccountabilityType[] accountabilityTypes) {

        List<List> temp = new ArrayList<>();

        for (AccountabilityType accountabilityType : accountabilityTypes) {
            temp.add(Arrays.asList(

                    accountabilityType.getName()

            ));
        }

        return Flowable.fromIterable(temp);
    }

}

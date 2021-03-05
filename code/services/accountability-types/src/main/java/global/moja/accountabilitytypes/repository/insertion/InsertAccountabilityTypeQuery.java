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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class InsertAccountabilityTypeQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts a new Accountability Type record into the database
     *
     * @param accountabilityType a bean containing the Accountability Type record details
     * @return the unique identifier of the newly inserted Accountability Type record
     */
    public Mono<Long> insertAccountabilityType(AccountabilityType accountabilityType) {

        log.trace("Entering insertAccountabilityType()");

        String query = "INSERT INTO accountability_type(name) VALUES(?)";

        return
                Mono.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameters(
                                        accountabilityType.getName()
                                )
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

}

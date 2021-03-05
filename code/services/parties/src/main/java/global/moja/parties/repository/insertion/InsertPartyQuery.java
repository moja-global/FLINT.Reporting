/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.parties.repository.insertion;

import global.moja.parties.models.Party;
import global.moja.parties.configurations.DatabaseConfig;
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
public class InsertPartyQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts a new Party record into the database
     *
     * @param party a bean containing the Party record details
     * @return the unique identifier of the newly inserted Party record
     */
    public Mono<Long> insertParty(Party party) {

        log.trace("Entering insertParty()");

        String query = "INSERT INTO party(party_type_id,name) VALUES(?,?)";

        return
                Mono.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameters(
                                        party.getPartyTypeId(),
                                        party.getName()
                                )
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

}

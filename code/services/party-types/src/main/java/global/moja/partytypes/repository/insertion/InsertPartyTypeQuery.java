/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.partytypes.repository.insertion;

import global.moja.partytypes.configurations.DatabaseConfig;
import global.moja.partytypes.models.PartyType;
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
public class InsertPartyTypeQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts a new Party Type record into the database
     *
     * @param partyType a bean containing the Party Type record details
     * @return the unique identifier of the newly inserted Party Type record
     */
    public Mono<Long> insertPartyType(PartyType partyType) {

        log.trace("Entering insertPartyType()");

        String query = "INSERT INTO party_type(parent_party_type_id,name) VALUES(?,?)";


        return
                Mono.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameters(
                                        partyType.getParentPartyTypeId(),
                                        partyType.getName()
                                )
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

}

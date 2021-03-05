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
public class InsertPartyTypesQuery {

    @Autowired
    DatabaseConfig databaseConfig;

    /**
     * Inserts new Party Types records into the database
     *
     * @param partyTypes an array of beans containing the Party Types records details
     * @return the unique identifiers of the newly inserted Party Types records
     */
    public Flux<Long> insertPartyTypes(PartyType[] partyTypes) {

        log.trace("Entering insertPartyTypes");

        String query = "INSERT INTO party_type(parent_party_type_id,name) VALUES(?,?)";

        return
                Flux.from(
                        databaseConfig
                                .getDatabase()
                                .update(query)
                                .parameterListStream(getParametersListStream(partyTypes))
                                .returnGeneratedKeys()
                                .getAs(Long.class));
    }

    private Flowable getParametersListStream(PartyType[] partyTypes) {

        List<List> temp = new ArrayList<>();

        for (PartyType partyType : partyTypes) {
            temp.add(Arrays.asList(
                    partyType.getParentPartyTypeId(),
                    partyType.getName()
            ));
        }

        return Flowable.fromIterable(temp);
    }

}

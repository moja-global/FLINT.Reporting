/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.crftables.util.endpoints;


import global.moja.crftables.models.Database;
import global.moja.crftables.models.Party;
import global.moja.crftables.models.QuantityObservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class EndpointsUtil {

    @Autowired
    DatabasesEndpointsUtil databasesEndpointsUtil;

    @Autowired
    PartiesEndpointsUtil partiesEndpointsUtil;

    @Autowired
    QuantityObservationsEndpointUtil quantityObservationsEndpointUtil;

    public Mono<Database> retrieveDatabase(Long databaseId) {
        return databasesEndpointsUtil.retrieveDatabase(databaseId);
    }

    public Mono<Party> retrieveParty(Long partyId) {
        return partiesEndpointsUtil.retrieveParty(partyId);
    }

    public Flux<QuantityObservation> retrieveQuantityObservations(
            Long partyId,
            Long databaseId,
            Integer minYear,
            Integer maxYear ) {

        return quantityObservationsEndpointUtil.retrieveQuantityObservations(
                partyId, databaseId, minYear,maxYear);
    }





}

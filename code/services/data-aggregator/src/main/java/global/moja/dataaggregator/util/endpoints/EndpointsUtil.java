/*
 * Copyright (C) 2021 Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataaggregator.util.endpoints;

import global.moja.dataaggregator.models.Accountability;
import global.moja.dataaggregator.models.QuantityObservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
public class EndpointsUtil {

    @Autowired
    AccountabilitiesEndpointUtil accountabilitiesEndpointUtil;

    @Autowired
    QuantityObservationsEndpointUtil quantityObservationsEndpointUtil;

    public Flux<Long> createQuantityObservations(QuantityObservation[] observations) {
        return quantityObservationsEndpointUtil.createQuantityObservations(observations);
    }

    public Flux<QuantityObservation> retrieveQuantityObservations(Long observationTypeId, List<Long> partiesId, Long databaseId ) {
        return quantityObservationsEndpointUtil.retrieveQuantityObservations(observationTypeId, partiesId, databaseId);
    }

    public Flux<Accountability> retrieveAccountabilities(Long accountabilityTypeId, Long parentPartyId) {
        return accountabilitiesEndpointUtil.retrieveAccountabilities(accountabilityTypeId, parentPartyId);
    }

}

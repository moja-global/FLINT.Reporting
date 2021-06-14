/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataaggregator.util.endpoints;

import global.moja.dataaggregator.models.QuantityObservation;
import global.moja.dataaggregator.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class QuantityObservationsEndpointUtil {

    @Autowired
    QuantityObservationsCreationEndpointUtil quantityObservationsCreationEndpointUtil;

    @Autowired
    QuantityObservationsRetrievalEndpointUtil quantityObservationsRetrievalEndpointUtil;

    public Flux<QuantityObservation> createQuantityObservations(QuantityObservation[] observations) {
        return quantityObservationsCreationEndpointUtil.createQuantityObservations(observations);
    }

    public Flux<QuantityObservation> retrieveQuantityObservations(Long observationTypeId, List<Long> partiesId, Long databaseId ) {
        return quantityObservationsRetrievalEndpointUtil
                .retrieveQuantityObservations(observationTypeId, partiesId, databaseId);
    }
}

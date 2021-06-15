/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.handlers.get;

import global.moja.quantityobservations.exceptions.ServerException;
import global.moja.quantityobservations.repository.QuantityObservationsRepository;
import global.moja.quantityobservations.models.QuantityObservation;
import global.moja.quantityobservations.util.builders.QueryParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class RetrieveQuantityObservationsHandler {

    @Autowired
    QuantityObservationsRepository repository;

    /**
     * Retrieves all Quantity Observations or specific Quantity Observations if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Quantity Observations records to be retrieved
     * @return the stream of responses containing the details of the retrieved Quantity Observations records
     */
    public Mono<ServerResponse> retrieveQuantityObservations(ServerRequest request) {

        log.trace("Entering retrieveQuantityObservations()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveQuantityObservationsConsideringQueryParameters(request),
                                QuantityObservation.class)
                        .onErrorMap(e -> new ServerException("Quantity Observations retrieval failed", e));
    }

    private Flux<QuantityObservation> retrieveQuantityObservationsConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectQuantityObservations(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .observationTypeId(request)
                                        .taskId(request)
                                        .partiesIds(request)
                                        .databaseId(request)
                                        .landUseCategoryId(request)
                                        .reportingTableId(request)
                                        .reportingVariableId(request)
                                        .year(request)
                                        .minYear(request)
                                        .maxYear(request)
                                        .build());
    }


}

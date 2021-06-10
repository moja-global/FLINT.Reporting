/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.quantityobservations.handlers.delete;

import global.moja.quantityobservations.exceptions.ServerException;
import global.moja.quantityobservations.repository.QuantityObservationsRepository;
import global.moja.quantityobservations.util.builders.QueryParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class DeleteQuantityObservationsHandler {

    @Autowired
    QuantityObservationsRepository repository;

    /**
     * Deletes all Quantity Observations or specific Quantity Observations records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Quantity Observations records to be deleted
     * @return the response containing the number of Quantity Observations records deleted
     */
    public Mono<ServerResponse> deleteQuantityObservations(ServerRequest request) {

        log.trace("Entering deleteQuantityObservations()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteQuantityObservationsConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Quantity Observations deletion failed", e));
    }


    private Mono<Integer> deleteQuantityObservationsConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteQuantityObservations(
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
                                        .build());
    }


}

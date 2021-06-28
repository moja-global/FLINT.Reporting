/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.vegetationhistoryvegetationtypes.handlers.get;

import global.moja.vegetationhistoryvegetationtypes.exceptions.ServerException;
import global.moja.vegetationhistoryvegetationtypes.repository.VegetationHistoryVegetationTypesRepository;
import global.moja.vegetationhistoryvegetationtypes.util.builders.QueryParametersBuilder;
import global.moja.vegetationhistoryvegetationtypes.models.VegetationHistoryVegetationType;
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
public class RetrieveVegetationHistoryVegetationTypesHandler {

    @Autowired
    VegetationHistoryVegetationTypesRepository repository;

    /**
     * Retrieves all Vegetation History Vegetation Types or specific Vegetation History Vegetation Types if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Vegetation History Vegetation Types records to be retrieved
     * @return the stream of responses containing the details of the retrieved Vegetation History Vegetation Types records
     */
    public Mono<ServerResponse> retrieveVegetationHistoryVegetationTypes(ServerRequest request) {

        log.trace("Entering retrieveVegetationHistoryVegetationTypes()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveVegetationHistoryVegetationTypesConsideringQueryParameters(request),
                                VegetationHistoryVegetationType.class)
                        .onErrorMap(e -> new ServerException("Vegetation History Vegetation Types retrieval failed", e));
    }

    private Flux<VegetationHistoryVegetationType> retrieveVegetationHistoryVegetationTypesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectVegetationHistoryVegetationTypes(
                                Long.parseLong(request.pathVariable("databaseId")),
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .vegetationHistoryId(request)
                                        .vegetationTypeId(request)
                                        .year(request)
                                        .partyId(request)
                                        .build());
    }


}

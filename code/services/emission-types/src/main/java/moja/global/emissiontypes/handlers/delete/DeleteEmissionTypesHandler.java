/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.emissiontypes.handlers.delete;

import moja.global.emissiontypes.exceptions.ServerException;
import moja.global.emissiontypes.repository.EmissionTypesRepository;
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
public class DeleteEmissionTypesHandler {

    @Autowired
    EmissionTypesRepository repository;

    /**
     * Deletes all emission type records or specific emission type records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the emission type records to be deleted
     * @return the response containing the number of emission type records deleted
     */
    public Mono<ServerResponse> deleteEmissionTypes(ServerRequest request) {

        log.trace("Entering deleteEmissionTypes()");

        return
                Mono.just(getIds(request))
                        .flatMap(ids ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(ids.length == 0 ? deleteAllEmissionTypes() : deleteEmissionTypesByIds(ids), Integer.class))
                        .onErrorMap(e -> new ServerException("Emission Type records deletion failed", e));
    }


    private Mono<Integer> deleteAllEmissionTypes() {

        return repository.deleteAllEmissionTypes();
    }

    private Mono<Integer> deleteEmissionTypesByIds(Long[] ids) {

        return repository.deleteEmissionTypesByIds(ids);
    }

    private Long[] getIds(ServerRequest request) {

        return
                request.queryParams().get("ids") == null ? new Long[]{}:
                        request.queryParams()
                                .get("ids")
                                .stream()
                                .sorted()
                                .map(Long::parseLong)
                                .toArray(Long[]::new);

    }


}

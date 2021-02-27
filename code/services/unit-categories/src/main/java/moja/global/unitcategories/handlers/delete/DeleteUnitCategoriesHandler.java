/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package moja.global.unitcategories.handlers.delete;

import moja.global.unitcategories.exceptions.ServerException;
import moja.global.unitcategories.repository.UnitCategoriesRepository;
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
public class DeleteUnitCategoriesHandler {

    @Autowired
    UnitCategoriesRepository repository;

    /**
     * Deletes all unit category records or specific unit category records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the unit category records to be deleted
     * @return the response containing the number of unit category records deleted
     */
    public Mono<ServerResponse> deleteUnitCategories(ServerRequest request) {

        log.trace("Entering deleteUnitCategories()");

        return
                Mono.just(getIds(request))
                        .flatMap(ids ->
                                ServerResponse
                                        .ok()
                                        .contentType(MediaType.APPLICATION_JSON)
                                        .body(ids.length == 0 ? deleteAllUnitCategories() : deleteUnitCategoriesByIds(ids), Integer.class))
                        .onErrorMap(e -> new ServerException("Unit Category records deletion failed", e));
    }


    private Mono<Integer> deleteAllUnitCategories() {

        return repository.deleteAllUnitCategories();
    }

    private Mono<Integer> deleteUnitCategoriesByIds(Long[] ids) {

        return repository.deleteUnitCategoriesByIds(ids);
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

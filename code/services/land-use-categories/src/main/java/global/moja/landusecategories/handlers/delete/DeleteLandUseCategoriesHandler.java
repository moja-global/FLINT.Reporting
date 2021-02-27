/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusecategories.handlers.delete;

import global.moja.landusecategories.util.builders.QueryParametersBuilder;
import global.moja.landusecategories.exceptions.ServerException;
import global.moja.landusecategories.repository.LandUseCategoriesRepository;
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
public class DeleteLandUseCategoriesHandler {

    @Autowired
    LandUseCategoriesRepository repository;

    /**
     * Deletes all Land Use Categories or specific Land Use Categories records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Land Use Categories records to be deleted
     * @return the response containing the number of Land Use Categories records deleted
     */
    public Mono<ServerResponse> deleteLandUseCategories(ServerRequest request) {

        log.trace("Entering deleteLandUseCategories()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteLandUseCategoriesConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Land Use Categories deletion failed", e));
    }


    private Mono<Integer> deleteLandUseCategoriesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteLandUseCategories(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .reportingFrameworkId(request)
                                        .parentLandUseCategoryId(request)
                                        .coverTypeId(request)
                                        .name(request)
                                        .build());
    }


}

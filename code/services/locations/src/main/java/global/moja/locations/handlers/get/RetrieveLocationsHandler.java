/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.locations.handlers.get;

import global.moja.locations.exceptions.ServerException;
import global.moja.locations.repository.LocationsRepository;
import global.moja.locations.util.builders.QueryParametersBuilder;
import global.moja.locations.models.Location;
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
public class RetrieveLocationsHandler {

    @Autowired
    LocationsRepository repository;

    /**
     * Retrieves all Locations or specific Locations if given query filters
     *
     * @param request the request, optionally containing the unique identifiers of the Locations records to be retrieved
     * @return the stream of responses containing the details of the retrieved Locations records
     */
    public Mono<ServerResponse> retrieveLocations(ServerRequest request) {

        log.trace("Entering retrieveLocations()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveLocationsConsideringQueryParameters(request),
                                Location.class)
                        .onErrorMap(e -> new ServerException("Locations retrieval failed", e));
    }

    private Flux<Location> retrieveLocationsConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectLocations(
                                Long.parseLong(request.pathVariable("databaseId")),
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .partyId(request)
                                        .tileId(request)
                                        .vegetationHistoryId(request)
                                        .build());
    }


}

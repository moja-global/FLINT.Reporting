/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataprocessor.util.endpoints;

import global.moja.dataprocessor.models.Location;
import global.moja.dataprocessor.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class LocationsEndpointsUtil {

    @Autowired
    WebClientUtil webClientUtil;

    public Flux<Location> retrieveLocations(Long databaseId, Long partyId) {

        String prefix = "[Database: " + databaseId + ", Party: " + partyId + "]";

        log.trace("{}",prefix);
        log.trace("------------------------------------------------------------------------");
        log.trace("{} Retrieving Locations", prefix);
        log.trace("------------------------------------------------------------------------");
        log.trace("{}",prefix);

        return webClientUtil
                .getLocationsWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/databases/{param1}/all")
                                .queryParam("partyId", "{param2}")
                                .build(Long.toString(databaseId),Long.toString(partyId)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(Location.class);
    }
}

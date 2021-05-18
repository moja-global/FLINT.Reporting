/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.businessintelligence.util.endpoints;

import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.models.Location;
import global.moja.businessintelligence.models.VegetationType;
import global.moja.businessintelligence.util.webclient.impl.WebClientUtil;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class VegetationTypesEndpointsUtil {

    @Autowired
    WebClientUtil webClientUtil;

    private final Map<Long, Map<Long, VegetationType>> cache = new HashMap<>();

    public Mono<VegetationType> retrieveVegetationType(Long databaseId, Long vegetationTypeId) {

        log.trace("Entering retrieveVegetationType()");
        log.debug("Database Id = {}",databaseId);
        log.debug("Vegetation Type Id = {}", vegetationTypeId);

        // Get the mapping of vegetation type id -> Vegetation Type from the local cache
        log.trace("Getting the mapping of vegetation type id -> Vegetation Type from the local cache");
        final Map<Long, VegetationType> m = cache.get(databaseId) == null? new HashMap<>(): cache.get(databaseId);

        // Check if the mapping is empty
        log.trace("Checking if the mapping is empty");
        if (m.isEmpty()) {

            // The mapping is null
            log.trace("The mapping is null");

            // Retrieve a fresh set of vegetation types from the api endpoint
            log.trace("Retrieving a fresh set of vegetation types from the api endpoint");
            return webClientUtil
                    .getVegetationTypesWebClient()
                    .get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/databases/{param1}/all")
                                    .build(Long.toString(databaseId)))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(VegetationType.class)
                    .collect(Collectors.toList())
                    .map(list -> {

                        //Add the vegetation type details to the cache
                        log.trace("Adding {} vegetation type record mappings to the local cache",list.size());
                        list.forEach(v -> m.put(v.getId(),v));
                        cache.put(databaseId,m);

                        // Retrieve and return the vegetation type from the local cache
                        log.trace("Retrieving and returning the vegetation type from the local cache");
                        return m.get(vegetationTypeId);

                    });
        } else {

            // The mapping is not null
            log.trace("The mapping is not null");

            // Retrieve and return the vegetation type from the local cache
            log.trace("Retrieving and returning the vegetation type from the local cache");
            return Mono.just(m.get(vegetationTypeId));
        }


    }

}

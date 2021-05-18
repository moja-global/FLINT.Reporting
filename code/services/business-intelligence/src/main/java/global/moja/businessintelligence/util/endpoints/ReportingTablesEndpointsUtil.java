/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.businessintelligence.util.endpoints;

import global.moja.businessintelligence.models.CoverType;
import global.moja.businessintelligence.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
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
public class CoverTypesEndpointsUtil {

    @Autowired
    WebClientUtil webClientUtil;

    private final Map<Long, CoverType> cache = new HashMap<>();

    public Mono<CoverType> retrieveCoverType(Long databaseId, Long coverTypeId) {

        log.trace("Entering retrieveCoverType()");
        log.debug("Database Id = {}",databaseId);
        log.debug("Cover Type Id = {}", coverTypeId);

        // Check if the local cache is empty
        log.trace("Checking if the local cache is empty");
        if (cache.isEmpty()) {

            // The local cache is empty
            log.trace("The local cache is empty");

            // Retrieve a fresh set of cover types from the api endpoint
            log.trace("Retrieving a fresh set of cover types from the api endpoint");
            return webClientUtil
                    .getCoverTypesWebClient()
                    .get()
                    .uri(uriBuilder ->
                            uriBuilder
                                    .path("/databases/{param1}/all")
                                    .build(Long.toString(databaseId)))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToFlux(CoverType.class)
                    .collect(Collectors.toList())
                    .map(list -> {

                        //Add the cover type details to the cache
                        log.trace("Adding {} cover type record mappings to the local cache",list.size());
                        list.forEach(v -> cache.put(v.getId(),v));

                        // Retrieve and return the cover type from the local cache
                        log.trace("Retrieving and returning the cover type from the local cache");
                        return cache.get(coverTypeId);

                    });
        } else {

            // The local cache is not empty
            log.trace("The local cache is not empty");

            // Retrieve and return the cover type from the local cache
            log.trace("Retrieving and returning the cover type from the local cache");
            return Mono.just(cache.get(coverTypeId));
        }


    }

}

/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.util.endpoints;

import global.moja.taskmanager.models.QuantityObservation;
import global.moja.taskmanager.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class QuantityObservationsEndpointUtil {

    @Autowired
    WebClientUtil webClientUtil;

    public Mono<Integer> deleteQuantityObservations(Long databaseId) {

        log.trace("Entering deleteQuantityObservations()");
        log.debug("Database Id = {}", databaseId);

        return webClientUtil
                .getQuantityObservationsWebClient()
                .delete()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/all")
                                .queryParam("databaseId", "{id1}")
                                .build(databaseId.toString()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Integer.class);
    }

}

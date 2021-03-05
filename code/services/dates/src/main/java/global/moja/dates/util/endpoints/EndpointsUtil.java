/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dates.util.endpoints;


import global.moja.dates.models.Database;
import global.moja.dates.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class EndpointsUtil {

    @Autowired
    private WebClientUtil webClientUtil;


    public Mono<Database> retrieveDatabaseById(Long id) {

        log.trace("Entering retrieveDatabaseById()");

        return webClientUtil
                .getDatabasesWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/ids/{id}")
                                .build(Long.toString(id)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Database.class);
    }
}

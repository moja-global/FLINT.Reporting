/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.databases.util.endpoints;

import global.moja.databases.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class TaskManagerEndpointUtil {

    @Autowired
    WebClientUtil webClientUtil;

    public Mono<Void> integrateDatabase(Long databaseId) {

        log.trace("Calling integrateDatabase()");

        return webClientUtil
                .getTaskManagerWebClient()
                .post()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/databases/{id}")
                                .build(Long.toString(databaseId)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

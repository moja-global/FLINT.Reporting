/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.util.endpoints;

import global.moja.taskmanager.models.Database;
import global.moja.taskmanager.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class DatabasesRetrievalEndpointUtil {

  @Autowired
  WebClientUtil webClientUtil;

  public Mono<Database> retrieveDatabase(Long databaseId) {

    log.trace("Entering retrieveDatabases()");

    return webClientUtil
        .getDatabasesWebClient()
        .get()
            .uri(uriBuilder ->
                    uriBuilder
                            .path("/api/v1/databases/ids/{id}")
                            .build(databaseId.toString()))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToMono(Database.class);
  }
}

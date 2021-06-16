/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.crftables.util.endpoints;

import global.moja.crftables.util.webclient.WebClientUtil;
import global.moja.crftables.models.QuantityObservation;
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
public class QuantityObservationsEndpointUtil {

  @Autowired
  WebClientUtil webClientUtil;

  public Flux<QuantityObservation> retrieveQuantityObservations(
          Long partyId,
          Long databaseId,
          Integer minYear,
          Integer maxYear ) {

    log.trace("Entering retrieveQuantityObservations()");
    log.debug("Party Id = {}", partyId);
    log.debug("Database Id = {}", databaseId);
    log.debug("Min Year = {}", minYear);
    log.debug("Max Year = {}", maxYear);

    return webClientUtil
        .getQuantityObservationsWebClient()
        .get()
            .uri(uriBuilder ->
                    uriBuilder
                            .path("/all")
                            .queryParam("partiesIds", "{id1}")
                            .queryParam("databaseId", "{id2}")
                            .queryParam("minYear", "{id3}")
                            .queryParam("maxYear", "{id4}")
                            .build(
                                    partyId.toString(),
                                    databaseId.toString(),
                                    minYear.toString(),
                                    maxYear.toString()
                            ))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(QuantityObservation.class);
  }
}

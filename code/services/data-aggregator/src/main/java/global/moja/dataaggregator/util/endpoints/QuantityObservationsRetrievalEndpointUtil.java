/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataaggregator.util.endpoints;

import global.moja.dataaggregator.models.Accountability;
import global.moja.dataaggregator.models.QuantityObservation;
import global.moja.dataaggregator.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class QuantityObservationsRetrievalEndpointUtil {

  @Autowired
  WebClientUtil webClientUtil;

  public Flux<QuantityObservation> retrieveQuantityObservations(Long observationTypeId, List<Long> partiesId, Long databaseId ) {

    log.trace("Entering retrieveQuantityObservations()");
    log.debug("Observation Type Id = {}", observationTypeId);
    log.debug("Parties Ids = {}", partiesId);
    log.debug("Database Id = {}", databaseId);

    return webClientUtil
        .getQuantityObservationsWebClient()
        .get()
            .uri(uriBuilder ->
                    uriBuilder
                            .path("/all")
                            .queryParam("observationTypeId", "{id1}")
                            .queryParam("partiesIds", "{id2}")
                            .queryParam("databaseId", "{id3}")
                            .build(
                                    observationTypeId.toString(),
                                    String.join(",",partiesId.stream().map(p -> p.toString()).collect(Collectors.toSet())),
                                    databaseId.toString()))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(QuantityObservation.class);
  }
}

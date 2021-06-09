/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.util.endpoints;

import global.moja.dataprocessor.models.QuantityObservation;
import global.moja.dataprocessor.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

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

  public Mono<List<Long>> createQuantityObservations(QuantityObservation[] observations) {

    log.trace("Calling createQuantityObservations()");

    return webClientUtil
        .getQuantityObservationsWebClient()
        .post()
		.uri("/all")
        .body(Mono.just(observations), QuantityObservation[].class)
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(Long.class)
				.collectList();
  }
}

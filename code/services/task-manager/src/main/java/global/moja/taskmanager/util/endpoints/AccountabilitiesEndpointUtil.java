/*
 * Copyright (C) 2020 The Second Mile
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.taskmanager.util.endpoints;

import global.moja.taskmanager.models.Accountability;
import global.moja.taskmanager.util.webclient.impl.WebClientUtil;
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
public class AccountabilitiesEndpointUtil {

  @Autowired
  WebClientUtil webClientUtil;

  public Flux<Accountability> retrieveAccountabilities(Long accountabilityTypeId, Long parentPartyId) {

    log.trace("Entering retrieveAccountabilities()");

    return webClientUtil
        .getAccountabilitiesWebClient()
        .get()
            .uri(uriBuilder ->
                    uriBuilder
                            .path("/all")
                            .queryParam("accountabilityTypeId", "{id1}")
                            .queryParam("parentPartyId", "{id2}")
                            .build(accountabilityTypeId.toString(), parentPartyId.toString()))
        .accept(MediaType.APPLICATION_JSON)
        .retrieve()
        .bodyToFlux(Accountability.class);
  }
}

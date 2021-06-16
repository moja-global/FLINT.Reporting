/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.crftables.util.endpoints;

import global.moja.crftables.models.Party;
import global.moja.crftables.util.webclient.WebClientUtil;
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
public class PartiesEndpointsUtil {

    @Autowired
    WebClientUtil webClientUtil;

    public Mono<Party> retrieveParty(Long partyId) {

        log.trace("Entering retrieveParties()");

        return webClientUtil
                .getPartiesWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/ids/{id}")
                                .build(partyId.toString()))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Party.class);

    }

}

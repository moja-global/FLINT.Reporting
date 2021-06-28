/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataprocessor.util.endpoints;

import global.moja.dataprocessor.models.VegetationHistoryVegetationType;
import global.moja.dataprocessor.util.webclient.impl.WebClientUtil;
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
public class VegetationHistoryVegetationTypesEndpointsUtil {

    @Autowired
    WebClientUtil webClientUtil;

    public Flux<VegetationHistoryVegetationType> retrieveVegetationHistoryVegetationTypes(
            Long databaseId, Long vegetationHistoryId) {

        log.trace("Entering retrieveVegetationHistoryVegetationTypes()");
        log.debug("Database Id = {}", databaseId);
        log.debug("Vegetation History Id = {}", vegetationHistoryId);

        return webClientUtil
                .getVegetationHistoryVegetationTypesWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/databases/{param1}/all")
                                .queryParam("vegetationHistoryId", "{param2}")
                                .build(Long.toString(databaseId),Long.toString(vegetationHistoryId)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(VegetationHistoryVegetationType.class);
    }


    public Flux<VegetationHistoryVegetationType> retrieveVegetationHistoryVegetationTypesByPartyId(
            Long databaseId, Long partyId) {

        log.trace("Entering retrieveVegetationHistoryVegetationTypesByPartyId()");
        log.debug("Database Id = {}", databaseId);
        log.debug("Party Id = {}", partyId);

        return webClientUtil
                .getVegetationHistoryVegetationTypesWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/databases/{param1}/all")
                                .queryParam("partyId", "{param2}")
                                .build(Long.toString(databaseId),Long.toString(partyId)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(VegetationHistoryVegetationType.class);
    }
}

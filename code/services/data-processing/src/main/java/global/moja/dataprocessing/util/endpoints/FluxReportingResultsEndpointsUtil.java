/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.dataprocessing.util.endpoints;

import global.moja.dataprocessing.models.FluxReportingResult;
import global.moja.dataprocessing.util.webclient.impl.WebClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @since 0.0.1
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 */
@Component
@Slf4j
public class FluxReportingResultsEndpointsUtil {

    @Autowired
    WebClientUtil webClientUtil;

    public Flux<FluxReportingResult> retrieveFluxReportingResults(
            Long databaseId, Long locationId) {

        log.trace("Entering retrieveFluxReportingResults()");
        log.debug("Database Id = {}",databaseId);
        log.debug("Location Id = {}", locationId);

        return webClientUtil
                .getFluxReportingResultsWebClient()
                .get()
                .uri(uriBuilder ->
                        uriBuilder
                                .path("/databases/{param1}/all")
                                .queryParam("locationId", "{param2}")
                                .build(Long.toString(databaseId),Long.toString(locationId)))
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(FluxReportingResult.class)
                .map(f -> {

                    // Format value to 6 decimal places
                    f.setFlux(f.getFlux() == null ? null: new BigDecimal(f.getFlux()).setScale(6, RoundingMode.HALF_UP).doubleValue());
                    return f;
                });
    }
}

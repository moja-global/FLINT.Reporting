/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.conversionandremainingperiods.handlers.delete;

import global.moja.conversionandremainingperiods.util.builders.QueryParametersBuilder;
import global.moja.conversionandremainingperiods.exceptions.ServerException;
import global.moja.conversionandremainingperiods.repository.ConversionAndRemainingPeriodsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class DeleteConversionAndRemainingPeriodsHandler {

    @Autowired
    ConversionAndRemainingPeriodsRepository repository;

    /**
     * Deletes all Conversion and Remaining Periods or specific Conversion and Remaining Periods records if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Conversion and Remaining Periods records to be deleted
     * @return the response containing the number of Conversion and Remaining Periods records deleted
     */
    public Mono<ServerResponse> deleteConversionAndRemainingPeriods(ServerRequest request) {

        log.trace("Entering deleteConversionAndRemainingPeriods()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(deleteConversionAndRemainingPeriodsConsideringQueryParameters(request), Integer.class)
                        .onErrorMap(e -> new ServerException("Conversion and Remaining Periods deletion failed", e));
    }


    private Mono<Integer> deleteConversionAndRemainingPeriodsConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .deleteConversionAndRemainingPeriods(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .previousLandCoverId(request)
                                        .currentLandCoverId(request)
                                        .build());
    }


}

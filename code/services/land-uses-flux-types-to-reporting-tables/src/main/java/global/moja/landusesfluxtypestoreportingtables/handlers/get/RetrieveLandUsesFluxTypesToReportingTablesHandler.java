/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.landusesfluxtypestoreportingtables.handlers.get;

import global.moja.landusesfluxtypestoreportingtables.exceptions.ServerException;
import global.moja.landusesfluxtypestoreportingtables.models.LandUseFluxTypeToReportingTable;
import global.moja.landusesfluxtypestoreportingtables.repository.LandUsesFluxTypesToReportingTablesRepository;
import global.moja.landusesfluxtypestoreportingtables.util.builders.QueryParametersBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 1.0
 */
@Component
@Slf4j
public class RetrieveLandUsesFluxTypesToReportingTablesHandler {

    @Autowired
    LandUsesFluxTypesToReportingTablesRepository repository;

    /**
     * Retrieves all Land Uses Flux Types To Reporting Tables or specific Land Uses Flux Types To Reporting Tables if given their unique identifiers
     *
     * @param request the request, optionally containing the unique identifiers of the Land Uses Flux Types To Reporting Tables records to be retrieved
     * @return the stream of responses containing the details of the retrieved Land Uses Flux Types To Reporting Tables records
     */
    public Mono<ServerResponse> retrieveLandUsesFluxTypesToReportingTables(ServerRequest request) {

        log.trace("Entering retrieveLandUsesFluxTypesToReportingTables()");

        return
                ServerResponse
                        .ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(retrieveLandUsesFluxTypesToReportingTablesConsideringQueryParameters(request),
                                LandUseFluxTypeToReportingTable.class)
                        .onErrorMap(e -> new ServerException("Land Uses Flux Types To Reporting Tables retrieval failed", e));
    }

    private Flux<LandUseFluxTypeToReportingTable> retrieveLandUsesFluxTypesToReportingTablesConsideringQueryParameters(ServerRequest request) {

        return
                repository
                        .selectLandUsesFluxTypesToReportingTables(
                                new QueryParametersBuilder()
                                        .ids(request)
                                        .landUseFluxTypeId(request)
                                        .emissionTypeId(request)
                                        .reportingTableId(request)
                                        .build());
    }


}

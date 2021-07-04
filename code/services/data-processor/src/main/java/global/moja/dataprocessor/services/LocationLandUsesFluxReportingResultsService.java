
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.LocationLandUsesFluxReportingResultsHistories;
import global.moja.dataprocessor.daos.LocationLandUsesFluxReportingResultsHistory;
import global.moja.dataprocessor.daos.LocationLandUsesHistories;
import global.moja.dataprocessor.models.FluxReportingResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationLandUsesFluxReportingResultsService {

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    private final String logMessagePrefix = "[Location Land Uses Flux Reporting Results Service]";

    Mono<LocationLandUsesFluxReportingResultsHistories> getLocationLandUsesFluxReportingResultsHistories(
            Long databaseId,
            LocationLandUsesHistories locationLandUsesHistories,
            Map<Long, Collection<FluxReportingResult>> fluxReportingResultMap) {

        return
                // 1. Retrieve the Flux Reporting Results records corresponding to the provided Database and Location id
                Flux.fromIterable(fluxReportingResultMap.getOrDefault(locationLandUsesHistories.getLocationId(), new ArrayList<>()))


                        // 2. Collect and map the Flux Reporting Results by their Date Dimension Id
                        .collectMultimap(FluxReportingResult::getDateId, result -> result)

                        // 3. Convert each Land Uses History record to the corresponding
                        // Land Use Fluxes History record
                        .flatMap(dateMappedFluxReportingResults ->

                                Flux.fromIterable(locationLandUsesHistories.getHistories())
                                        .map(landUsesHistoricDetail ->

                                                LocationLandUsesFluxReportingResultsHistory
                                                        .builder()
                                                        .itemNumber(landUsesHistoricDetail.getItemNumber())
                                                        .year(landUsesHistoricDetail.getYear())
                                                        .landUseCategory(landUsesHistoricDetail.getLandUseCategory())
                                                        .confirmed(landUsesHistoricDetail.getConfirmed())
                                                        .fluxReportingResults(
                                                               new ArrayList<>(
                                                                       dateMappedFluxReportingResults.getOrDefault(
                                                                               configurationDataProvider
                                                                                       .getDateDimensionId(
                                                                                               databaseId,
                                                                                               landUsesHistoricDetail
                                                                                                       .getYear()),
                                                                               new ArrayList<>()
                                                                       )
                                                               )
                                                             )
                                                        .build())
                                        .collectList()
                        )

                        // 4. Build and return the Location Vegetation Types Histories record
                        .map(records ->
                                LocationLandUsesFluxReportingResultsHistories
                                        .builder()
                                        .locationId(locationLandUsesHistories.getLocationId())
                                        .partyId(locationLandUsesHistories.getPartyId())
                                        .tileId(locationLandUsesHistories.getTileId())
                                        .vegetationHistoryId(locationLandUsesHistories.getVegetationHistoryId())
                                        .unitCount(locationLandUsesHistories.getUnitCount())
                                        .unitAreaSum(locationLandUsesHistories.getUnitAreaSum())
                                        .histories(records)
                                        .build());
    }


}

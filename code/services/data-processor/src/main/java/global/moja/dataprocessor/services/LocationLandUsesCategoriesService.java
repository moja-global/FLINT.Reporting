
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.*;
import global.moja.dataprocessor.daos.LocationCoverTypesHistories;
import global.moja.dataprocessor.exceptions.ServerException;
import global.moja.dataprocessor.models.CoverType;
import global.moja.dataprocessor.util.LandUsesCategoriesAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationLandUsesCategoriesService {

    @Autowired
    LandUsesCategoriesAllocator decisionTree;

    private final String logMessagePrefix = "[Location Land Uses Categories Service]";

    public Mono<LocationLandUsesHistories> getLocationLandUsesCategoriesHistories(
            LocationCoverTypesHistories locationCoverTypesHistories) {

        log.trace("{} - Entering getLocationLandUsesCategoriesHistories()", logMessagePrefix);
        log.debug("{} - Location Cover Types Histories = {}", logMessagePrefix, locationCoverTypesHistories);


        // Validate the Location Cover Types Histories
        log.trace("Validating the Location Cover Types Histories");
        if (locationCoverTypesHistories == null) {
            return Mono.error(new ServerException(logMessagePrefix + " - The Location Cover Types Histories should not be null"));
        }

        // Instantiate a list to temporarily hold Location Land Uses Histories as they are being processed.
        // This will be referred to - in its most up to date format - in the decision allocation tree
        final List<LocationLandUsesHistory> locationLandUsesHistories = new ArrayList<>();

        return

                // 1. Create a Flux from the Cover Types Historic Details.
                Flux.fromIterable(locationCoverTypesHistories.getHistories())

                        // 2. Filter out records that can't be processed
                        .filter(coverTypesHistoricDetail -> coverTypesHistoricDetail.getCoverType() != null)

                        // 3. Convert each Location Cover Types History to the corresponding Location Land Uses History.
                        // Keep a reference to this record in the Land Uses Historic Detail for the benefit of the
                        // next processing step
                        .map(coverTypesHistoricDetail -> {
                            LocationLandUsesHistory l = decisionTree.allocateLandUseCategory(
                                    coverTypesHistoricDetail.getItemNumber(),
                                    locationCoverTypesHistories.getHistories(),
                                    locationLandUsesHistories
                            );

                            locationLandUsesHistories.add(l);

                            return l;
                        })

                        .onErrorMap(e -> new ServerException(logMessagePrefix + " - Land Use Category Assignment failed", e))

                        // 4. Collect the Land Uses Histories records.
                        .collect(Collectors.toList())

                        // 5. Build and return the Location Land uses Histories record.
                        .map(records ->
                                LocationLandUsesHistories
                                        .builder()
                                        .locationId(locationCoverTypesHistories.getLocationId())
                                        .partyId(locationCoverTypesHistories.getPartyId())
                                        .tileId(locationCoverTypesHistories.getTileId())
                                        .vegetationHistoryId(locationCoverTypesHistories.getVegetationHistoryId())
                                        .unitCount(locationCoverTypesHistories.getUnitCount())
                                        .unitAreaSum(locationCoverTypesHistories.getUnitAreaSum())
                                        .histories(records)
                                        .build());
    }


}

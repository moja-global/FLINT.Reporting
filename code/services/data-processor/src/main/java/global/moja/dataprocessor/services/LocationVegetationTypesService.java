
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.LocationVegetationTypesHistories;
import global.moja.dataprocessor.daos.LocationVegetationTypesHistory;
import global.moja.dataprocessor.models.Location;
import global.moja.dataprocessor.models.VegetationHistoryVegetationType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationVegetationTypesService {

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    private final String logMessagePrefix = "[Location Vegetation Types Service]";

    Mono<LocationVegetationTypesHistories> getLocationVegetationTypesHistories(
            Long databaseId,
            Location location,
            Map<Long, Collection<VegetationHistoryVegetationType>> vegetationHistoryVegetationTypeMap) {

        return Mono.just(
                LocationVegetationTypesHistories
                        .builder()
                        .locationId(location.getId())
                        .partyId(location.getPartyId())
                        .tileId(location.getTileId())
                        .vegetationHistoryId(location.getVegetationHistoryId())
                        .unitCount(location.getUnitCount())
                        .unitAreaSum(location.getUnitAreaSum())
                        .histories(
                                vegetationHistoryVegetationTypeMap
                                        .getOrDefault(location.getVegetationHistoryId(),new ArrayList<>())
                                        .stream()
                                        .map(v ->
                                                LocationVegetationTypesHistory
                                                        .builder()
                                                        .itemNumber(v.getItemNumber())
                                                        .year(v.getYear())
                                                        .vegetationType(
                                                                configurationDataProvider
                                                                        .getVegetationType(
                                                                                databaseId,
                                                                                v.getVegetationTypeId()))
                                                        .build())
                                        .collect(Collectors.toList()))
                        .build());
    }
}

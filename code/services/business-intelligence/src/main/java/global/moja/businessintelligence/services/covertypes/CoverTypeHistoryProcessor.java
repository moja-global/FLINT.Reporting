/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package global.moja.businessintelligence.services.covers;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.CoverTypeHistory;
import global.moja.businessintelligence.models.CoverType;
import global.moja.businessintelligence.models.Location;
import global.moja.businessintelligence.models.VegetationType;
import global.moja.businessintelligence.util.builders.CoverTypeHistoryBuilder;
import global.moja.businessintelligence.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CoverTypeHistoryProcessor {

    @Autowired
    EndpointsUtil endpointsUtil;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;


    public Mono<List<CoverTypeHistory>> processCoverTypeHistory(Long databaseId, Location location) {

        log.trace("Entering getCoverTypesHistory()");
        log.debug("Database Id = {}", databaseId);
        log.debug("Location Id = {}", databaseId);
        log.debug("Vegetation History Id = {}", location.getVegetationHistoryId());

        return

                // Use the vegetation history id to retrieve the corresponding vegetation history vegetation types
                // records from the api endpoint
                endpointsUtil
                        .retrieveVegetationHistoryVegetationTypes(
                                databaseId,
                                location.getVegetationHistoryId())
                        .flatMap(vegetationHistoryVegetationType -> {

                            // Use the vegetation type id detail in each vegetation history vegetation
                            // type record to retrieve the corresponding vegetation type record from the
                            // configuration data provider
                            log.trace("Retrieving Vegetation Type Record");
                            VegetationType vegetationType = configurationDataProvider
                                    .getVegetationType(
                                            databaseId,
                                            vegetationHistoryVegetationType.getVegetationTypeId());

                            // Check if the Vegetation Type Record is null
                            log.trace("Checking if the Vegetation Type Record is null");
                            log.debug("Vegetation Type = {}", vegetationType);

                            if (vegetationType == null) {

                                // Log a warning message
                                log.warn("Setting Location {}, Timestep {} Cover Type to Null due to missing " +
                                                "Vegetation Type {} Record in Database {}",
                                        location.getId(),
                                        vegetationHistoryVegetationType.getItemNumber(),
                                        vegetationHistoryVegetationType.getVegetationTypeId(),
                                        databaseId);

                                // Setting the location's cover type for the target timestep to null and return
                                return Mono.just(new CoverTypeHistoryBuilder()
                                        .itemNumber(vegetationHistoryVegetationType.getItemNumber())
                                        .year(vegetationHistoryVegetationType.getYear())
                                        .coverType(null)
                                        .build());

                            }


                            // Use the cover type id detail in each vegetation type record
                            // to retrieve the corresponding cover type record from the
                            // configuration data provider
                            log.trace("Retrieving Cover Type Record");
                            CoverType coverType = configurationDataProvider
                                    .getCoverType(vegetationType.getCoverTypeId());


                            // Check if the Cover Type Record is null
                            log.trace("Checking if the Cover Type Record is null");
                            log.debug("Cover Type = {}", coverType);

                            if (coverType == null) {

                                // Record is null
                                // Log a warning message
                                log.warn("No Cover Type Record with Id {} was found in the Database",
                                        vegetationType.getCoverTypeId());

                                // Log a warning message
                                log.warn("Setting Location {}, Timestep {} Cover Type to Null due to missing " +
                                                "Vegetation Type {} Record in Database {}",
                                        location.getId(),
                                        vegetationHistoryVegetationType.getItemNumber(),
                                        vegetationHistoryVegetationType.getVegetationTypeId(),
                                        databaseId);

                                // Setting the location's cover type for the target timestep to null and return
                                return Mono.just(new CoverTypeHistoryBuilder()
                                        .itemNumber(vegetationHistoryVegetationType.getItemNumber())
                                        .year(vegetationHistoryVegetationType.getYear())
                                        .coverType(null)
                                        .build());

                            }


                            // Use the cover type record to transform the
                            // vegetation history vegetation type record to the corresponding
                            // cover type history record
                            log.trace("Transforming the vegetation history vegetation type record to the cover type " +
                                    "history record");
                            return Mono.just(new CoverTypeHistoryBuilder()
                                    .itemNumber(vegetationHistoryVegetationType.getItemNumber())
                                    .year(vegetationHistoryVegetationType.getYear())
                                    .coverType(coverType)
                                    .build());
                        })

                        // Collect and return the transformed records
                        .collect(Collectors.toList());
    }
}

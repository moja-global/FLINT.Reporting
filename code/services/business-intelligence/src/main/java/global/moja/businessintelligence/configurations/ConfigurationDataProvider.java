
/*
 * Copyright (C) 2020 The Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.configurations;

import global.moja.businessintelligence.models.*;
import global.moja.businessintelligence.util.LandUseChangeAction;
import global.moja.businessintelligence.util.endpoints.EndpointsUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Configuration
@PropertySource(value = {"classpath:hosts.properties"})
@Getter
@Slf4j
public class ConfigurationDataProvider {

    @Autowired
    EndpointsUtil endpointsUtil;

    private final List<ConversionAndRemainingPeriod> conversionAndRemainingPeriods = new ArrayList<>();
    private final List<CoverType> coverTypes = new ArrayList<>();
    private final List<Database> databases = new ArrayList<>();
    private final List<LandUseCategory> landUseCategories = new ArrayList<>();
    private final List<ReportingTable> reportingTables = new ArrayList<>();
    private final Map<Long, List<VegetationType>> databasesVegetationTypes = new HashMap<>();

    @PostConstruct
    private void init() {

        // Retrieve & Set Conversion & Remaining Periods Records
        endpointsUtil
                .retrieveConversionAndRemainingPeriods()
                .collect(Collectors.toList())
                .doOnNext(list -> {
                    log.debug("Adding {} Conversion & Remaining Periods Records to the data configuration");
                    conversionAndRemainingPeriods.addAll(list);
                })
                .thenMany(

                        // Retrieve & Set Cover Types Records
                        endpointsUtil
                                .retrieveCoverTypes()
                                .collect(Collectors.toList())
                                .doOnNext(list -> {
                                    log.debug("Adding {} Cover Type Records to the data configuration", list.size());
                                    coverTypes.addAll(list);
                                }))
                .thenMany(

                        // Retrieve & Set Databases Records
                        endpointsUtil
                                .retrieveDatabases()
                                .collect(Collectors.toList())
                                .doOnNext(list -> {
                                    log.debug("Adding {} Databases Records to the data configuration", list.size());
                                    databases.addAll(list);
                                }))
                .thenMany(

                        // Retrieve & Set Land Use Categories Records
                        endpointsUtil
                                .retrieveLandUseCategories()
                                .collect(Collectors.toList())
                                .doOnNext(list -> {
                                    log.debug("Adding {} Land Use Categories Records to the data configuration", list.size());
                                    landUseCategories.addAll(list);
                                }))
                .thenMany(

                        // Retrieve & Set Reporting Tables Records
                        endpointsUtil
                                .retrieveReportingTables()
                                .collect(Collectors.toList())
                                .doOnNext(list -> {
                                    log.debug("Adding {} Reporting Tables Records to the data configuration", list.size());
                                    reportingTables.addAll(list);
                                }))
                .thenMany(

                        // Retrieve & Set Vegetation Types Records
                        Flux.fromStream(databases.stream())
                                .flatMap(database ->
                                        endpointsUtil
                                                .retrieveVegetationTypes(database.getId())
                                                .collect(Collectors.toList())
                                                .doOnNext(list -> {
                                                    log.debug("Adding {} Vegetation Types Records to the data configuration", list.size());
                                                    databasesVegetationTypes.put(database.getId(), list);
                                                })))
                .subscribe();

    }

    //<editor-fold desc="Conversion & Remaining Period">


    public ConversionAndRemainingPeriod getConversionAndRemainingPeriod(
            Long previousCoverTypeId, Long currentCoverTypeId) {

        log.trace("Entering getConversionAndRemainingPeriod()");
        log.debug("Previous Cover Type Id = {}", previousCoverTypeId);
        log.debug("Current Cover Type Id = {}", currentCoverTypeId);

        return
                conversionAndRemainingPeriods.stream()
                        .filter(conversionAndRemainingPeriod ->
                                previousCoverTypeId.equals(conversionAndRemainingPeriod.getPreviousLandCoverId()) &&
                                        currentCoverTypeId.equals(conversionAndRemainingPeriod.getCurrentLandCoverId()))
                        .findAny()
                        .orElse(null);
    }


    public ConversionAndRemainingPeriod getConversionAndRemainingPeriod(
            String previousCoverTypeDescription, String currentCoverTypeDescription) {

        log.trace("Entering getConversionAndRemainingPeriod()");
        log.debug("Previous Cover Type Description = {}", previousCoverTypeDescription);
        log.debug("Current Cover Type Description = {}", currentCoverTypeDescription);

        // Get the previous cover type
        log.trace("Get the record corresponding to the Previous Cover Type Description");
        CoverType previousCoverType = getCoverType(previousCoverTypeDescription);

        // Get the current cover type
        log.trace("Get the record corresponding to the Current Cover Type Description");
        CoverType currentCoverType = getCoverType(currentCoverTypeDescription);

        // Check if the previous and current cover types are both non null
        log.trace("Checking if the previous and current cover types are both non null");
        log.debug("Previous Cover Type = {}", previousCoverType);
        log.debug("Current Cover Type = {}", currentCoverType);

        if (previousCoverType != null && currentCoverType != null) {

            // The previous and current cover types are both non null
            log.trace("The previous and current cover types are both non null");

            // Return
            return getConversionAndRemainingPeriod(previousCoverType.getId(), currentCoverType.getId());

        } else {

            // The previous and current cover types are not both null
            log.trace("The previous and current cover types are not both null");

            return null;
        }

    }

    //</editor-fold>

    //<editor-fold desc="Cover Types">

    public CoverType getCoverType(Long coverTypeId) {

        log.trace("Entering getCoverType()");
        log.debug("Cover Type Id = {}", coverTypeId);

        return
                coverTypes.stream()
                        .filter(coverType ->
                                coverTypeId.equals(coverType.getId()))
                        .findAny()
                        .orElse(null);
    }

    public CoverType getCoverType(String coverTypeDescription) {

        log.trace("Entering getCoverType()");
        log.debug("Cover Type Description = {}", coverTypeDescription);

        return
                coverTypes.stream()
                        .filter(coverType ->
                                coverTypeDescription.equals(coverType.getDescription()))
                        .findAny()
                        .orElse(null);
    }

    //</editor-fold>

    //<editor-fold desc="Land Use Categories">

    public LandUseCategory getLandUseCategory(String landUseCategoryName) {

        log.trace("Entering getLandUseCategory()");
        log.debug("Land Use Category Name = {}", landUseCategoryName);

        return
                landUseCategories.stream()
                        .filter(landUseCategory ->
                                landUseCategoryName.equals(landUseCategory.getName()))
                        .findAny()
                        .orElse(null);
    }

    public LandUseCategory getLandUseCategory(
            String previousCoverTypeDescription,
            String currentCoverTypeDescription,
            LandUseChangeAction landUseChangeAction) {

        log.trace("Entering getLandUseCategory()");
        log.debug("Previous Cover Type Description = {}", previousCoverTypeDescription);
        log.debug("Current Cover Type Description = {}", currentCoverTypeDescription);
        log.debug("Land Use Change Action = {}", landUseChangeAction);

        return getLandUseCategory(
                new StringBuilder()
                        .append(previousCoverTypeDescription)
                        .append(" ")
                        .append(landUseChangeAction.getDescription())
                        .append(" ")
                        .append(currentCoverTypeDescription)
                        .toString());
    }
    //</editor-fold>

    //<editor-fold desc="Reporting Tables">

    public ReportingTable getReportingTable(Long reportingTableId) {

        log.trace("Entering getReportingTable()");
        log.debug("Reporting Table Id = {}", reportingTableId);

        return
                reportingTables.stream()
                        .filter(reportingTable ->
                                reportingTableId.equals(reportingTable.getId()))
                        .findAny()
                        .orElse(null);
    }

    public ReportingTable getReportingTable(String reportingTableNumber) {

        log.trace("Entering getReportingTable()");
        log.debug("Reporting Table Number = {}", reportingTableNumber);

        return
                reportingTables.stream()
                        .filter(reportingTable ->
                                reportingTableNumber.equals(reportingTable.getNumber()))
                        .findAny()
                        .orElse(null);
    }

    //</editor-fold>

    //<editor-fold desc="Vegetation Types">

    public VegetationType getVegetationType(Long databaseId, Long vegetationTypeId) {

        log.trace("Entering getVegetationType()");
        log.debug("Database Id = {}", vegetationTypeId);
        log.debug("Vegetation Type Id = {}", vegetationTypeId);

        if(databasesVegetationTypes.get(databaseId) != null) {

            return
                    databasesVegetationTypes
                            .get(databaseId)
                            .stream()
                            .filter(vegetationType ->
                                    vegetationTypeId.equals(vegetationType.getId()))
                            .findAny()
                            .orElse(null);
        } else {
            return null;
        }

    }

    //</editor-fold>
}


/*
 * Copyright (C) 2020 The Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.configurations;

import global.moja.businessintelligence.daos.LandUseHistoricDetail;
import global.moja.businessintelligence.models.*;
import global.moja.businessintelligence.util.LandUseChangeAction;
import global.moja.businessintelligence.util.endpoints.EndpointsUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.time.Duration;
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
@Slf4j
public class ConfigurationDataProvider {

    private EndpointsUtil endpointsUtil;

    private final List<ConversionAndRemainingPeriod> conversionAndRemainingPeriods;
    private final List<CoverType> coverTypes;
    private final List<Database> databases;
    private final List<LandUseCategory> landUseCategories;
    private final List<ReportingTable> reportingTables;
    private final Map<Long, List<VegetationType>> databasesVegetationTypes = new HashMap<>();


    @Autowired
    public ConfigurationDataProvider(EndpointsUtil endpointsUtil) {

        this.endpointsUtil = endpointsUtil;

        this.conversionAndRemainingPeriods =
                endpointsUtil
                        .retrieveConversionAndRemainingPeriods()
                        .collect(Collectors.toList())
                        .block();

        this.coverTypes =
                endpointsUtil
                        .retrieveCoverTypes()
                        .collect(Collectors.toList())
                        .block();

        this.databases =
                endpointsUtil
                        .retrieveDatabases()
                        .collect(Collectors.toList())
                        .block();

        this.landUseCategories =
                endpointsUtil
                        .retrieveLandUseCategories()
                        .collect(Collectors.toList())
                        .block();

        this.reportingTables =
                endpointsUtil
                        .retrieveReportingTables()
                        .collect(Collectors.toList())
                        .block();

        this.databases.forEach(database -> {
            try{
                databasesVegetationTypes.put(
                        database.getId(),
                        endpointsUtil
                                .retrieveVegetationTypes(database.getId())
                                .collect(Collectors.toList())
                                .block(Duration.ofMillis(10000))
                );
            } catch (Exception e) {
                log.error("{} Database Vegetation Types retrieval failed", database.getLabel());
            }

        });


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

    public CoverType
    getCoverType(Long coverTypeId) {

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
                                coverTypeDescription.equalsIgnoreCase(coverType.getDescription()))
                        .findAny()
                        .orElse(null);
    }

    public CoverType getCoverType(LandUseHistoricDetail landUseHistoricDetail) {

        log.trace("Entering getCoverType()");
        log.debug("Land Use Historic Detail = {}", landUseHistoricDetail);

        try{
            return
                    getCoverType(landUseHistoricDetail.getLandUseCategory().getCoverTypeId());
        }catch (Exception e){
            return null;
        }
    }



    //</editor-fold>

    //<editor-fold desc="Land Use Categories">

    public LandUseCategory getLandUseCategory(String landUseCategoryName) {

        log.trace("Entering getLandUseCategory()");
        log.debug("Land Use Category Name = {}", landUseCategoryName);

        return
                landUseCategories.stream()
                        .filter(landUseCategory ->
                                landUseCategoryName.equalsIgnoreCase(landUseCategory.getName()))
                        .findAny()
                        .orElse(null);
    }

    public LandUseCategory getLandUseCategory(
            Long previousCoverTypeId,
            Long currentCoverTypeId,
            LandUseChangeAction landUseChangeAction) {

        log.trace("Entering getLandUseCategory()");
        log.debug("Previous Cover Type Id = {}", previousCoverTypeId);
        log.debug("Current Cover Type Id = {}", currentCoverTypeId);
        log.debug("Land Use Change Action = {}", landUseChangeAction);

        return getLandUseCategory(
                new StringBuilder()
                        .append(getCoverType(previousCoverTypeId).getDescription())
                        .append(" ")
                        .append(landUseChangeAction.getDescription())
                        .append(" ")
                        .append(getCoverType(currentCoverTypeId).getDescription())
                        .toString());
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

    //</editor-fold>

    //<editor-fold desc="Vegetation Types">

    public VegetationType getVegetationType(Long databaseId, Long vegetationTypeId) {

        log.trace("Entering getVegetationType()");
        log.debug("Database Id = {}", vegetationTypeId);
        log.debug("Vegetation Type Id = {}", vegetationTypeId);

        if (databasesVegetationTypes.get(databaseId) != null) {

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

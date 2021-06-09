
/*
 * Copyright (C) 2020 The Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessor.configurations;

import global.moja.dataprocessor.models.*;
import global.moja.dataprocessor.util.LandUseChangeAction;
import global.moja.dataprocessor.util.endpoints.EndpointsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

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

    private final List<ConversionAndRemainingPeriod> conversionAndRemainingPeriods;
    private final List<CoverType> coverTypes;
    private final List<EmissionType> emissionTypes;
    private final List<LandUseCategory> landUseCategories;
    private final List<LandUseFluxType> landUsesFluxTypes;
    private final List<LandUseFluxTypeToReportingTable> landUsesFluxTypesToReportingTables;
    private final List<FluxToReportingVariable> fluxesToReportingVariables;
    private final Map<Long, List<VegetationType>> databasesVegetationTypes = new HashMap<>();
    private final Map<Long, List<Date>> databasesDates = new HashMap<>();


    @Autowired
    public ConfigurationDataProvider(EndpointsUtil endpointsUtil) {

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

        this.emissionTypes =
                endpointsUtil
                        .retrieveEmissionTypes()
                        .collect(Collectors.toList())
                        .block();

        this.landUseCategories =
                endpointsUtil
                        .retrieveLandUseCategories()
                        .collect(Collectors.toList())
                        .block();


        this.landUsesFluxTypes =
                endpointsUtil
                        .retrieveLandUsesFluxTypes()
                        .collect(Collectors.toList())
                        .block();

        this.landUsesFluxTypesToReportingTables =
                endpointsUtil
                        .retrieveLandUsesFluxTypesToReportingTables()
                        .collect(Collectors.toList())
                        .block();

        this.fluxesToReportingVariables =
                endpointsUtil
                        .retrieveFluxesToReportingVariables()
                        .collect(Collectors.toList())
                        .block();

        List<Database> databases = endpointsUtil
                .retrieveDatabases()
                .collect(Collectors.toList())
                .block();

        databases.forEach(database -> {

            // Vegetation Types
            try {
                databasesVegetationTypes.put(
                        database.getId(),
                        endpointsUtil
                                .retrieveVegetationTypes(database.getId())
                                .collect(Collectors.toList())
                                .block(Duration.ofMillis(5000))
                );
            } catch (Exception e) {
                log.error("{} Database Vegetation Types retrieval failed", database.getLabel());
            }

            // Dates
            try {
                databasesDates.put(
                        database.getId(),
                        endpointsUtil
                                .retrieveDates(database.getId())
                                .collect(Collectors.toList())
                                .block(Duration.ofMillis(5000))
                );
            } catch (Exception e) {
                log.error("{} Database Dates retrieval failed", database.getLabel());
            }

        });

    }


    //<editor-fold desc="Conversion & Remaining Period">

    public ConversionAndRemainingPeriod getConversionAndRemainingPeriod(
            Long previousCoverTypeId, Long currentCoverTypeId) {

        log.trace("Entering getConversionAndRemainingPeriod()");
        log.debug("Previous Cover Type Id = {}", previousCoverTypeId);
        log.debug("Current Cover Type Id = {}", currentCoverTypeId);

        ConversionAndRemainingPeriod c =
                conversionAndRemainingPeriods
                        .stream()
                        .filter(conversionAndRemainingPeriod ->
                                previousCoverTypeId.equals(conversionAndRemainingPeriod.getPreviousLandCoverId()) &&
                                        currentCoverTypeId.equals(conversionAndRemainingPeriod.getCurrentLandCoverId()))
                        .findAny()
                        .orElse(null);

        log.debug("Conversion & Remaining Period = {}", c);

        return c;
    }

    //</editor-fold>

    //<editor-fold desc="Cover Types">

    public CoverType getCoverType(Long coverTypeId) {

        log.trace("Entering getCoverType()");
        log.debug("Cover Type Id = {}", coverTypeId);

        CoverType c =
                coverTypes.stream()
                        .filter(coverType ->
                                coverTypeId.equals(coverType.getId()))
                        .findAny()
                        .orElse(null);

        log.debug("Cover Type = {}", c);

        return c;
    }

    public CoverType getCoverType(String coverTypeDescription) {

        log.trace("Entering getCoverType()");
        log.debug("Cover Type Description = {}", coverTypeDescription);

        CoverType c =
                coverTypes.stream()
                        .filter(coverType ->
                                coverTypeDescription.equalsIgnoreCase(coverType.getDescription()))
                        .findAny()
                        .orElse(null);

        log.debug("Cover Type = {}", c);

        return c;
    }

    //</editor-fold>

    //<editor-fold desc="Emission Types">

    public EmissionType getEmissionType(String emissionTypeAbbreviation) {

        log.trace("Entering getEmissionType()");
        log.debug("Emission Type Description = {}", emissionTypeAbbreviation);

        EmissionType e =
                emissionTypes.stream()
                        .filter(emissionType ->
                                emissionTypeAbbreviation.equalsIgnoreCase(emissionType.getAbbreviation()))
                        .findAny()
                        .orElse(null);

        log.debug("Emission Type = {}", e);

        return e;
    }

    //</editor-fold>

    //<editor-fold desc="Land Use Categories">

    public LandUseCategory getLandUseCategory(String landUseCategoryName) {

        log.trace("Entering getLandUseCategory()");
        log.debug("Land Use Category Name = {}", landUseCategoryName);

        LandUseCategory l =
                landUseCategories.stream()
                        .filter(landUseCategory ->
                                landUseCategoryName.equalsIgnoreCase(landUseCategory.getName()))
                        .findAny()
                        .orElse(null);

        log.debug("Land Use Category  = {}", l);

        return l;
    }

    public LandUseCategory getLandUseCategory(
            Long previousCoverTypeId,
            Long currentCoverTypeId,
            LandUseChangeAction landUseChangeAction) {

        log.trace("Entering getLandUseCategory()");
        log.debug("Previous Cover Type Id = {}", previousCoverTypeId);
        log.debug("Current Cover Type Id = {}", currentCoverTypeId);
        log.debug("Land Use Change Action = {}", landUseChangeAction);

        LandUseCategory l = getLandUseCategory(
                getCoverType(previousCoverTypeId).getDescription() +
                        " " +
                        landUseChangeAction.getDescription() +
                        " " +
                        getCoverType(currentCoverTypeId).getDescription());

        log.debug("Land Use Category  = {}", l);

        return l;
    }

    public Long getParentLandUseCategoryId(Long landUseCategoryId) {

        log.trace("Entering getParentLandUseCategoryId()");
        log.debug("Land Use Category Id = {}", landUseCategoryId);

        Long id =
                landUseCategories
                        .stream()
                        .filter(landUseCategory -> landUseCategoryId.equals(landUseCategory.getId()))
                        .findAny()
                        .filter(l -> l.getParentLandUseCategoryId() != null)
                        .filter(l -> !l.getParentLandUseCategoryId().equals(0L))
                        .map(LandUseCategory::getParentLandUseCategoryId)
                        .orElse(null);

        log.debug("Parent Land Use Category Id = {}", id);

        return id;
    }

    //</editor-fold>

    //<editor-fold desc="Land Use Flux Types">
    public LandUseFluxType getLandUseFluxType(
            Long landUseCategoryId,
            Long fluxTypeId) {

        log.trace("Entering getLandUseFluxType()");
        log.debug("Land Use Category Id = {}", landUseCategoryId);
        log.debug("Flux Type Id = {}", fluxTypeId);


        // The Land Use Flux Type is tied to the topmost Land Use Category Id
        // So if we have Grassland Remaining Grassland, we need to get its
        // Parent Land Use Category: Grassland, to get the Land Use Flux Type
        Long topmostLandUseCategoryId = landUseCategoryId;

        while (getParentLandUseCategoryId(topmostLandUseCategoryId) != null) {
            topmostLandUseCategoryId = getParentLandUseCategoryId(topmostLandUseCategoryId);
        }


        // Mark the last adjusted Land Use Category as the final Land Use Category and use it to retrieve the
        // Land Use Flux Type
        final Long finalLandUseCategoryId = topmostLandUseCategoryId;

        LandUseFluxType l =
                landUsesFluxTypes
                        .stream()
                        .filter(landUseFluxType ->
                                landUseFluxType.getLandUseCategoryId().equals(finalLandUseCategoryId) &&
                                        landUseFluxType.getFluxTypeId().equals(fluxTypeId))
                        .findAny()
                        .orElse(null);

        log.debug("Land Use Flux Type = {}", l);

        return l;
    }

    //</editor-fold>

    //<editor-fold desc="Land Use Flux Type To Reporting Table">
    public List<LandUseFluxTypeToReportingTable> getLandUsesFluxTypesToReportingTables(
            Long landUseCategoryId,
            Long fluxTypeId) {

        log.trace("Entering getLandUseFluxType()");
        log.debug("Land Use Category Id = {}", landUseCategoryId);
        log.debug("Flux Type Id = {}", fluxTypeId);

        List<LandUseFluxTypeToReportingTable> l =
        getLandUsesFluxTypesToReportingTables(getLandUseFluxType(landUseCategoryId, fluxTypeId));

        log.debug("Land Use Flux Types To Reporting Tables = {}", l);

        return l;


    }

    private List<LandUseFluxTypeToReportingTable> getLandUsesFluxTypesToReportingTables(
            LandUseFluxType landUseFluxType) {

        log.debug("Land Use Flux Type = {}", landUseFluxType);

        List<LandUseFluxTypeToReportingTable> l;
        if (landUseFluxType == null || landUseFluxType.getId() == null) {
            l = new ArrayList<>();
        } else {
           l =
                    landUsesFluxTypesToReportingTables
                            .stream()
                            .filter(landUseFluxTypeToReportingTable ->
                                    landUseFluxTypeToReportingTable.getLandUseFluxTypeId().equals(landUseFluxType.getId()))
                            .collect(Collectors.toList());
        }

        log.debug("Land Use Flux Types To Reporting Tables = {}", l);

        return l;
    }
    //</editor-fold>

    //<editor-fold desc="Fluxes To Reporting Variables">
    public List<FluxToReportingVariable> getFluxesToReportingVariables(
            Long startPoolId,
            Long endPoolId) {

        log.trace("Entering getFluxesToReportingVariables()");
        log.debug("Start Pool Id = {}", startPoolId);
        log.debug("End Pool Id = {}", endPoolId);

        List<FluxToReportingVariable> l =
                fluxesToReportingVariables
                        .stream()
                        .filter(fluxToReportingVariable ->
                                fluxToReportingVariable.getStartPoolId().equals(startPoolId) &&
                                        fluxToReportingVariable.getEndPoolId().equals(endPoolId))
                        .collect(Collectors.toList());

        log.debug("Fluxes To Reporting Variables = {}", l);

        return l;
    }

    //</editor-fold>

    //<editor-fold desc="Vegetation Types">

    public VegetationType getVegetationType(Long databaseId, Long vegetationTypeId) {

        log.trace("Entering getVegetationType()");
        log.debug("Database Id = {}", vegetationTypeId);
        log.debug("Vegetation Type Id = {}", vegetationTypeId);

        VegetationType v = null;
        if (databasesVegetationTypes.get(databaseId) != null) {

            v =
                    databasesVegetationTypes
                            .get(databaseId)
                            .stream()
                            .filter(vegetationType ->
                                    vegetationTypeId.equals(vegetationType.getId()))
                            .findAny()
                            .orElse(null);
        }

        log.debug("Vegetation Type = {}", v);

        return v;

    }

    //</editor-fold>

    //<editor-fold desc="Dates">

    public Long getDateDimensionId(Long databaseId, Integer year) {

        log.trace("Entering getId()");
        log.debug("Database Id = {}", databaseId);
        log.debug("Year = {}", year);

        Long id = null;
        if (databasesDates.get(databaseId) != null) {

            // TODO Confirm with Jim
            if (year == 0) {
                id =
                        databasesDates
                                .get(databaseId)
                                .stream()
                                .sorted()
                                .findFirst()
                                .map(Date::getId)
                                .orElse(null);
            } else {
                id =
                        databasesDates
                                .get(databaseId)
                                .stream()
                                .filter(date ->
                                        year.equals(date.getYear()))
                                .findAny()
                                .map(Date::getId)
                                .orElse(null);
            }


        }

        log.debug("Date Dimension Id = {}", id);

        return id;

    }

    //</editor-fold>
}


/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.businessintelligence.services;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.AllocatedFluxReportingResult;
import global.moja.businessintelligence.daos.LocationLandUsesAllocatedFluxReportingResult;
import global.moja.businessintelligence.daos.LocationLandUsesAllocatedFluxReportingResults;
import global.moja.businessintelligence.daos.LocationLandUsesFluxReportingResultsHistories;
import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.models.FluxReportingResult;
import global.moja.businessintelligence.models.FluxToReportingVariable;
import global.moja.businessintelligence.models.LandUseFluxTypeToReportingTable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class LocationLandUsesFluxReportingResultsAllocationService {

    @Value("${carbon.dioxide.emission.type.id}")
    Long CARBON_DIOXIDE_EMISSION_TYPE;

    @Value("${methane.emission.type.id}")
    Long METHANE_EMISSION_TYPE;

    @Value("${nitrous.oxide.emission.type.id}")
    Long NITROUS_OXIDE_EMISSION_TYPE;

    @Value("${area.reporting.variable.id}")
    Long AREA_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.living.biomass.reporting.variable.id}")
    Long NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.dead.organic.matter.reporting.variable.id}")
    Long NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.mineral.soils.reporting.variable.id}")
    Long NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE;

    @Value("${net.carbon.stock.change.in.organic.soils.reporting.variable.id}")
    Long NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE;

    @Value("${net.emissions.removals.reporting.variable.id}")
    Long NET_EMISSIONS_REMOVALS_REPORTING_VARIABLE;

    @Value("${methane.reporting.variable.id}")
    Long METHANE_REPORTING_VARIABLE;

    @Value("${nitrous.oxide.reporting.variable.id}")
    Long NITROUS_OXIDE_REPORTING_VARIABLE;

    Long[] CARBON_STOCK_CHANGE_POOLS_REPORTING_VARIABLES;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    @PostConstruct
    private void init() {
        CARBON_STOCK_CHANGE_POOLS_REPORTING_VARIABLES = new Long[]{
                NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE,
                NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE,
                NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE,
                NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE
        };
    }

    public Mono<LocationLandUsesAllocatedFluxReportingResults> allocateLocationLandUsesFluxReportingResults
            (LocationLandUsesFluxReportingResultsHistories locationLandUsesFluxReportingResultsHistories) {

        log.trace("Entering allocateLocationLandUsesFluxReportingResults()");
        log.debug("Location Land Uses Flux Reporting Results Histories = {}", locationLandUsesFluxReportingResultsHistories);

        // Validate the Location Land Uses Flux Reporting Results Histories
        log.trace("Validating the Location Land Uses Flux Reporting Results Histories");
        if (locationLandUsesFluxReportingResultsHistories == null) {
            log.error("The Location Land Uses Flux Reporting Results Histories should not be null");
            return Mono.error(new ServerException("The Location Land Uses Flux Reporting Results Histories should not be null"));
        }

        // Validate the Location Land Uses Flux Reporting Results Histories' histories
        log.trace("Validating the Location Land Uses Flux Reporting Results Histories' histories");
        if (locationLandUsesFluxReportingResultsHistories.getHistories() == null) {
            log.error("The Location Land Uses Flux Reporting Results Histories' histories should not be null");
            return Mono.error(new ServerException("The Location Land Uses Flux Reporting Results Histories' histories should not be null"));
        }

        return

                // 1. Create a Flux from the Land Uses Flux Reporting Results Histories' histories
                Flux.fromIterable(locationLandUsesFluxReportingResultsHistories.getHistories())

                        // 2. Filter out records with a null Land Use Category
                        .filter(locationLandUsesFluxReportingResultsHistory -> locationLandUsesFluxReportingResultsHistory.getLandUseCategory() != null)

                        // 3. Filter out records with a null or empty flux reporting results
                        .filter(locationLandUsesFluxReportingResultsHistory ->
                                locationLandUsesFluxReportingResultsHistory.getFluxReportingResults() != null &&
                                !locationLandUsesFluxReportingResultsHistory.getFluxReportingResults().isEmpty())

                        // 5. Sort the records by item number
                        .sort()

                        // 6. Allocate the Flux Reporting Results
                        .flatMap(locationLandUsesFluxReportingResultsHistory ->

                            Flux.fromIterable(locationLandUsesFluxReportingResultsHistory.getFluxReportingResults())

                                    // 6.1 Sort the records by item number
                                    .sort()

                                    // 6.2. Allocate the Flux Reporting Results
                                    .map(fluxReportingResult ->
                                            allocateFluxReportingResults(
                                                    locationLandUsesFluxReportingResultsHistory
                                                            .getLandUseCategory()
                                                            .getId(),
                                                    fluxReportingResult))

                                    // 6.3. Collate the allocated Flux Reporting Results
                                    .collectList()

                                    // 6.4. Build the Location Land Uses Allocated Flux Reporting Result record
                                    .map(allocatedFluxReportingResults ->
                                            LocationLandUsesAllocatedFluxReportingResult
                                                    .builder()
                                                    .itemNumber(locationLandUsesFluxReportingResultsHistory.getItemNumber())
                                                    .year(locationLandUsesFluxReportingResultsHistory.getYear())
                                                    .landUseCategory(locationLandUsesFluxReportingResultsHistory.getLandUseCategory())
                                                    .confirmed(locationLandUsesFluxReportingResultsHistory.getConfirmed())
                                                    .allocatedFluxReportingResults(
                                                            allocatedFluxReportingResults
                                                                    .stream()
                                                                    .flatMap(Collection::stream)
                                                                    .collect(Collectors.toList()))
                                            .build())
                        )

                        // 4. Collate the Location Land Uses Allocated Flux Reporting Result records
                        .collect(Collectors.toList())

                        // 5. Build and return the Location Land Uses Allocated Flux Reporting Results records
                        .map(records ->
                                LocationLandUsesAllocatedFluxReportingResults
                                        .builder()
                                        .locationId(locationLandUsesFluxReportingResultsHistories.getLocationId())
                                        .partyId(locationLandUsesFluxReportingResultsHistories.getPartyId())
                                        .tileId(locationLandUsesFluxReportingResultsHistories.getTileId())
                                        .vegetationHistoryId(locationLandUsesFluxReportingResultsHistories.getVegetationHistoryId())
                                        .unitCount(locationLandUsesFluxReportingResultsHistories.getUnitCount())
                                        .unitAreaSum(locationLandUsesFluxReportingResultsHistories.getUnitAreaSum())
                                        .results(records)
                                        .build());
    }


    // TODO Find out how to best add error handling
    private List<AllocatedFluxReportingResult> allocateFluxReportingResults(
            Long landUseCategoryId,
            FluxReportingResult fluxReportingResult) {

        log.trace("Entering allocateFluxReportingResults()");

        log.debug("Land Use Category Id = {}", landUseCategoryId);
        log.debug("Flux Reporting Result = {}", fluxReportingResult);

        List<LandUseFluxTypeToReportingTable> landUsesFluxTypesToReportingTables =
                configurationDataProvider.getLandUsesFluxTypesToReportingTables(
                        landUseCategoryId,
                        fluxReportingResult.getFluxTypeId());

        log.debug("Land Uses Flux Types To Reporting Tables = {}", landUsesFluxTypesToReportingTables);


        List<FluxToReportingVariable> fluxesToReportingVariables =
                configurationDataProvider.getFluxesToReportingVariables(
                        fluxReportingResult.getSourcePoolId(),
                        fluxReportingResult.getSinkPoolId());

        log.debug("Flux To Reporting Variables = {}", fluxesToReportingVariables);

        // Instantiate a container to host the Allocated Flux Reporting Results
        log.trace("Instantiating a container to host the Allocated Flux Reporting Results");
        final List<AllocatedFluxReportingResult> allocatedFluxReportingResults = new ArrayList<>();

        // Collate the Land Use Flux Type Carbon Dioxide Emissions Reporting Tables
        log.trace("Collating the Land Use Flux Type Carbon Dioxide Emissions Reporting Tables");
        List<Long> carbonDioxideAggregationTables =
                landUsesFluxTypesToReportingTables
                        .stream()
                        .filter(l -> l.getEmissionTypeId().equals(CARBON_DIOXIDE_EMISSION_TYPE))
                        .map(LandUseFluxTypeToReportingTable::getReportingTableId)
                        .collect(Collectors.toList());
        log.debug("Carbon Dioxide Aggregation Tables = {}", carbonDioxideAggregationTables);

        // Collate the Land Use Flux Type Methane Emissions Reporting Tables
        log.trace("Collating the Land Use Flux Type Methane Emissions Reporting Tables");
        List<Long> methaneAggregationTables =
                landUsesFluxTypesToReportingTables
                        .stream()
                        .filter(l -> l.getEmissionTypeId().equals(METHANE_EMISSION_TYPE))
                        .map(LandUseFluxTypeToReportingTable::getReportingTableId)
                        .collect(Collectors.toList());
        log.debug("Methane Aggregation Tables = {}", methaneAggregationTables);

        // Collate the Land Use Flux Type Nitrous Oxide Emissions Reporting Tables
        log.trace("Collating the Land Use Flux Type Nitrous Oxide Emissions Reporting Tables");
        List<Long> nitrousOxideAggregationTables =
                landUsesFluxTypesToReportingTables
                        .stream()
                        .filter(l -> l.getEmissionTypeId().equals(NITROUS_OXIDE_EMISSION_TYPE))
                        .map(LandUseFluxTypeToReportingTable::getReportingTableId)
                        .collect(Collectors.toList());
        log.debug("Nitrous Oxide Aggregation Tables = {}", nitrousOxideAggregationTables);

        // Define the Flux Allocation Rule
        log.trace("Defining the Flux Allocation Rule");
        Consumer<FluxToReportingVariable> rule = f -> {

            // Consider only the reporting variables that should not be ignored
            if (!(f.getRule().equalsIgnoreCase("ignore"))) {

                // Create a stream from the actual tables that the allocation should be done against
                Stream<Long> tables =
                        Arrays.stream(CARBON_STOCK_CHANGE_POOLS_REPORTING_VARIABLES)
                                .anyMatch(reportingVariableId -> f.getReportingVariableId().equals(reportingVariableId)) ?
                                carbonDioxideAggregationTables.stream() :
                                f.getReportingVariableId().equals(METHANE_REPORTING_VARIABLE) ?
                                        methaneAggregationTables.stream() :
                                        f.getReportingVariableId().equals(NITROUS_OXIDE_REPORTING_VARIABLE) ?
                                                nitrousOxideAggregationTables.stream() : Stream.empty();

                // Stream and carry out the allocation
                tables
                        .filter(id -> id >= 1L) // Valid table Ids start from 1 (0 is an API placeholder for nulls)
                        .forEach(t ->
                                allocatedFluxReportingResults.add(
                                AllocatedFluxReportingResult
                                        .builder()
                                        .fluxReportingResultId(fluxReportingResult.getId())
                                        .reportingTableId(t)
                                        .reportingVariableId(f.getReportingVariableId())
                                        .flux(fluxReportingResult.getFlux() * ((f.getRule().equalsIgnoreCase("subtract")) ? -1.0 : 1.0))
                                        .build()));

            }

        };


        // Apply the Flux Allocation Rule
        log.trace("Applying the Flux Allocation Rule");
        fluxesToReportingVariables.forEach(rule);

        // Return the allocated fluxes
        return allocatedFluxReportingResults;


    }

}

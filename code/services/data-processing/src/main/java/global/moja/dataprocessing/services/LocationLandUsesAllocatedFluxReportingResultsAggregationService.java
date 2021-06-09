
/*
 * Copyright (C) 2021 Moja Global
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0.
 * If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package global.moja.dataprocessing.services;

import global.moja.dataprocessing.configurations.ConfigurationDataProvider;
import global.moja.dataprocessing.daos.Aggregation;
import global.moja.dataprocessing.daos.LocationLandUsesAllocatedFluxReportingResults;
import global.moja.dataprocessing.daos.LocationLandUsesAllocatedFluxReportingResultsAggregation;
import global.moja.dataprocessing.exceptions.ServerException;
import global.moja.dataprocessing.util.FluxReportingResultsAllocator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Service
@Slf4j
public class LocationLandUsesAllocatedFluxReportingResultsAggregationService {


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

    @Value("${net.carbon.dioxide.emissions.removals.reporting.variable.id}")
    Long NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE;

    @Value("${methane.reporting.variable.id}")
    Long METHANE_REPORTING_VARIABLE;

    @Value("${nitrous.oxide.reporting.variable.id}")
    Long NITROUS_OXIDE_REPORTING_VARIABLE;

    private Set<Long> CARBON_STOCK_CHANGE_POOLS_REPORTING_VARIABLES;

    @Autowired
    ConfigurationDataProvider configurationDataProvider;

    @Autowired
    FluxReportingResultsAllocator fluxReportingResultsAllocator;

    @PostConstruct
    private void init() {
        CARBON_STOCK_CHANGE_POOLS_REPORTING_VARIABLES = new HashSet<>(
                Arrays.asList(
                    NET_CARBON_STOCK_CHANGE_IN_LIVING_BIOMASS_REPORTING_VARIABLE,
                    NET_CARBON_STOCK_CHANGE_IN_DEAD_ORGANIC_MATTER_REPORTING_VARIABLE,
                    NET_CARBON_STOCK_CHANGE_IN_MINERAL_SOILS_REPORTING_VARIABLE,
                    NET_CARBON_STOCK_CHANGE_IN_ORGANIC_SOILS_REPORTING_VARIABLE
                ));
    }

    public Mono<LocationLandUsesAllocatedFluxReportingResultsAggregation> aggregateLocationLandUsesAllocatedFluxReportingResults
            (LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults) {

        log.trace("Entering aggregateLocationLandUsesAllocatedFluxReportingResults()");
        log.debug("Location Land Uses Allocated Flux Reporting Results = {}",
                locationLandUsesAllocatedFluxReportingResults);

        // Validate the Location Land Uses Allocated Flux Reporting Results Results
        log.trace("Validating the Location Land Uses Allocated Flux Reporting Results Results");
        if (locationLandUsesAllocatedFluxReportingResults == null) {
            log.error("The Location Land Uses Allocated Flux Reporting Results should not be null");
            return Mono.error(
                    new ServerException("The Location Land Uses Allocated Flux Reporting Results should not be null"));
        }

        // Validate the Location Land Uses Allocated Flux Reporting Results' results
        log.trace("Validating the Location Land Uses Allocated Flux Reporting Results' results");
        if (locationLandUsesAllocatedFluxReportingResults.getAllocations() == null) {
            log.error("The Location Land Uses Allocated Flux Reporting Results' results should not be null");
            return Mono.error(
                    new ServerException("The Location Land Uses Allocated Flux Reporting Results' results should not be null"));
        }

        // Aggregate the Location Land Uses Allocated Flux Reporting Results' results
        log.trace("Aggregating the Location Land Uses Allocated Flux Reporting Results' results");
        final List<Aggregation> aggregations = new ArrayList<>();

        // Aggregate Area Allocations
        log.trace("Aggregating Area Allocations");
        aggregations.addAll(aggregateAreaAllocations(locationLandUsesAllocatedFluxReportingResults));

        // Aggregate Carbon Stock Interchange Pools Allocations
        log.trace("Aggregating Carbon Stock Interchange Pools Allocations");
        aggregations.addAll(aggregateCarbonInterchangePoolsAllocations(locationLandUsesAllocatedFluxReportingResults));

        // Aggregate Carbon Dioxide Emissions / Removals Equivalents
        log.trace("Aggregating Carbon Dioxide Emissions / Removals Equivalents");
        aggregations.addAll(aggregateNetCarbonDioxideEmissionsRemovals(locationLandUsesAllocatedFluxReportingResults));

        // Aggregate Methane Allocations
        log.trace("Aggregating Methane Allocations");
        aggregations.addAll(aggregateMethaneAllocations(locationLandUsesAllocatedFluxReportingResults));

        // Aggregate Nitrous Oxide Allocations
        log.trace("Aggregating Nitrous Oxide Allocations");
        aggregations.addAll(aggregateNitrousOxideAllocations(locationLandUsesAllocatedFluxReportingResults));

        // Sort the aggregations
        log.trace("Sorting the aggregations");
        Collections.sort(aggregations);

        // Return the aggregated result
        log.trace("Returning the aggregated result");
        return Mono.just(
                LocationLandUsesAllocatedFluxReportingResultsAggregation
                        .builder()
                        .locationId(locationLandUsesAllocatedFluxReportingResults.getLocationId())
                        .partyId(locationLandUsesAllocatedFluxReportingResults.getPartyId())
                        .tileId(locationLandUsesAllocatedFluxReportingResults.getTileId())
                        .vegetationHistoryId(locationLandUsesAllocatedFluxReportingResults.getVegetationHistoryId())
                        .unitCount(locationLandUsesAllocatedFluxReportingResults.getUnitCount())
                        .unitAreaSum(locationLandUsesAllocatedFluxReportingResults.getUnitAreaSum())
                        .aggregations(aggregations)
                        .build());


    }


    private List<Aggregation> aggregateAreaAllocations(
            LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults) {


        final List<Aggregation> aggregations = new ArrayList<>();

        locationLandUsesAllocatedFluxReportingResults

                // Get the Location Land Uses Allocated Flux Reporting Results
                .getAllocations()

                // Create a stream out of the Location Land Uses Allocated Flux Reporting Results
                .stream()

                // Filter out the Location Land Uses Allocated Flux Reporting Results that don't have a Land Use Category
                .filter(locationLandUsesAllocatedFluxReportingResult ->
                        locationLandUsesAllocatedFluxReportingResult.getLandUseCategory() != null)

                // Filter out the Location Land Uses Allocated Flux Reporting Results that don't have Year information
                .filter(locationLandUsesAllocatedFluxReportingResult ->
                        locationLandUsesAllocatedFluxReportingResult.getYear() != null)

                // Sort the remaining records
                .sorted()

                // Loop through the remaining Land Uses Allocated Flux Reporting Results and do the aggregation from
                // the point of view of the nested allocations
                .forEach(locationLandUsesAllocatedFluxReportingResult -> {

                    locationLandUsesAllocatedFluxReportingResult

                            // Get the nested Allocations
                            .getAllocations()

                            // Create a stream from the the nested Allocations
                            .stream()

                            // Sort the stream
                            .sorted()

                            // Loop through each Allocation and do the aggregation
                            .forEach(allocation -> {

                                // Try retrieving the Area Aggregation record
                                Aggregation area =
                                        aggregations
                                                .stream()
                                                .filter(a -> a.getReportingTableId().equals(allocation.getReportingTableId()))
                                                .filter(a -> a.getLandUseCategoryId().equals(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId()))
                                                .filter(a -> a.getReportingVariableId().equals(AREA_REPORTING_VARIABLE))
                                                .filter(a -> a.getYear().equals(locationLandUsesAllocatedFluxReportingResult.getYear()))
                                                .findAny()
                                                .orElse(null);

                                // If the Area Aggregation record does not exist, create it
                                if (area == null) {
                                    aggregations.add(
                                            Aggregation
                                                    .builder()
                                                    .reportingTableId(allocation.getReportingTableId())
                                                    .landUseCategoryId(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId())
                                                    .reportingVariableId(AREA_REPORTING_VARIABLE)
                                                    .year(locationLandUsesAllocatedFluxReportingResult.getYear())
                                                    .amount(new BigDecimal(locationLandUsesAllocatedFluxReportingResults.getUnitAreaSum()))
                                                    .build());
                                }
                            });
                });

        return aggregations;

    }

    private List<Aggregation> aggregateCarbonInterchangePoolsAllocations(
            LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults) {

        return aggregateVariables(
                locationLandUsesAllocatedFluxReportingResults,
                CARBON_STOCK_CHANGE_POOLS_REPORTING_VARIABLES);

    }

    private List<Aggregation> aggregateNetCarbonDioxideEmissionsRemovals(
            LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults) {

        final List<Aggregation> aggregations = new ArrayList<>();

        final BigDecimal conversionFactor =
                new BigDecimal(-44)
                        .divide(new BigDecimal(12), 6, RoundingMode.HALF_UP);

        locationLandUsesAllocatedFluxReportingResults

                // Get the Location Land Uses Allocated Flux Reporting Results
                .getAllocations()

                // Create a stream out of the Location Land Uses Allocated Flux Reporting Results
                .stream()

                // Sort the remaining records
                .sorted()

                // Loop through the remaining Land Uses Allocated Flux Reporting Results and do the aggregation from
                // the point of view of the nested allocations
                .forEach(locationLandUsesAllocatedFluxReportingResult -> {

                    locationLandUsesAllocatedFluxReportingResult

                            // Get the nested Allocations
                            .getAllocations()

                            // Create a stream from the the nested Allocations
                            .stream()

                            // Filter out Allocations that contain variables outside our required scope
                            .filter(a -> CARBON_STOCK_CHANGE_POOLS_REPORTING_VARIABLES.contains(a.getReportingVariableId()))

                            // Sort the remaining Allocations
                            .sorted()

                            // Loop through each Allocation and do the aggregation
                            .forEach(allocation -> {

                                // Get the previous aggregation - if it exists
                                Aggregation previousAggregation =
                                        aggregations
                                                .stream()
                                                .filter(a -> a.getReportingTableId().equals(allocation.getReportingTableId()))
                                                .filter(a -> a.getLandUseCategoryId().equals(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId()))
                                                .filter(a -> a.getReportingVariableId().equals(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE))
                                                .filter(a -> a.getYear().equals(locationLandUsesAllocatedFluxReportingResult.getYear()))
                                                .findAny()
                                                .orElse(null);

                                // Update the previous aggregation if it exists
                                // Else create totally new aggregation record
                                if (previousAggregation != null) {
                                    if (allocation.getFlux() != null) {
                                        previousAggregation.setAmount(previousAggregation.getAmount().add(
                                                new BigDecimal(allocation.getFlux())
                                                        .multiply(conversionFactor)));
                                    }
                                } else {
                                    aggregations.add(
                                            Aggregation
                                                    .builder()
                                                    .reportingTableId(allocation.getReportingTableId())
                                                    .landUseCategoryId(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId())
                                                    .reportingVariableId(NET_CARBON_DIOXIDE_EMISSIONS_REMOVALS_REPORTING_VARIABLE)
                                                    .year(locationLandUsesAllocatedFluxReportingResult.getYear())
                                                    .amount(allocation.getFlux() == null ?
                                                            BigDecimal.ZERO :
                                                            new BigDecimal(allocation.getFlux())
                                                                    .multiply(conversionFactor)
                                                    )
                                                    .build());
                                }


                            });
                });

        return aggregations;

    }

    private List<Aggregation> aggregateMethaneAllocations(
            LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults) {

        return aggregateVariables(
                locationLandUsesAllocatedFluxReportingResults,
                new HashSet<>(Arrays.asList(METHANE_REPORTING_VARIABLE)));

    }

    private List<Aggregation> aggregateNitrousOxideAllocations(
            LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults) {

        return aggregateVariables(
                locationLandUsesAllocatedFluxReportingResults,
                new HashSet<>(Arrays.asList(NITROUS_OXIDE_REPORTING_VARIABLE)));

    }

    private List<Aggregation> aggregateVariables(
            LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults,
            Set<Long> variables) {

        final List<Aggregation> aggregations = new ArrayList<>();

        locationLandUsesAllocatedFluxReportingResults

                // Get the Location Land Uses Allocated Flux Reporting Results
                .getAllocations()

                // Create a stream out of the Location Land Uses Allocated Flux Reporting Results
                .stream()

                // Sort the remaining records
                .sorted()

                // Loop through the remaining Land Uses Allocated Flux Reporting Results and do the aggregation from
                // the point of view of the nested allocations
                .forEach(locationLandUsesAllocatedFluxReportingResult -> {

                    locationLandUsesAllocatedFluxReportingResult

                            // Get the nested Allocations
                            .getAllocations()

                            // Create a stream from the the nested Allocations
                            .stream()

                            // Filter out Allocations that contain variables outside our required scope
                            .filter(a -> variables.contains(a.getReportingVariableId()))

                            // Sort the remaining Allocations
                            .sorted()

                            // Loop through each Allocation and do the aggregation
                            .forEach(allocation -> {

                                // Get the previous aggregation - if it exists
                                Aggregation previousAggregation =
                                        aggregations
                                                .stream()
                                                .filter(a -> a.getReportingTableId().equals(allocation.getReportingTableId()))
                                                .filter(a -> a.getLandUseCategoryId().equals(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId()))
                                                .filter(a -> a.getReportingVariableId().equals(allocation.getReportingVariableId()))
                                                .filter(a -> a.getYear().equals(locationLandUsesAllocatedFluxReportingResult.getYear()))
                                                .findAny()
                                                .orElse(null);

                                // Update the previous aggregation if it exists
                                // Else create totally new aggregation record
                                if (previousAggregation != null) {
                                    if (allocation.getFlux() != null) {
                                        previousAggregation.setAmount(previousAggregation.getAmount().add(new BigDecimal(allocation.getFlux())));
                                    }
                                } else {
                                    aggregations.add(
                                            Aggregation
                                                    .builder()
                                                    .reportingTableId(allocation.getReportingTableId())
                                                    .landUseCategoryId(locationLandUsesAllocatedFluxReportingResult.getLandUseCategory().getId())
                                                    .reportingVariableId(allocation.getReportingVariableId())
                                                    .year(locationLandUsesAllocatedFluxReportingResult.getYear())
                                                    .amount(allocation.getFlux() == null ? BigDecimal.ZERO : new BigDecimal(allocation.getFlux()))
                                                    .build());
                                }


                            });
                });

        return aggregations;

    }




}

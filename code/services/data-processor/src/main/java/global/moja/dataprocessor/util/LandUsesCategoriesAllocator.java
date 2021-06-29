package global.moja.dataprocessor.util;

import global.moja.dataprocessor.configurations.ConfigurationDataProvider;
import global.moja.dataprocessor.daos.LocationCoverTypesHistory;
import global.moja.dataprocessor.daos.LocationLandUsesHistory;
import global.moja.dataprocessor.models.ConversionAndRemainingPeriod;
import global.moja.dataprocessor.models.CoverType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Slf4j
public class LandUsesCategoriesAllocator {

    private final CoverType croplandCoverType;
    private final CoverType grasslandCoverType;
    private final CoverType otherLandCoverType;
    private final ConfigurationDataProvider configurationDataProvider;
    private final String lineSeparator;

    @Autowired
    public LandUsesCategoriesAllocator(ConfigurationDataProvider configurationDataProvider) {
        this.configurationDataProvider = configurationDataProvider;
        this.croplandCoverType = configurationDataProvider.getCoverType("Cropland");
        this.grasslandCoverType = configurationDataProvider.getCoverType("Grassland");
        this.otherLandCoverType = configurationDataProvider.getCoverType("Other land");
        this.lineSeparator = System.getProperty("line.separator");
    }

    // TODO Find out how to best add error handling
    public LocationLandUsesHistory allocateLandUseCategory(
            Long timestep,
            List<LocationCoverTypesHistory> locationCoverTypesHistories,
            List<LocationLandUsesHistory> locationLandUsesHistories) {


        log.trace("Entering allocateLandUseCategory()");
        log.debug("Timestep = {}", timestep);
        log.debug("Cover Types History = {}", locationCoverTypesHistories);
        log.debug("Land Uses History = {}", locationLandUsesHistories);

        // Declare variables
        log.trace("Declaring variables");
        
        LocationCoverTypesHistory currentLocationCoverTypesHistory;
        LocationCoverTypesHistory lastLocationCoverTypesHistory;
        LocationCoverTypesHistory immediateNextLocationCoverTypesHistory;
        LocationCoverTypesHistory differentNextLocationCoverTypesHistory;

        LocationLandUsesHistory immediatePreviousLocationLandUsesHistory;
        LocationLandUsesHistory differentPreviousLocationLandUsesHistory;
        
        ConversionAndRemainingPeriod conversionAndRemainingPeriod;

        // Initialize the current Timestep's Historic Cover Type Detail
        log.trace("Initializing the current Timestep's Historic Cover Type Detail");

        currentLocationCoverTypesHistory =
                locationCoverTypesHistories
                        .stream()
                        .filter(c -> c.getItemNumber().equals(timestep))
                        .findFirst()
                        .orElse(null);

        log.debug("Current Cover Type History = {}", currentLocationCoverTypesHistory);


        // Check if this is the Initial Timestep
        log.trace("{}{}Checking if this is the Initial Timestep{}", lineSeparator, lineSeparator, lineSeparator);

        if (isThisTheInitialTimestep(timestep)) {

            // Check whether the Current Cover Type is Grassland
            log.trace("{}{}Checking whether the Current Cover Type is Grassland{}", lineSeparator, lineSeparator, lineSeparator);

            if (isCoverTypeGrassland(currentLocationCoverTypesHistory)) {

                // Initialize the Conversion & Remaining Period from Cropland to Grassland
                log.trace("Initializing the Conversion & Remaining Period from Cropland to Grassland");

                conversionAndRemainingPeriod =
                        configurationDataProvider
                                .getConversionAndRemainingPeriod(croplandCoverType.getId(), grasslandCoverType.getId());

                log.trace("Conversion & Remaining Period = {}", conversionAndRemainingPeriod);


                // Check whether the Cover Type becomes Cropland within the Cropland to Grassland Conversion Period 
                // without becoming any other Cover Type
                log.trace("{}{}Checking whether the Cover Type becomes Cropland within the Cropland to Grassland " +
                                "Conversion Period without becoming any other Cover Type{}",
                        lineSeparator, lineSeparator, lineSeparator);

                if (doesTheCoverTypeBecomeCroplandWithinTheCroplandToGrasslandConversionPeriodWithoutBecomingAnyOtherCoverType(
                        locationCoverTypesHistories, timestep)) {

                    // Classify the Land Use as Cropland Remaining Cropland
                    log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                    return
                            LocationLandUsesHistory.builder()
                                    .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                    .year(currentLocationCoverTypesHistory.getYear())
                                    .landUseCategory(
                                            configurationDataProvider
                                                    .getLandUseCategory(
                                                            croplandCoverType.getId(),
                                                            croplandCoverType.getId(),
                                                            LandUseChangeAction.REMAINING))
                                    .confirmed(true)
                                    .build();

                } else {

                    // Classify the Land Use as Land Remaining Land of the initial Cover Type
                    log.trace("Classifying the Land Use as Land Remaining Land of the initial Cover Type");

                    return
                            LocationLandUsesHistory.builder()
                                    .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                    .year(currentLocationCoverTypesHistory.getYear())
                                    .landUseCategory(
                                            configurationDataProvider
                                                    .getLandUseCategory(
                                                            currentLocationCoverTypesHistory.getCoverType().getId(),
                                                            currentLocationCoverTypesHistory.getCoverType().getId(),
                                                            LandUseChangeAction.REMAINING))
                                    .confirmed(true)
                                    .build();
                }


            } else {

                // Classify the Land Use as Land Remaining Land of the initial Cover Type
                log.trace("Classifying the Land Use as Land Remaining Land of the initial Cover Type");

                return
                        LocationLandUsesHistory.builder()
                                .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                .year(currentLocationCoverTypesHistory.getYear())
                                .landUseCategory(
                                        configurationDataProvider
                                                .getLandUseCategory(
                                                        currentLocationCoverTypesHistory.getCoverType().getId(),
                                                        currentLocationCoverTypesHistory.getCoverType().getId(),
                                                        LandUseChangeAction.REMAINING))
                                .confirmed(true)
                                .build();
            }


        } else {

            // Initialize the previous Land Uses History.
            log.trace("Initializing the previous Land Use History");

            immediatePreviousLocationLandUsesHistory =
                    getImmediatePreviousLandUseHistoricDetail(currentLocationCoverTypesHistory, locationLandUsesHistories);
            differentPreviousLocationLandUsesHistory =
                    getDifferentPreviousLandUseHistoricDetail(currentLocationCoverTypesHistory, locationLandUsesHistories);

            log.debug("Immediate Previous Land Uses History = {}", immediatePreviousLocationLandUsesHistory);
            log.debug("Different Previous Land Uses History = {}", differentPreviousLocationLandUsesHistory);


            // Initialize the next Cover Type History.
            log.trace("Initializing the next Cover Type History");

            immediateNextLocationCoverTypesHistory =
                    getImmediateNextCoverTypeHistoricDetail(currentLocationCoverTypesHistory, locationCoverTypesHistories);
            differentNextLocationCoverTypesHistory =
                    getDifferentNextCoverTypeHistoricDetail(currentLocationCoverTypesHistory, locationCoverTypesHistories);

            log.debug("Immediate Next Cover Type History = {}", immediateNextLocationCoverTypesHistory);
            log.debug("Different Next Cover Type History = {}", differentNextLocationCoverTypesHistory);


            // Get the last Cover Type History.
            // By this, we mean get a History whose time step is the last timestep and
            log.trace("Initializing the last Cover Type History");
            lastLocationCoverTypesHistory =
                    getLastCoverTypeHistoricDetail(locationCoverTypesHistories);

            log.debug("Last Cover Type History = {}", lastLocationCoverTypesHistory);


            // Initialize the Conversion & Remaining Period from the Previous Land Use to the current Cover Type
            log.trace("Initializing the Conversion & Remaining Period from the Previous Land Use");
            conversionAndRemainingPeriod = differentPreviousLocationLandUsesHistory == null ? null :
                    getConversionAndRemainingPeriod(differentPreviousLocationLandUsesHistory, currentLocationCoverTypesHistory);

            log.trace("The Conversion & Remaining Period = {}", conversionAndRemainingPeriod);


            // Check if the Cover Type changed since the Initial Timestep
            log.trace("{}{}Checking if the Cover Type changed since the Initial Timestep{}",
                    lineSeparator, lineSeparator, lineSeparator);

            if (hasTheCoverTypeChangedSinceTheInitialTimestep(locationLandUsesHistories, currentLocationCoverTypesHistory)) {

                // Check if the Cover Type remained the same for longer than the Land Conversion Period of the
                // Previous Land Use
                log.trace("{}{}Checking if the Cover Type remained the same for longer than the Land " +
                        "Conversion Period of the previous Land Use{}", lineSeparator, lineSeparator, lineSeparator);

                if(conversionAndRemainingPeriod == null) {
                    log.info("");
                    log.warn("CONVERSION / REMAINING PERIOD NOT FOUND");
                    log.info("Previous Land Use History = {}", differentPreviousLocationLandUsesHistory);
                    log.info("Current Cover Type History = {}", currentLocationCoverTypesHistory);
                    log.info("");

                    // Todo Confirm with rdl
                    return
                            LocationLandUsesHistory.builder()
                                    .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                    .year(currentLocationCoverTypesHistory.getYear())
                                    .landUseCategory(null)
                                    .confirmed(true)
                                    .build();
                }


                if (hasTheCoverTypeRemainedTheSameForLongerThanTheLandConversionPeriodOfThePreviousLandUse(
                        differentPreviousLocationLandUsesHistory,
                        differentNextLocationCoverTypesHistory,
                        lastLocationCoverTypesHistory,
                        conversionAndRemainingPeriod)) {

                    // Check if the Cover Type remained the same for longer than the Land Remaining Period
                    log.trace("{}{}Checking if the Cover Type remained the same for longer than the Land " +
                            "Remaining Period{}", lineSeparator, lineSeparator, lineSeparator);

                    if (hasTheCoverTypeRemainedTheSameForLongerThanTheLandRemainingPeriod(
                            currentLocationCoverTypesHistory,
                            differentPreviousLocationLandUsesHistory,
                            conversionAndRemainingPeriod)) {

                        // Classify the Land Use as Land Remaining Land of the Current Cover Type
                        log.trace("Classifying the Land Use as Land Remaining Land of the Current Cover Type");

                        return
                                LocationLandUsesHistory.builder()
                                        .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                        .year(currentLocationCoverTypesHistory.getYear())
                                        .landUseCategory(
                                                configurationDataProvider
                                                        .getLandUseCategory(
                                                                currentLocationCoverTypesHistory.getCoverType().getId(),
                                                                currentLocationCoverTypesHistory.getCoverType().getId(),
                                                                LandUseChangeAction.REMAINING))
                                        .confirmed(true)
                                        .build();

                    } else {


                        // Classify the Land Use as Land Converted to Current Cover
                        log.trace("Classifying the Land Use as Land Converted to Current Cover");

                        return
                                LocationLandUsesHistory.builder()
                                        .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                        .year(currentLocationCoverTypesHistory.getYear())
                                        .landUseCategory(
                                                configurationDataProvider
                                                        .getLandUseCategory(
                                                                differentPreviousLocationLandUsesHistory
                                                                        .getLandUseCategory()
                                                                        .getCoverTypeId(),
                                                                currentLocationCoverTypesHistory
                                                                        .getCoverType()
                                                                        .getId(),
                                                                LandUseChangeAction.CONVERSION))
                                        .confirmed(true)
                                        .build();
                    }

                } else {

                    // Check whether the Cover Types for the Current and Next Time Step together with the Land Use
                    // Class of the Previous Time Step include both Cropland and Grassland and exclude all other
                    // Cover Types
                    log.trace("{}{}Checking whether the Cover Types for the Current and Next Time Step together with " +
                            "the Land Use Class of the Previous Time Step include both Cropland and Grassland " +
                            "and exclude all other Cover Types {}", lineSeparator, lineSeparator, lineSeparator);

                    if (doTheCoverTypesOfTheCurrentAndNextTimeStepAndTheLandUseClassOfThePreviousTimeStepIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(
                            currentLocationCoverTypesHistory,
                            immediatePreviousLocationLandUsesHistory,
                            immediateNextLocationCoverTypesHistory)) {

                        // Calculate how long the Cover Types for the Current and Next Time Steps as well as the
                        // Land Use Classes of the Previous Time Steps have included Cropland and Grassland
                        // Cover Types and excluded all other Cover Types
                        log.trace("{}{} Calculating how long the Cover Types for the Current and Next Time Steps as " +
                                        "well as the Land Use Classes of the Previous Time Steps have included Cropland and " +
                                        "Grassland Cover Types and excluded all other Cover Types{}",
                                lineSeparator, lineSeparator, lineSeparator);

                        long years = howLongHaveTheCurrentAndFutureCoverTypesAndPreviousLandUseClassesBeenCroplandAndGrasslandExcludingAnythingElse(
                                currentLocationCoverTypesHistory,
                                lastLocationCoverTypesHistory,
                                locationCoverTypesHistories,
                                locationLandUsesHistories);


                        // Check whether the Cover Types for the Current and Next Time Steps as well as the
                        // Land Use Classes of the Previous Time Steps have NOT included Cropland and Grassland
                        // Cover Types and excluded all other Cover Types for longer than the land Conversion Period
                        log.trace("{}{} Checking whether the Cover Types for the Current and Next Time Steps as " +
                                        "well as the Land Use Classes of the Previous Time Steps have NOT included " +
                                        "Cropland and Grassland Cover Types and excluded all other Cover Types " +
                                        "for longer than the land Conversion Period{}",
                                lineSeparator, lineSeparator, lineSeparator);


                        if (!hasTheCoverTypeBeenCroplandOrGrasslandForAPeriodLongerThanTheLandConversionPeriod(
                                conversionAndRemainingPeriod,
                                years)) {

                            // Check if the length of time to the end of the simulation is less than the Land Conversion
                            // Period for the Previous Land Use
                            log.trace("{}{}Checking if the length of time to the end of the simulation is less than the " +
                                    "Land Conversion Period for the Previous Land Use{}", lineSeparator, lineSeparator, lineSeparator);

                            if (isLengthOfTimeToTheEndOfTheSimulationLessThanTheLandConversionPeriodOfThePreviousLandUse(
                                    currentLocationCoverTypesHistory,
                                    lastLocationCoverTypesHistory,
                                    conversionAndRemainingPeriod)) {

                                // Check whether the Previous Land Use is Cropland
                                log.trace("{}{} Checking whether the Previous Land Use is Cropland {}",
                                        lineSeparator, lineSeparator, lineSeparator);

                                if (isCoverTypeCropland(differentPreviousLocationLandUsesHistory)) {

                                    // Classify the Land Use as the Previous Land Use
                                    // TODO Confirm with rdl
                                    log.trace("Classifying the Land Use as the Previous Land Use");
                                    return
                                            LocationLandUsesHistory.builder()
                                                    .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                                    .year(currentLocationCoverTypesHistory.getYear())
                                                    .landUseCategory(differentPreviousLocationLandUsesHistory.getLandUseCategory())
                                                    .confirmed(differentPreviousLocationLandUsesHistory.getConfirmed())
                                                    .build();

                                } else {

                                    // Classify the Land Use as Land (of the Previous Land Use) Converted to Cropland
                                    log.trace("Classifying the Land Use as Land (of the Previous Land Use) Converted to Cropland");

                                    return
                                            LocationLandUsesHistory.builder()
                                                    .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                                    .year(currentLocationCoverTypesHistory.getYear())
                                                    .landUseCategory(
                                                            configurationDataProvider
                                                                    .getLandUseCategory(
                                                                            differentPreviousLocationLandUsesHistory
                                                                                    .getLandUseCategory()
                                                                                    .getCoverTypeId(),
                                                                            croplandCoverType.getId(),
                                                                            LandUseChangeAction.CONVERSION))
                                                    .confirmed(false)
                                                    .build();
                                }
                            }
                        }


                        // Check whether the Cover Types for the Current and Next Time Steps as well as the
                        // Land Use Classes of the Previous Time Steps have included Cropland and Grassland
                        // Cover Types and excluded all other Cover Types since the Initial Time Step or
                        // for longer than the land Conversion Period
                        log.trace("{}{} Checking whether the Cover Types for the Current and Next Time Steps as " +
                                "well as the Land Use Classes of the Previous Time Steps have included " +
                                "Cropland and Grassland Cover Types and excluded all other Cover Types " +
                                "since the Initial Timestep or for longer than the land Remaining Period" +
                                "{}", lineSeparator, lineSeparator, lineSeparator);


                        if (hasTheCoverTypeBeenCroplandOrGrasslandSinceTheInitialTimestep(
                                currentLocationCoverTypesHistory, locationLandUsesHistories) ||
                                hasTheCoverTypeBeenCroplandOrGrasslandForAPeriodLongerThanTheLandRemainingPeriod(
                                        conversionAndRemainingPeriod, years)) {

                            // Classify the Land Use as Cropland Remaining Cropland
                            log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                            return
                                    LocationLandUsesHistory.builder()
                                            .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                            .year(currentLocationCoverTypesHistory.getYear())
                                            .landUseCategory(
                                                    configurationDataProvider
                                                            .getLandUseCategory(
                                                                    croplandCoverType.getId(),
                                                                    croplandCoverType.getId(),
                                                                    LandUseChangeAction.REMAINING))
                                            .confirmed(true)
                                            .build();

                        } else {

                            // Check whether the Previous Land Use is Cropland
                            log.trace("{}{} Checking whether the Previous Land Use is Cropland {}",
                                    lineSeparator, lineSeparator, lineSeparator);

                            if (isCoverTypeCropland(differentPreviousLocationLandUsesHistory)) {

                                // Classify the Land Use as the Previous Land Use
                                // TODO Confirm with rdl
                                log.trace("Classifying the Land Use as the Previous Land Use");
                                return
                                        LocationLandUsesHistory.builder()
                                                .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                                .year(currentLocationCoverTypesHistory.getYear())
                                                .landUseCategory(differentPreviousLocationLandUsesHistory.getLandUseCategory())
                                                .confirmed(differentPreviousLocationLandUsesHistory.getConfirmed())
                                                .build();

                            } else {

                                // Classify the Land Use as Land (of the Previous Land Use) Converted to Cropland
                                log.trace("Classifying the Land Use as Land (of the Previous Land Use) Converted to Cropland");

                                return
                                        LocationLandUsesHistory.builder()
                                                .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                                .year(currentLocationCoverTypesHistory.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        differentPreviousLocationLandUsesHistory
                                                                                .getLandUseCategory()
                                                                                .getCoverTypeId(),
                                                                        croplandCoverType.getId(),
                                                                        LandUseChangeAction.CONVERSION))
                                                .confirmed(true)
                                                .build();
                            }


                        }

                    } else {

                        // Check if the length of time to the end of the simulation is less than the Land Conversion
                        // Period for the Previous Land Use
                        log.trace("{}{}Checking if the length of time to the end of the simulation is less than the " +
                                "Land Conversion Period for the Previous Land Use{}", lineSeparator, lineSeparator, lineSeparator);

                        if (isLengthOfTimeToTheEndOfTheSimulationLessThanTheLandConversionPeriodOfThePreviousLandUse(
                                currentLocationCoverTypesHistory,
                                lastLocationCoverTypesHistory,
                                conversionAndRemainingPeriod)) {

                            // Classify as unconfirmed Land Converted to Current Cover
                            log.trace("Classifying as unconfirmed Land Converted to Current Cover");

                            return
                                    LocationLandUsesHistory.builder()
                                            .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                            .year(currentLocationCoverTypesHistory.getYear())
                                            .landUseCategory(
                                                    configurationDataProvider
                                                            .getLandUseCategory(
                                                                    differentPreviousLocationLandUsesHistory
                                                                            .getLandUseCategory().getCoverTypeId(),
                                                                    currentLocationCoverTypesHistory.getCoverType().getId(),
                                                                    LandUseChangeAction.CONVERSION))
                                            .confirmed(false)
                                            .build();

                        } else {

                            // Check if the Cover Type changed back to a Previous Land Use before the end of its
                            // Land Conversion Period
                            log.trace("{}{}Checking if the Cover Type changed back to a Previous Land Use before the end of " +
                                    "its Land Conversion Period{}", lineSeparator, lineSeparator, lineSeparator);

                            if (doesTheCoverTypeChangeBackToAPreviousLandUseBeforeTheEndOfItsLandConversionPeriod(
                                    currentLocationCoverTypesHistory,
                                    locationCoverTypesHistories,
                                    locationLandUsesHistories,
                                    conversionAndRemainingPeriod)) {

                                // Classify as Land Remaining Land of Previous Land Use
                                log.trace("Classifying as Land Remaining Land of Previous Land Use");

                                return
                                        LocationLandUsesHistory.builder()
                                                .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                                .year(currentLocationCoverTypesHistory.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        differentPreviousLocationLandUsesHistory
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        differentPreviousLocationLandUsesHistory
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        LandUseChangeAction.REMAINING))
                                                .confirmed(true)
                                                .build();
                            } else {


                                // Classify as Previous Land Converted to Current Cover
                                log.trace("Classifying as Previous Land Converted to Current Cover");

                                return
                                        LocationLandUsesHistory.builder()
                                                .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                                .year(currentLocationCoverTypesHistory.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        differentPreviousLocationLandUsesHistory
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        currentLocationCoverTypesHistory
                                                                                .getCoverType().getId(),
                                                                        LandUseChangeAction.CONVERSION))
                                                .confirmed(true)
                                                .build();
                            }

                        }
                    }

                }

            } else {

                // Classify the Land Use as Land Remaining Land of the initial Cover Type
                log.trace("Classifying the Land Use as Land Remaining Land of the initial Cover Type");

                return
                        LocationLandUsesHistory.builder()
                                .itemNumber(currentLocationCoverTypesHistory.getItemNumber())
                                .year(currentLocationCoverTypesHistory.getYear())
                                .landUseCategory(
                                        configurationDataProvider
                                                .getLandUseCategory(
                                                        currentLocationCoverTypesHistory.getCoverType().getId(),
                                                        currentLocationCoverTypesHistory.getCoverType().getId(),
                                                        LandUseChangeAction.REMAINING))
                                .confirmed(true)
                                .build();


            }

        }


    }


    private ConversionAndRemainingPeriod getConversionAndRemainingPeriod(
            LocationLandUsesHistory previousLocationLandUsesHistory,
            LocationCoverTypesHistory currentLocationCoverTypesHistory) {

        return configurationDataProvider.getConversionAndRemainingPeriod(
                previousLocationLandUsesHistory.getLandUseCategory().getCoverTypeId(),
                currentLocationCoverTypesHistory.getCoverType().getId());
    }


    /**
     * Retrieves the last Cover Type History.
     */
    private LocationCoverTypesHistory getLastCoverTypeHistoricDetail(
            List<LocationCoverTypesHistory> locationCoverTypesHistories) {

        return locationCoverTypesHistories
                .stream()
                .sorted()
                .reduce((first, second) -> second)
                .orElse(null);
    }


    /**
     * Retrieves the immediate-previous Land Uses History.
     */
    private LocationLandUsesHistory getImmediatePreviousLandUseHistoricDetail(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationLandUsesHistory> locationLandUsesHistories) {
        return getPreviousLandUseHistoricDetail(currentLocationCoverTypesHistory, locationLandUsesHistories, true);
    }


    /**
     * Retrieves the different-previous Land Uses History.
     */
    private LocationLandUsesHistory getDifferentPreviousLandUseHistoricDetail(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationLandUsesHistory> locationLandUsesHistories) {
        return getPreviousLandUseHistoricDetail(currentLocationCoverTypesHistory, locationLandUsesHistories, false);
    }


    /**
     * Retrieves the immediate-previous, or different-previous Land Uses History.
     * <p>
     * Retrieves the immediate-previous Land Uses History by looking back one step
     * from the current timestep.
     * <p>
     * Retrieves the different-previous Land Uses History by step-wisely looking
     * back from the current timestep and establishing the first point at which the
     * Cover Type of the current timestep was different from the Cover Type of a
     * previous Land Use.
     * <p>
     * It then returns that Land Use history
     */
    private LocationLandUsesHistory getPreviousLandUseHistoricDetail(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationLandUsesHistory> locationLandUsesHistories,
            boolean immediate) {

        if (immediate) {
            return
                    locationLandUsesHistories
                            .stream()
                            .filter(c ->
                                    c.getItemNumber() == (currentLocationCoverTypesHistory.getItemNumber() - 1))
                            .reduce((first, second) -> second)
                            .orElse(null);
        } else {
            return
                    locationLandUsesHistories
                            .stream()
                            .filter(l -> l.getLandUseCategory() != null)
                            .filter(c ->
                                    c.getItemNumber() < currentLocationCoverTypesHistory.getItemNumber() &&
                                            !c.getLandUseCategory().getCoverTypeId()
                                                    .equals(currentLocationCoverTypesHistory.getCoverType().getId()))
                            .reduce((first, second) -> second)
                            .orElse(null);
        }


    }


    /**
     * Retrieves the immediate-next Cover Type History.
     *
     */
    private LocationCoverTypesHistory getImmediateNextCoverTypeHistoricDetail(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationCoverTypesHistory> locationCoverTypesHistories) {
        return getNextCoverTypeHistoricDetail(currentLocationCoverTypesHistory, locationCoverTypesHistories, true);
    }


    /**
     * Retrieves the different-next Cover Type History.
     *
     */
    private LocationCoverTypesHistory getDifferentNextCoverTypeHistoricDetail(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationCoverTypesHistory> locationCoverTypesHistories) {
        return getNextCoverTypeHistoricDetail(currentLocationCoverTypesHistory, locationCoverTypesHistories, false);
    }


    /**
     * Retrieves the immediate-next, or different-next Cover Type History.
     * <p>
     * Retrieves the immediate-next Cover Type History by looking forward one step
     * from the current timestep.
     * <p>
     * Retrieves the different-next Cover Type History by step-wisely looking
     * forward from the current timestep and establishing the first point at which the
     * current Cover Type was different from a succeeding Cover Type
     * <p>
     * It then returns that Cover Type history
     */
    private LocationCoverTypesHistory getNextCoverTypeHistoricDetail(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationCoverTypesHistory> locationCoverTypesHistories,
            boolean immediate) {

        if (immediate) {
            return
                    locationCoverTypesHistories
                            .stream()
                            .filter(c -> c.getItemNumber() == (currentLocationCoverTypesHistory.getItemNumber() + 1))
                            .reduce((first, second) -> first)
                            .orElse(null);
        } else {
            return
                    locationCoverTypesHistories
                            .stream()
                            .filter(c -> c.getCoverType() != null)
                            .filter(c ->
                                    c.getItemNumber() > currentLocationCoverTypesHistory.getItemNumber() &&
                                            !c.getCoverType().getId().equals(
                                                    currentLocationCoverTypesHistory.getCoverType().getId()))
                            .reduce((first, second) -> first)
                            .orElse(null);
        }


    }


    /**
     * Retrieves the number of years between the Previous Land Use and the Next Cover Type.
     * <p>
     * Substitutes the Previous Land Use Timestep with the Initial Timestep when null and
     * the Next Cover Type Timestep with the Final Timestep when Null
     */
    private long getYearsInBetween(
            LocationLandUsesHistory previousLocationLandUsesHistory,
            LocationCoverTypesHistory nextLocationCoverTypesHistory,
            LocationCoverTypesHistory finalLocationCoverTypesHistory) {

        long years;

        if (previousLocationLandUsesHistory != null &&
                nextLocationCoverTypesHistory != null) {

            years =
                    LongStream.range(
                            previousLocationLandUsesHistory.getItemNumber() + 1,
                            nextLocationCoverTypesHistory.getItemNumber()).count();

        } else if (previousLocationLandUsesHistory != null) {

            years =
                    LongStream.rangeClosed(
                            previousLocationLandUsesHistory.getItemNumber() + 1,
                            finalLocationCoverTypesHistory.getItemNumber()).count();

        } else if (nextLocationCoverTypesHistory != null) {

            years =
                    LongStream.range(
                            0L,
                            nextLocationCoverTypesHistory.getItemNumber()).count();

        } else {

            years =
                    LongStream.rangeClosed(
                            0L,
                            finalLocationCoverTypesHistory.getItemNumber()).count();
        }

        return years;
    }

    /**
     * Retrieves the number of years between the Current Cover Type
     * and the Final Cover Type
     */
    private long getYearsInBetween(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            LocationCoverTypesHistory finalLocationCoverTypesHistory) {

        long years = 0L;

        if (currentLocationCoverTypesHistory != null &&
                finalLocationCoverTypesHistory != null) {

            if (!currentLocationCoverTypesHistory.getItemNumber()
                    .equals(finalLocationCoverTypesHistory.getItemNumber())) {

                years =
                        LongStream.rangeClosed(
                                currentLocationCoverTypesHistory.getItemNumber() + 1,
                                finalLocationCoverTypesHistory.getItemNumber()).count();

            }

        }

        return years;
    }


    /**
     * Retrieves the number of years between the Previous Land Use and the Current Cover Type
     */
    private long getYearsInBetween(
            LocationLandUsesHistory previousLocationLandUsesHistory,
            LocationCoverTypesHistory currentLocationCoverTypesHistory) {

        long years = 0L;

        if (previousLocationLandUsesHistory != null &&
                currentLocationCoverTypesHistory != null) {

            years =
                    LongStream.rangeClosed(
                            previousLocationLandUsesHistory.getItemNumber() + 1,
                            currentLocationCoverTypesHistory.getItemNumber()).count();

        }

        return years;
    }


    /**
     * Retrieves the Cover Type id of the passed in timestep from the land use details if it exists,
     * or, from the Cover type details otherwise
     */
    private Long getCoverTypeId(
            List<LocationCoverTypesHistory> locationCoverTypesHistories,
            List<LocationLandUsesHistory> locationLandUsesHistories,
            Long timestep) {

        Long coverTypeId = null;

        LocationLandUsesHistory previousLocationLandUsesHistory =
                locationLandUsesHistories
                        .stream()
                        .sorted()
                        .filter(l -> l.getItemNumber().equals(timestep))
                        .findFirst()
                        .orElse(null);

        if (previousLocationLandUsesHistory != null) {

            coverTypeId = previousLocationLandUsesHistory.getLandUseCategory().getCoverTypeId();

        } else {

            LocationCoverTypesHistory previousLocationCoverTypesHistory =
                    locationCoverTypesHistories
                            .stream()
                            .sorted()
                            .filter(l -> l.getItemNumber().equals(timestep))
                            .findFirst()
                            .orElse(null);

            if (previousLocationCoverTypesHistory != null) {
                coverTypeId = previousLocationCoverTypesHistory.getCoverType().getId();
            }
        }

        return coverTypeId;
    }


    /**
     * Checks if the passed in Timestep is the Initial Timestep
     */
    private boolean isThisTheInitialTimestep(Long timestep) {

        boolean result = timestep.equals(0L);
        log.trace(result ? "This is the Initial Timestep" : "This is not the Initial Timestep");
        return result;
    }


    /**
     * Checks if the passed in Cover Type in the Cover Type History is Grassland
     */
    private boolean isCoverTypeGrassland(
            LocationCoverTypesHistory locationCoverTypesHistory) {

        boolean result = locationCoverTypesHistory.getCoverType().getId().equals(grasslandCoverType.getId());
        log.trace(result ? "Cover Type is Grassland" : "Cover Type is not Grassland");
        return result;

    }


    /**
     * Checks if the Cover Type in the Land Use History is Cropland
     */
    private boolean isCoverTypeCropland(
            LocationLandUsesHistory locationLandUsesHistory) {

        boolean result = locationLandUsesHistory.getLandUseCategory().getCoverTypeId().equals(croplandCoverType.getId());
        log.trace(result ? "Cover Type is Cropland" : "Cover Type is not Cropland");
        return result;

    }


    /**
     * Checks if the Cover Type becomes Cropland within the Cropland to grassland conversion period without becoming any
     * other Cover Type after the current Timestep
     */
    private boolean doesTheCoverTypeBecomeCroplandWithinTheCroplandToGrasslandConversionPeriodWithoutBecomingAnyOtherCoverType(
            List<LocationCoverTypesHistory> coverTypeHistories,
            Long currentTimestep) {

        boolean result = false;


        // Check if the Cover Type becomes Cropland in the future
        log.trace("Checking if the Cover Type becomes Cropland in the future");
        LocationCoverTypesHistory croplandLocationCoverTypesHistory =
                coverTypeHistories
                        .stream()
                        .sorted()
                        .filter(c -> c.getItemNumber() > currentTimestep &&
                                c.getCoverType().getId().equals(croplandCoverType.getId()))
                        .findFirst()
                        .orElse(null);

        log.trace(croplandLocationCoverTypesHistory != null ?
                "The Cover Type becomes Cropland in the future" :
                "The Cover Type does not become Cropland in the future");

        if (croplandLocationCoverTypesHistory != null) {


            // Check if a non Grassland, non Cropland Cover Type preceded the Cover Type becoming Cropland
            log.trace("Checking if a non Grassland, non Cropland Cover Type preceded the Cover Type becoming Cropland");

            LocationCoverTypesHistory nonGrasslandNonCroplandLocationCoverTypesHistory =
                    coverTypeHistories
                            .stream()
                            .sorted()
                            .filter(c -> c.getItemNumber() > currentTimestep &&
                                    c.getItemNumber() < croplandLocationCoverTypesHistory.getItemNumber() &&
                                    !c.getCoverType().getId().equals(croplandCoverType.getId()) &&
                                    !c.getCoverType().getId().equals(grasslandCoverType.getId()))
                            .findFirst()
                            .orElse(null);

            log.trace(nonGrasslandNonCroplandLocationCoverTypesHistory != null ?
                    "A non Grassland, non Cropland Cover Type preceded the Cover Type becoming Cropland" :
                    "A non Grassland, non Cropland Cover Type did not precede the Cover Type becoming Cropland");


            if (nonGrasslandNonCroplandLocationCoverTypesHistory == null) {

                // Get the total number of years that elapsed before the Cover Type turned into Cropland
                log.trace("Getting the total number of years that elapsed before the Cover Type turned into Cropland");

                long years =
                        LongStream.range(
                                currentTimestep,
                                croplandLocationCoverTypesHistory.getItemNumber()).count();

                log.debug("The Cover Type turned Cropland after {} years", years);

                // Get the Cropland to Grassland Conversion / Remaining Period
                log.trace("Getting the Cropland to Grassland Conversion / Remaining Period");
                ConversionAndRemainingPeriod croplandToGrasslandConversionAndRemainingPeriod = configurationDataProvider
                        .getConversionAndRemainingPeriod(croplandCoverType.getId(), grasslandCoverType.getId());

                log.debug("Grassland Conversion / Remaining Period = {}",
                        croplandToGrasslandConversionAndRemainingPeriod);

                // TODO Ascertain that within means less or equal to
                // Check if the number of years is within the Cropland to Grassland Conversion Period
                log.trace("Checking if the number of years is within than the Cropland to Grassland Conversion Period");

                result = (years <= croplandToGrasslandConversionAndRemainingPeriod.getConversionPeriod());

                log.trace(result ?
                        "The number of years is within than the Cropland to Grassland Conversion Period" :
                        "The number of years is not within than the Cropland to Grassland Conversion Period");


            }

        }

        log.trace(result ?
                "The Cover Type becomes Cropland within the Cropland to Grassland Conversion Period " +
                        "without becoming any other Cover Type" :
                "The Cover Type does not become Cropland within the Cropland to Grassland Conversion Period " +
                        "without becoming any other Cover Type");

        return result;
    }


    /**
     * Checks whether the Cover Type has changed since the Initial Timestep
     */
    private boolean hasTheCoverTypeChangedSinceTheInitialTimestep(
            List<LocationLandUsesHistory> locationLandUsesHistories,
            LocationCoverTypesHistory currentLocationCoverTypesHistory) {


        boolean result =
                locationLandUsesHistories
                        .stream()
                        .filter(l -> l.getLandUseCategory() != null)
                        .anyMatch(l ->
                                l.getItemNumber() < currentLocationCoverTypesHistory.getItemNumber() &&
                                        !l.getLandUseCategory().getCoverTypeId().equals(currentLocationCoverTypesHistory
                                                .getCoverType().getId()));

        log.trace(result ?
                "The Cover Type has changed since the Initial Timestep" :
                "The Cover Type has not changed since the Initial Timestep");

        return result;
    }


    /**
     * Checks whether the current Cover Type remained the same for longer than the Land Conversion Period of the
     * Previous Land Use
     */
    private boolean hasTheCoverTypeRemainedTheSameForLongerThanTheLandConversionPeriodOfThePreviousLandUse(
            LocationLandUsesHistory previousLocationLandUsesHistory,
            LocationCoverTypesHistory nextLocationCoverTypesHistory,
            LocationCoverTypesHistory lastLocationCoverTypesHistory,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod
    ) {

        // Get the number of years between the Previous and Next Cover Type (or Final Cover Type if Next is Null)
        log.trace("Getting the number of years between the Previous and Next Cover Type (or Final Cover Type if Next is Null)");
        long years = getYearsInBetween(
                previousLocationLandUsesHistory,
                nextLocationCoverTypesHistory,
                lastLocationCoverTypesHistory);
        log.debug("Number of years between the Previous and Next Cover Type = {}", years);

        // Check if the number of years is greater than the Land Conversion Period
        log.trace("Checking if the number of years is greater than the Land Conversion Period");
        boolean result = (years > conversionAndRemainingPeriod.getConversionPeriod());

        log.trace(result ?
                "The current Cover Type remained the same for longer than the Land Conversion Period of the " +
                        "Previous Land Use" :
                "The current Cover Type did not remain the same for longer than the Land Conversion Period of the " +
                        "Previous Land Use");

        return result;

    }


    /**
     * Checks whether the current Cover Type remained the same for longer than the Land Remaining Period
     */
    private boolean hasTheCoverTypeRemainedTheSameForLongerThanTheLandRemainingPeriod(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            LocationLandUsesHistory previousLocationLandUsesHistory,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {


        // Get the number of years between the Previous and Current Cover Type
        log.trace("Getting the number of years between the Previous and Current Cover Type");
        long years = getYearsInBetween(previousLocationLandUsesHistory, currentLocationCoverTypesHistory);
        log.debug("Number of years between the Previous and Current Cover Type = {}", years);


        // Check if the number of years is greater than the Land Remaining Period
        log.trace("Checking if the number of years is greater than the Land Remaining Period");
        boolean result = (years > conversionAndRemainingPeriod.getRemainingPeriod());
        log.trace(result ?
                "The current Cover Type remained the same for longer than the Land Remaining Period" :
                "The current Cover Type did not remain the same for longer than the Land Remaining Period");

        return result;

    }


    /**
     * Establishes whether the Cover Types for the Current and Next Time Step as well as the Land Use Class of the
     * Previous Time Step includes Cropland and Grassland Cover Types and exclude all other Cover Types given a timestep
     */
    private boolean doTheCoverTypesOfTheCurrentAndNextTimeStepAndTheLandUseClassOfThePreviousTimeStepIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            LocationLandUsesHistory previousLocationLandUsesHistory,
            LocationCoverTypesHistory nextLocationCoverTypesHistory) {

        // Get the Id of the Previous Cover Type
        log.trace("Getting the Id of the Previous Cover Type");
        Long previousCoverTypeId;
        try {
            previousCoverTypeId = previousLocationLandUsesHistory.getLandUseCategory().getCoverTypeId();
        } catch (NullPointerException ex) {
            previousCoverTypeId = null;
        }
        log.debug("Previous Cover Type Id = {}", previousCoverTypeId);

        // Get the Id of the Current Cover Type
        log.trace("Getting the Id of the Current Cover Type");
        Long currentCoverTypeId;
        try {
            currentCoverTypeId = currentLocationCoverTypesHistory.getCoverType().getId();
        } catch (NullPointerException ex) {
            currentCoverTypeId = null;
        }
        log.debug("Current Cover Type Id = {}", currentCoverTypeId);

        // Get the Id of the Next Cover Type
        log.trace("Getting the Id of the Next Cover Type");
        Long nextCoverTypeId;
        try {
            nextCoverTypeId = nextLocationCoverTypesHistory.getCoverType().getId();
        } catch (NullPointerException ex) {
            nextCoverTypeId = null;
        }
        log.debug("Next Cover Type Id = {}", currentCoverTypeId);

        // Check whether the Cover Types for the Current and Next Time Steps together with the Land Use
        // Class of the Previous Time Step include both Cropland and Grassland and exclude all other
        // Cover Types
        log.trace("Checking whether the Cover Types for the Current and Next Time Steps together with " +
                "the Land Use Class of the Previous Time Step include both Cropland and Grassland " +
                "and exclude all other Cover Types");
        boolean result;
        if (previousCoverTypeId == null || currentCoverTypeId == null || nextCoverTypeId == null) {
            result = false;
        } else {
            result = doCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(
                    Arrays.asList(previousCoverTypeId, currentCoverTypeId, nextCoverTypeId));
        }

        log.trace(result ?
                "The Cover Types for the Current and Next Time Step together with the Land Use Class of the " +
                        "Previous Time Step include both Cropland and Grassland and exclude all other Cover Types" :
                "The Cover Types for the Current and Next Time Step together with the Land Use Class of the " +
                        "Previous Time Step DO NOT include both Cropland and Grassland and exclude all other " +
                        "Cover Types");

        return result;

    }

    /**
     * Calculates how long the Cover Types for the Current and Next Time Steps as well as the Land Use Classes of the
     * Previous Time Steps have included Cropland and Grassland Cover Types and excluded all other Cover Types
     */
    private long howLongHaveTheCurrentAndFutureCoverTypesAndPreviousLandUseClassesBeenCroplandAndGrasslandExcludingAnythingElse(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            LocationCoverTypesHistory lastLocationCoverTypesHistory,
            List<LocationCoverTypesHistory> locationCoverTypesHistories,
            List<LocationLandUsesHistory> locationLandUsesHistories) {


        // Check how far back the Cover Types was Cropland or Grassland to the exclusion of all other
        // Cover Types
        log.trace("Checking how far back the Cover Types was Cropland or Grassland to the exclusion of " +
                "all other Cover Types");

        // The assumption is when this method is called, the previous Land Use Class is Cropland or Grassland
        // So lets initialize the furthest step back to this step
        long farBack = currentLocationCoverTypesHistory.getItemNumber() - 1;

        // Next, lets check if the trend persists backwards starting a step before the previous step
        long step = farBack - 1;
        while (step >= 0) {
            if (isCoverTypeCroplandOrGrassland(
                    locationCoverTypesHistories,
                    locationLandUsesHistories,
                    step)) {

                farBack = step;

                step -= 1;

            } else {
                break;
            }
        }

        log.debug("The Previous, Current and Next Cover Types include both " +
                "Cropland and Grassland and exclude all other Cover Types as far back as " +
                "Timestep {}", farBack);

        // Check how far front the Cover Types was Cropland or Grassland to the exclusion of all other
        // Cover Types
        log.trace("Checking how far front the Cover Types was Cropland or Grassland to the exclusion of " +
                "all other Cover Types");

        // The assumption is when this method is called, the next Cover Type is Cropland or Grassland
        // So lets initialize the furthest step front to this step
        long farFront = currentLocationCoverTypesHistory.getItemNumber() + 1;

        // Next, lets check if the trend persists forward starting a step after the next step
        step = farFront + 1;
        while (step <= lastLocationCoverTypesHistory.getItemNumber()) {
            if (isCoverTypeCroplandOrGrassland(
                    locationCoverTypesHistories,
                    locationLandUsesHistories,
                    step)) {

                farFront = step;

                step += 1;
            } else {
                break;
            }
        }

        log.debug("The Previous, Current and Next Cover Types include both " +
                "Cropland and Grassland and exclude all other Cover Types as far front as " +
                "Timestep {}", farFront);


        long years = LongStream.rangeClosed(farBack, farFront).count();

        log.debug("Years = {}", years);

        return years;
    }


    /**
     * Checks whether the provided number of years is greater than the Land Conversion Period of the Previous Land Use
     */
    private boolean hasTheCoverTypeBeenCroplandOrGrasslandForAPeriodLongerThanTheLandConversionPeriod(
            ConversionAndRemainingPeriod conversionAndRemainingPeriod,
            Long years) {

        boolean result = years > conversionAndRemainingPeriod.getConversionPeriod();
        log.trace(result ?
                "This has been the case for a period longer than the Land Conversion Period" :
                "This has not been the case for a period longer than the Land Conversion Period");

        return result;
    }

    /**
     * Checks whether the Cover Type has been Cropland or Grassland since the Initial Timestep
     */
    private boolean hasTheCoverTypeBeenCroplandOrGrasslandSinceTheInitialTimestep(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationLandUsesHistory> locationLandUsesHistories) {


        boolean excludesAllOtherCoverTypesSinceTheInitialTimestep =
                locationLandUsesHistories
                        .stream()
                        .filter(l -> l.getLandUseCategory() != null)
                        .filter(l -> l.getItemNumber() < currentLocationCoverTypesHistory.getItemNumber())
                        .noneMatch(l ->
                                !l.getLandUseCategory().getCoverTypeId().equals(croplandCoverType.getId()) &&
                                        !l.getLandUseCategory().getCoverTypeId().equals(grasslandCoverType.getId()));


        log.trace(excludesAllOtherCoverTypesSinceTheInitialTimestep ?
                "The Cover Type has been Cropland or Grassland since the Initial Timestep" :
                "The Cover Type has not been Cropland or Grassland since the Initial Timestep");

        return excludesAllOtherCoverTypesSinceTheInitialTimestep;
    }


    /**
     * Checks whether the provided number of years is greater than the Land Remaining Period of the Current Cover Type
     */
    private boolean hasTheCoverTypeBeenCroplandOrGrasslandForAPeriodLongerThanTheLandRemainingPeriod(
            ConversionAndRemainingPeriod conversionAndRemainingPeriod,
            Long years) {


        boolean result = years > conversionAndRemainingPeriod.getRemainingPeriod();
        log.trace(result ?
                "This has been the case for a period longer than the Land Remaining Period" :
                "This has not been the case for a period longer than the Land Remaining Period");

        return result;
    }


    /**
     * Checks whether the length of time to the end of the simulation is less than the land conversion period of the
     * previous land use
     */
    private boolean isLengthOfTimeToTheEndOfTheSimulationLessThanTheLandConversionPeriodOfThePreviousLandUse(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            LocationCoverTypesHistory lastLocationCoverTypesHistory,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        // Get the number of years to the end of the simulation i.e the years between the Current & Last Cover Types
        log.trace("Getting the number of years to the end of the simulation");
        long years = getYearsInBetween(currentLocationCoverTypesHistory, lastLocationCoverTypesHistory);
        log.debug("Years = {}", years);

        // TODO Confirm about the LT
        // Check whether the number of years is less than the Land Conversion Period of the previous Land Use
        log.trace("Checking whether the number of years is less than the Land Conversion Period of the previous " +
                "Land Use");
        boolean result = years < conversionAndRemainingPeriod.getConversionPeriod();

        log.trace(result ?
                "The number of years is less than the Land Conversion Period of the previous Land Use" :
                years == conversionAndRemainingPeriod.getConversionPeriod() ?
                        "The number of years is equal to the Land Conversion Period of the previous Land Use" :
                        "The number of years is greater than the Land Conversion Period of the previous Land Use");

        return result;
    }


    /**
     * Checks whether a passed in collection of Cover Types Ids includes only Cropland and Grassland Cover Types
     */
    private boolean doCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(
            List<Long> targetCoverTypesIds) {

        // Check if the target Cover Types Ids include Cropland Cover Type
        log.trace("Checking if the target Cover Types Ids include Cropland Cover Type");
        boolean includesCroplandCoverType =
                targetCoverTypesIds
                        .stream()
                        .anyMatch(id -> id.equals(croplandCoverType.getId()));

        log.debug("Target Cover Types includes Cropland Cover Type = {}", includesCroplandCoverType);

        if (includesCroplandCoverType) {

            // Check if the target Cover Types Ids include Grassland Cover Type
            log.trace("Checking if the target Cover Types Ids include Grassland Cover Type");
            boolean includesGrasslandCoverType =
                    targetCoverTypesIds
                            .stream()
                            .anyMatch(id -> id.equals(grasslandCoverType.getId()));
            log.debug("Target Cover Types includes Grassland Cover Type = {}", includesGrasslandCoverType);

            if (includesGrasslandCoverType) {

                // Check if the target Cover Types Ids exclude all other Cover Types
                log.trace("Checking if the target Cover Types Ids exclude all other Cover Types");

                boolean excludesAllOtherCoverTypes =
                        targetCoverTypesIds
                                .stream()
                                .noneMatch(id ->
                                        !id.equals(croplandCoverType.getId()) &&
                                                !id.equals(grasslandCoverType.getId()));

                log.debug("Target Cover Types exclude all other Cover Types = {}", excludesAllOtherCoverTypes);

                return excludesAllOtherCoverTypes;

            }

        }


        return false;
    }


    /**
     * Checks whether the Cover Type corresponding to the specified Timestep is Cropland or Grassland
     */
    private boolean isCoverTypeCroplandOrGrassland(
            List<LocationCoverTypesHistory> locationCoverTypesHistories,
            List<LocationLandUsesHistory> locationLandUsesHistories,
            Long timestep) {

        Long coverTypeId =
                getCoverTypeId(locationCoverTypesHistories, locationLandUsesHistories, timestep);

        if (coverTypeId == null) {
            return false;
        } else {
            return coverTypeId.equals(croplandCoverType.getId()) || coverTypeId.equals(grasslandCoverType.getId());

        }

    }


    /**
     * Checks whether the Current Cover Type reverts back to a previous Land Use before the end of its
     * Conversion Period
     */

    // TODO Confirm with Rob about the LTE
    private boolean doesTheCoverTypeChangeBackToAPreviousLandUseBeforeTheEndOfItsLandConversionPeriod(
            LocationCoverTypesHistory currentLocationCoverTypesHistory,
            List<LocationCoverTypesHistory> locationCoverTypesHistories,
            List<LocationLandUsesHistory> locationLandUsesHistories,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        // Get a set of the previous non Current Cover Types Ids
        log.trace("Getting a set of the previous non Current Cover Types Ids");
        Set<Long> previousNonCurrentCoverTypesIds =
                locationLandUsesHistories
                        .stream()
                        .filter(l -> l.getLandUseCategory() != null)
                        .filter(c ->
                                c.getItemNumber() < currentLocationCoverTypesHistory.getItemNumber() &&
                                        !c.getLandUseCategory().getCoverTypeId().equals(
                                                currentLocationCoverTypesHistory
                                                        .getCoverType().getId()))
                        .map(c -> c.getLandUseCategory().getCoverTypeId())
                        .collect(Collectors.toSet());
        log.debug("Previous non Current Cover Types Ids = {}", previousNonCurrentCoverTypesIds);

        // Get a set of the next non Current Cover Types History
        log.trace("Getting a set of the next non Current Cover Types History");
        Set<LocationCoverTypesHistory> nextNonCurrentCoverTypesHistories =
                locationCoverTypesHistories
                        .stream()

                        .filter(c ->
                                c.getItemNumber() > currentLocationCoverTypesHistory.getItemNumber() &&
                                        !c.getCoverType().getId().equals(
                                                currentLocationCoverTypesHistory
                                                        .getCoverType().getId()))
                        .collect(Collectors.toSet());
        log.debug("Next non Current Cover Types Ids = {}", nextNonCurrentCoverTypesHistories);


        // Check whether the cover type changed back to a previous land use before the end of its land conversion period
        log.trace("Checking whether the cover type changed back to a previous land use before the end of its " +
                "Land Conversion Period");
        boolean result =
                nextNonCurrentCoverTypesHistories
                        .stream()
                        .anyMatch(c ->
                                previousNonCurrentCoverTypesIds.contains(c.getCoverType().getId()) &&
                                        getYearsInBetween(currentLocationCoverTypesHistory, c)
                                                <= conversionAndRemainingPeriod.getConversionPeriod());

        log.trace(result ?
                "The cover type changed back to a Previous Land Use before the end of its Land Conversion Period" :
                "The cover type did not change back to a Previous Land Use before the end of its Land Conversion Period");

        return result;

    }

    @Override
    public String toString() {
        return "Decision Tree";
    }

}

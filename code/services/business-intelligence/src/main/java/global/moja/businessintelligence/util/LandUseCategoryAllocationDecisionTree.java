package global.moja.businessintelligence.services;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.CoverTypesHistoricDetail;
import global.moja.businessintelligence.daos.LandUsesHistoricDetail;
import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.models.ConversionAndRemainingPeriod;
import global.moja.businessintelligence.models.CoverType;
import global.moja.businessintelligence.util.LandUseChangeAction;
import global.moja.businessintelligence.util.builders.LandUseHistoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
public class LandUseAllocationDecisionTree {


    private final CoverType croplandCoverType;
    private final CoverType grasslandCoverType;
    private final ConfigurationDataProvider configurationDataProvider;
    private final String lineSeparator;

    @Autowired
    public LandUseAllocationDecisionTree(ConfigurationDataProvider configurationDataProvider) {
        this.configurationDataProvider = configurationDataProvider;
        this.croplandCoverType = configurationDataProvider.getCoverType("Cropland");
        this.grasslandCoverType = configurationDataProvider.getCoverType("Grassland");
        this.lineSeparator = System.getProperty("line.separator");
    }

    public LandUsesHistoricDetail process(
            Long timestep,
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails,
            List<LandUsesHistoricDetail> landUsesHistoricDetails) throws ServerException {


        log.trace("Entering Land Uses Processor");
        log.trace("----------------------------");

        // Declare variables
        log.trace("Declaring variables");
        CoverTypesHistoricDetail currentCoverTypesHistoricDetail;

        LandUsesHistoricDetail immediatePreviousLandUsesHistoricDetail;
        LandUsesHistoricDetail differentPreviousLandUsesHistoricDetail;

        CoverTypesHistoricDetail immediateNextCoverTypesHistoricDetail;
        CoverTypesHistoricDetail differentNextCoverTypesHistoricDetail;

        CoverTypesHistoricDetail lastCoverTypesHistoricDetail;

        ConversionAndRemainingPeriod conversionAndRemainingPeriod;


        // Validate the passed-in arguments and throw a Server Exception when one is invalid
        log.trace("Validating the passed-in arguments");

        validate(timestep, coverTypesHistoricDetails, landUsesHistoricDetails);

        // Initialize the current Timestep's Historic Cover Type Detail
        log.trace("Initializing the current Timestep's Historic Cover Type Detail");

        currentCoverTypesHistoricDetail =
                coverTypesHistoricDetails
                        .stream()
                        .filter(c -> c.getItemNumber().equals(timestep))
                        .findFirst()
                        .orElse(null);

        log.debug("Current Cover Type Historic Detail = {}", currentCoverTypesHistoricDetail);


        // Check if this is the Initial Timestep
        log.trace("{}{}Checking if this is the Initial Timestep{}", lineSeparator, lineSeparator, lineSeparator);

        if (isThisTheInitialTimestep(timestep)) {

            // Check whether the Current Cover Type is Grassland
            log.trace("{}{}Checking whether the Current Cover Type is Grassland{}", lineSeparator, lineSeparator, lineSeparator);

            if (isCoverTypeGrassland(currentCoverTypesHistoricDetail)) {

                // Initialize the Conversion & Remaining Period from Cropland to Grassland Land Use
                log.trace("Initializing the Conversion & Remaining Period from Cropland to Grassland Land Use");

                conversionAndRemainingPeriod =
                        configurationDataProvider
                                .getConversionAndRemainingPeriod(croplandCoverType.getId(), grasslandCoverType.getId());

                log.trace("The Conversion & Remaining Period from Cropland to Grassland = {}", conversionAndRemainingPeriod);


                // Check whether the Cover Type becomes Cropland within the Cropland to Grassland Conversion Period 
                // without becoming any other Cover Type
                log.trace("{}{}Checking whether the Cover Type becomes Cropland within the Cropland to Grassland " +
                                "Conversion Period without becoming any other Cover Type{}",
                        lineSeparator, lineSeparator, lineSeparator);

                if (doesTheCoverTypeBecomeCroplandWithinTheCroplandToGrasslandConversionPeriodWithoutBecomingAnyOtherCoverType(
                        coverTypesHistoricDetails, timestep)) {

                    // Classify the Land Use as Cropland Remaining Cropland
                    log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                    return
                            new LandUseHistoryBuilder()
                                    .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                    .year(currentCoverTypesHistoricDetail.getYear())
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
                            new LandUseHistoryBuilder()
                                    .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                    .year(currentCoverTypesHistoricDetail.getYear())
                                    .landUseCategory(
                                            configurationDataProvider
                                                    .getLandUseCategory(
                                                            currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                            currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                            LandUseChangeAction.REMAINING))
                                    .confirmed(true)
                                    .build();
                }


            } else {

                // Classify the Land Use as Land Remaining Land of the initial Cover Type
                log.trace("Classifying the Land Use as Land Remaining Land of the initial Cover Type");

                return
                        new LandUseHistoryBuilder()
                                .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                .year(currentCoverTypesHistoricDetail.getYear())
                                .landUseCategory(
                                        configurationDataProvider
                                                .getLandUseCategory(
                                                        currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                        currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                        LandUseChangeAction.REMAINING))
                                .confirmed(true)
                                .build();
            }


        } else {

            // Initialize the previous Land Use Historic Detail.
            log.trace("Initializing the previous Land Use Historic details");

            immediatePreviousLandUsesHistoricDetail =
                    getImmediatePreviousLandUseHistoricDetail(currentCoverTypesHistoricDetail, landUsesHistoricDetails);
            differentPreviousLandUsesHistoricDetail =
                    getDifferentPreviousLandUseHistoricDetail(currentCoverTypesHistoricDetail, landUsesHistoricDetails);

            log.debug("Immediate Previous Land Use Historic Detail = {}", immediatePreviousLandUsesHistoricDetail);
            log.debug("Different Previous Land Use Historic Detail = {}", differentPreviousLandUsesHistoricDetail);


            // Initialize the next Cover Type historic Detail.
            log.trace("Initializing the next Cover Type Historic details");

            immediateNextCoverTypesHistoricDetail =
                    getImmediateNextCoverTypeHistoricDetail(currentCoverTypesHistoricDetail, coverTypesHistoricDetails);
            differentNextCoverTypesHistoricDetail =
                    getDifferentNextCoverTypeHistoricDetail(currentCoverTypesHistoricDetail, coverTypesHistoricDetails);

            log.debug("Immediate Next Cover Type Historic Detail = {}", immediateNextCoverTypesHistoricDetail);
            log.debug("Different Next Cover Type Historic Detail = {}", differentNextCoverTypesHistoricDetail);


            // Get the last Cover Type historic detail.
            // By this, we mean get a historic detail
            // whose time step is the last timestep and
            log.trace("Initializing the last Cover Type Historic Detail");
            lastCoverTypesHistoricDetail =
                    getLastCoverTypeHistoricDetail(coverTypesHistoricDetails);

            log.debug("Last Cover Type Historic Detail = {}", lastCoverTypesHistoricDetail);


            // Initialize the Conversion & Remaining Period from the Previous Land Use to the current Cover Type
            log.trace("Initializing the Conversion & Remaining Period from the Previous Land Use");
            conversionAndRemainingPeriod = differentPreviousLandUsesHistoricDetail == null ? null :
                    getConversionAndRemainingPeriod(differentPreviousLandUsesHistoricDetail, currentCoverTypesHistoricDetail);

            log.trace("The Conversion & Remaining Period = {}", conversionAndRemainingPeriod);


            // Check if the Cover Type changed since the Initial Timestep
            log.trace("{}{}Checking if the Cover Type changed since the Initial Timestep{}",
                    lineSeparator, lineSeparator, lineSeparator);

            if (hasTheCoverTypeChangedSinceTheInitialTimestep(landUsesHistoricDetails, currentCoverTypesHistoricDetail)) {

                // Check if the Cover Type remained the same for longer than the Land Conversion Period of the
                // Previous Land Use
                log.trace("{}{}Checking if the Cover Type remained the same for longer than the Land " +
                        "Conversion Period of the previous Land Use{}", lineSeparator, lineSeparator, lineSeparator);

                if (hasTheCoverTypeRemainedTheSameForLongerThanTheLandConversionPeriodOfThePreviousLandUse(
                        differentPreviousLandUsesHistoricDetail,
                        differentNextCoverTypesHistoricDetail,
                        lastCoverTypesHistoricDetail,
                        conversionAndRemainingPeriod)) {

                    // Check if the Cover Type remained the same for longer than the Land Remaining Period
                    log.trace("{}{}Checking if the Cover Type remained the same for longer than the Land " +
                            "Remaining Period{}", lineSeparator, lineSeparator, lineSeparator);

                    if (hasTheCoverTypeRemainedTheSameForLongerThanTheLandRemainingPeriod(
                            currentCoverTypesHistoricDetail,
                            differentPreviousLandUsesHistoricDetail,
                            conversionAndRemainingPeriod)) {

                        // Classify the Land Use as Land Remaining Land of the Current Cover Type
                        log.trace("Classifying the Land Use as Land Remaining Land of the Current Cover Type");

                        return
                                new LandUseHistoryBuilder()
                                        .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                        .year(currentCoverTypesHistoricDetail.getYear())
                                        .landUseCategory(
                                                configurationDataProvider
                                                        .getLandUseCategory(
                                                                currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                                currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                                LandUseChangeAction.REMAINING))
                                        .confirmed(true)
                                        .build();

                    } else {


                        // Classify the Land Use as Land Converted to Current Cover
                        log.trace("Classifying the Land Use as Land Converted to Current Cover");

                        return
                                new LandUseHistoryBuilder()
                                        .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                        .year(currentCoverTypesHistoricDetail.getYear())
                                        .landUseCategory(
                                                configurationDataProvider
                                                        .getLandUseCategory(
                                                                differentPreviousLandUsesHistoricDetail
                                                                        .getLandUseCategory()
                                                                        .getCoverTypeId(),
                                                                currentCoverTypesHistoricDetail
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
                            currentCoverTypesHistoricDetail,
                            immediatePreviousLandUsesHistoricDetail,
                            immediateNextCoverTypesHistoricDetail)) {

                        // Calculate how long the Cover Types for the Current and Next Time Steps as well as the
                        // Land Use Classes of the Previous Time Steps have included Cropland and Grassland
                        // Cover Types and excluded all other Cover Types
                        log.trace("{}{} Calculating how long the Cover Types for the Current and Next Time Steps as " +
                                        "well as the Land Use Classes of the Previous Time Steps have included Cropland and " +
                                        "Grassland Cover Types and excluded all other Cover Types{}",
                                lineSeparator, lineSeparator, lineSeparator);

                        long years = howLongHaveTheCurrentAndFutureCoverTypesAndPreviousLandUseClassesBeenCroplandAndGrasslandExcludingAnythingElse(
                                currentCoverTypesHistoricDetail,
                                lastCoverTypesHistoricDetail,
                                coverTypesHistoricDetails,
                                landUsesHistoricDetails);


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
                                    currentCoverTypesHistoricDetail,
                                    lastCoverTypesHistoricDetail,
                                    conversionAndRemainingPeriod)) {

                                // Check whether the Previous Land Use is Cropland
                                log.trace("{}{} Checking whether the Previous Land Use is Cropland {}",
                                        lineSeparator, lineSeparator, lineSeparator);

                                if (isCoverTypeCropland(differentPreviousLandUsesHistoricDetail)) {

                                    // Classify the Land Use as the Previous Land Use
                                    // TODO Confirm with rdl
                                    log.trace("Classifying the Land Use as the Previous Land Use");
                                    return
                                            new LandUseHistoryBuilder()
                                                    .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                                    .year(currentCoverTypesHistoricDetail.getYear())
                                                    .landUseCategory(differentPreviousLandUsesHistoricDetail.getLandUseCategory())
                                                    .confirmed(differentPreviousLandUsesHistoricDetail.getConfirmed())
                                                    .build();

                                } else {

                                    // Classify the Land Use as Land (of the Previous Land Use) Converted to Cropland
                                    log.trace("Classifying the Land Use as Land (of the Previous Land Use) Converted to Cropland");

                                    return
                                            new LandUseHistoryBuilder()
                                                    .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                                    .year(currentCoverTypesHistoricDetail.getYear())
                                                    .landUseCategory(
                                                            configurationDataProvider
                                                                    .getLandUseCategory(
                                                                            differentPreviousLandUsesHistoricDetail
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
                                currentCoverTypesHistoricDetail, landUsesHistoricDetails) ||
                                hasTheCoverTypeBeenCroplandOrGrasslandForAPeriodLongerThanTheLandRemainingPeriod(
                                        conversionAndRemainingPeriod, years)) {

                            // Classify the Land Use as Cropland Remaining Cropland
                            log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                            return
                                    new LandUseHistoryBuilder()
                                            .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                            .year(currentCoverTypesHistoricDetail.getYear())
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

                            if (isCoverTypeCropland(differentPreviousLandUsesHistoricDetail)) {

                                // Classify the Land Use as the Previous Land Use
                                // TODO Confirm with rdl
                                log.trace("Classifying the Land Use as the Previous Land Use");
                                return
                                        new LandUseHistoryBuilder()
                                                .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                                .year(currentCoverTypesHistoricDetail.getYear())
                                                .landUseCategory(differentPreviousLandUsesHistoricDetail.getLandUseCategory())
                                                .confirmed(differentPreviousLandUsesHistoricDetail.getConfirmed())
                                                .build();

                            } else {

                                // Classify the Land Use as Land (of the Previous Land Use) Converted to Cropland
                                log.trace("Classifying the Land Use as Land (of the Previous Land Use) Converted to Cropland");

                                return
                                        new LandUseHistoryBuilder()
                                                .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                                .year(currentCoverTypesHistoricDetail.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        differentPreviousLandUsesHistoricDetail
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
                                currentCoverTypesHistoricDetail,
                                lastCoverTypesHistoricDetail,
                                conversionAndRemainingPeriod)) {

                            // Classify as unconfirmed Land Converted to Current Cover
                            log.trace("Classifying as unconfirmed Land Converted to Current Cover");

                            return
                                    new LandUseHistoryBuilder()
                                            .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                            .year(currentCoverTypesHistoricDetail.getYear())
                                            .landUseCategory(
                                                    configurationDataProvider
                                                            .getLandUseCategory(
                                                                    differentPreviousLandUsesHistoricDetail
                                                                            .getLandUseCategory().getCoverTypeId(),
                                                                    currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                                    LandUseChangeAction.CONVERSION))
                                            .confirmed(false)
                                            .build();

                        } else {

                            // Check if the Cover Type changed back to a Previous Land Use before the end of its
                            // Land Conversion Period
                            log.trace("{}{}Checking if the Cover Type changed back to a Previous Land Use before the end of " +
                                    "its Land Conversion Period{}", lineSeparator, lineSeparator, lineSeparator);

                            if (doesTheCoverTypeChangeBackToAPreviousLandUseBeforeTheEndOfItsLandConversionPeriod(
                                    currentCoverTypesHistoricDetail,
                                    coverTypesHistoricDetails,
                                    landUsesHistoricDetails,
                                    conversionAndRemainingPeriod)) {

                                // Classify as Land Remaining Land of Previous Land Use
                                log.trace("Classifying as Land Remaining Land of Previous Land Use");

                                return
                                        new LandUseHistoryBuilder()
                                                .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                                .year(currentCoverTypesHistoricDetail.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        differentPreviousLandUsesHistoricDetail
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        differentPreviousLandUsesHistoricDetail
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        LandUseChangeAction.REMAINING))
                                                .confirmed(true)
                                                .build();
                            } else {


                                // Classify as Previous Land Converted to Current Cover
                                log.trace("Classifying as Previous Land Converted to Current Cover");

                                return
                                        new LandUseHistoryBuilder()
                                                .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                                .year(currentCoverTypesHistoricDetail.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        differentPreviousLandUsesHistoricDetail
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        currentCoverTypesHistoricDetail
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
                        new LandUseHistoryBuilder()
                                .itemNumber(currentCoverTypesHistoricDetail.getItemNumber())
                                .year(currentCoverTypesHistoricDetail.getYear())
                                .landUseCategory(
                                        configurationDataProvider
                                                .getLandUseCategory(
                                                        currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                        currentCoverTypesHistoricDetail.getCoverType().getId(),
                                                        LandUseChangeAction.REMAINING))
                                .confirmed(true)
                                .build();


            }

        }


    }


    private ConversionAndRemainingPeriod getConversionAndRemainingPeriod(
            LandUsesHistoricDetail previousLandUsesHistoricDetail,
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail) {

        return configurationDataProvider.getConversionAndRemainingPeriod(
                previousLandUsesHistoricDetail.getLandUseCategory().getCoverTypeId(),
                currentCoverTypesHistoricDetail.getCoverType().getId());
    }


    public boolean validate(
            Long timestep,
            List<CoverTypesHistoricDetail> coverTypeHistories,
            List<LandUsesHistoricDetail> landUseHistories) throws ServerException {


        if (timestep == null) {
            throw new ServerException("Timestep is null");
        }

        if (timestep < 0L) {
            throw new ServerException("Timestep is less than 0");
        }

        if (coverTypeHistories == null) {
            throw new ServerException("Historic Cover Types list is null");
        }

        if (coverTypeHistories.isEmpty()) {
            throw new ServerException("Historic Cover Types list is empty");
        }

        if (landUseHistories == null) {
            throw new ServerException("Historic Land Uses list is null");
        }

        if (timestep > 0 && landUseHistories.isEmpty()) {
            throw new ServerException("Historic Land Uses list is empty");
        }

        if (timestep.equals(0L)) {

            Optional<CoverTypesHistoricDetail> found = Optional.empty();
            for (CoverTypesHistoricDetail c : coverTypeHistories) {
                if (c.getItemNumber() == 0L) {
                    found = Optional.of(c);
                    break;
                }
            }
            if (found.isEmpty()) {
                throw new ServerException("Timestep 0 Cover Type is null");
            }

        } else {

            for (long i = 0L; i <= timestep; i++) {

                Optional<CoverTypesHistoricDetail> found = Optional.empty();
                for (CoverTypesHistoricDetail c : coverTypeHistories) {
                    if (c.getItemNumber() <= i) {
                        found = Optional.of(c);
                        break;
                    }
                }
                if (found.isEmpty()) {
                    throw new ServerException("Timestep " + i + " Cover Type is null");
                }
            }

            for (long i = 0L; i < timestep; i++) {

                Optional<LandUsesHistoricDetail> found = Optional.empty();
                for (LandUsesHistoricDetail l : landUseHistories) {
                    if (l.getItemNumber() <= i) {
                        found = Optional.of(l);
                        break;
                    }
                }
                if (found.isEmpty()) {
                    throw new ServerException("Timestep " + i + " Cover Type is null");
                }
            }

        }

        return true;
    }

    public boolean validate(ConversionAndRemainingPeriod conversionAndRemainingPeriod) throws ServerException {

        if (conversionAndRemainingPeriod == null) {
            throw new ServerException("Conversion & Remaining Period is null");
        }

        if (conversionAndRemainingPeriod.getConversionPeriod() == null) {
            throw new ServerException("Conversion Period is null");
        }

        if (conversionAndRemainingPeriod.getRemainingPeriod() == null) {
            throw new ServerException("Remaining Period is null");
        }

        return true;

    }


    /**
     * Retrieves the last Cover Type Historic Detail.
     */
    private CoverTypesHistoricDetail getLastCoverTypeHistoricDetail(
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails) {

        return coverTypesHistoricDetails
                .stream()
                .sorted()
                .reduce((first, second) -> second)
                .orElse(null);
    }


    /**
     * Retrieves the immediate-previous Land Use Historic Detail.
     */
    private LandUsesHistoricDetail getImmediatePreviousLandUseHistoricDetail(
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<LandUsesHistoricDetail> landUsesHistoricDetails) {
        return getPreviousLandUseHistoricDetail(currentCoverTypesHistoricDetail, landUsesHistoricDetails, true);
    }


    /**
     * Retrieves the different-previous Land Use Historic Detail.
     */
    private LandUsesHistoricDetail getDifferentPreviousLandUseHistoricDetail(
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<LandUsesHistoricDetail> landUsesHistoricDetails) {
        return getPreviousLandUseHistoricDetail(currentCoverTypesHistoricDetail, landUsesHistoricDetails, false);
    }


    /**
     * Retrieves the immediate-previous, or different-previous Land Use Historic Detail.
     * <p>
     * Retrieves the immediate-previous Land Use Historic Detail by looking back one step
     * from the current timestep.
     * <p>
     * Retrieves the different-previous Land Use Historic Detail by step-wisely looking
     * back from the current timestep and establishing the first point at which the
     * Cover Type of the current timestep was different from the Cover Type of a
     * previous Land Use.
     * <p>
     * It then returns that Land Use history
     */
    private LandUsesHistoricDetail getPreviousLandUseHistoricDetail(
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<LandUsesHistoricDetail> landUsesHistoricDetails,
            boolean immediate) {

        if (immediate) {
            return
                    landUsesHistoricDetails
                            .stream()
                            .filter(c ->
                                    c.getItemNumber() == (currentCoverTypesHistoricDetail.getItemNumber() - 1))
                            .reduce((first, second) -> second)
                            .orElse(null);
        } else {
            return
                    landUsesHistoricDetails
                            .stream()
                            .filter(c ->
                                    c.getItemNumber() < currentCoverTypesHistoricDetail.getItemNumber() &&
                                            !c.getLandUseCategory().getCoverTypeId()
                                                    .equals(currentCoverTypesHistoricDetail.getCoverType().getId()))
                            .reduce((first, second) -> second)
                            .orElse(null);
        }


    }


    /**
     * Retrieves the immediate-next Cover Type Historic Detail.
     *
     */
    private CoverTypesHistoricDetail getImmediateNextCoverTypeHistoricDetail(
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails) {
        return getNextCoverTypeHistoricDetail(currentCoverTypesHistoricDetail, coverTypesHistoricDetails, true);
    }


    /**
     * Retrieves the different-next Cover Type Historic Detail.
     *
     */
    private CoverTypesHistoricDetail getDifferentNextCoverTypeHistoricDetail(
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails) {
        return getNextCoverTypeHistoricDetail(currentCoverTypesHistoricDetail, coverTypesHistoricDetails, false);
    }


    /**
     * Retrieves the immediate-next, or different-next Cover Type Historic Detail.
     * <p>
     * Retrieves the immediate-next Cover Type Historic Detail by looking forward one step
     * from the current timestep.
     * <p>
     * Retrieves the different-next Cover Type Historic Detail by step-wisely looking
     * forward from the current timestep and establishing the first point at which the
     * current Cover Type was different from a succeeding Cover Type
     * <p>
     * It then returns that Cover Type history
     */
    private CoverTypesHistoricDetail getNextCoverTypeHistoricDetail(
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails,
            boolean immediate) {

        if (immediate) {
            return
                    coverTypesHistoricDetails
                            .stream()
                            .filter(c ->
                                    c.getItemNumber() == (currentCoverTypesHistoricDetail.getItemNumber() + 1))
                            .reduce((first, second) -> first)
                            .orElse(null);
        } else {
            return
                    coverTypesHistoricDetails
                            .stream()
                            .filter(c ->
                                    c.getItemNumber() > currentCoverTypesHistoricDetail.getItemNumber() &&
                                            !c.getCoverType().getId().equals(
                                                    currentCoverTypesHistoricDetail.getCoverType().getId()))
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
            LandUsesHistoricDetail previousLandUsesHistoricDetail,
            CoverTypesHistoricDetail nextCoverTypesHistoricDetail,
            CoverTypesHistoricDetail finalCoverTypesHistoricDetail) {

        long years;

        if (previousLandUsesHistoricDetail != null &&
                nextCoverTypesHistoricDetail != null) {

            years =
                    LongStream.range(
                            previousLandUsesHistoricDetail.getItemNumber() + 1,
                            nextCoverTypesHistoricDetail.getItemNumber()).count();

        } else if (previousLandUsesHistoricDetail != null) {

            years =
                    LongStream.rangeClosed(
                            previousLandUsesHistoricDetail.getItemNumber() + 1,
                            finalCoverTypesHistoricDetail.getItemNumber()).count();

        } else if (nextCoverTypesHistoricDetail != null) {

            years =
                    LongStream.range(
                            0L,
                            nextCoverTypesHistoricDetail.getItemNumber()).count();

        } else {

            years =
                    LongStream.rangeClosed(
                            0L,
                            finalCoverTypesHistoricDetail.getItemNumber()).count();
        }

        return years;
    }

    /**
     * Retrieves the number of years between the Current Cover Type
     * and the Final Cover Type
     */
    private long getYearsInBetween(
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            CoverTypesHistoricDetail finalCoverTypesHistoricDetail) {

        long years = 0L;

        if (currentCoverTypesHistoricDetail != null &&
                finalCoverTypesHistoricDetail != null) {

            if (!currentCoverTypesHistoricDetail.getItemNumber()
                    .equals(finalCoverTypesHistoricDetail.getItemNumber())) {

                years =
                        LongStream.rangeClosed(
                                currentCoverTypesHistoricDetail.getItemNumber() + 1,
                                finalCoverTypesHistoricDetail.getItemNumber()).count();

            }

        }

        return years;
    }


    /**
     * Retrieves the number of years between the Previous Land Use and the Current Cover Type
     */
    private long getYearsInBetween(
            LandUsesHistoricDetail previousLandUsesHistoricDetail,
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail) {

        long years = 0L;

        if (previousLandUsesHistoricDetail != null &&
                currentCoverTypesHistoricDetail != null) {

            years =
                    LongStream.rangeClosed(
                            previousLandUsesHistoricDetail.getItemNumber() + 1,
                            currentCoverTypesHistoricDetail.getItemNumber()).count();

        }

        return years;
    }


    /**
     * Retrieves the Cover Type id of the passed in timestep from the land use details if it exists,
     * or, from the Cover type details otherwise
     */
    private Long getCoverTypeId(
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails,
            List<LandUsesHistoricDetail> landUsesHistoricDetails,
            Long timestep) {

        Long coverTypeId = null;

        LandUsesHistoricDetail previousLandUsesHistoricDetail =
                landUsesHistoricDetails
                        .stream()
                        .sorted()
                        .filter(l -> l.getItemNumber().equals(timestep))
                        .findFirst()
                        .orElse(null);

        if (previousLandUsesHistoricDetail != null) {

            coverTypeId = previousLandUsesHistoricDetail.getLandUseCategory().getCoverTypeId();

        } else {

            CoverTypesHistoricDetail previousCoverTypesHistoricDetail =
                    coverTypesHistoricDetails
                            .stream()
                            .sorted()
                            .filter(l -> l.getItemNumber().equals(timestep))
                            .findFirst()
                            .orElse(null);

            if (previousCoverTypesHistoricDetail != null) {
                coverTypeId = previousCoverTypesHistoricDetail.getCoverType().getId();
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
            CoverTypesHistoricDetail coverTypesHistoricDetail) {

        boolean result = coverTypesHistoricDetail.getCoverType().getId().equals(grasslandCoverType.getId());
        log.trace(result ? "Cover Type is Grassland" : "Cover Type is not Grassland");
        return result;

    }


    /**
     * Checks if the Cover Type in the Land Use History is Cropland
     */
    private boolean isCoverTypeCropland(
            LandUsesHistoricDetail landUsesHistoricDetail) {

        boolean result = landUsesHistoricDetail.getLandUseCategory().getCoverTypeId().equals(croplandCoverType.getId());
        log.trace(result ? "Cover Type is Cropland" : "Cover Type is not Cropland");
        return result;

    }


    /**
     * Checks if the Cover Type becomes Cropland within the Cropland to grassland conversion period without becoming any
     * other Cover Type after the current Timestep
     */
    private boolean doesTheCoverTypeBecomeCroplandWithinTheCroplandToGrasslandConversionPeriodWithoutBecomingAnyOtherCoverType(
            List<CoverTypesHistoricDetail> coverTypeHistories,
            Long currentTimestep) {

        boolean result = false;


        // Check if the Cover Type becomes Cropland in the future
        log.trace("Checking if the Cover Type becomes Cropland in the future");
        CoverTypesHistoricDetail croplandCoverTypesHistoricDetail =
                coverTypeHistories
                        .stream()
                        .sorted()
                        .filter(c -> c.getItemNumber() > currentTimestep &&
                                c.getCoverType().getId().equals(croplandCoverType.getId()))
                        .findFirst()
                        .orElse(null);

        log.trace(croplandCoverTypesHistoricDetail != null ?
                "The Cover Type becomes Cropland in the future" :
                "The Cover Type does not become Cropland in the future");

        if (croplandCoverTypesHistoricDetail != null) {


            // Check if a non Grassland, non Cropland Cover Type preceded the Cover Type becoming Cropland
            log.trace("Checking if a non Grassland, non Cropland Cover Type preceded the Cover Type becoming Cropland");

            CoverTypesHistoricDetail nonGrasslandNonCroplandCoverTypesHistoricDetail =
                    coverTypeHistories
                            .stream()
                            .sorted()
                            .filter(c -> c.getItemNumber() > currentTimestep &&
                                    c.getItemNumber() < croplandCoverTypesHistoricDetail.getItemNumber() &&
                                    !c.getCoverType().getId().equals(croplandCoverType.getId()) &&
                                    !c.getCoverType().getId().equals(grasslandCoverType.getId()))
                            .findFirst()
                            .orElse(null);

            log.trace(nonGrasslandNonCroplandCoverTypesHistoricDetail != null ?
                    "A non Grassland, non Cropland Cover Type preceded the Cover Type becoming Cropland" :
                    "A non Grassland, non Cropland Cover Type did not precede the Cover Type becoming Cropland");


            if (nonGrasslandNonCroplandCoverTypesHistoricDetail == null) {

                // Get the total number of years that elapsed before the Cover Type turned into Cropland
                log.trace("Getting the total number of years that elapsed before the Cover Type turned into Cropland");

                long years =
                        LongStream.range(
                                currentTimestep,
                                croplandCoverTypesHistoricDetail.getItemNumber()).count();

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
            List<LandUsesHistoricDetail> landUsesHistoricDetails,
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail) {


        boolean result =
                landUsesHistoricDetails
                        .stream()
                        .anyMatch(l ->
                                l.getItemNumber() < currentCoverTypesHistoricDetail.getItemNumber() &&
                                        !l.getLandUseCategory().getCoverTypeId().equals(currentCoverTypesHistoricDetail
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
            LandUsesHistoricDetail previousLandUsesHistoricDetail,
            CoverTypesHistoricDetail nextCoverTypesHistoricDetail,
            CoverTypesHistoricDetail lastCoverTypesHistoricDetail,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod
    ) {

        // Get the number of years between the Previous and Next Cover Type (or Final Cover Type if Next is Null)
        log.trace("Getting the number of years between the Previous and Next Cover Type (or Final Cover Type if Next is Null)");
        long years = getYearsInBetween(
                previousLandUsesHistoricDetail,
                nextCoverTypesHistoricDetail,
                lastCoverTypesHistoricDetail);
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
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            LandUsesHistoricDetail previousLandUsesHistoricDetail,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {


        // Get the number of years between the Previous and Current Cover Type
        log.trace("Getting the number of years between the Previous and Current Cover Type");
        long years = getYearsInBetween(previousLandUsesHistoricDetail, currentCoverTypesHistoricDetail);
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
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            LandUsesHistoricDetail previousLandUsesHistoricDetail,
            CoverTypesHistoricDetail nextCoverTypesHistoricDetail) {

        // Get the Id of the Previous Cover Type
        log.trace("Getting the Id of the Previous Cover Type");
        Long previousCoverTypeId;
        try {
            previousCoverTypeId = previousLandUsesHistoricDetail.getLandUseCategory().getCoverTypeId();
        } catch (NullPointerException ex) {
            previousCoverTypeId = null;
        }
        log.debug("Previous Cover Type Id = {}", previousCoverTypeId);

        // Get the Id of the Current Cover Type
        log.trace("Getting the Id of the Current Cover Type");
        Long currentCoverTypeId;
        try {
            currentCoverTypeId = currentCoverTypesHistoricDetail.getCoverType().getId();
        } catch (NullPointerException ex) {
            currentCoverTypeId = null;
        }
        log.debug("Current Cover Type Id = {}", currentCoverTypeId);

        // Get the Id of the Next Cover Type
        log.trace("Getting the Id of the Next Cover Type");
        Long nextCoverTypeId;
        try {
            nextCoverTypeId = nextCoverTypesHistoricDetail.getCoverType().getId();
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
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            CoverTypesHistoricDetail lastCoverTypesHistoricDetail,
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails,
            List<LandUsesHistoricDetail> landUsesHistoricDetails) {


        // Check how far back the Cover Types was Cropland or Grassland to the exclusion of all other
        // Cover Types
        log.trace("Checking how far back the Cover Types was Cropland or Grassland to the exclusion of " +
                "all other Cover Types");

        // The assumption is when this method is called, the previous Land Use Class is Cropland or Grassland
        // So lets initialize the furthest step back to this step
        long farBack = currentCoverTypesHistoricDetail.getItemNumber() - 1;

        // Next, lets check if the trend persists backwards starting a step before the previous step
        long step = farBack - 1;
        while (step >= 0) {
            if (isCoverTypeCroplandOrGrassland(
                    coverTypesHistoricDetails,
                    landUsesHistoricDetails,
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
        long farFront = currentCoverTypesHistoricDetail.getItemNumber() + 1;

        // Next, lets check if the trend persists forward starting a step after the next step
        step = farFront + 1;
        while (step <= lastCoverTypesHistoricDetail.getItemNumber()) {
            if (isCoverTypeCroplandOrGrassland(
                    coverTypesHistoricDetails,
                    landUsesHistoricDetails,
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
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<LandUsesHistoricDetail> landUsesHistoricDetails) {


        boolean excludesAllOtherCoverTypesSinceTheInitialTimestep =
                landUsesHistoricDetails
                        .stream()
                        .filter(l -> l.getItemNumber() < currentCoverTypesHistoricDetail.getItemNumber())
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
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            CoverTypesHistoricDetail lastCoverTypesHistoricDetail,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        // Get the number of years to the end of the simulation i.e the years between the Current & Last Cover Types
        log.trace("Getting the number of years to the end of the simulation");
        long years = getYearsInBetween(currentCoverTypesHistoricDetail, lastCoverTypesHistoricDetail);
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
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails,
            List<LandUsesHistoricDetail> landUsesHistoricDetails,
            Long timestep) {

        Long coverTypeId =
                getCoverTypeId(coverTypesHistoricDetails, landUsesHistoricDetails, timestep);

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
            CoverTypesHistoricDetail currentCoverTypesHistoricDetail,
            List<CoverTypesHistoricDetail> coverTypesHistoricDetails,
            List<LandUsesHistoricDetail> landUsesHistoricDetails,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        // Get a set of the previous non Current Cover Types Ids
        log.trace("Getting a set of the previous non Current Cover Types Ids");
        Set<Long> previousNonCurrentCoverTypesIds =
                landUsesHistoricDetails
                        .stream()
                        .filter(c ->
                                c.getItemNumber() < currentCoverTypesHistoricDetail.getItemNumber() &&
                                        !c.getLandUseCategory().getCoverTypeId().equals(
                                                currentCoverTypesHistoricDetail
                                                        .getCoverType().getId()))
                        .map(c -> c.getLandUseCategory().getCoverTypeId())
                        .collect(Collectors.toSet());
        log.debug("Previous non Current Cover Types Ids = {}", previousNonCurrentCoverTypesIds);

        // Get a set of the next non Current Cover Types History
        log.trace("Getting a set of the next non Current Cover Types History");
        Set<CoverTypesHistoricDetail> nextNonCurrentCoverTypesHistories =
                coverTypesHistoricDetails
                        .stream()
                        .filter(c ->
                                c.getItemNumber() > currentCoverTypesHistoricDetail.getItemNumber() &&
                                        !c.getCoverType().getId().equals(
                                                currentCoverTypesHistoricDetail
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
                                        getYearsInBetween(currentCoverTypesHistoricDetail, c)
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

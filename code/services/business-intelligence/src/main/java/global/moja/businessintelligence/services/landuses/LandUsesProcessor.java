package global.moja.businessintelligence.services.landuses;

import global.moja.businessintelligence.configurations.ConfigurationDataProvider;
import global.moja.businessintelligence.daos.CoverTypeHistoricDetail;
import global.moja.businessintelligence.daos.LandUseHistoricDetail;
import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.models.ConversionAndRemainingPeriod;
import global.moja.businessintelligence.models.CoverType;
import global.moja.businessintelligence.util.LandUseChangeAction;
import global.moja.businessintelligence.util.builders.LandUseHistoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 * @author Kwaje Anthony <tony@miles.co.ke>
 * @version 1.0
 * @since 0.0.1
 */
@Component
@Qualifier("cropLandUsesProcessor")
@Slf4j
public class CropLandUsesProcessor {


    private final CoverType croplandCoverType;
    private final CoverType grasslandCoverType;
    private final ConfigurationDataProvider configurationDataProvider;
    private final String lineSeparator;

    @Autowired
    public CropLandUsesProcessor(ConfigurationDataProvider configurationDataProvider) {
        this.configurationDataProvider = configurationDataProvider;
        this.croplandCoverType = configurationDataProvider.getCoverType("Cropland");
        this.grasslandCoverType = configurationDataProvider.getCoverType("Grassland");
        this.lineSeparator = System.getProperty("line.separator");
    }

    public LandUseHistoricDetail process(
            Long timestep,
            List<CoverTypeHistoricDetail> coverTypesHistoricDetails,
            List<LandUseHistoricDetail> landUseHistoricDetails) throws ServerException {


        log.trace("Entering Land Uses Processor");
        log.trace("----------------------------");

        // Validate the passed-in arguments and throw a Server Exception when one is invalid
        log.trace("Validating the passed-in arguments");

        validate(timestep, coverTypesHistoricDetails, landUseHistoricDetails);

        // Get the Current Cover Type Historic Detail
        log.trace("Getting the Current Cover Type Historic Detail");

        CoverTypeHistoricDetail currentCoverTypeHistoricDetail =
                getCurrentCoverTypeHistoricDetail(timestep, coverTypesHistoricDetails);

        // Check if this is the Initial Timestep
        log.trace("{}Checking if this is the Initial Timestep{}", lineSeparator, lineSeparator);

        if (isInitialTimestep(timestep)) {

            // This is the Initial Timestep
            log.trace("This is the Initial Timestep");

            // Check whether the Current Cover Type is Grassland
            log.trace("{}Checking whether the Current Cover Type is Grassland{}", lineSeparator, lineSeparator);

            if (isCurrentCoverTypeGrassland(currentCoverTypeHistoricDetail, grasslandCoverType)) {

                // The Current Cover Type is Grassland
                log.trace("The Current Cover Type is Grassland");

                // Check whether the Cover Type becomes Cropland within the Cropland to Grassland Conversion Period 
                // without becoming any other Cover Type
                log.trace("{}Checking whether the Cover Type becomes Cropland within the Cropland to Grassland " +
                        "Conversion Period without becoming any other Cover Type{}", lineSeparator, lineSeparator);
                if (doesTheCoverTypeBecomeCroplandWithinTheCroplandToGrasslandConversionPeriodWithoutBecomingAnyOtherCoverType(
                        currentCoverTypeHistoricDetail,
                        croplandCoverType,
                        configurationDataProvider
                                .getConversionAndRemainingPeriod(croplandCoverType.getId(), grasslandCoverType.getId()),
                        coverTypesHistoricDetails)) {

                    // The Cover Type becomes Cropland within the Cropland to Grassland Conversion Period 
                    // without becoming any other Cover Type
                    log.trace("The Cover Type becomes Cropland within the Cropland to Grassland Conversion Period " +
                            "without becoming any other Cover Type");

                    // Classify the Land Use as Cropland Remaining Cropland
                    log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                    return
                            new LandUseHistoryBuilder()
                                    .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                    .year(currentCoverTypeHistoricDetail.getYear())
                                    .landUseCategory(
                                            configurationDataProvider
                                                    .getLandUseCategory(
                                                            croplandCoverType.getId(),
                                                            croplandCoverType.getId(),
                                                            LandUseChangeAction.REMAINING))
                                    .confirmed(true)
                                    .build();

                } else {

                    // The Cover Type does not becomes Cropland within the Cropland to Grassland Conversion Period
                    // without becoming any other Cover Type
                    log.trace("The Cover Type does not becomes Cropland within the Cropland to Grassland Conversion " +
                            "Period without becoming any other Cover Type");

                    // Classify the Land Use as Land Remaining Land of the initial Cover Type
                    log.trace("Classifying the Land Use as Land Remaining Land of the initial Cover Type");

                    return
                            new LandUseHistoryBuilder()
                                    .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                    .year(currentCoverTypeHistoricDetail.getYear())
                                    .landUseCategory(
                                            configurationDataProvider
                                                    .getLandUseCategory(
                                                            currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                            currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                            LandUseChangeAction.REMAINING))
                                    .confirmed(true)
                                    .build();
                }


            } else {

                // The Current Cover Type is not Grassland
                log.trace("The Current Cover Type is not Grassland");

                // Classify the Land Use as Land Remaining Land of the initial Cover Type
                log.trace("Classifying the Land Use as Land Remaining Land of the initial Cover Type");

                return
                        new LandUseHistoryBuilder()
                                .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                .year(currentCoverTypeHistoricDetail.getYear())
                                .landUseCategory(
                                        configurationDataProvider
                                                .getLandUseCategory(
                                                        currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                        currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                        LandUseChangeAction.REMAINING))
                                .confirmed(true)
                                .build();
            }


        } else {

            // This is the not the Initial Timestep
            log.trace("This is not the Initial Timestep");

            // Get the Historic Details of the Previous Land Use i.e. the Historic Details of the last Land Use whose
            // Cover Type was different from the Current Cover Type
            log.trace("Getting the Historic Details of the Previous Land Use i.e. the Historic Details of the last " +
                    "Land Use whose Cover Type was different from the Current Cover Type");

            LandUseHistoricDetail previousLandUseHistoricDetail =
                    getPreviousLandUseHistoricDetail(currentCoverTypeHistoricDetail, landUseHistoricDetails);

            log.debug("Previous Land Use Historic Detail = {}", previousLandUseHistoricDetail);

            // Check if the Cover Type changed since the Initial Timestep
            log.trace("{}Checking if the Cover Type changed since the Initial Timestep{}",
                    lineSeparator, lineSeparator);

            if (hasCoverTypeChanged(previousLandUseHistoricDetail)) {

                // The Cover Type changed since the Initial Timestep
                log.trace("The Cover Type changed since the Initial Timestep");

                // Get the Conversion & Remaining Period from the Previous Land Use
                log.trace("Getting the Conversion & Remaining Period from the Previous Land Use");

                ConversionAndRemainingPeriod conversionAndRemainingPeriod =
                        getConversionAndRemainingPeriod(previousLandUseHistoricDetail, currentCoverTypeHistoricDetail);

                // Get the Historic Details of a Next Cover Type i.e the Historic Details of a Future Cover Type that is
                // different from the Current Cover Type
                log.trace("Getting the Historic Details of a Next Cover Type i.e the Historic Details of a Future " +
                        "Cover Type that is different from the Current Cover Type");

                CoverTypeHistoricDetail nextCoverTypeHistoricDetail =
                        getNextCoverTypeHistoricDetail(currentCoverTypeHistoricDetail, coverTypesHistoricDetails);

                log.debug("Next Cover Type = {}", nextCoverTypeHistoricDetail);

                // Get the Historic Details of the Final Cover Type i.e the Historic Details of a Future Cover Type
                // whose Timestep is the series' Last Timestep
                log.trace("Getting the Historic Details of the Final Cover Type i.e the Historic Details of a Future " +
                        "Cover Type whose Timestep is the series' Last Timestep");
                CoverTypeHistoricDetail lastCoverTypeHistoricDetail =
                        getLastCoverTypeHistoricDetail(coverTypesHistoricDetails);

                log.debug("Last Cover Type = {}", lastCoverTypeHistoricDetail);

                // Check if the Cover Type remained the same for longer than the Land Conversion Period of the
                // Previous Land Use
                log.trace("{}Checking if the Cover Type remained the same for longer than the Land " +
                        "Conversion Period of the previous Land Use{}", lineSeparator, lineSeparator);

                if (hasCoverTypeRemainedTheSameForLongerThanTheLandConversionPeriodOfThePreviousLandUse(
                        previousLandUseHistoricDetail,
                        nextCoverTypeHistoricDetail,
                        lastCoverTypeHistoricDetail,
                        conversionAndRemainingPeriod)) {

                    // The Cover Type remained the same for longer than the Land Conversion Period of the Previous
                    // Land Use
                    log.trace("The Cover Type remained the same for longer than the Land Conversion Period " +
                            "of the previous Land Use");

                    // Check if the Cover Type remained the same for longer than the Land Remaining Period
                    log.trace("{}Checking if the Cover Type remained the same for longer than the Land " +
                            "Remaining Period{}", lineSeparator, lineSeparator);

                    if (hasCoverTypeRemainedTheSameForLongerThanTheLandRemainingPeriod(
                            previousLandUseHistoricDetail,
                            currentCoverTypeHistoricDetail,
                            conversionAndRemainingPeriod)) {

                        // The Cover Type remained the same for longer than the Land Remaining Period
                        log.trace("The Cover Type remained the same for longer than the Land Remaining Period");

                        // Classify the Land Use as Cropland Remaining Cropland
                        log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                        return
                                new LandUseHistoryBuilder()
                                        .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                        .year(currentCoverTypeHistoricDetail.getYear())
                                        .landUseCategory(
                                                configurationDataProvider
                                                        .getLandUseCategory(
                                                                "Cropland",
                                                                "Cropland",
                                                                LandUseChangeAction.REMAINING))
                                        .confirmed(true)
                                        .build();

                    } else {

                        // The Cover Type did not remain the same for longer than the Land Remaining Period
                        log.trace("The Cover Type did not remain the same for longer than the Land Remaining Period");

                        // Classify the Land Use as Land Converted to Current Cover
                        log.trace("Classifying the Land Use as Land Converted to Current Cover");

                        return
                                new LandUseHistoryBuilder()
                                        .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                        .year(currentCoverTypeHistoricDetail.getYear())
                                        .landUseCategory(
                                                configurationDataProvider
                                                        .getLandUseCategory(
                                                                previousLandUseHistoricDetail
                                                                        .getLandUseCategory().getCoverTypeId(),
                                                                currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                                LandUseChangeAction.CONVERSION))
                                        .confirmed(true)
                                        .build();
                    }

                } else {

                    // The Cover Type did not remain the same for longer than the Land Conversion Period of the
                    // Previous Land Use
                    log.trace("The Cover Type did not remain the same for longer than the Land Conversion Period " +
                            "of the previous Land Use");


                    // Get the ids of the Cover Types identifiers of the previous, current and the next Timestep
                    log.trace("Getting the ids of the Cover Types of the previous, current and the next Timestep");

                    List<Long> neighbouringCoverTypesIds =
                            Stream.concat(
                                    landUseHistoricDetails
                                            .stream()
                                            .filter(l ->
                                                    l.getItemNumber() == timestep - 1)
                                            .map(l -> l.getLandUseCategory().getCoverTypeId()),
                                    coverTypesHistoricDetails
                                            .stream()
                                            .filter(c ->
                                                    (c.getItemNumber().equals(timestep)) ||
                                                            (c.getItemNumber() == timestep + 1))
                                            .map(c -> c.getCoverType().getId())
                            ).collect(Collectors.toList());

                    log.debug("Cover Types Ids = {}", neighbouringCoverTypesIds);

                    // Check whether the Previous, Current and Next Cover Types include both Cropland and Grassland
                    // and exclude all other Cover Types
                    log.trace("{}Checking whether the Previous, Current and Next Cover Types include both Cropland " +
                            "and Grassland and exclude all other Cover Types {}", lineSeparator, lineSeparator);

                    if (doNeighbouringCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(
                            neighbouringCoverTypesIds)) {

                        // The Previous, Current and Next Cover Types include both Cropland and Grassland and exclude
                        // all other Cover Types
                        log.trace("The Previous, Current and Next Cover Types include both Cropland and Grassland " +
                                "and exclude all other Cover Types");

                        // Get the previous Non Cropland, Non Grassland Land Use
                        log.trace("Getting the previous Non Cropland, Non Grassland Land Use");

                        LandUseHistoricDetail previousNonCroplandNonGrasslandUseHistoricDetail =
                                landUseHistoricDetails
                                        .stream()
                                        .filter(c ->
                                                c.getItemNumber() < timestep &&
                                                        !c.getLandUseCategory().getCoverTypeId()
                                                                .equals(currentCoverTypeHistoricDetail.getCoverType().getId()) &&
                                                        !c.getLandUseCategory().getCoverTypeId()
                                                                .equals(grasslandCoverType.getId())
                                        )
                                        .reduce((first, second) -> second)
                                        .orElse(null);

                        log.debug("Previous Non Cropland, Non Grassland Land Use = {}",
                                previousNonCroplandNonGrasslandUseHistoricDetail);

                        // Get the next Non Cropland, Non Grassland Cover Type
                        log.trace("Getting the next Non Cropland, Non Grassland Cover Type");
                        CoverTypeHistoricDetail nextNonCroplandNonGrasslandCoverTypeHistoricDetail =
                                coverTypesHistoricDetails
                                        .stream()
                                        .filter(c -> c.getItemNumber() > timestep &&
                                                !c.getCoverType().getId()
                                                        .equals(currentCoverTypeHistoricDetail.getCoverType().getId()) &&
                                                !c.getCoverType().getId()
                                                        .equals(grasslandCoverType.getId()))
                                        .findFirst()
                                        .orElse(null);

                        log.debug("Next Non Cropland, Non Grassland Cover Type = {}",
                                nextNonCroplandNonGrasslandCoverTypeHistoricDetail);

                        // Check whether Previous Land Use Cover Type together with the Current and succeeding Cover
                        // Types include Cropland or Grassland and exclude all other Cover Types for a period longer
                        // than Land Conversion Period
                        log.trace("{}Checking whether Previous Land Use Cover Type together with the Current and " +
                                        "succeeding Cover Types include Cropland or Grassland and exclude all other " +
                                        "Cover Types for a period longer than Land Conversion Period{}",
                                lineSeparator, lineSeparator);

                        if (hasCoverTypeRemainedTheSameForLongerThanTheLandConversionPeriodOfThePreviousLandUse(
                                previousNonCroplandNonGrasslandUseHistoricDetail,
                                nextNonCroplandNonGrasslandCoverTypeHistoricDetail,
                                lastCoverTypeHistoricDetail,
                                conversionAndRemainingPeriod)) {

                            // The Previous Land Use Cover Type together with the Current and succeeding Cover
                            // Types include Cropland or Grassland and exclude all other Cover Types for a period longer
                            // than Land Conversion Period
                            log.trace("The Previous Land Use Cover Type together with the Current and " +
                                    "succeeding Cover Types include Cropland or Grassland and exclude all other " +
                                    "Cover Types for a period longer than Land Conversion Period");

                            // Check whether Previous Land Uses Cover Types together with the Current Cover Type
                            // include Cropland or Grassland and exclude all other Cover Types since the Initial
                            // Timestep
                            log.trace("{}Checking whether Previous Land Uses Cover Types together with the Current " +
                                    "Cover Type include Cropland or Grassland and exclude all other " +
                                    "Cover Types since the Initial Timestep{}", lineSeparator, lineSeparator);

                            if (doPreviousCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElseSinceInitialTimestep(
                                    currentCoverTypeHistoricDetail,
                                    landUseHistoricDetails)) {

                                // The Previous Land Uses Cover Types together with the Current Cover Type
                                // include Cropland or Grassland and exclude all other Cover Types since the Initial
                                // Timestep
                                log.trace("{}The Previous Land Use Cover Types together with the Current " +
                                        "Cover Type include Cropland or Grassland and exclude all other " +
                                        "Cover Types since the Initial Timestep{}", lineSeparator, lineSeparator);

                                // Classify the Land Use as Cropland Remaining Cropland
                                log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                                return
                                        new LandUseHistoryBuilder()
                                                .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                                .year(currentCoverTypeHistoricDetail.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        croplandCoverType.getId(),
                                                                        croplandCoverType.getId(),
                                                                        LandUseChangeAction.REMAINING))
                                                .confirmed(true)
                                                .build();

                            } else {

                                // The Previous Land Uses Cover Types together with the Current Cover Type do not
                                // include Cropland or Grassland and exclude all other Cover Types since the Initial
                                // Timestep
                                log.trace("{}The Previous Land Use Cover Types together with the Current " +
                                        "Cover Type do not include Cropland or Grassland and exclude all other " +
                                        "Cover Types since the Initial Timestep{}", lineSeparator, lineSeparator);


                                // Check whether Previous Land Uses together with the Current Cover Type
                                // include Cropland or Grassland and exclude all other Cover Types for a period longer
                                // than the Land Remaining Period
                                log.trace("{}Checking whether Previous Land Uses together with the Current Cover Type " +
                                        "include Cropland or Grassland and exclude all other Cover Types for a " +
                                        "period longer than the Land Remaining Period{}", lineSeparator, lineSeparator);

                                if (hasCoverTypeRemainedTheSameForLongerThanTheLandRemainingPeriod(
                                        previousNonCroplandNonGrasslandUseHistoricDetail,
                                        currentCoverTypeHistoricDetail,
                                        conversionAndRemainingPeriod)) {

                                    // The Previous Land Uses Cover Types together with the Current Cover Type
                                    // include Cropland or Grassland and exclude all other Cover Types for a period longer
                                    // than the Land Remaining Period
                                    log.trace("{}The Previous Land Uses together with the Current Cover Type " +
                                                    "include Cropland or Grassland and exclude all other Cover Types for a " +
                                                    "period longer than the Land Remaining Period{}",
                                            lineSeparator, lineSeparator);

                                    // Classify the Land Use as Cropland Remaining Cropland
                                    log.trace("Classifying the Land Use as Cropland Remaining Cropland");

                                    return
                                            new LandUseHistoryBuilder()
                                                    .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                                    .year(currentCoverTypeHistoricDetail.getYear())
                                                    .landUseCategory(
                                                            configurationDataProvider
                                                                    .getLandUseCategory(
                                                                            croplandCoverType.getId(),
                                                                            croplandCoverType.getId(),
                                                                            LandUseChangeAction.REMAINING))
                                                    .confirmed(true)
                                                    .build();

                                } else {

                                    // The Previous Land Uses together with the Current Cover Type
                                    // did not include Cropland or Grassland and exclude all other Cover Types for
                                    // a period longer than the Land Remaining Period
                                    log.trace("{}The Previous Land Uses together with the Current Cover Type " +
                                                    "did not include Cropland or Grassland and exclude all other " +
                                                    "Cover Types for a period longer than the Land Remaining Period{}",
                                            lineSeparator, lineSeparator);

                                    // Classify the Land Use as Land Converted to Cropland
                                    log.trace("Classifying the Land Use as Land Converted to Cropland");

                                    return
                                            new LandUseHistoryBuilder()
                                                    .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                                    .year(currentCoverTypeHistoricDetail.getYear())
                                                    .landUseCategory(
                                                            configurationDataProvider
                                                                    .getLandUseCategory(
                                                                            previousLandUseHistoricDetail
                                                                                    .getLandUseCategory()
                                                                                    .getCoverTypeId(),
                                                                            croplandCoverType.getId(),
                                                                            LandUseChangeAction.CONVERSION))
                                                    .confirmed(true)
                                                    .build();
                                }


                            }


                        } else {

                            // The Previous Land Use Cover Types together with the Current and succeeding Cover
                            // Types do not include Cropland or Grassland and exclude all other Cover Types
                            // for a period longer than Land Conversion Period
                            log.trace("The Previous Land Use Cover Types together with the Current and " +
                                    "succeeding Cover Types do not include Cropland or Grassland and exclude all other " +
                                    "Cover Types for a period longer than Land Conversion Period");

                            // Classify as Unconfirmed land (of the previous land use) converted to Cropland
                            log.trace("Classifying as Unconfirmed land (of the previous land use) converted to Cropland");
                            return
                                    new LandUseHistoryBuilder()
                                            .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                            .year(currentCoverTypeHistoricDetail.getYear())
                                            .landUseCategory(
                                                    configurationDataProvider
                                                            .getLandUseCategory(
                                                                    previousLandUseHistoricDetail
                                                                            .getLandUseCategory()
                                                                            .getCoverTypeId(),
                                                                    croplandCoverType.getId(),
                                                                    LandUseChangeAction.CONVERSION))
                                            .confirmed(false)
                                            .build();
                        }

                    } else {

                        // The Previous, Current and Next Cover Types do not include both Cropland and Grassland
                        // and exclude all other Cover Types
                        log.trace("The Previous, Current and Next Cover Types do not include both Cropland and " +
                                "Grassland and exclude all other Cover Types");

                        // Check if the length of time to the end of the simulation is less than the Land Conversion
                        // Period for the Previous Land Use
                        log.trace("{}Checking if the length of time to the end of the simulation is less than the " +
                                "Land Conversion Period for the Previous Land Use{}", lineSeparator, lineSeparator);

                        if (isLengthOfTimeToTheEndOfTheSimulationLessThanTheLandConversionPeriodOfThePreviousLandUse(
                                currentCoverTypeHistoricDetail,
                                lastCoverTypeHistoricDetail,
                                conversionAndRemainingPeriod)) {

                            // The length of time to the end of the simulation is less than the Land Conversion
                            // Period for the Previous Land Use
                            log.trace("The length of time to the end of the simulation less than the " +
                                    "Land Conversion Period for the Previous Land Use");


                            // Classify as unconfirmed Land Converted to Current Cover
                            log.trace("Classifying as unconfirmed Land Converted to Current Cover");

                            return
                                    new LandUseHistoryBuilder()
                                            .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                            .year(currentCoverTypeHistoricDetail.getYear())
                                            .landUseCategory(
                                                    configurationDataProvider
                                                            .getLandUseCategory(
                                                                    previousLandUseHistoricDetail
                                                                            .getLandUseCategory().getCoverTypeId(),
                                                                    currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                                    LandUseChangeAction.CONVERSION))
                                            .confirmed(false)
                                            .build();

                        } else {

                            // The length of time to the end of the simulation is not less than the Land Conversion
                            // Period for the Previous Land Use
                            log.trace("The length of time to the end of the simulation is not less than the " +
                                    "Land Conversion Period for the Previous Land Use");

                            // Get a set of the previous non Current Cover Types Ids
                            log.trace("Getting a set of the previous non Current Cover Types Ids");
                            Set<Long> previousNonCurrentCoverTypesIds =
                                    landUseHistoricDetails
                                            .stream()
                                            .filter(c ->
                                                    c.getItemNumber() < timestep &&
                                                            !c.getLandUseCategory().getCoverTypeId().equals(
                                                                    currentCoverTypeHistoricDetail
                                                                            .getCoverType().getId()))
                                            .map(c -> c.getLandUseCategory().getCoverTypeId())
                                            .collect(Collectors.toSet());

                            // Get a set of the next non Current Cover Types History
                            log.trace("Getting a set of the next non Current Cover Types History");
                            Set<CoverTypeHistoricDetail> nextNonCurrentCoverTypesHistories =
                                    coverTypesHistoricDetails
                                            .stream()
                                            .filter(c ->
                                                    c.getItemNumber() > timestep &&
                                                            !c.getCoverType().getId().equals(
                                                                    currentCoverTypeHistoricDetail
                                                                            .getCoverType().getId()))
                                            .collect(Collectors.toSet());

                            // Check if the Cover Type changed back to a Previous Land Use before the end of its
                            // Land Conversion Period
                            log.trace("{}Checking if the Cover Type changed back to a Previous Land Use before the end of " +
                                    "its Land Conversion Period{}", lineSeparator, lineSeparator);

                            if (doesTheCoverTypeChangeBackToAPreviousLandUseBeforeTheEndOfItsLandConversionPeriod(
                                    previousNonCurrentCoverTypesIds,
                                    currentCoverTypeHistoricDetail,
                                    nextNonCurrentCoverTypesHistories,
                                    conversionAndRemainingPeriod)) {

                                // Cover Type changed back to a Previous Land Use before the end of its Land
                                // Conversion Period
                                log.trace("Cover type changed back to a Previous Land Use before the end " +
                                        "of its Land Conversion Period");

                                // Classify as Land Remaining Land of Previous Land Use
                                log.trace("Classifying as Land Remaining Land of Previous Land Use");

                                return
                                        new LandUseHistoryBuilder()
                                                .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                                .year(currentCoverTypeHistoricDetail.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        previousLandUseHistoricDetail
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        previousLandUseHistoricDetail
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        LandUseChangeAction.REMAINING))
                                                .confirmed(true)
                                                .build();
                            } else {

                                // Cover Type did not change back to a Previous Land Use before the end of its Land
                                // Conversion Period
                                log.trace("Cover type did not change back to a Previous Land Use before the end " +
                                        "of its Land Conversion Period");

                                // Classify as Previous Land Converted to Current Cover
                                log.trace("Classifying as Previous Land Converted to Current Cover");

                                return
                                        new LandUseHistoryBuilder()
                                                .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                                .year(currentCoverTypeHistoricDetail.getYear())
                                                .landUseCategory(
                                                        configurationDataProvider
                                                                .getLandUseCategory(
                                                                        previousLandUseHistoricDetail
                                                                                .getLandUseCategory().getCoverTypeId(),
                                                                        currentCoverTypeHistoricDetail
                                                                                .getCoverType().getId(),
                                                                        LandUseChangeAction.CONVERSION))
                                                .confirmed(true)
                                                .build();
                            }

                        }
                    }

                }

            } else {

                // The Cover Type has not changed since the Initial Timestep
                log.trace("The Cover Type has not changed since the Initial Timestep");

                // Classify the Land Use as Land Remaining Land of the initial Cover Type
                log.trace("Classifying the Land Use as Land Remaining Land of the initial Cover Type");

                return
                        new LandUseHistoryBuilder()
                                .itemNumber(currentCoverTypeHistoricDetail.getItemNumber())
                                .year(currentCoverTypeHistoricDetail.getYear())
                                .landUseCategory(
                                        configurationDataProvider
                                                .getLandUseCategory(
                                                        currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                        currentCoverTypeHistoricDetail.getCoverType().getId(),
                                                        LandUseChangeAction.REMAINING))
                                .confirmed(true)
                                .build();


            }

        }


    }


    private ConversionAndRemainingPeriod getConversionAndRemainingPeriod(
            LandUseHistoricDetail previousLandUseHistoricDetail,
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail) {

        return configurationDataProvider.getConversionAndRemainingPeriod(
                previousLandUseHistoricDetail.getLandUseCategory().getCoverTypeId(),
                currentCoverTypeHistoricDetail.getCoverType().getId());
    }



    public boolean validate(
            Long timestep,
            List<CoverTypeHistoricDetail> coverTypeHistories,
            List<LandUseHistoricDetail> landUseHistories) throws ServerException {


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

        if (landUseHistories.isEmpty()) {
            throw new ServerException("Historic Land Uses list is empty");
        }

        if (timestep.equals(0L)) {

            Optional<CoverTypeHistoricDetail> found = Optional.empty();
            for (CoverTypeHistoricDetail c : coverTypeHistories) {
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

                Optional<CoverTypeHistoricDetail> found = Optional.empty();
                for (CoverTypeHistoricDetail c : coverTypeHistories) {
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

                Optional<LandUseHistoricDetail> found = Optional.empty();
                for (LandUseHistoricDetail l : landUseHistories) {
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
     * Looks for the Historic Detail of a Cover Type whose Timestep matches the provided Timestep
     *
     * @param timestep                  The Target Timestep
     * @param coverTypesHistoricDetails The Historic Cover Types Details
     * @return The Historic Detail of the Cover Type whose Timestep matches the provided Timestep
     */
    private CoverTypeHistoricDetail getCurrentCoverTypeHistoricDetail(
            Long timestep, List<CoverTypeHistoricDetail> coverTypesHistoricDetails) {

        return coverTypesHistoricDetails
                .stream()
                .filter(c -> c.getItemNumber().equals(timestep))
                .findFirst()
                .orElse(null);
    }


    /**
     * Looks for the last Cover Type Historic Detail
     *
     * @param coverTypesHistoricDetails The Historic Cover Types Details
     * @return The last Cover Type Historic Detail
     */
    private CoverTypeHistoricDetail getLastCoverTypeHistoricDetail(
            List<CoverTypeHistoricDetail> coverTypesHistoricDetails) {

        return coverTypesHistoricDetails
                .stream()
                .sorted()
                .reduce((first, second) -> second)
                .orElse(null);
    }


    /**
     * Looks at the Cover Type of the Current Timestep and the Cover Types of the previous Land Uses
     * to establish the point at which the Land Use was different i.e the Land Use Cover Type was different from the
     * Current Cover Type.
     *
     * @param currentCoverTypeHistoricDetail The Historic Detail of the Current Cover Type
     * @param landUseHistoricDetails         The Historic Details of the Previous Land Uses
     * @return The recent-most Historic Land Use Detail whose Cover Type is different from the Current Cover Type
     */
    private LandUseHistoricDetail getPreviousLandUseHistoricDetail(
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            List<LandUseHistoricDetail> landUseHistoricDetails) {

        return
                landUseHistoricDetails
                        .stream()
                        .filter(c ->
                                c.getItemNumber() < currentCoverTypeHistoricDetail.getItemNumber() &&
                                        !c.getLandUseCategory().getCoverTypeId()
                                                .equals(currentCoverTypeHistoricDetail.getCoverType().getId()))
                        .reduce((first, second) -> second)
                        .orElse(null);
    }


    /**
     * Looks at the Cover Type of the Current Timestep and the Cover Types of the succeeding Timesteps
     * to establish the point at which the Cover Type Changes i.e becomes different from the
     * Current Cover Type.
     *
     * @param currentCoverTypeHistoricDetail The Historic Detail of the Current Cover Type
     * @param coverTypeHistoricDetails       The Historic Details of all Cover Types
     * @return The next Historic Cover Type Detail
     */
    private CoverTypeHistoricDetail getNextCoverTypeHistoricDetail(
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            List<CoverTypeHistoricDetail> coverTypeHistoricDetails) {

        return
                coverTypeHistoricDetails
                        .stream()
                        .filter(c ->
                                c.getItemNumber() > currentCoverTypeHistoricDetail.getItemNumber() &&
                                        !c.getCoverType().getId().equals(
                                                currentCoverTypeHistoricDetail.getCoverType().getId()))
                        .reduce((first, second) -> first)
                        .orElse(null);
    }


    /**
     * Gets the number of years between the Previous Land Use and the Next Cover Type. Substitutes the Previous Land Use
     * Timestep with the Initial Timestep when null and the Next Cover Type Timestep with the Final Timestep when Null
     *
     * @param previousLandUseHistoricDetail The Historic Details of the Previous Land Use if available
     * @param nextCoverTypeHistoricDetail   The Historic Details of the Next Cover Type if available
     * @param finalCoverTypeHistoricDetail  The Historic Details of the Final Cover Type (to be used as a substitute
     *                                      if the Next Cover Type is not available)
     * @return The number of years in between
     */
    private long getYearsInBetween(
            LandUseHistoricDetail previousLandUseHistoricDetail,
            CoverTypeHistoricDetail nextCoverTypeHistoricDetail,
            CoverTypeHistoricDetail finalCoverTypeHistoricDetail) {

        long years;

        if (previousLandUseHistoricDetail != null &&
                nextCoverTypeHistoricDetail != null) {

            years =
                    LongStream.range(
                            previousLandUseHistoricDetail.getItemNumber() + 1,
                            nextCoverTypeHistoricDetail.getItemNumber()).count();

        } else if (previousLandUseHistoricDetail != null) {

            years =
                    LongStream.rangeClosed(
                            previousLandUseHistoricDetail.getItemNumber() + 1,
                            finalCoverTypeHistoricDetail.getItemNumber()).count();

        } else if (nextCoverTypeHistoricDetail != null) {

            years =
                    LongStream.range(
                            0L,
                            nextCoverTypeHistoricDetail.getItemNumber()).count();

        } else {

            years =
                    LongStream.rangeClosed(
                            0L,
                            finalCoverTypeHistoricDetail.getItemNumber()).count();
        }

        return years;
    }


    /**
     * Gets the number of years between the Historic Detail of the Current Cover Type and the Historic Detail of the
     * Final Cover Type
     *
     * @param currentCoverTypeHistoricDetail The Historic Details of the Current Cover Type
     * @param finalCoverTypeHistoricDetail   The Historic Details of the Final Cover Type
     * @return The number of years in between
     */
    private long getYearsInBetween(
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            CoverTypeHistoricDetail finalCoverTypeHistoricDetail) {

        long years = 0L;

        if (currentCoverTypeHistoricDetail != null &&
                finalCoverTypeHistoricDetail != null) {

            if (!currentCoverTypeHistoricDetail.getItemNumber()
                    .equals(finalCoverTypeHistoricDetail.getItemNumber())) {

                years =
                        LongStream.rangeClosed(
                                currentCoverTypeHistoricDetail.getItemNumber() + 1,
                                finalCoverTypeHistoricDetail.getItemNumber()).count();

            }

        }

        return years;
    }


    /**
     * Gets the number of years between the Previous Land Use and the Next Cover Type. Substitutes the Previous Land Use
     * Timestep with the Initial Timestep when null and the Next Cover Type Timestep with the Final Timestep when Null
     *
     * @param previousLandUseHistoricDetail The Historic Details of the Previous Land Use if available
     * @param currentCoverTypeHistoricDetail   The Historic Details of the Next Cover Type if available
     * @return The number of years in between
     */
    private long getYearsInBetween(
            LandUseHistoricDetail previousLandUseHistoricDetail,
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail) {

        long years = 0L;

        if (previousLandUseHistoricDetail != null &&
                currentCoverTypeHistoricDetail != null) {

            years =
                    LongStream.rangeClosed(
                            previousLandUseHistoricDetail.getItemNumber() + 1,
                            currentCoverTypeHistoricDetail.getItemNumber()).count();

        }

        return years;
    }


    /**
     * Checks if this is the Initial Timestep
     *
     * @param timestep The passed in Timestep
     * @return A boolean value indicating whether or the provided Timestep is the Initial Timestep
     */
    private boolean isInitialTimestep(Long timestep) {
        log.debug("Timestep = {}", timestep);
        return timestep.equals(0L);
    }


    /**
     * Checks if this is the Current Cover Type is Grassland
     *
     * @param currentCoverTypeHistoricDetail The Historic Details of the Current Cover Type
     * @param grasslandCoverType             Grassland Cover Type details bean
     * @return A boolean value indicating whether Current Cover Type is Grassland
     */
    private boolean isCurrentCoverTypeGrassland(
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            CoverType grasslandCoverType) {

        return currentCoverTypeHistoricDetail.getCoverType().getId().equals(grasslandCoverType.getId());
    }


    /**
     * Does the cover type become Cropland within the Cropland to grassland conversion period without becoming any
     * other cover type
     */

    private boolean doesTheCoverTypeBecomeCroplandWithinTheCroplandToGrasslandConversionPeriodWithoutBecomingAnyOtherCoverType(
            CoverTypeHistoricDetail currentGrasslandCoverTypeHistoricDetail,
            CoverType croplandCoverType,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod,
            List<CoverTypeHistoricDetail> coverTypeHistories) {

        // Check if there is a Cropland Cover Type in the timesteps that follow the current one
        log.trace("Checking if there is a Cropland Cover Type in the timesteps that follow the current one");
        CoverTypeHistoricDetail croplandCoverTypeHistoricDetail =
                coverTypeHistories
                        .stream()
                        .sorted()
                        .filter(c -> c.getItemNumber() > currentGrasslandCoverTypeHistoricDetail.getItemNumber() &&
                                c.getCoverType().getId().equals(
                                        croplandCoverType.getId()))
                        .findFirst()
                        .orElse(null);

        log.debug("Cropland Cover Type Historic detail = {}", croplandCoverTypeHistoricDetail);

        if (croplandCoverTypeHistoricDetail != null) {

            // Check if a non Grassland, non Cropland Cover Type occurs before the Cropland Cover Type
            log.trace("Checking if a non Grassland, non Cropland Cover Type occurs before the Cropland Cover Type");

            CoverTypeHistoricDetail nonGrasslandNonCroplandCoverTypeHistoricDetail =
                    coverTypeHistories
                            .stream()
                            .sorted()
                            .filter(c -> c.getItemNumber() > currentGrasslandCoverTypeHistoricDetail.getItemNumber() &&
                                    c.getItemNumber() < croplandCoverTypeHistoricDetail.getItemNumber() &&
                                    !c.getCoverType().getId().equals(croplandCoverType.getId()) &&
                                    !c.getCoverType().getId().equals(currentGrasslandCoverTypeHistoricDetail
                                            .getCoverType().getId()))
                            .findFirst()
                            .orElse(null);

            log.debug("Preceding Non Grassland, Non Cropland Cover Type Historic detail = {}",
                    croplandCoverTypeHistoricDetail);

            if (nonGrasslandNonCroplandCoverTypeHistoricDetail == null) {

                // Get the total number of years that elapsed before the Cover Type turned into Cropland
                log.trace("Getting the total number of years that elapsed before the Cover Type turned into Cropland");

                long years =
                        LongStream.rangeClosed(
                                currentGrasslandCoverTypeHistoricDetail.getItemNumber() + 1,
                                croplandCoverTypeHistoricDetail.getItemNumber()).count();

                log.debug("Total number of years that elapsed before the Cover Type turned into Cropland = {}", years);


                // Check if the number of years is not greater than the Cropland to Grassland Conversion Period
                log.trace("Checking if the number of years is not greater than the Cropland to Grassland Conversion Period");
                return !(years > conversionAndRemainingPeriod.getConversionPeriod());

            }

        }


        return false;
    }


    /**
     * Looks at the Current Cover Type and the Previous Land Use Class to establish whether the
     * Land Use has changed since the Initial Timestep
     *
     * @param previousLandUseHistoricDetail The Historic Details of the Previous Land Use
     * @return A boolean value indicating whether or not the Cover Type has changed
     */
    private boolean hasCoverTypeChanged(
            LandUseHistoricDetail previousLandUseHistoricDetail) {

        // Check whether the Cover Type changed by checking whether the Previous Land Use is not null
        log.trace("Checking whether the Cover Type changed by checking whether the Previous Land Use is not null");
        return previousLandUseHistoricDetail != null;
    }


    /**
     * Establishes whether the current Cover Type remained the same for longer than the Land Conversion Period of the
     * previous Land Use
     *
     * @param previousLandUseHistoricDetail The Historic Details of the Previous Land Use
     * @param nextCoverTypeHistoricDetail   The Historic Details of the Next Cover Type
     * @param lastCoverTypeHistoricDetail   The Historic Details of the Last Cover Type
     * @param conversionAndRemainingPeriod  The Conversion and Remaining Period from the Previous Land Use to the new
     *                                      Land Use as specified by the policies
     * @return A boolean value indicating whether or not the Cover Type has remained the same for longer than the
     * Land Conversion Period of the previous Land Use
     */
    private boolean hasCoverTypeRemainedTheSameForLongerThanTheLandConversionPeriodOfThePreviousLandUse(
            LandUseHistoricDetail previousLandUseHistoricDetail,
            CoverTypeHistoricDetail nextCoverTypeHistoricDetail,
            CoverTypeHistoricDetail lastCoverTypeHistoricDetail,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        // Get the total number of successive years that the Current Cover Type has stayed as the Current Cover Type
        log.trace("Getting the total number of successive years that the Current Cover Type has stayed as the " +
                "Current Cover Type");

        long years = getYearsInBetween(
                previousLandUseHistoricDetail,
                nextCoverTypeHistoricDetail,
                lastCoverTypeHistoricDetail);

        log.debug("Total number of successive years under the Current Cover Type = {}", years);

        // Check whether the number of successive of the current Cover Type is greater than the Land Conversion
        // Period of the previous Land Use
        log.trace("Checking whether the number of successive of the current Cover Type is greater than the " +
                "Land Conversion Period of the previous Land Use");
        return years > conversionAndRemainingPeriod.getConversionPeriod();
    }


    /**
     * Establishes whether the current Cover Type remained the same for longer than the Land Conversion Period of the
     * previous Land Use
     *
     * @param currentCoverTypeHistoricDetail The Historic Details of the Current Cover Type
     * @param lastCoverTypeHistoricDetail    The Historic Details of the Last Cover Type
     * @param conversionAndRemainingPeriod   The Conversion and Remaining Period from the Previous Land Use to the new
     *                                       Land Use as specified by the policies
     * @return A boolean value indicating whether or not the Cover Type has remained the same for longer than the
     * Land Conversion Period of the previous Land Use
     */

    // TODO Confirm with Rob about the LTE
    private boolean isLengthOfTimeToTheEndOfTheSimulationLessThanTheLandConversionPeriodOfThePreviousLandUse(
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            CoverTypeHistoricDetail lastCoverTypeHistoricDetail,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        // Get the number of years between the Historic Detail of the Current Cover Type and the Historic Detail of the
        // Final Cover Type
        log.trace("Getting the number of years between the Historic Detail of the Current Cover Type and the " +
                "Historic Detail of the Final Cover Type");

        long years = getYearsInBetween(currentCoverTypeHistoricDetail, lastCoverTypeHistoricDetail); 

        log.debug("Total number of successive years between the Current & Final Cover Types = {}", years);

        // Check whether the number of years is less than the Land Conversion Period of the previous Land Use
        log.trace("Checking whether the number of years is less than the Land Conversion Period of the previous " +
                "Land Use");
        return years <= conversionAndRemainingPeriod.getConversionPeriod();
    }


    /**
     * Establishes whether the current Cover Type has remained the same for longer the Land Remaining Period
     *
     * @param previousLandUseHistoricDetail  The Historic Details of the Previous Land Use
     * @param currentCoverTypeHistoricDetail The Historic Details of the Current Cover Type
     * @param conversionAndRemainingPeriod   The Conversion and Remaining Period from the Previous Land Use to the new
     *                                       Land Use as specified by the policies
     * @return A boolean value indicating whether or not the Cover Type has remained the same for longer than the
     * Land Remaining Period
     */
    private boolean hasCoverTypeRemainedTheSameForLongerThanTheLandRemainingPeriod(
            LandUseHistoricDetail previousLandUseHistoricDetail,
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        // Establish the total number of years that the Cover Type has remained as the Current Cover Type
        log.trace("Getting the total number of years that the Cover Type has remained as the Current Cover Type");

        long years = getYearsInBetween(previousLandUseHistoricDetail, currentCoverTypeHistoricDetail);

        log.debug("Total number of successive years under the Current Cover Type = {}", years);

        // Check whether the number of successive of the current Cover Type is greater than the Land Remaining 
        // Period
        log.trace("Checking whether the number of successive of the current Cover Type is greater than the " +
                "Land Remaining Period");

        return years > conversionAndRemainingPeriod.getRemainingPeriod();
    }


    /**
     * Establishes whether a passed in collection of Cover Types Ids includes only Cropland and Grassland Cover Types
     *
     * @param targetCoverTypesIds The collection of Cover Types Ids to check
     * @return A boolean value indicating whether or not whether the passed in collection of Cover Types Ids
     * includes only Cropland and Grassland Cover Types
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
                                            !id.equals(croplandCoverType.getId()) || 
                                                    !id.equals(grasslandCoverType.getId()));
                    
                    log.debug("Target Cover Types exclude all other Cover Types = {}", excludesAllOtherCoverTypes);

                    return excludesAllOtherCoverTypes;

                }

            }


        return false;
    }


    /**
     * Establishes whether a passed in collection of Previous, Current and Next Cover Types Ids includes only 
     * Cropland and Grassland Cover Types
     *
     * @param targetCoverTypesIds The collection of Cover Types Ids to check
     * @return A boolean value indicating whether or not whether the passed in collection of Cover Types Ids
     * includes only Cropland and Grassland Cover Types
     */
    private boolean doNeighbouringCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(
            List<Long> targetCoverTypesIds) {


        // Check if the target Cover Types Ids includes 3 elements: The Previous, Current & Next Cover Type Ids
        log.trace("Check if the target Cover Types Ids includes 3 elements: The Previous, Current & Next Cover Type Ids");
        
        boolean includes3Elements = targetCoverTypesIds.size() == 3;
        
        log.debug("Target Cover Types Ids includes 3 elements = {}", includes3Elements);

        if (includes3Elements) {

            return doCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(targetCoverTypesIds);
        }

        return false;
    }

    /**
     * Establishes whether previous Land Uses includes only Cropland and Grassland Cover Types
     *
     * @param currentCoverTypeHistoricDetail  The Historic Details of the Current Cover Type
     * @param landUsesHistoricDetails The Historic Details of the Previous Land Uses
     * @return A boolean value indicating whether or not whether the passed in collection of Cover Types Ids
     * includes only Cropland and Grassland Cover Types
     */
    private boolean doPreviousCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElseSinceInitialTimestep(
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            List<LandUseHistoricDetail> landUsesHistoricDetails) {


        return doCoverTypesIncludeBothCroplandAndGrasslandAndExcludeEverythingElse(
                landUsesHistoricDetails
                        .stream()
                        .map(LandUseHistoricDetail::getItemNumber)
                        .filter(itemNumber -> itemNumber < currentCoverTypeHistoricDetail.getItemNumber())
                        .collect(Collectors.toList()));

    }


    /**
     * Establishes whether the Current Cover Type reverts back to a previous Land Use before the end of its 
     * Conversion Period
     *
     * @param previousNonCurrentCoverTypesIds   The ids of previous Cover Types that differ from the Current Cover Type
     * @param currentCoverTypeHistoricDetail    The Historic Details of the Current Cover Type     *
     * @param nextNonCurrentCoverTypesHistories The ids of future Cover Types that differ from the Current Cover Type
     * @param conversionAndRemainingPeriod      The Conversion and Remaining Period from the Previous Land Use to the new
     *                                          Land Use as specified by the policies
     * @return A boolean value indicating whether whether a Cover Type reverts back to a previous Land Use before
     * the end of its Conversion Period
     */
    
    // TODO Confirm with Rob about the LTE
    private boolean doesTheCoverTypeChangeBackToAPreviousLandUseBeforeTheEndOfItsLandConversionPeriod(
            Set<Long> previousNonCurrentCoverTypesIds,
            CoverTypeHistoricDetail currentCoverTypeHistoricDetail,
            Set<CoverTypeHistoricDetail> nextNonCurrentCoverTypesHistories,
            ConversionAndRemainingPeriod conversionAndRemainingPeriod) {

        return nextNonCurrentCoverTypesHistories
                .stream()
                .anyMatch(c ->
                        previousNonCurrentCoverTypesIds.contains(c.getCoverType().getId()) &&
                                getYearsInBetween(currentCoverTypeHistoricDetail, c) 
                                        <=  conversionAndRemainingPeriod.getConversionPeriod());

    }
    
    @Override
    public String toString() {
        return "Decision Tree";
    }

}

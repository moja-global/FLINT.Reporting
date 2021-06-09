package global.moja.dataprocessing.util;

import global.moja.dataprocessing.daos.LocationCoverTypesHistory;
import global.moja.dataprocessing.daos.LocationLandUsesHistory;
import global.moja.dataprocessing.models.CoverType;
import global.moja.dataprocessing.models.LandUseCategory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class GrassLandUsesCategoriesAllocatorIT {

    @Autowired
    LandUsesCategoriesAllocator landUsesCategoriesAllocator;


    /**
     * Decision 5
     * Classify as Cropland Remaining Cropland
     * Within is here assumed to mean less or equal to
     *
      *
     */
    @Test
    public void Given_InitialTimestepAndGrassCoversFollowedByCropCoversWithinTheCroplandToGrasslandConversionPeriod_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned()  {

        Long timestep = 0L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 9,
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);

        List<LocationLandUsesHistory> landUseHistories = new ArrayList<>();

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(0L)
                .year(1990)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(10L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(9L)
                                .coverTypeId(2L)
                                .name("Cropland Remaining Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }


    /**
     * Decision 6
     * Classify as land remaining land of the initial cover type
     *
      *
     */
    @Test
    public void Given_InitialTimestepAndGrassCoversFollowedByCropCoversOutsideTheCroplandToGrasslandConversionPeriod_When_GetLandUse_Then_GrasslandRemainingGrasslandWillBeReturned()  {

        Long timestep = 0L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 10,
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);

        List<LocationLandUsesHistory> landUseHistories = new ArrayList<>();

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(0L)
                .year(1990)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(18L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(17L)
                                .coverTypeId(3L)
                                .name("Grassland Remaining Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }


    /**
     * Decision 8
     * Classify as land remaining land of the initial land use type
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndGrassCoversFromInitialTimestep_When_GetLandUse_Then_GrasslandRemainingGrasslandWillBeReturned()  {

        Long timestep = 1L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(18L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(17L)
                        .coverTypeId(3L)
                        .name("Grassland Remaining Grassland")
                        .version(1)
                        .build(), 1);


        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(18L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(17L)
                                .coverTypeId(3L)
                                .name("Grassland Remaining Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    /**
     * Decision 11
     * Classify as land remaining land of the current cover type
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassCoversForLongerThanPreviousLandConversionPeriodAndLongerThanGrasslandRemainingPeriod_When_GetLandUse_Then_GrasslandRemainingGrasslandWillBeReturned()  {

        Long timestep = 21L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 21);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 20);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(21L)
                .year(2011)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(18L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(17L)
                                .coverTypeId(3L)
                                .name("Grassland Remaining Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    /**
     * Decision 12
     * Classify as land converted to land of the current cover type
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversForLongerThanPreviousLandConversionPeriodAndShorterThanGrasslandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToGrasslandWillBeReturned()  {

        Long timestep = 4L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 4);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 3);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(4L)
                .year(1994)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(21L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(19L)
                                .coverTypeId(3L)
                                .name("Forest land Converted To Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }


    /**
     * Decision 16
     * Classify as Unconfirmed land (of the previous land use) converted to Cropland
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassAndCropCoversForLessThanPreviousLandConversionPeriodAndTimeToEndOfSeriesLessThanPreviousLandConversionPeriod_When_GetLandUse_Then_UnconfirmedPreviousLandConvertedToCroplandWillBeReturned()  {

        Long timestep = 2L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2,
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 1);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 1);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(2L)
                .year(1992)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(12L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(11L)
                                .coverTypeId(2L)
                                .name("Forest land Converted To Cropland")
                                .version(1)
                                .build())
                .confirmed(false)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }


    // TODO Confirm with rdl

    /**
     * Decision 16
     * Classify as Previous Land Use
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndCropCoverFollowedByGrassAndCropCoversForLessThanPreviousLandConversionPeriodAndTimeToEndOfSeriesLessThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandUseWillBeReturned()  {

        Long timestep = 1L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2,
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);


        List<LocationLandUsesHistory> landUseHistories = Collections.singletonList(
                LocationLandUsesHistory.builder()
                        .itemNumber(0L)
                        .year(1990)
                        .landUseCategory(
                                LandUseCategory.builder()
                                        .id(10L)
                                        .reportingFrameworkId(1L)
                                        .parentLandUseCategoryId(9L)
                                        .coverTypeId(2L)
                                        .name("Cropland Remaining Cropland")
                                        .version(1)
                                        .build())
                        .confirmed(true)
                        .build()
        );

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(10L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(9L)
                                .coverTypeId(2L)
                                .name("Cropland Remaining Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    /**
     * Decision 18
     * Classify as Cropland remaining Cropland
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassAndCropCoversForMoreThanPreviousLandConversionPeriodAndMoreThanLandRemainingPeriod_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned()  {

        Long timestep = 2L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);

        coverTypeHistories.addAll(getInterchangingCoverTypeHistories(
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),
                22,
                2,
                1992,
                3
        ));


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(12L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(11L)
                        .coverTypeId(2L)
                        .name("Forest land Converted To Cropland")
                        .version(1)
                        .build(), 1);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(2L)
                .year(1992)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(10L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(9L)
                                .coverTypeId(2L)
                                .name("Cropland Remaining Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    /**
     * Decision 19
     * Classify as land (previous land use) converted to Cropland
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassAndCropCoversForMoreThanPreviousLandConversionPeriodAndLessThanLandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToCroplandWillBeReturned()  {

        Long timestep = 2L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);

        coverTypeHistories.addAll(getInterchangingCoverTypeHistories(
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),

                5,
                2,
                1992,
                3
        ));


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 1);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(2L)
                .year(1992)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(12L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(11L)
                                .coverTypeId(2L)
                                .name("Forest land Converted To Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }


    // TODO Confirm with rdl
    /**
     * Decision 19
     * Classify as land (previous land use) converted to Cropland
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndCropCoverFollowedByGrassAndCropCoversForMoreThanPreviousLandConversionPeriodAndLessThanLandRemainingPeriod_When_GetLandUse_Then_PreviousLandUseWillBeReturned()  {

        Long timestep = 3L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);

        coverTypeHistories.addAll(getInterchangingCoverTypeHistories(
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),

                12,
                2,
                1992,
                3
        ));


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(12L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(11L)
                        .coverTypeId(2L)
                        .name("Forest land Converted To Cropland")
                        .version(1)
                        .build(), 2);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(3L)
                .year(1993)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(12L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(11L)
                                .coverTypeId(2L)
                                .name("Forest land Converted To Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    /**
     * Decision 21
     * Classify as Unconfirmed land (previous land use) converted to land (current cover type)
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversForShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationShorterThanPreviousLandConversionPeriod_When_GetLandUse_Then_UnconfirmedPreviousLandConvertedToGrasslandWillBeReturned()  {

        Long timestep = 1L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);


        List<LocationLandUsesHistory> landUseHistories = Collections.singletonList(
                LocationLandUsesHistory.builder()
                        .itemNumber(0L)
                        .year(1990)
                        .landUseCategory(
                                LandUseCategory.builder()
                                        .id(2L)
                                        .reportingFrameworkId(1L)
                                        .parentLandUseCategoryId(1L)
                                        .coverTypeId(1L)
                                        .name("Forest land Remaining Forest land")
                                        .version(1)
                                        .build())
                        .confirmed(true)
                        .build()
        );

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(21L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(19L)
                                .coverTypeId(3L)
                                .name("Forest land Converted To Grassland")
                                .version(1)
                                .build())
                .confirmed(false)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    /**
     * Decision 23
     * Classify as land remaining land (of the previous land use)
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversFollowedByNonGrasslandCoversForAPeriodShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationLongerThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandRemainingPreviousLandWillBeReturned()  {

        Long timestep = 1L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2,
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 2);


        List<LocationLandUsesHistory> landUseHistories = new ArrayList<>();
        landUseHistories.add(LocationLandUsesHistory.builder()
                .itemNumber(0L)
                .year(1990)
                .landUseCategory(LandUseCategory.builder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build())
                .build());

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(2L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(1L)
                                .coverTypeId(1L)
                                .name("Forest land Remaining Forest land")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    /**
     * Decision 24
     * Classify as land (previous land use) converted to land (current cover type)
     *
      *
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversForAPeriodShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationLongerThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandConvertedToGrasslandWillBeReturned()  {

        Long timestep = 1L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 3,
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 1);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(12L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(11L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 1);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(21L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(19L)
                                .coverTypeId(3L)
                                .name("Forest land Converted To Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);


    }

    private List<LocationCoverTypesHistory> getCoverTypeHistories(
            CoverType coverType, int coverTypesCount) {

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        List<LocationCoverTypesHistory> coverTypeHistories = new ArrayList<>();

        // Add First Cover Type
        IntStream
                .range(0, coverTypesCount)
                .mapToObj(i ->
                        LocationCoverTypesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(coverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;
    }

    private List<LocationCoverTypesHistory> getCoverTypeHistories(
            CoverType firstCoverType, CoverType secondCoverType, int secondCoverTypesCount) {

        List<LocationCoverTypesHistory> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        LocationCoverTypesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(firstCoverType)
                                .build())
                .forEach(coverTypeHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondCoverTypesCount)
                .mapToObj(i ->
                        LocationCoverTypesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(secondCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;

    }


    private List<LocationCoverTypesHistory> getCoverTypeHistories(
            CoverType firstCoverType,
            CoverType secondCoverType, int secondCoverTypesCount,
            CoverType thirdCoverType, int thirdCoverTypesCount) {

        List<LocationCoverTypesHistory> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        LocationCoverTypesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(firstCoverType)
                                .build())
                .forEach(coverTypeHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondCoverTypesCount)
                .mapToObj(i ->
                        LocationCoverTypesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(secondCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        // Add Third Cover Type
        IntStream
                .range(0, thirdCoverTypesCount)
                .mapToObj(i ->
                        LocationCoverTypesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(thirdCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;

    }

    private List<LocationCoverTypesHistory> getInterchangingCoverTypeHistories(
            CoverType firstCoverType, CoverType secondCoverType, int yearCount, long previousStep, int previousYear, int idx) {

        List<LocationCoverTypesHistory> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(previousStep);
        AtomicInteger year = new AtomicInteger(previousYear);

        int index = idx;
        boolean status = true;

        do {
            if (status) {
                coverTypeHistories.add(LocationCoverTypesHistory.builder()
                        .itemNumber(step.incrementAndGet())
                        .year(year.incrementAndGet())
                        .coverType(firstCoverType)
                        .build());
            } else {
                coverTypeHistories.add(LocationCoverTypesHistory.builder()
                        .itemNumber(step.incrementAndGet())
                        .year(year.incrementAndGet())
                        .coverType(secondCoverType)
                        .build());
            }

            index += 1;
            status = !status;

        } while (index <= yearCount);

        return coverTypeHistories;

    }


    private List<LocationLandUsesHistory> getLandUseHistories(
            LandUseCategory landUse, int landUsesCount) {

        List<LocationLandUsesHistory> landUseHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, landUsesCount)
                .mapToObj(i ->
                        LocationLandUsesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(landUse)
                                .confirmed(true)
                                .build())
                .forEach(landUseHistories::add);

        return landUseHistories;

    }


    private List<LocationLandUsesHistory> getLandUseHistories(
            LandUseCategory firstLandUse, LandUseCategory secondLandUse, int secondLandUsesCount) {

        List<LocationLandUsesHistory> landUseHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        LocationLandUsesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(firstLandUse)
                                .build())
                .forEach(landUseHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondLandUsesCount)
                .mapToObj(i ->
                        LocationLandUsesHistory.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(secondLandUse)
                                .confirmed(true)
                                .build())
                .forEach(landUseHistories::add);

        return landUseHistories;

    }

}
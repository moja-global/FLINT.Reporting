package global.moja.businessintelligence.services;

import global.moja.businessintelligence.daos.CoverTypesHistoricDetail;
import global.moja.businessintelligence.daos.LandUsesHistoricDetail;
import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.models.CoverType;
import global.moja.businessintelligence.models.LandUseCategory;
import global.moja.businessintelligence.util.LandUseCategoryAllocationDecisionTree;
import global.moja.businessintelligence.util.builders.CoverTypeBuilder;
import global.moja.businessintelligence.util.builders.LandUseCategoryBuilder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class GrasslandLandUsesDecisionsIT {

    @Autowired
    LandUseCategoryAllocationDecisionTree landUseCategoryAllocationDecisionTree;


    /**
     * Decision 5
     * Classify as Cropland Remaining Cropland
     * Within is here assumed to mean less or equal to
     *
     * @throws ServerException
     */
    @Test
    public void Given_InitialTimestepAndGrassCoversFollowedByCropCoversWithinTheCroplandToGrasslandConversionPeriod_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned() throws ServerException {

        Long timestep = 0L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 9,
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);

        List<LandUsesHistoricDetail> landUseHistories = new ArrayList<>();

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(0L)
                .year(1990)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(10L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(9L)
                                .coverTypeId(2L)
                                .name("Cropland Remaining Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }


    /**
     * Decision 6
     * Classify as land remaining land of the initial cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_InitialTimestepAndGrassCoversFollowedByCropCoversOutsideTheCroplandToGrasslandConversionPeriod_When_GetLandUse_Then_GrasslandRemainingGrasslandWillBeReturned() throws ServerException {

        Long timestep = 0L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 10,
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);

        List<LandUsesHistoricDetail> landUseHistories = new ArrayList<>();

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(0L)
                .year(1990)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(18L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(17L)
                                .coverTypeId(3L)
                                .name("Grassland Remaining Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }


    /**
     * Decision 8
     * Classify as land remaining land of the initial land use type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndGrassCoversFromInitialTimestep_When_GetLandUse_Then_GrasslandRemainingGrasslandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(18L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(17L)
                        .coverTypeId(3L)
                        .name("Grassland Remaining Grassland")
                        .version(1)
                        .build(), 1);


        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(18L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(17L)
                                .coverTypeId(3L)
                                .name("Grassland Remaining Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    /**
     * Decision 11
     * Classify as land remaining land of the current cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassCoversForLongerThanPreviousLandConversionPeriodAndLongerThanGrasslandRemainingPeriod_When_GetLandUse_Then_GrasslandRemainingGrasslandWillBeReturned() throws ServerException {

        Long timestep = 21L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 21);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 20);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(21L)
                .year(2011)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(18L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(17L)
                                .coverTypeId(3L)
                                .name("Grassland Remaining Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    /**
     * Decision 12
     * Classify as land converted to land of the current cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversForLongerThanPreviousLandConversionPeriodAndShorterThanGrasslandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToGrasslandWillBeReturned() throws ServerException {

        Long timestep = 4L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 4);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 3);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(4L)
                .year(1994)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(21L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(19L)
                                .coverTypeId(3L)
                                .name("Forest land Converted To Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }


    /**
     * Decision 16
     * Classify as Unconfirmed land (of the previous land use) converted to Cropland
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassAndCropCoversForLessThanPreviousLandConversionPeriodAndTimeToEndOfSeriesLessThanPreviousLandConversionPeriod_When_GetLandUse_Then_UnconfirmedPreviousLandConvertedToCroplandWillBeReturned() throws ServerException {

        Long timestep = 2L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2,
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 1);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 1);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(2L)
                .year(1992)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(12L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(11L)
                                .coverTypeId(2L)
                                .name("Forest land Converted To Cropland")
                                .version(1)
                                .build())
                .confirmed(false)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }


    // TODO Confirm with rdl

    /**
     * Decision 16
     * Classify as Previous Land Use
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndCropCoverFollowedByGrassAndCropCoversForLessThanPreviousLandConversionPeriodAndTimeToEndOfSeriesLessThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandUseWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2,
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);


        List<LandUsesHistoricDetail> landUseHistories = Arrays.asList(
                LandUsesHistoricDetail.builder()
                        .itemNumber(0L)
                        .year(1990)
                        .landUseCategory(
                                new LandUseCategoryBuilder()
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

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(10L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(9L)
                                .coverTypeId(2L)
                                .name("Cropland Remaining Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    /**
     * Decision 18
     * Classify as Cropland remaining Cropland
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassAndCropCoversForMoreThanPreviousLandConversionPeriodAndMoreThanLandRemainingPeriod_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned() throws ServerException {

        Long timestep = 2L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);

        coverTypeHistories.addAll(getInterchangingCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
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


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(12L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(11L)
                        .coverTypeId(2L)
                        .name("Forest land Converted To Cropland")
                        .version(1)
                        .build(), 1);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(2L)
                .year(1992)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(10L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(9L)
                                .coverTypeId(2L)
                                .name("Cropland Remaining Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    /**
     * Decision 19
     * Classify as land (previous land use) converted to Cropland
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonGrassCoverFollowedByGrassAndCropCoversForMoreThanPreviousLandConversionPeriodAndLessThanLandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToCroplandWillBeReturned() throws ServerException {

        Long timestep = 2L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);

        coverTypeHistories.addAll(getInterchangingCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
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


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(21L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(19L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 1);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(2L)
                .year(1992)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(12L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(11L)
                                .coverTypeId(2L)
                                .name("Forest land Converted To Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }


    // TODO Confirm with rdl
    /**
     * Decision 19
     * Classify as land (previous land use) converted to Cropland
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndCropCoverFollowedByGrassAndCropCoversForMoreThanPreviousLandConversionPeriodAndLessThanLandRemainingPeriod_When_GetLandUse_Then_PreviousLandUseWillBeReturned() throws ServerException {

        Long timestep = 3L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);

        coverTypeHistories.addAll(getInterchangingCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
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


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(12L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(11L)
                        .coverTypeId(2L)
                        .name("Forest land Converted To Cropland")
                        .version(1)
                        .build(), 2);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(3L)
                .year(1993)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(12L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(11L)
                                .coverTypeId(2L)
                                .name("Forest land Converted To Cropland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    /**
     * Decision 21
     * Classify as Unconfirmed land (previous land use) converted to land (current cover type)
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversForShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationShorterThanPreviousLandConversionPeriod_When_GetLandUse_Then_UnconfirmedPreviousLandConvertedToGrasslandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2);


        List<LandUsesHistoricDetail> landUseHistories = Arrays.asList(
                LandUsesHistoricDetail.builder()
                        .itemNumber(0L)
                        .year(1990)
                        .landUseCategory(
                                new LandUseCategoryBuilder()
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

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(21L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(19L)
                                .coverTypeId(3L)
                                .name("Forest land Converted To Grassland")
                                .version(1)
                                .build())
                .confirmed(false)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    /**
     * Decision 23
     * Classify as land remaining land (of the previous land use)
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversFollowedByNonGrasslandCoversForAPeriodShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationLongerThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandRemainingPreviousLandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 2,
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 2);


        List<LandUsesHistoricDetail> landUseHistories = new ArrayList<>();
        landUseHistories.add(LandUsesHistoricDetail.builder()
                .itemNumber(0L)
                .year(1990)
                .landUseCategory(new LandUseCategoryBuilder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build())
                .build());

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(2L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(1L)
                                .coverTypeId(1L)
                                .name("Forest land Remaining Forest land")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    /**
     * Decision 24
     * Classify as land (previous land use) converted to land (current cover type)
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndANonGrassCoverFollowedByGrassCoversForAPeriodShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationLongerThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandConvertedToGrasslandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 3,
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 1);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(3L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(12L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(11L)
                        .coverTypeId(3L)
                        .name("Forest land Converted To Grassland")
                        .version(1)
                        .build(), 1);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(1L)
                .year(1991)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(21L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(19L)
                                .coverTypeId(3L)
                                .name("Forest land Converted To Grassland")
                                .version(1)
                                .build())
                .confirmed(true)
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);


    }

    private List<CoverTypesHistoricDetail> getCoverTypeHistories(
            CoverType coverType, int coverTypesCount) {

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        List<CoverTypesHistoricDetail> coverTypeHistories = new ArrayList<>();

        // Add First Cover Type
        IntStream
                .range(0, coverTypesCount)
                .mapToObj(i ->
                        CoverTypesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(coverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;
    }

    private List<CoverTypesHistoricDetail> getCoverTypeHistories(
            CoverType firstCoverType, CoverType secondCoverType, int secondCoverTypesCount) {

        List<CoverTypesHistoricDetail> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        CoverTypesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(firstCoverType)
                                .build())
                .forEach(coverTypeHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondCoverTypesCount)
                .mapToObj(i ->
                        CoverTypesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(secondCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;

    }


    private List<CoverTypesHistoricDetail> getCoverTypeHistories(
            CoverType firstCoverType,
            CoverType secondCoverType, int secondCoverTypesCount,
            CoverType thirdCoverType, int thirdCoverTypesCount) {

        List<CoverTypesHistoricDetail> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        CoverTypesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(firstCoverType)
                                .build())
                .forEach(coverTypeHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondCoverTypesCount)
                .mapToObj(i ->
                        CoverTypesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(secondCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        // Add Third Cover Type
        IntStream
                .range(0, thirdCoverTypesCount)
                .mapToObj(i ->
                        CoverTypesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(thirdCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;

    }

    private List<CoverTypesHistoricDetail> getInterchangingCoverTypeHistories(
            CoverType firstCoverType, CoverType secondCoverType, int yearCount, long previousStep, int previousYear, int idx) {

        List<CoverTypesHistoricDetail> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(previousStep);
        AtomicInteger year = new AtomicInteger(previousYear);

        int index = idx;
        boolean status = true;

        do {
            if (status) {
                coverTypeHistories.add(CoverTypesHistoricDetail.builder()
                        .itemNumber(step.incrementAndGet())
                        .year(year.incrementAndGet())
                        .coverType(firstCoverType)
                        .build());
            } else {
                coverTypeHistories.add(CoverTypesHistoricDetail.builder()
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


    private List<LandUsesHistoricDetail> getLandUseHistories(
            LandUseCategory landUse, int landUsesCount) {

        List<LandUsesHistoricDetail> landUseHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, landUsesCount)
                .mapToObj(i ->
                        LandUsesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(landUse)
                                .confirmed(true)
                                .build())
                .forEach(landUseHistories::add);

        return landUseHistories;

    }


    private List<LandUsesHistoricDetail> getLandUseHistories(
            LandUseCategory firstLandUse, LandUseCategory secondLandUse, int secondLandUsesCount) {

        List<LandUsesHistoricDetail> landUseHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        LandUsesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(firstLandUse)
                                .build())
                .forEach(landUseHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondLandUsesCount)
                .mapToObj(i ->
                        LandUsesHistoricDetail.builder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(secondLandUse)
                                .confirmed(true)
                                .build())
                .forEach(landUseHistories::add);

        return landUseHistories;

    }

}
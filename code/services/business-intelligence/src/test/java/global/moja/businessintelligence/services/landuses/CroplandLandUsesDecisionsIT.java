package global.moja.businessintelligence.services.landuses;

import global.moja.businessintelligence.daos.CoverTypeHistoricDetail;
import global.moja.businessintelligence.daos.LandUseHistoricDetail;
import global.moja.businessintelligence.exceptions.ServerException;
import global.moja.businessintelligence.models.CoverType;
import global.moja.businessintelligence.models.LandUseCategory;
import global.moja.businessintelligence.util.builders.CoverTypeBuilder;
import global.moja.businessintelligence.util.builders.CoverTypeHistoryBuilder;
import global.moja.businessintelligence.util.builders.LandUseCategoryBuilder;
import global.moja.businessintelligence.util.builders.LandUseHistoryBuilder;
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
class CroplandLandUsesDecisionsIT {

    @Autowired
    LandUsesProcessor landUsesProcessor;

    /**
     * Decision 3
     * Classify as land remaining land of the initial cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_InitialTimestepAndCropCover_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned() throws ServerException {

        Long timestep = 0L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 1);

        List<LandUseHistoricDetail> landUseHistories = new ArrayList<>();

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 8
     * Classify as land remaining land of the initial land use type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndCropCoversFromInitialTimestep_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(), 2);


        List<LandUseHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(10L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(9L)
                        .coverTypeId(2L)
                        .name("Cropland Remaining Cropland")
                        .version(1)
                        .build(), 1);


        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 11
     * Classify as land remaining land of the current cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonCropCoverFollowedByCropCoversForLongerThanPreviousLandConversionPeriodAndLongerThanCroplandRemainingPeriod_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned() throws ServerException {

        Long timestep = 21L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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
                        .build(), 21);


        List<LandUseHistoricDetail> landUseHistories = getLandUseHistories(
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
                        .build(), 20);

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
                .itemNumber(21L)
                .year(2011)
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 12
     * Classify as land converted to land of the current cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonCropCoverFollowedByCropCoversForLongerThanPreviousLandConversionPeriodAndShorterThanCroplandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToCroplandWillBeReturned() throws ServerException {

        Long timestep = 4L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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
                        .build(), 4);


        List<LandUseHistoricDetail> landUseHistories = getLandUseHistories(
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
                        .build(), 3);

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
                .itemNumber(4L)
                .year(1994)
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 16
     * Classify as Unconfirmed land (of the previous land use) converted to Cropland
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonCropCoverFollowedByCropAndGrassCoversForLessThanPreviousLandConversionPeriodAndTimeToEndOfSeriesLessThanPreviousLandConversionPeriod_When_GetLandUse_Then_UnconfirmedPreviousLandConvertedToCroplandWillBeReturned() throws ServerException {

        Long timestep = 2L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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
                        .build(), 2,
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 1);


        List<LandUseHistoricDetail> landUseHistories = getLandUseHistories(
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

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 18
     * Classify as Cropland remaining Cropland
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonCropCoverFollowedByCropAndGrassCoversForMoreThanPreviousLandConversionPeriodAndMoreThanLandRemainingPeriod_When_GetLandUse_Then_CroplandRemainingCroplandWillBeReturned() throws ServerException {

        Long timestep = 2L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                22,
                2,
                1992,
                3
        ));



        List<LandUseHistoricDetail> landUseHistories = getLandUseHistories(
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

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 19
     * Classify as land (previous land use) converted to Cropland
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonCropCoverFollowedByCropAndGrassCoversForMoreThanPreviousLandConversionPeriodAndLessThanLandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToCroplandWillBeReturned() throws ServerException {

        Long timestep = 2L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                5,
                2,
                1992,
                3
        ));



        List<LandUseHistoricDetail> landUseHistories = getLandUseHistories(
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

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 21
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonCroplandCoverFollowedByCroplandCoversForLessThanPreviousLandConversionPeriodAndTimeToEndOfSeriesLessThanPreviousLandConversionPeriod_When_GetLandUse_Then_UnconfirmedPreviousLandConvertedToCroplandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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


        List<LandUseHistoricDetail> landUseHistories = Arrays.asList(
                new LandUseHistoryBuilder()
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


        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
                .itemNumber(1L)
                .year(1991)
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 23
     * Classify as land remaining land (of the previous land use)
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndANonCroplandCoverFollowedByCroplandCoversFollowedByNonCroplandCoversForAPeriodShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationLongerThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandRemainingPreviousLandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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
                        .build(), 2,
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 2);


        List<LandUseHistoricDetail> landUseHistories = new ArrayList<>();
        landUseHistories.add(new LandUseHistoryBuilder()
                .itemNumber(0L)
                .year(1990)
                .landUseCategory(new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build())
                .build());

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    /**
     * Decision 24
     * Classify as land (previous land use) converted to land (current cover type)
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndANonCroplandCoverFollowedByCroplandCoversForAPeriodShorterThanPreviousLandConversionPeriodAndALengthOfTimeToTheEndOfTheSimulationLongerThanPreviousLandConversionPeriod_When_GetLandUse_Then_PreviousLandConvertedToCroplandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypeHistoricDetail> coverTypeHistories = getCoverTypeHistories(
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
                        .build(), 3,
                new CoverTypeBuilder()
                        .id(3L)
                        .code("G")
                        .description("Grassland")
                        .version(1)
                        .build(), 1);


        List<LandUseHistoricDetail> landUseHistories = getLandUseHistories(
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

        LandUseHistoricDetail landUseHistoricDetail = new LandUseHistoryBuilder()
                .itemNumber(1L)
                .year(1991)
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


        assertThat(landUsesProcessor.process(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUseHistoricDetail);


    }

    private List<CoverTypeHistoricDetail> getCoverTypeHistories(
            CoverType coverType, int coverTypesCount) {

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        List<CoverTypeHistoricDetail> coverTypeHistories = new ArrayList<>();

        // Add First Cover Type
        IntStream
                .range(0, coverTypesCount)
                .mapToObj(i ->
                        new CoverTypeHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(coverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;
    }

    private List<CoverTypeHistoricDetail> getCoverTypeHistories(
            CoverType firstCoverType, CoverType secondCoverType, int secondCoverTypesCount) {

        List<CoverTypeHistoricDetail> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        new CoverTypeHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(firstCoverType)
                                .build())
                .forEach(coverTypeHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondCoverTypesCount)
                .mapToObj(i ->
                        new CoverTypeHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(secondCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;

    }


    private List<CoverTypeHistoricDetail> getCoverTypeHistories(
            CoverType firstCoverType,
            CoverType secondCoverType, int secondCoverTypesCount,
            CoverType thirdCoverType, int thirdCoverTypesCount) {

        List<CoverTypeHistoricDetail> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        new CoverTypeHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(firstCoverType)
                                .build())
                .forEach(coverTypeHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondCoverTypesCount)
                .mapToObj(i ->
                        new CoverTypeHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(secondCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        // Add Third Cover Type
        IntStream
                .range(0, thirdCoverTypesCount)
                .mapToObj(i ->
                        new CoverTypeHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .coverType(thirdCoverType)
                                .build())
                .forEach(coverTypeHistories::add);

        return coverTypeHistories;

    }

    private List<CoverTypeHistoricDetail> getInterchangingCoverTypeHistories(
            CoverType firstCoverType, CoverType secondCoverType, int totalDesiredNumberOfYears, long previousStep, int previousYear, int startIndex) {

        List<CoverTypeHistoricDetail> coverTypeHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(previousStep);
        AtomicInteger year = new AtomicInteger(previousYear);

        int index = startIndex;
        boolean status = true;

        do {
            if(status){
                coverTypeHistories.add(new CoverTypeHistoryBuilder()
                        .itemNumber(step.incrementAndGet())
                        .year(year.incrementAndGet())
                        .coverType(firstCoverType)
                        .build());
            } else {
                coverTypeHistories.add(new CoverTypeHistoryBuilder()
                        .itemNumber(step.incrementAndGet())
                        .year(year.incrementAndGet())
                        .coverType(secondCoverType)
                        .build());
            }

            index += 1;
            status = !status;

        }while(index <= totalDesiredNumberOfYears);

        return coverTypeHistories;

    }


    private List<LandUseHistoricDetail> getLandUseHistories(
            LandUseCategory landUse, int landUsesCount) {

        List<LandUseHistoricDetail> landUseHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, landUsesCount)
                .mapToObj(i ->
                        new LandUseHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(landUse)
                                .confirmed(true)
                                .build())
                .forEach(landUseHistories::add);

        return landUseHistories;

    }


    private List<LandUseHistoricDetail> getLandUseHistories(
            LandUseCategory firstLandUse, LandUseCategory secondLandUse, int secondLandUsesCount) {

        List<LandUseHistoricDetail> landUseHistories = new ArrayList<>();

        AtomicLong step = new AtomicLong(-1L);
        AtomicInteger year = new AtomicInteger(1989);

        // Add First Cover Type
        IntStream
                .range(0, 1)
                .mapToObj(i ->
                        new LandUseHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(firstLandUse)
                                .build())
                .forEach(landUseHistories::add);


        // Add Second Cover Type
        IntStream
                .range(0, secondLandUsesCount)
                .mapToObj(i ->
                        new LandUseHistoryBuilder()
                                .itemNumber(step.incrementAndGet())
                                .year(year.incrementAndGet())
                                .landUseCategory(secondLandUse)
                                .confirmed(true)
                                .build())
                .forEach(landUseHistories::add);

        return landUseHistories;

    }

}
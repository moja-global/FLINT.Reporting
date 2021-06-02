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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ForestlandLandUsesDecisionsIT {

    @Autowired
    LandUseCategoryAllocationDecisionTree landUseCategoryAllocationDecisionTree;

    /**
     * Decision 3
     * Classifying as land remaining land of the initial cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_InitialTimestepAndForestCover_When_GetLandUse_Then_ForestLandRemainingForestLandWillBeReturned() throws ServerException {

        Long timestep = 0L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 1);

        List<LandUsesHistoricDetail> landUseHistories = new ArrayList<>();

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
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
                .build();


        assertThat(landUseCategoryAllocationDecisionTree.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(landUsesHistoricDetail);
    }


    /**
     * Decision 8
     * Classifying as land remaining land of the initial land use type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndForestCoversFromInitialTimestep_When_GetLandUse_Then_ForestLandRemainingForestLandWillBeReturned() throws ServerException {

        Long timestep = 1L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 2);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(), 1);


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
     * Decision 11
     * Classify as land remaining land of the current cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonForestCoverFollowedByForestCoversForAPeriodLongerThanPreviousLandConversionPeriodAndLongerThanForestLandRemainingPeriod_When_GetLandUse_Then_ForestLandRemainingForestLandWillBeReturned() throws ServerException {

        Long timestep = 21L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 21);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(10L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(9L)
                        .coverTypeId(2L)
                        .name("Cropland Remaining Cropland")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(4L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(3L)
                        .coverTypeId(1L)
                        .name("Cropland Converted To Forest land")
                        .version(1)
                        .build(), 20);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(21L)
                .year(2011)
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
     * Decision 12
     * Classify as land converted to land of the current cover type
     *
     * @throws ServerException
     */
    @Test
    public void Given_NonInitialTimestepAndNonForestCoverFollowedByForestCoversForAPeriodLongerThanPreviousLandConversionPeriodAndShorterThanForestLandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToForestLandWillBeReturned() throws ServerException {

        Long timestep = 4L;

        List<CoverTypesHistoricDetail> coverTypeHistories = getCoverTypeHistories(
                new CoverTypeBuilder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                new CoverTypeBuilder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 4);


        List<LandUsesHistoricDetail> landUseHistories = getLandUseHistories(
                new LandUseCategoryBuilder()
                        .id(10L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(9L)
                        .coverTypeId(2L)
                        .name("Cropland Remaining Cropland")
                        .version(1)
                        .build(),
                new LandUseCategoryBuilder()
                        .id(4L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(3L)
                        .coverTypeId(1L)
                        .name("Cropland Converted To Forest land")
                        .version(1)
                        .build(), 3);

        LandUsesHistoricDetail landUsesHistoricDetail = LandUsesHistoricDetail.builder()
                .itemNumber(4L)
                .year(1994)
                .landUseCategory(
                        new LandUseCategoryBuilder()
                                .id(4L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(3L)
                                .coverTypeId(1L)
                                .name("Cropland Converted To Forest land")
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
            if(status){
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

        }while(index <= yearCount);

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
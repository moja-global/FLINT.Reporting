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
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ForestLandUsesCategoriesAllocatorIT {

    @Autowired
    LandUsesCategoriesAllocator landUsesCategoriesAllocator;

    /**
     * Decision 3
     * Classifying as land remaining land of the initial cover type
     *
     *
     */
    @Test
    public void Given_InitialTimestepAndForestCover_When_GetLandUse_Then_ForestLandRemainingForestLandWillBeReturned() {

        Long timestep = 0L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 1);

        List<LocationLandUsesHistory> landUseHistories = new ArrayList<>();

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
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
                .build();


        assertThat(landUsesCategoriesAllocator.allocateLandUseCategory(timestep, coverTypeHistories, landUseHistories))
                .isEqualTo(locationLandUsesHistory);
    }


    /**
     * Decision 8
     * Classifying as land remaining land of the initial land use type
     *
     *
     */
    @Test
    public void Given_NonInitialTimestepAndForestCoversFromInitialTimestep_When_GetLandUse_Then_ForestLandRemainingForestLandWillBeReturned() {

        Long timestep = 1L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 2);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(2L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(1L)
                        .coverTypeId(1L)
                        .name("Forest land Remaining Forest land")
                        .version(1)
                        .build(), 1);


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
     * Decision 11
     * Classify as land remaining land of the current cover type
     *
     *
     */
    @Test
    public void Given_NonInitialTimestepAndNonForestCoverFollowedByForestCoversForAPeriodLongerThanPreviousLandConversionPeriodAndLongerThanForestLandRemainingPeriod_When_GetLandUse_Then_ForestLandRemainingForestLandWillBeReturned() {

        Long timestep = 21L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 21);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(10L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(9L)
                        .coverTypeId(2L)
                        .name("Cropland Remaining Cropland")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(4L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(3L)
                        .coverTypeId(1L)
                        .name("Cropland Converted To Forest land")
                        .version(1)
                        .build(), 20);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(21L)
                .year(2011)
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
     * Decision 12
     * Classify as land converted to land of the current cover type
     *
     *
     */
    @Test
    public void Given_NonInitialTimestepAndNonForestCoverFollowedByForestCoversForAPeriodLongerThanPreviousLandConversionPeriodAndShorterThanForestLandRemainingPeriod_When_GetLandUse_Then_PreviousLandConvertedToForestLandWillBeReturned() {

        Long timestep = 4L;

        List<LocationCoverTypesHistory> coverTypeHistories = getCoverTypeHistories(
                CoverType.builder()
                        .id(2L)
                        .code("C")
                        .description("Cropland")
                        .version(1)
                        .build(),
                CoverType.builder()
                        .id(1L)
                        .code("F")
                        .description("Forest land")
                        .version(1)
                        .build(), 4);


        List<LocationLandUsesHistory> landUseHistories = getLandUseHistories(
                LandUseCategory.builder()
                        .id(10L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(9L)
                        .coverTypeId(2L)
                        .name("Cropland Remaining Cropland")
                        .version(1)
                        .build(),
                LandUseCategory.builder()
                        .id(4L)
                        .reportingFrameworkId(1L)
                        .parentLandUseCategoryId(3L)
                        .coverTypeId(1L)
                        .name("Cropland Converted To Forest land")
                        .version(1)
                        .build(), 3);

        LocationLandUsesHistory locationLandUsesHistory = LocationLandUsesHistory.builder()
                .itemNumber(4L)
                .year(1994)
                .landUseCategory(
                        LandUseCategory.builder()
                                .id(4L)
                                .reportingFrameworkId(1L)
                                .parentLandUseCategoryId(3L)
                                .coverTypeId(1L)
                                .name("Cropland Converted To Forest land")
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
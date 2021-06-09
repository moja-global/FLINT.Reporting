package global.moja.businessintelligence.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.businessintelligence.daos.LocationLandUsesAllocatedFluxReportingResults;
import global.moja.businessintelligence.daos.LocationLandUsesFluxReportingResultsHistories;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class LocationLandUsesAllocatedFluxReportingResultsAggregationServiceIT {

    @Autowired
    LocationLandUsesFluxReportingResultsAllocationService locationLandUsesFluxReportingResultsAllocationService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String locationLandUsesFluxReportingResultsHistoriesTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_flux_reporting_results_histories.json";
    static String locationLandUsesAllocatedFluxReportingResultsHistoriesTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_allocated_flux_reporting_results.json";

    static LocationLandUsesFluxReportingResultsHistories  locationLandUsesFluxReportingResultsHistories;
    static LocationLandUsesAllocatedFluxReportingResults expected;

    @BeforeAll
    public static void setUp() {

        try {
            locationLandUsesFluxReportingResultsHistories = new ObjectMapper()
                    .readValue(
                            Paths.get(locationLandUsesFluxReportingResultsHistoriesTestFile).toFile(),
                            LocationLandUsesFluxReportingResultsHistories.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(locationLandUsesAllocatedFluxReportingResultsHistoriesTestFile).toFile(),
                                    LocationLandUsesAllocatedFluxReportingResults.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @AfterAll
    public static void tearDown() {
        locationLandUsesFluxReportingResultsHistories = null;
        expected = null;
    }

    @Test
    public void Given_LocationLandUsesFluxReportingResultsHistories_When_AllocateLocationLandUsesFluxReportingResults_Then_TheLocationLandUsesAllocatedFluxReportingResultsWillBeReturned() {

        LocationLandUsesAllocatedFluxReportingResults result =
                locationLandUsesFluxReportingResultsAllocationService
                .allocateLocationLandUsesFluxReportingResults(locationLandUsesFluxReportingResultsHistories)
                .block();

        assertThat(result.getLocationId()).isEqualTo(expected.getLocationId());
        assertThat(result.getPartyId()).isEqualTo(expected.getPartyId());
        assertThat(result.getTileId()).isEqualTo(expected.getTileId());
        assertThat(result.getVegetationHistoryId()).isEqualTo(expected.getVegetationHistoryId());
        assertThat(result.getUnitCount()).isEqualTo(expected.getUnitCount());
        assertThat(result.getUnitAreaSum()).isEqualTo(expected.getUnitAreaSum());
        assertThat(result.getAllocations()).isNotNull();
        assertThat(result.getAllocations().get(result.getAllocations().size() - 1).getAllocations()).isNotNull();
        assertThat(result.getAllocations().get(result.getAllocations().size() - 1).getAllocations()).isEqualTo(expected.getAllocations().get(expected.getAllocations().size() - 1).getAllocations());

    }

}
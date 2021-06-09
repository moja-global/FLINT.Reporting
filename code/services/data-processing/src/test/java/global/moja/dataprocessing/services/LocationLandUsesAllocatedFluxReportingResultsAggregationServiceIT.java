package global.moja.dataprocessing.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.dataprocessing.daos.LocationLandUsesAllocatedFluxReportingResults;
import global.moja.dataprocessing.daos.LocationLandUsesAllocatedFluxReportingResultsAggregation;
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
    LocationLandUsesAllocatedFluxReportingResultsAggregationService locationLandUsesAllocatedFluxReportingResultsAggregationService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String locationLandUsesAllocatedFluxReportingResultsTestFile =
            absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_allocated_flux_reporting_results.json";

    static String locationLandUsesAllocatedFluxReportingResultsAggregateTestFile =
            absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_allocated_flux_reporting_results_aggregate.json";

    static LocationLandUsesAllocatedFluxReportingResults locationLandUsesAllocatedFluxReportingResults;
    static LocationLandUsesAllocatedFluxReportingResultsAggregation expected;

    @BeforeAll
    public static void setUp() {

        try {
            locationLandUsesAllocatedFluxReportingResults = new ObjectMapper()
                    .readValue(
                            Paths.get(locationLandUsesAllocatedFluxReportingResultsTestFile).toFile(),
                            LocationLandUsesAllocatedFluxReportingResults.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(locationLandUsesAllocatedFluxReportingResultsAggregateTestFile).toFile(),
                                    LocationLandUsesAllocatedFluxReportingResultsAggregation.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @AfterAll
    public static void tearDown() {
        locationLandUsesAllocatedFluxReportingResults = null;
        expected = null;
    }

    @Test
    public void Given_LocationLandUsesFluxReportingResultsAggregations_When_AggregateLocationLandUsesAllocatedFluxReportingResults_Then_TheLocationLandUsesAllocatedFluxReportingResultsAggregationWillBeReturned() {

        LocationLandUsesAllocatedFluxReportingResultsAggregation result =
                locationLandUsesAllocatedFluxReportingResultsAggregationService
                .aggregateLocationLandUsesAllocatedFluxReportingResults(locationLandUsesAllocatedFluxReportingResults)
                .block();

        assertThat(result.getLocationId()).isEqualTo(expected.getLocationId());
        assertThat(result.getPartyId()).isEqualTo(expected.getPartyId());
        assertThat(result.getTileId()).isEqualTo(expected.getTileId());
        assertThat(result.getVegetationHistoryId()).isEqualTo(expected.getVegetationHistoryId());
        assertThat(result.getUnitCount()).isEqualTo(expected.getUnitCount());
        assertThat(result.getUnitAreaSum()).isEqualTo(expected.getUnitAreaSum());
        assertThat(result.getAggregations()).isNotNull();
        assertThat(result.getAggregations()).isEqualTo(expected.getAggregations());

        for(int j = 0; j < expected.getAggregations().size(); j++) {

            assertThat(result.getAggregations().get(j)).isEqualTo(expected.getAggregations().get(j));
        }
        
        
        
        assertThat(result.getAggregations().size()).isEqualTo(expected.getAggregations().size());

        assertThat(result.getAggregations()).isEqualTo(expected.getAggregations());

    }

}
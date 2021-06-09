package global.moja.businessintelligence.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.businessintelligence.daos.LocationLandUsesAllocatedFluxReportingResultsAggregation;
import global.moja.businessintelligence.models.QuantityObservation;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class PartyLandUsesAllocatedFluxReportingResultsAggregationServiceIT {

    @Autowired
    PartyLandUsesAllocatedFluxReportingResultsAggregationService partyLandUsesAllocatedFluxReportingResultsAggregationService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String locationLandUsesAllocatedFluxReportingResultsAggregateTestFile =
            absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_allocated_flux_reporting_results_aggregate.json";

    static String dataProcessingQuantityObservationsTestFile =
            absolutePath +
                    FileSystems.getDefault().getSeparator() +
                    "data_processing_quantity_observations.json";

    static List<LocationLandUsesAllocatedFluxReportingResultsAggregation> locationLandUsesAllocatedFluxReportingResultsAggregations;
    static List<QuantityObservation> expected;

    @BeforeAll
    public static void setUp() {

        try {
            locationLandUsesAllocatedFluxReportingResultsAggregations =
                    Collections.singletonList(
                            new ObjectMapper()
                                    .readValue(
                                            Paths.get(locationLandUsesAllocatedFluxReportingResultsAggregateTestFile).toFile(),
                                            LocationLandUsesAllocatedFluxReportingResultsAggregation.class)
                    );
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(dataProcessingQuantityObservationsTestFile).toFile(),
                                    new TypeReference<>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @AfterAll
    public static void tearDown() {
        locationLandUsesAllocatedFluxReportingResultsAggregations = null;
        expected = null;
    }

    @Test
    public void Given_PartyIdAndDatabaseIdAndLocationLandUsesAllocatedFluxReportingResultsAggregations_When_AggregateFluxReportingResultsAggregations_Then_QuantityObservationsWillBeReturned() {

        assertThat(partyLandUsesAllocatedFluxReportingResultsAggregationService
                .aggregateFluxReportingResultsAggregations(1L,1L, locationLandUsesAllocatedFluxReportingResultsAggregations)
                .block())
                .isEqualTo(expected);


    }

}
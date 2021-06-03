package global.moja.businessintelligence.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.businessintelligence.daos.LocationLandUsesFluxReportingResultsHistories;
import global.moja.businessintelligence.daos.LocationLandUsesHistories;
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
class LocationLandUsesFluxReportingResultsHistoriesProcessorIT {

    @Autowired
    LocationLandUsesFluxReportingResultsService locationLandUsesFluxReportingResultsService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String locationLandUsesFluxesHistoryTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_fluxes_history.json";
    static String locationLandUsesHistoryTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_history.json";

    static LocationLandUsesHistories locationLandUsesHistories;
    static LocationLandUsesFluxReportingResultsHistories expected;

    @BeforeAll
    public static void setUp() {

        try {
            locationLandUsesHistories = new ObjectMapper()
                    .readValue(
                            Paths.get(locationLandUsesHistoryTestFile).toFile(),
                            LocationLandUsesHistories.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(locationLandUsesFluxesHistoryTestFile).toFile(),
                                    LocationLandUsesFluxReportingResultsHistories.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    @AfterAll
    public static void tearDown() {
        locationLandUsesHistories = null;
        expected = null;
    }

    @Test
    public void Given_DatabaseIdAndLocation_When_Process_Then_TheCorrespondingLocationCoverTypesHistoryWillBeReturned() {

        LocationLandUsesFluxReportingResultsHistories result =
                locationLandUsesFluxReportingResultsService
                .generateLocationLandUsesFluxReportingResultsHistories(1L, locationLandUsesHistories)
                .block();

        assertThat(result.getLocationId()).isEqualTo(expected.getLocationId());
        assertThat(result.getPartyId()).isEqualTo(expected.getPartyId());
        assertThat(result.getTileId()).isEqualTo(expected.getTileId());
        assertThat(result.getVegetationHistoryId()).isEqualTo(expected.getVegetationHistoryId());
        assertThat(result.getUnitCount()).isEqualTo(expected.getUnitCount());
        assertThat(result.getUnitAreaSum()).isEqualTo(expected.getUnitAreaSum());
        assertThat(result.getHistories()).isNotNull();
        assertThat(result.getHistories().size()).isEqualTo(expected.getHistories().size());
        assertThat(result.getHistories().get(result.getHistories().size() - 1)).isEqualTo(expected.getHistories().get(expected.getHistories().size() - 1));
        assertThat(result.getHistories().get(result.getHistories().size() - 2)).isEqualTo(expected.getHistories().get(expected.getHistories().size() - 2));

    }

}
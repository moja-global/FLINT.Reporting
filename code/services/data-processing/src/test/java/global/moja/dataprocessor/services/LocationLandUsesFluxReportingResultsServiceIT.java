package global.moja.dataprocessor.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.dataprocessor.daos.LocationLandUsesFluxReportingResultsHistories;
import global.moja.dataprocessor.daos.LocationLandUsesFluxReportingResultsHistory;
import global.moja.dataprocessor.daos.LocationLandUsesHistories;
import global.moja.dataprocessor.models.FluxReportingResult;
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
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class LocationLandUsesFluxReportingResultsServiceIT {

    @Autowired
    LocationLandUsesFluxReportingResultsService locationLandUsesFluxReportingResultsService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String locationLandUsesFluxesHistoryTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_flux_reporting_results_histories.json";
    static String locationLandUsesHistoryTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_land_uses_histories.json";

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
    public void Given_DatabaseIdAndLocationLandUsesHistories_When_GetLocationLandUsesFluxReportingResultsHistories_Then_LocationLandUsesFluxReportingResultsHistoriesWillBeReturned() {

        LocationLandUsesFluxReportingResultsHistories result =
                locationLandUsesFluxReportingResultsService
                .getLocationLandUsesFluxReportingResultsHistories(1L, locationLandUsesHistories)
                .block();

        assertThat(result.getLocationId()).isEqualTo(expected.getLocationId());
        assertThat(result.getPartyId()).isEqualTo(expected.getPartyId());
        assertThat(result.getTileId()).isEqualTo(expected.getTileId());
        assertThat(result.getVegetationHistoryId()).isEqualTo(expected.getVegetationHistoryId());
        assertThat(result.getUnitCount()).isEqualTo(expected.getUnitCount());
        assertThat(result.getUnitAreaSum()).isEqualTo(expected.getUnitAreaSum());
        assertThat(result.getHistories()).isNotNull();

        LocationLandUsesFluxReportingResultsHistory actualLocationLandUsesFluxReportingResultsHistory = result.getHistories().get(result.getHistories().size() - 1);
        LocationLandUsesFluxReportingResultsHistory expectedLocationLandUsesFluxReportingResultsHistory = expected.getHistories().get(expected.getHistories().size() - 1);

        assertThat(actualLocationLandUsesFluxReportingResultsHistory.getItemNumber()).isEqualTo(expectedLocationLandUsesFluxReportingResultsHistory.getItemNumber());
        assertThat(actualLocationLandUsesFluxReportingResultsHistory.getYear()).isEqualTo(expectedLocationLandUsesFluxReportingResultsHistory.getYear());
        assertThat(actualLocationLandUsesFluxReportingResultsHistory.getLandUseCategory()).isEqualTo(expectedLocationLandUsesFluxReportingResultsHistory.getLandUseCategory());
        assertThat(actualLocationLandUsesFluxReportingResultsHistory.getConfirmed()).isEqualTo(expectedLocationLandUsesFluxReportingResultsHistory.getConfirmed());

        List<FluxReportingResult> actualFluxReportingResults = actualLocationLandUsesFluxReportingResultsHistory.getFluxReportingResults();
        List<FluxReportingResult> expectedFluxReportingResults = expectedLocationLandUsesFluxReportingResultsHistory.getFluxReportingResults();

        for(int j = 0; j < expectedFluxReportingResults.size(); j++) {

            assertThat(actualFluxReportingResults.get(j).getId()).isEqualTo(expectedFluxReportingResults.get(j).getId());
            assertThat(actualFluxReportingResults.get(j).getDateId()).isEqualTo(expectedFluxReportingResults.get(j).getDateId());
            assertThat(actualFluxReportingResults.get(j).getLocationId()).isEqualTo(expectedFluxReportingResults.get(j).getLocationId());
            assertThat(actualFluxReportingResults.get(j).getFluxTypeId()).isEqualTo(expectedFluxReportingResults.get(j).getFluxTypeId());
            assertThat(actualFluxReportingResults.get(j).getSourcePoolId()).isEqualTo(expectedFluxReportingResults.get(j).getSourcePoolId());
            assertThat(actualFluxReportingResults.get(j).getSinkPoolId()).isEqualTo(expectedFluxReportingResults.get(j).getSinkPoolId());
            assertThat(actualFluxReportingResults.get(j).getItemCount()).isEqualTo(expectedFluxReportingResults.get(j).getItemCount());
            assertThat(actualFluxReportingResults.get(j).getFlux()).isEqualTo(expectedFluxReportingResults.get(j).getFlux());
        }


    }

}
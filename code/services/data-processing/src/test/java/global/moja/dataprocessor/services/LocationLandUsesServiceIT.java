package global.moja.dataprocessor.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.dataprocessor.daos.LocationCoverTypesHistories;
import global.moja.dataprocessor.daos.LocationLandUsesHistories;
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
class LocationLandUsesServiceIT {

    @Autowired
    LocationLandUsesService locationLandUsesService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String locationCoverTypesHistoryTestFile = absolutePath +
                    FileSystems.getDefault().getSeparator() +
            "location_cover_types_histories.json";
    static String locationLandUsesHistoryTestFile = absolutePath +
                    FileSystems.getDefault().getSeparator() +
            "location_land_uses_histories.json";

    static LocationCoverTypesHistories locationCoverTypesHistories;
    static LocationLandUsesHistories expected;

    @BeforeAll
    public static void setUp() {

        try {
            locationCoverTypesHistories =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(locationCoverTypesHistoryTestFile).toFile(),
                                    LocationCoverTypesHistories.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(locationLandUsesHistoryTestFile).toFile(),
                                    LocationLandUsesHistories.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @AfterAll
    public static void tearDown() {
        locationCoverTypesHistories = null;
        expected = null;
    }

    @Test
    public void Given_DatabaseIdAndLocation_When_Process_Then_TheCorrespondingLocationCoverTypesHistoryWillBeReturned() {

        assertThat(locationLandUsesService
                .getLocationLandUsesHistories(locationCoverTypesHistories)
                .block())
                .isEqualTo(expected);

    }

}
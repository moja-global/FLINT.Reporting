package global.moja.businessintelligence.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.businessintelligence.daos.LocationVegetationTypesHistories;
import global.moja.businessintelligence.models.Location;
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
class LocationVegetationTypesServiceIT {

    @Autowired
    LocationVegetationTypesService locationVegetationTypesService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String locationVegetationTypesHistoryTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "location_vegetation_types_history.json";

    static Location location;
    static LocationVegetationTypesHistories expected;

    @BeforeAll
    public static void setUp() {

        location = new Location(
                1L,
                34L,
                1L,
                1L,
                508981L,
                39329.575347296675);

        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(locationVegetationTypesHistoryTestFile).toFile(),
                                    LocationVegetationTypesHistories.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @AfterAll
    public static void tearDown() {
        location = null;
        expected = null;
    }

    @Test
    public void Given_DatabaseIdAndLocation_When_Process_Then_TheCorrespondingLocationVegetationTypesHistoryWillBeReturned() {

        assertThat(locationVegetationTypesService
                .generateLocationVegetationTypesHistories(1L,location)
                .block())
                .isEqualTo(expected);

    }

}
package global.moja.dataaggregator.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.dataaggregator.models.QuantityObservation;
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
class QuantityObservationsAggregationServiceIT {

    @Autowired
    QuantityObservationsAggregationService quantityObservationsAggregationService;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String processedQuantityObservationsTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "processed_quantity_observations.json";

    static String aggregatedQuantityObservationsTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "aggregated_quantity_observations.json";

    static List<QuantityObservation> processedQuantityObservations;
    static List<QuantityObservation> expected;

    @BeforeAll
    public static void setUp() {
        try {
            processedQuantityObservations =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(processedQuantityObservationsTestFile).toFile(),
                                    new TypeReference<List<QuantityObservation>>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            expected =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(aggregatedQuantityObservationsTestFile).toFile(),
                                    new TypeReference<List<QuantityObservation>>() {});
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @AfterAll
    public static void tearDown() {
        processedQuantityObservations = null;
        expected = null;
    }

    @Test
    public void Given_TaskIdAndParentPartyIdAndProcessedQuantityObservations_When_AggregateProcessedQuantityObservations_Then_AggregatedQuantityObservationsWillBeReturned() {
        assertThat(quantityObservationsAggregationService
                .aggregateProcessedQuantityObservations(2L, 1L, processedQuantityObservations)
                .block())
                .isEqualTo(expected);
    }

}
package global.moja.taskmanager.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.taskmanager.configurations.RabbitConfig;
import global.moja.taskmanager.daos.DataAggregationRequest;
import global.moja.taskmanager.daos.DataAggregationResponse;
import global.moja.taskmanager.models.Accountability;
import global.moja.taskmanager.util.DataAggregationStatus;
import global.moja.taskmanager.util.endpoints.EndpointsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import reactor.core.publisher.Flux;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class DataAggregationServiceIT {

    @Spy
    @Autowired
    EndpointsUtil endpointsUtil;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    @Autowired
    DataAggregationService dataAggregationServiceService;

    Path resourceDirectory = Paths.get("src", "test", "resources");
    String absolutePath = resourceDirectory.toFile().getAbsolutePath();
    String processedQuantityObservationsTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() +
            "processed_quantity_observations.json";

    List<QuantityObservation> processedQuantityObservations;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);

        try {
            processedQuantityObservations =
                    new ObjectMapper()
                            .readValue(
                                    Paths.get(processedQuantityObservationsTestFile).toFile(),
                                    new TypeReference<>() {
                                    });
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    @Test
    public void Given_TaskIdDatabaseIdParentPartyIdAccountabilityTypeId_When_AggregateProcessedObservations_Then_AggregatedQuantityObservationsWillBeReturned() {

        Mockito.doReturn(
                Flux.just(
                        Accountability
                                .builder()
                                .accountabilityTypeId(1L)
                                .parentPartyId(1L)
                                .subsidiaryPartyId(2L)
                                .version(1)
                                .build(),
                        Accountability
                                .builder()
                                .accountabilityTypeId(1L)
                                .parentPartyId(1L)
                                .subsidiaryPartyId(3L)
                                .version(1)
                                .build()
                ))
                .when(endpointsUtil)
                .retrieveAccountabilities(anyLong(),anyLong());

        Mockito.doReturn(
                Flux.fromIterable(processedQuantityObservations))
                .when(endpointsUtil)
                .retrieveQuantityObservations(anyLong(), any(List.class), anyLong());

        Mockito.doReturn(
                Flux.just(1L, 2L, 3L, 4L, 5L))
                .when(endpointsUtil)
                .createQuantityObservations(any(QuantityObservation[].class));

        dataAggregationServiceService.processData(
                DataAggregationRequest
                        .builder()
                        .taskId(1L)
                        .parentPartyId(1L)
                        .databaseId(1L)
                        .build());

        verify(rabbitTemplate).convertAndSend(
                RabbitConfig.PROCESSED_DATA_AGGREGATION_RESULTS_QUEUE,
                DataAggregationResponse
                        .builder()
                        .taskId(1L)
                        .databaseId(1L)
                        .parentPartyId(1L)
                        .statusCode(DataAggregationStatus.SUCCEEDED.getId())
                        .build()
        );
    }

}
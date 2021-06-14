package global.moja.dataprocessor.services;

import global.moja.dataprocessor.configurations.RabbitConfig;
import global.moja.dataprocessor.daos.DataProcessingRequest;
import global.moja.dataprocessor.daos.DataProcessingResponse;
import global.moja.dataprocessor.models.QuantityObservation;
import global.moja.dataprocessor.util.DataProcessingStatus;
import global.moja.dataprocessor.util.endpoints.EndpointsUtil;
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
import reactor.core.publisher.Mono;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class DataProcessingServiceIT {

    @Spy
    @Autowired
    EndpointsUtil endpointsUtil;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    @Autowired
    DataProcessingService dataProcessingServiceService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void Given_PartyIdAndDatabaseIdAndLocationLandUsesAllocatedFluxReportingResultsAggregations_When_AggregateFluxReportingResultsAggregations_Then_QuantityObservationsWillBeReturned() {

        Mockito.doReturn(Flux.just(Arrays.asList(1L,2L,3L,4L,5L))).when(endpointsUtil).createQuantityObservations(any(QuantityObservation[].class));

        dataProcessingServiceService.processData(
                DataProcessingRequest
                        .builder()
                        .taskId(1L)
                        .partyId(1L)
                        .databaseId(1L)
                        .build());

        verify(rabbitTemplate).convertAndSend(
                RabbitConfig.RAW_DATA_PROCESSING_RESULTS_QUEUE,
                DataProcessingResponse
                        .builder()
                        .taskId(1L)
                        .databaseId(1L)
                        .partyId(1L)
                        .statusCode(DataProcessingStatus.SUCCEEDED.getId())
                        .build()
        );
    }

}
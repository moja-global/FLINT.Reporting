package global.moja.dataprocessing.util;

import global.moja.dataprocessing.daos.Allocation;
import global.moja.dataprocessing.exceptions.ServerException;
import global.moja.dataprocessing.models.FluxReportingResult;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class FluxReportingResultsAllocatorIT {

    @Autowired
    FluxReportingResultsAllocator fluxReportingResultsAllocator;


    /**
     * @throws ServerException
     */
    @Test
    public void Given_LandUseCategoryIdAndFluxReportingResult_When_AllocateFluxReportingResults_Then_AllocationsWillBeReturned() throws Exception {

        Long landUseCategoryId = 18L;

        FluxReportingResult fluxReportingResult =
                new ObjectMapper()
                        .readValue("" +
                                "{\n" +
                                "\"id\": 10228,\n" +
                                "\"dateId\": 31,\n" +
                                "\"locationId\": 1,\n" +
                                "\"fluxTypeId\": 5,\n" +
                                "\"sourcePoolId\": 51,\n" +
                                "\"sinkPoolId\": 1,\n" +
                                "\"flux\": \"70.364749526856\",\n" +
                                "\"itemCount\": 6107772\n" +
                                "}", FluxReportingResult.class);

        List<Allocation> expected = Arrays.asList(
                Allocation
                        .builder()
                        .fluxReportingResultId(10228L)
                        .reportingTableId(5L)
                        .reportingVariableId(4L)
                        .flux(-70.364749526856)
                        .build());

        assertThat(fluxReportingResultsAllocator.allocateFluxReportingResults(landUseCategoryId, fluxReportingResult))
                .isEqualTo(expected);

    }
}
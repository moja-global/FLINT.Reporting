package global.moja.taskmanager.configurations;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import global.moja.taskmanager.Application;
import global.moja.taskmanager.models.Accountability;
import global.moja.taskmanager.models.AccountabilityRule;
import global.moja.taskmanager.util.endpoints.EndpointsUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.core.publisher.Flux;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class ConfigurationDataProviderTest {

    @Spy
    @Autowired
    EndpointsUtil endpointsUtil;

    ConfigurationDataProvider configurationDataProvider;

    static Path resourceDirectory = Paths.get("src", "test", "resources");
    static String absolutePath = resourceDirectory.toFile().getAbsolutePath();

    static String accountabilitiesTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() + "accountabilities.json";
    static String accountabilityRulesTestFile = absolutePath +
            FileSystems.getDefault().getSeparator() + "accountability_rules.json";

    static List<Accountability> accountabilities;
    static List<AccountabilityRule> accountabilityRules;

    @BeforeAll
    public static void setupAll() throws Exception {

        accountabilities = new ObjectMapper()
                .readValue(Paths.get(accountabilitiesTestFile).toFile(), new TypeReference<>() {
                });

        accountabilityRules = new ObjectMapper()
                .readValue(Paths.get(accountabilityRulesTestFile).toFile(), new TypeReference<>() {
                });


    }

    @AfterAll
    public static void afterEach() {
        accountabilities = null;
        accountabilityRules = null;
    }

    @BeforeEach
    public void setup() throws Exception {

        MockitoAnnotations.openMocks(this);

        Mockito.doReturn(
                Flux.fromIterable(accountabilityRules))
                .when(endpointsUtil)
                .retrieveAccountabilityRules(anyLong());

        LongStream
                .rangeClosed(1, 5)
                .forEach(i -> Mockito.doReturn(
                        Flux.fromIterable(
                                accountabilities
                                        .stream()
                                        .filter(accountability -> accountability.getAccountabilityRuleId().equals(i))
                                        .collect(Collectors.toList())
                        ))
                        .when(endpointsUtil)
                        .retrieveAccountabilities(i));



        configurationDataProvider = new ConfigurationDataProvider(endpointsUtil);
        ReflectionTestUtils.setField(configurationDataProvider, "ADMINISTRATIVE_HIERARCHY_ACCOUNTABILITY_TYPE_ID", 1L);
        ReflectionTestUtils.setField(configurationDataProvider, "DATA_PROCESSING_ENTRY_LEVEL_PARTY_TYPE_ID", 5L);
        configurationDataProvider.init();
    }

    @Test
    public void Given_TaskManagerDataHasBeenConfigured_When_GetDataProcessingLevelPartiesIds_Then_CorrectlyOrderedDataProcessingLevelPartiesIdsWillBeReturned() {
        List<Long> expected = new LinkedList<>(Arrays.asList(11L,15L,12L,13L,14L));
        assertThat(configurationDataProvider.getDataProcessingLevelPartiesIds()).isEqualTo(expected);
    }

    @Test
    public void Given_TaskManagerDataHasBeenConfigured_When_GetTotalDataProcessingIssues_Then_CorrectDataProcessingIssuesNumberWillBeReturned() {
        Integer expected = 5;
        assertThat(configurationDataProvider.getTotalDataProcessingIssues()).isEqualTo(expected);
    }

    @Test
    public void Given_TaskManagerDataHasBeenConfigured_When_GetDataAggregationLevelPartiesIds_Then_CorrectlyOrderedDataAggregationLevelPartiesIdsWillBeReturned() {
        Map<Integer, LinkedList<Long>> expected = new LinkedHashMap();
        expected.put(1, new LinkedList<>(Arrays.asList(7L,10L,8L,9L)));
        expected.put(2, new LinkedList<>(Arrays.asList(4L,6L,5L)));
        expected.put(3, new LinkedList<>(Arrays.asList(2L,3L)));
        expected.put(4, new LinkedList<>(Arrays.asList(1L)));

        assertThat(configurationDataProvider.getDataAggregationLevelsPartiesIds()).isEqualTo(expected);
    }

    @Test
    public void Given_TaskManagerDataHasBeenConfigured_When_GetTotalDataAggregationIssues_Then_CorrectDataAggregationIssuesNumberWillBeReturned() {
        Integer expected = 10;
        assertThat(configurationDataProvider.getTotalDataAggregationIssues()).isEqualTo(expected);
    }

}
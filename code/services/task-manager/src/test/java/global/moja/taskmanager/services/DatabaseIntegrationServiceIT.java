package global.moja.taskmanager.services;

import global.moja.taskmanager.util.endpoints.EndpointsUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.nio.file.Path;
import java.nio.file.Paths;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
class DatabaseIntegrationServiceIT {

    @Spy
    @Autowired
    EndpointsUtil endpointsUtil;

    @MockBean
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    @Autowired
    DatabaseIntegrationService databaseIntegrationServiceService;

    Path resourceDirectory = Paths.get("src", "test", "resources");
    String absolutePath = resourceDirectory.toFile().getAbsolutePath();



    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);



    }


}
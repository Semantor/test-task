import com.haulmont.testtask.TestConfigForNonJpa;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = TestConfigForNonJpa.class)
public class TestingWebApplicationTests {

    @Test
    public void contextLoads() {
    }

}
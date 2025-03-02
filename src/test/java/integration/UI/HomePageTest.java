package integration.UI;
import com.example.snap2transact.DriverFactory;
import com.example.snap2transact.Snap2TransactApplication;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;


@SpringBootTest(classes = Snap2TransactApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HomePageTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp() {
        // Set up ChromeDriver using WebDriverManager
        driver = DriverFactory.getDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }



    @Test
    public void testHomePage() {
        driver.get("http://localhost:9090");
        String pageTitle = driver.getTitle();  // Get title directly
        Assertions.assertEquals("Receipt Upload", pageTitle);
        WebElement dataExtract = driver.findElement(By.cssSelector("p.text-gray-600.text-center"));
        Assertions.assertEquals("Extracted data will appear here...", dataExtract.getText());

    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

}

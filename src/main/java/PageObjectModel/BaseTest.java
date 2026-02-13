package PageObjectModel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.testng.SkipException;
import org.testng.annotations.*;
import io.github.bonigarcia.wdm.WebDriverManager;

public class BaseTest {
    protected WebDriver driver;

    @BeforeClass
    public void setup() {
        WebDriverManager.chromedriver().setup();
        try {
            driver = createChromeDriver();
        } catch (RuntimeException ex) {
            throw new SkipException("Skipping UI tests: unable to start ChromeDriver (" + ex.getMessage() + ")", ex);
        }
        driver.manage().window().maximize();
    }

    private ChromeOptions buildChromeOptions() {
        ChromeOptions options = new ChromeOptions();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        if (headless) {
            options.addArguments("--headless=new");
        }
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        return options;
    }

    private WebDriver createChromeDriver() {
        ChromeOptions options = buildChromeOptions();
        try {
            return new ChromeDriver(options);
        } catch (RuntimeException ex) {
            if (ex.getMessage() != null && ex.getMessage().contains("Unable to find a free port")) {
                int fallbackPort = Integer.parseInt(System.getProperty("chrome.driver.port", "9515"));
                ChromeDriverService service = new ChromeDriverService.Builder().usingPort(fallbackPort).build();
                return new ChromeDriver(service, options);
            }
            throw ex;
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}

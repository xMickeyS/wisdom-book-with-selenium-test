// 6410402104 พิชญา เสนา
package th.ac.ku.book;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddBookTests {

    @LocalServerPort
    private Integer port;

    private static WebDriver driver;
    private static WebDriverWait wait;

    // ใช้ @FindBy แทนการเรียกเมทอด webDriver.findElement() เพื่อให้โค้ดสั้นลง
    @FindBy(id = "nameInput")
    private WebElement nameField;

    @FindBy(id = "authorInput")
    private WebElement authorField;

    @FindBy(id = "priceInput")
    private WebElement priceField;

    @FindBy(id = "submitButton")
    private WebElement submitButton;

    // ก่อนรัน test case
    @BeforeAll
    public static void beforeAll() {
        WebDriverManager.firefoxdriver().setup();
        driver = new FirefoxDriver(); // สร้าง webdriver สำหรับ firefox browser
        wait = new WebDriverWait(driver, Duration.ofSeconds(1)); // สร้าง web driver wait เพื่อรอจนกว่าเว็บจะโหลดเสร็จก่อนจะเริ่มทดสอบ
    }

    // สำหรับแต่ละ test case
    @BeforeEach
    public void beforeEach() {
        driver.get("http://localhost:" + port + "/book/add"); // ไปหน้าที่ต้องการทดสอบ โดยทดสอบหน้าการเพิ่มหนังสือ
        PageFactory.initElements(driver, this);
    }

    // สำหรับแต่ละ test case
    @AfterEach
    public void afterEach() throws InterruptedException {
        Thread.sleep(3000); // ให้ test case หยุดการทำงาน (sleep) ก่อนที่ selenium จะทดสอบใน test case ต่อไป
    }

    // เมื่อรันครบทุก test case
    @AfterAll
    public static void afterAll() {
        if (driver != null)
            driver.quit(); // ปิด browser
    }

    @Test
    void testAddBook() {
        // กรอกข้อมูลใน input field ที่ต้องการ
        nameField.sendKeys("Clean Code");
        authorField.sendKeys("Robert Martin");
        priceField.sendKeys("600");

        submitButton.click();

        WebElement name = wait.until(webDriver -> webDriver
                .findElement(By.xpath("//table/tbody/tr[1]/td[1]")));
        WebElement author = driver
                .findElement(By.xpath("//table/tbody/tr[1]/td[2]"));
        WebElement price = driver
                .findElement(By.xpath("//table/tbody/tr[1]/td[3]"));

        assertEquals("Clean Code", name.getText());
        assertEquals("Robert Martin", author.getText());
        assertEquals("600.00", price.getText());

    }

}



package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;

public abstract class AllureOnlyBaseTest {
    protected WebDriver driver;

    @BeforeEach
    public void setUp(TestInfo testInfo) {
        // 设置Allure标签
        Allure.label("test-class", testInfo.getTestClass().map(Class::getSimpleName).orElse("Unknown"));
        Allure.label("test-method", testInfo.getTestMethod().map(m -> m.getName()).orElse("Unknown"));

        // 初始化WebDriver
        initWebDriver();

        Allure.step("测试初始化完成");
    }

    @Step("初始化WebDriver")
    protected void initWebDriver() {
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();

            driver.manage().window().maximize();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

            Allure.step("WebDriver初始化成功");
        } catch (Exception e) {
            Allure.step("WebDriver初始化失败: " + e.getMessage(), () -> { throw e; });
        }
    }

    @Step("打开QQ邮箱登录页面")
    protected void openQQMailLoginPage() {
        try {
            driver.get("https://mail.qq.com");
            Allure.addAttachment("页面信息", "text/plain",
                    "URL: " + driver.getCurrentUrl() + "\n" +
                            "标题: " + driver.getTitle());
            Allure.step("成功打开QQ邮箱页面");
        } catch (Exception e) {
            Allure.step("打开页面失败: " + e.getMessage(), () -> { throw e; });
        }
    }

    @Step("截图")
    protected void takeScreenshot(String name) {
        if (driver != null) {
            try {
                byte[] screenshot = ((org.openqa.selenium.TakesScreenshot) driver)
                        .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
                Allure.addAttachment(name, "image/png",
                        new java.io.ByteArrayInputStream(screenshot), ".png");
            } catch (Exception e) {
                Allure.step("截图失败: " + e.getMessage());
            }
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
            Allure.step("WebDriver已关闭");
        }
        Allure.step("测试结束");
    }
}
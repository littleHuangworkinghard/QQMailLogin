package base;

import Pages.QQMailLoginAdvanced;
import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.AfterEach;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import report.ExtentReportManager;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public abstract class DualReportBaseTest {
    protected WebDriver driver;
    protected QQMailLoginAdvanced loginPage;
    private static final boolean USE_EXTENT_REPORTS = true;
    private static final boolean USE_ALLURE_REPORTS = true;
    @BeforeEach
    public void setUp(){
        // 1. 初始化WebDriver
        initWebDriver();

        // 2. ExtentReports记录
        if (USE_EXTENT_REPORTS) {
            ExtentReportManager.logInfo("开始初始化WebDriver...");
        }

        // 3. Allure记录
        if (USE_ALLURE_REPORTS) {
            Allure.addAttachment("测试开始时间", "text/plain",
                    java.time.LocalDateTime.now().toString());
            Allure.step("初始化WebDriver和浏览器");
        }
    }
    private void initWebDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));

        // 记录到两个报告系统
        logToBothReports("WebDriver初始化成功", "INFO");
    }
    @AfterEach
    public void tearDown() {
        try {
            if (driver != null) {
                // 测试失败时截图
                if (isTestFailed()) {
                    captureScreenshotForBoth("测试失败截图");
                }

                logToBothReports("正在关闭浏览器...", "INFO");
                driver.quit();
                logToBothReports("浏览器已关闭", "INFO");
            }
        } catch (Exception e) {
            logToBothReports("关闭浏览器时出错: " + e.getMessage(), "WARNING");
        }
    }

    /**
     * 同时记录到两个报告系统
     */
    protected void logToBothReports(String message, String level) {
        // ExtentReports
        if (USE_EXTENT_REPORTS) {
            switch (level.toUpperCase()) {
                case "INFO":
                    ExtentReportManager.logInfo(message);
                    break;
                case "PASS":
                    ExtentReportManager.logPass(message);
                    break;
                case "FAIL":
                    ExtentReportManager.logFail(message);
                    break;
                case "WARNING":
                    ExtentReportManager.logWarning(message);
                    break;
            }
        }

        // Allure
        if (USE_ALLURE_REPORTS) {
            Allure.addAttachment("日志-" + level, "text/plain", message);
        }
    }

    /**
     * 为两个报告系统截图
     */
    protected void captureScreenshotForBoth(String screenshotName) {
        if (driver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);

            // ExtentReports截图
            if (USE_EXTENT_REPORTS) {
                ExtentReportManager.logWithScreenshot(driver, screenshotName,
                        com.aventstack.extentreports.Status.INFO);
            }

            // Allure截图
            if (USE_ALLURE_REPORTS) {
                Allure.addAttachment(screenshotName,
                        "image/png",
                        new ByteArrayInputStream(screenshot),
                        ".png");
            }
        }
    }
    /**
     * 打开QQ邮箱页面（双报告记录）
     */
    @Step("打开QQ邮箱登录页面")  // Allure注解
    protected void openQQMailLoginPage() {
        String stepName = "打开QQ邮箱登录页面";

        // Allure步骤（自动记录）
        // ExtentReports需要手动记录
        if (USE_EXTENT_REPORTS) {
            ExtentReportManager.logInfo("正在" + stepName + "...");
        }

        driver.get("https://mail.qq.com");
        String title = driver.getTitle();

        logToBothReports(stepName + "成功，标题: " + title, "INFO");
        captureScreenshotForBoth("QQ邮箱登录页面");
    }

    /**
     * 初始化登录页面（双报告记录）
     */
    @Step("初始化登录页面对象")
    protected void initLoginPage() {
        try {
            loginPage = new QQMailLoginAdvanced(driver);
            logToBothReports("登录页面对象初始化完成", "INFO");
        } catch (Exception e) {
            logToBothReports("初始化登录页面对象失败: " + e.getMessage(), "FAIL");
            throw e;
        }
    }

    /**
     * 检查测试是否失败（简化版本）
     */
    private boolean isTestFailed() {
        // 这里可以更复杂地判断，比如从测试上下文获取状态
        // 简化处理：始终返回false，实际应该根据测试结果判断
        return false;
    }
}

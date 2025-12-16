package report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentReporter;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> extentTest  = new ThreadLocal<>();
    private static final String REPORT_DIR ="test-reports/extentreports";
    private static final String SCREENSHOT_DIR = REPORT_DIR + File.separator + "screenshots";
    public static void init(){
        if(extent==null){
            createReportDirectory();
            String timestamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
            String reportName = "ExtentReport-" + timestamp + ".html";
            String reportPath = REPORT_DIR + File.separator + reportName;

            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setDocumentTitle("QQ邮箱测试报告 - ExtentReports");
            sparkReporter.config().setReportName("QQ邮箱自动化测试");
            sparkReporter.config().setTheme(Theme.DARK);
            sparkReporter.config().setEncoding("UTF-8");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            extent.setSystemInfo("测试环境", "QQ邮箱网页版");
            extent.setSystemInfo("报告类型", "ExtentReports");
            extent.setSystemInfo("共存系统", "Allure Reports");
            extent.setSystemInfo("测试框架", "JUnit 5 + Selenium");

            System.out.println("ExtentReports初始化完成，报告将保存到: " + reportPath);
        }
    }
    private static void createReportDirectory() {
        new File(REPORT_DIR).mkdirs();
        new File(SCREENSHOT_DIR).mkdirs();
    }
    public static ExtentTest createTest(String testName, String description) {
        init();
        ExtentTest test = extent.createTest(testName, description);
        extentTest.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return extentTest.get();
    }

    public static void logInfo(String message) {
        getTest().log(Status.INFO, message);
    }

    public static void logPass(String message) {
        getTest().log(Status.PASS, message);
    }

    public static void logFail(String message) {
        getTest().log(Status.FAIL, message);
    }

    public static void logWarning(String message) {
        getTest().log(Status.WARNING, message);
    }

    public static void logWithScreenshot(WebDriver driver, String message, Status status) {
        String screenshotPath = captureScreenshot(driver, getTest().getModel().getName());

        if (screenshotPath != null) {
            try {
                getTest().log(status, message,
                        MediaEntityBuilder.createScreenCaptureFromPath(screenshotPath).build());
            } catch (Exception e) {
                getTest().log(status, message + " (截图失败: " + e.getMessage() + ")");
            }
        } else {
            getTest().log(status, message);
        }
    }

    private static String captureScreenshot(WebDriver driver, String testName) {
        if (driver == null) return null;

        try {
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS").format(new Date());
            String safeTestName = testName.replaceAll("[^a-zA-Z0-9-_]", "_");
            String fileName = safeTestName + "_" + timestamp + ".png";
            String filePath = SCREENSHOT_DIR + File.separator + fileName;

            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File(filePath);
            FileUtils.copyFile(screenshot, destFile);

            return "screenshots/" + fileName;
        } catch (Exception e) {
            System.err.println("ExtentReports截图失败: " + e.getMessage());
            return null;
        }
    }

    public static void endTest() {
        if (extent != null) {
            extent.flush();
        }
    }

    public static void close() {
        endTest();
    }
}

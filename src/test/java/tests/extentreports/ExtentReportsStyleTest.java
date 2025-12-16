package tests.extentreports;
import base.DualReportBaseTest;
import org.junit.jupiter.api.*;
import report.ExtentReportManager;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ExtentReportsStyleTest extends DualReportBaseTest {
    @BeforeAll
    public static void setupExtentReports() {
        // 初始化ExtentReports
        ExtentReportManager.init();
        ExtentReportManager.createTest("ExtentReports风格测试套件",
                "使用ExtentReports API记录测试");
    }

    @BeforeEach
    public void setupTest(TestInfo testInfo) {
        // 为每个测试方法创建ExtentTest
        ExtentReportManager.createTest(testInfo.getDisplayName(),
                testInfo.getTestMethod().map(m -> m.getName()).orElse(""));
    }

    @Test
    @DisplayName("ExtentReports测试：正常登录")
    public void testNormalLoginWithExtent() {
        ExtentReportManager.logInfo("开始ExtentReports风格的登录测试");

        // 记录测试步骤到ExtentReports
        ExtentReportManager.logInfo("步骤1: 打开QQ邮箱页面");
        openQQMailLoginPage();

        ExtentReportManager.logInfo("步骤2: 初始化登录页面");
        initLoginPage();

        ExtentReportManager.logInfo("步骤3: 执行登录流程");
        loginPage.fullLoginProcess();

        ExtentReportManager.logInfo("步骤4: 验证登录结果");
        boolean isSuccess = loginPage.isLoginSuccessful();

        if (isSuccess) {
            ExtentReportManager.logPass("登录成功测试通过");
            assertTrue(isSuccess);
        } else {
            ExtentReportManager.logFail("登录失败");
            fail("登录应该成功");
        }

        ExtentReportManager.logInfo("ExtentReports测试完成");
    }
    @AfterAll
    public static void tearDownExtentReports() {
        ExtentReportManager.close();
        System.out.println("ExtentReports报告已生成到: test-reports/extentreports/");
    }
}

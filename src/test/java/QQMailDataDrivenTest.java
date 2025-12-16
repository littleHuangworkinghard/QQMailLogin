import Configs.ConfigManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class QQMailDataDrivenTest extends QQMailBaseTest{
    @ParameterizedTest(name = "[{index}] {0}")
    @MethodSource("provideLoginTestData")
    public void testLoginWithMethodSource(String testName, String username,
                                          String password, boolean expectedSuccess){
        System.out.println("执行测试: " + testName);
        System.out.println("用户名: " + username);
        System.out.println("密码: " + maskPassword(password));
        performLoginTest(username,password,expectedSuccess);
    }
    /**
     * 提供测试数据的静态方法
     */
    static Stream<Arguments> provideLoginTestData() throws IOException {
        ConfigManager config = new ConfigManager();
        String mail = config.get("qq.mail");
        String authCode = config.get("qq.auth.code");
        return Stream.of(
                Arguments.of("正确凭证", mail, authCode, true),
                Arguments.of("错误密码", "test@qq.com", "wrong_password", false),
                Arguments.of("空用户名", "", "valid_auth_code", false),
                Arguments.of("空密码", "test@qq.com", "", false)
        );
    }
    /**
     * 隐藏敏感信息
     */
    private String maskPassword(String password) {
        if (password == null || password.length() <= 3) {
            return "***";
        }
        return password.substring(0, 3) + "***" + password.substring(password.length() - 3);
    }
    // ========== 方法2：使用 @CsvSource 提供数据 ==========

    @ParameterizedTest(name = "CSV测试：{0}")
    @CsvSource({
            "正确邮箱和授权码, test@qq.com, valid_auth_code, true",
            "错误授权码, test@qq.com, wrong_code, false",
            "无效邮箱格式, invalid_email, valid_auth_code, false"
    })
    @DisplayName("CSV源数据驱动测试")
    public void testLoginWithCsvSource(String testName, String username,
                                       String password, boolean expectedSuccess) {
        System.out.println("CSV测试: " + testName);
        performLoginTest(username, password, expectedSuccess);
    }

    // ========== 方法3：使用 @CsvFileSource 从文件读取 ==========

    @ParameterizedTest(name = "文件测试：{0}")
    @CsvFileSource(resources = "/testData/login_data.csv", numLinesToSkip = 1)
    @DisplayName("CSV文件数据驱动测试")
    public void testLoginWithCsvFile(String testName, String username,
                                     String password, String expectedResult) {
        boolean expectedSuccess = "SUCCESS".equals(expectedResult);
        System.out.println("文件测试: " + testName);
        performLoginTest(username, password, expectedSuccess);
    }

    // ========== 方法4：使用 TestData 对象 ==========

    @ParameterizedTest(name = "对象测试：{0}")
    @MethodSource("provideTestDataObjects")
    @DisplayName("对象数据驱动测试")
    public void testLoginWithDataObject(TestData testData) {
        System.out.println("测试用例: " + testData.getTestCaseName());
        System.out.println("描述: " + testData.getDescription());

        performLoginTest(
                testData.getUserName(),
                testData.getPassword(),
                testData.isExpectedResult()
        );
    }

    static Stream<TestData> provideTestDataObjects() {
        return TestDataProvider.provideTestData();
    }

    // ========== 辅助方法 ==========

    /**
     * 执行登录测试的通用方法
     */
    private void performLoginTest(String username, String password, boolean expectedSuccess) {
        openQQMailLoginPage();
        initLoginPage();

        try {
            // 先切换到密码登录模式
            loginPage.switchToPwdLogin();

            // 输入凭证
            loginPage.login(username, password);

            // 提交登录
            loginPage.submitLogin();

            // 等待并检查结果
            boolean actualSuccess = false;
            try {
                actualSuccess = loginPage.isLoginSuccessful();
            } catch (Exception e) {
                // 登录失败的情况
                actualSuccess = false;
            }

            // 验证结果
            if (expectedSuccess) {
                assertTrue(actualSuccess,
                        "测试用例预期成功但实际失败。用户名: " + username);
                System.out.println("✅ 测试通过：预期成功，实际成功");
            } else {
                assertFalse(actualSuccess,
                        "测试用例预期失败但实际成功。用户名: " + username);
                System.out.println("✅ 测试通过：预期失败，实际失败");
            }

        } catch (Exception e) {
            // 处理异常情况
            if (!expectedSuccess) {
                System.out.println("✅ 测试通过：预期失败，抛出异常");
            } else {
                fail("预期成功但发生异常: " + e.getMessage());
            }
        }
    }
}

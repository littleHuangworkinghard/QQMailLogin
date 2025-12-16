import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class QQMailLoginTest extends QQMailBaseTest {
    @Test
    public void testNornalLogin() {
        openQQMailLoginPage();
        initLoginPage();
        loginPage.fullLoginProcess();
        Assertions.assertTrue(loginPage.isLoginSuccessful());
        System.out.println("✅ 测试通过：正常登录成功");
    }

    @Test
    public void testMultipleLoginAttempts() {
        openQQMailLoginPage();
        initLoginPage();
        for (int i = 1; i <= 3; i++) {
            System.out.println("第" + i + "次登录尝试...");
            try {
                loginPage.fullLoginProcess();
                boolean isSuccess = loginPage.isLoginSuccessful();

                if (isSuccess) {
                    System.out.println("✅ 第" + i + "次登录成功");
                    break;
                } else {
                    System.out.println("❌ 第" + i + "次登录失败");
                    // 刷新页面重新尝试
                    webDriver.navigate().refresh();
                    Thread.sleep(2000);
                    initLoginPage(); // 重新初始化页面对象
                }
            } catch (Exception e) {
                System.err.println("第" + i + "次登录出错: " + e.getMessage());
            }
        }
    }
}

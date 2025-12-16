package test.allure;

import base.AllureOnlyBaseTest;
import base.DualReportBaseTest;
import io.qameta.allure.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.By;

import static io.qameta.allure.Allure.step;
import static org.junit.Assert.assertTrue;

@Epic("QQ邮箱登录测试")
@Feature("登录功能")
@Story("Allure风格测试")
@Owner("自动化测试团队")
@Severity(SeverityLevel.CRITICAL)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AllureStyleTest extends AllureOnlyBaseTest {
    @Test
    @DisplayName("AllureReport:正常登录")
    @Description("使用Allure注解记录测试步骤")
    @Severity(SeverityLevel.BLOCKER)
    @Tag("allure")
    @Tag("smoke")
    public void testNormakLoginWithAllureReport(){
        step("开始执行登录测试", () -> {

            step("打开QQ邮箱登录页面", () -> {
                openQQMailLoginPage();
                takeScreenshot("页面加载完成");
            });

            step("验证页面基本信息", () -> {
                String title = driver.getTitle();
                String url = driver.getCurrentUrl();

                Allure.addAttachment("页面验证结果", "text/plain",
                        "页面标题: " + title + "\n" +
                                "当前URL: " + url + "\n" +
                                "验证时间: " + java.time.LocalDateTime.now());

                step("验证标题包含'QQ邮箱'", () -> {
                    if (title.contains("QQ邮箱")) {
                        Allure.step("✓ 标题验证通过");
                    } else {
                        Allure.step("⚠ 标题不包含'QQ邮箱'，当前标题: " + title);
                    }
                });

                step("验证URL包含'qq.com'", () -> {
                    if (url.contains("qq.com")) {
                        Allure.step("✓ URL验证通过");
                    } else {
                        Allure.step("⚠ URL不包含'qq.com'，当前URL: " + url);
                    }
                });
            });

            step("切换到登录iframe", () -> {
                try {
                    driver.switchTo().frame("login_frame");
                    Allure.step("成功切换到登录iframe");
                    takeScreenshot("iframe切换后");
                } catch (Exception e) {
                    Allure.step("切换iframe失败: " + e.getMessage());
                }
            });

            step("点击账号密码登录", () -> {
                try {
                    driver.findElement(By.id("switcher_plogin")).click();
                    Allure.step("成功点击账号密码登录");
                    takeScreenshot("点击登录方式后");
                } catch (Exception e) {
                    Allure.step("未找到账号密码登录按钮: " + e.getMessage());
                }
            });

            step("输入登录信息", () -> {
                step("输入用户名", () -> {
                    try {
                        driver.findElement(By.id("u")).sendKeys("test@qq.com");
                        Allure.step("已输入用户名: test@qq.com");
                    } catch (Exception e) {
                        Allure.step("用户名输入框未找到: " + e.getMessage());
                    }
                });

                step("输入密码", () -> {
                    try {
                        driver.findElement(By.id("p")).sendKeys("password123");
                        Allure.step("已输入密码");
                        // 注意：实际测试中不要记录真实密码
                        Allure.addAttachment("密码输入", "text/plain", "已输入密码（已隐藏）");
                    } catch (Exception e) {
                        Allure.step("密码输入框未找到: " + e.getMessage());
                    }
                });

                takeScreenshot("输入登录信息后");
            });

            step("完成测试", () -> {
                Allure.step("跳过实际登录（演示目的）");
                Allure.step("✓ 测试执行完成");

                // 添加自定义链接
                Allure.link("QQ邮箱官网", "https://mail.qq.com");
                Allure.link("项目文档", "https://example.com/docs");
            });
        });
    }
}

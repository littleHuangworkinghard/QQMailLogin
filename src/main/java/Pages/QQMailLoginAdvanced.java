package Pages;

import Configs.ConfigManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.time.Duration;

public class QQMailLoginAdvanced {
    private WebDriver webDriver;
    private WebDriverWait wait;

    private ConfigManager configManager;
    private final By loginFrame = By.id("login_frame");
    private final By ptloginiFrame = By.id("ptlogin_iframe");
    private final By pwdLoginLocator = By.xpath("//div[@id=\"bottom_qlogin\"]/a[@id=\"switcher_plogin\"]");
    private final By userNameLocator = By.id("u");
    private final By pwdLocator = By.id("p");

    private final By loginLocator = By.id("login_button");

    public QQMailLoginAdvanced(WebDriver webDriver) {
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));

        try{
            this.configManager = new ConfigManager();
            System.out.println("配置文件加载成功");

        } catch (IOException e){
            System.err.println("警告: 无法加载配置文件，请创建config.properties文件");
            System.err.println("错误信息: " + e.getMessage());
            // 可以在这里设置默认值或抛出异常
            throw new RuntimeException("配置文件加载失败", e);
        }
    }
    public QQMailLoginAdvanced(WebDriver webDriver, ConfigManager config){
        this.webDriver = webDriver;
        this.wait = new WebDriverWait(webDriver, Duration.ofSeconds(15));
        this.configManager = config;
    }
    public void switchToPwdLogin() {
        WebElement login_iframe = wait.until(ExpectedConditions.presenceOfElementLocated(loginFrame));
        webDriver.switchTo().frame(login_iframe);
        WebElement ptlogini_Frame = wait.until(ExpectedConditions.presenceOfElementLocated(ptloginiFrame));
        webDriver.switchTo().frame(ptlogini_Frame);
        wait.until(ExpectedConditions.presenceOfElementLocated(pwdLoginLocator)).click();
    }
    public void login(String username,String pwd){
        WebElement userNameInput= wait.until(ExpectedConditions.presenceOfElementLocated(userNameLocator));
        userNameInput.clear();
        userNameInput.sendKeys(username);
        WebElement pwdInput = wait.until(ExpectedConditions.presenceOfElementLocated(pwdLocator));
        pwdInput.clear();;
        pwdInput.sendKeys(pwd);
        System.out.println("输入用户名: " + username);
        System.out.println("输入密码: " + maskPassword(pwd));
    }
    /*自动登录方法*/
    public void autoLogin(){
        try{
            String userName = configManager.getQQMail();
            String password = configManager.getAuthCode();
            switchToPwdLogin();
            login(userName,password);
            System.out.println("自动登录成功完成");
        }catch(Exception e){
            System.err.println("自动登录失败: " + e.getMessage());
            throw new RuntimeException("自动登录失败", e);
        }
    }
    public void submitLogin() {
        // 按回车键提交（或找到登录按钮点击）
        wait.until(ExpectedConditions.presenceOfElementLocated(loginLocator)).click();
        System.out.println("提交登录表单");
    }
    /**
     * 完整的自动化登录流程
     */
    public void fullLoginProcess() {
        System.out.println("开始QQ邮箱自动登录流程...");
        autoLogin();
        submitLogin();
        System.out.println("登录流程完成");
    }
    /**
     * 检查是否登录成功（根据页面元素判断）
     */
    public boolean isLoginSuccessful() {
        try {
            // 等待页面标题变化（排除登录页面）
            wait.until(driver -> {
                String title = driver.getTitle();
                // 成功条件：有QQ邮箱但不是登录页面
                return title != null &&
                        title.contains("QQ邮箱") &&
                        !title.contains("登录QQ邮箱") &&
                        !title.contains("登录");
            });

            System.out.println("登录成功，页面标题: " + webDriver.getTitle());
            return true;

        } catch (Exception e) {
            System.err.println("登录失败或页面未跳转: " + e.getMessage());
            return false;
        }
    }
    /**
     * 返回配置管理器（可选，如果需要其他配置）
     */
    public ConfigManager getConfig() {
        return configManager;
    }

    /**
     * 辅助方法：隐藏密码显示
     */
    private String maskPassword(String password) {
        if (password == null || password.length() <= 3) {
            return "***";
        }
        return password.substring(0, 3) + "***" + password.substring(password.length() - 3);
    }
}

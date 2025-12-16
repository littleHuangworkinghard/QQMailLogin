import Pages.QQMailLoginAdvanced;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.time.Duration;


public class TestQQLogin {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver webDriver = null;
        try{
            webDriver = new ChromeDriver();
            webDriver.manage().window().maximize();
            webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            webDriver.get("https://mail.qq.com");
            System.out.println("已打开QQ邮箱登录页面");

            QQMailLoginAdvanced loginPage = new QQMailLoginAdvanced(webDriver);
            loginPage.fullLoginProcess();
            if(loginPage.isLoginSuccessful()){
                System.out.println("=== 登录成功！ ===");
            } else {
                System.out.println("=== 登录失败 ===");
            }

        }catch(Exception e){
            System.err.println("程序执行出错: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (webDriver!=null){
                webDriver.quit();
                System.out.println("浏览器已关闭");
            }

        }
    }
}

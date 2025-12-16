import Pages.QQMailLoginAdvanced;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

public class QQMailBaseTest {
    protected WebDriver webDriver;
    protected QQMailLoginAdvanced loginPage;
    @BeforeEach
    public void setUp(){
        WebDriverManager.chromedriver().setup();
        // 设置Chrome选项（可选）
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
//        options.addArguments("--headless");  // 无头模式，不显示浏览器

        webDriver= new ChromeDriver(options);
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        webDriver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        System.out.println("WebDriver初始化完成");
    }
    @AfterEach
    public void tearDown(){
        if(webDriver!=null){
            webDriver.quit();
            System.out.println("WebDriver已关闭");
        }
    }

    protected void openQQMailLoginPage(){
        webDriver.get("https://mail.qq.com");
        System.out.println("已打开QQ邮箱登录页面: " + webDriver.getTitle());
        try{
            Thread.sleep(2000);
        }catch (InterruptedException e){
            e.printStackTrace();;
        }
    }
    protected void initLoginPage() {
        loginPage = new QQMailLoginAdvanced(webDriver);
        System.out.println("登录页面对象初始化完成");
    }
}

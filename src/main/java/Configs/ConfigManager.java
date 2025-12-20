package Configs;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigManager {
    private Properties props;

    public ConfigManager() throws IOException {
        props = new Properties();
        String mailFromSystem = System.getProperty("qq.mail");
        String authCodeFromSystem = System.getProperty("qq.auth.code");

        // 2. 如果系统属性没有，尝试从环境变量获取
        if (mailFromSystem == null || mailFromSystem.trim().isEmpty()) {
            mailFromSystem = System.getenv("QQ_MAIL");
        }
        if (authCodeFromSystem == null || authCodeFromSystem.trim().isEmpty()) {
            authCodeFromSystem = System.getenv("QQ_AUTH_CODE");
        }
        // 1. 无论系统属性是否存在，都先尝试加载配置文件（获取所有基础配置）
        String configFileName = (System.getenv("CI") != null || System.getenv("JENKINS_URL") != null)
                ? "config-ci.properties"
                : "config.properties";
        loadConfigFile(configFileName);

        // 2. 如果系统属性提供了核心凭据，则用它们覆盖配置文件中的值（最高优先级）
        if (mailFromSystem != null && !mailFromSystem.trim().isEmpty()) {
            props.setProperty("qq.mail", mailFromSystem);
            System.out.println("[INFO] 配置 ‘qq.mail’ 已从系统属性覆盖。");
        }
        if (authCodeFromSystem != null && !authCodeFromSystem.trim().isEmpty()) {
            props.setProperty("qq.auth.code", authCodeFromSystem);
            System.out.println("[INFO] 配置 ‘qq.auth.code’ 已从系统属性覆盖。");
        }

        // 3. 最终检查必要配置是否存在
        if (props.getProperty("qq.mail") == null || props.getProperty("qq.auth.code") == null) {
            throw new RuntimeException("配置加载失败：缺少必要的 ‘qq.mail’ 或 ‘qq.auth.code’ 配置项。");
        }
        System.out.println("[INFO] 配置加载完成。");
    }

    /**
     * 加载指定文件名的配置文件
     */
    private void loadConfigFile(String configFileName) throws IOException {
        InputStream input = getClass().getClassLoader().getResourceAsStream(configFileName);
        if (input == null) {
            // 对于本地开发，可尝试从文件系统读取
            File configFile = new File(configFileName);
            if (configFile.exists()) {
                input = new FileInputStream(configFile);
            } else {
                throw new FileNotFoundException("未找到配置文件: " + configFileName + "。请确保文件位于类路径或当前目录。");
            }
        }
        // 使用 StandardCharsets.UTF_8 指定编码
        props.load(new InputStreamReader(input, StandardCharsets.UTF_8));
        System.out.println("[INFO] 已加载基础配置文件: " + configFileName);
    }

    // 获取QQ邮箱用户名
    public String getQQMail() {
        return props.getProperty("qq.mail");
    }

    // 获取授权码（不是QQ密码！）
    public String getAuthCode() {
        return props.getProperty("qq.auth.code");
    }

    // 可选：获取SMTP服务器
    public String getSmtpHost() {
        return props.getProperty("smtp.host", "smtp.qq.com");
    }

    // 可选：获取SMTP端口
    public int getSmtpPort() {
        return Integer.parseInt(props.getProperty("smtp.port", "587"));
    }

    // 通用方法（如果需要其他配置）
    public String get(String key) {
        return props.getProperty(key);
    }

    public String get(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}

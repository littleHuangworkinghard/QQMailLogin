package Configs;

import java.io.*;
import java.util.Properties;

public class ConfigManager {
    private Properties props;

    public ConfigManager() throws IOException {
        props = new Properties();
        // 方式1：从resources目录加载（推荐）
        InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");

        // 方式2：如果方式1失败，尝试从当前目录加载
        if (input == null) {
            File configFile = new File("config.properties");
            if (configFile.exists()) {
                input = new FileInputStream(configFile);
            } else {
                throw new FileNotFoundException("请创建 config.properties 文件");
            }
        }

        props.load(new InputStreamReader(input, "UTF-8"));
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

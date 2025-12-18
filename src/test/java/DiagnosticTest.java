import org.junit.jupiter.api.Test;
public class DiagnosticTest {
    @Test
    public void testChineseOutput() {
        // 测试1: 直接输出中文字符串
        System.out.println("[诊断] 测试直接输出中文");
        // 测试2: 输出带变量的中文
        String var = "变量";
        System.out.println("[诊断] 测试带变量的中文: " + var);
    }
}
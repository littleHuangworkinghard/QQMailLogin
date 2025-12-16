
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestDataProvider {
    public static List<TestData> getAllTestData() {
        return Arrays.asList(
                // 正常情况
                new TestData("TC001", "valid_user@qq.com", "correct_auth_code",
                        true, "正确的用户名和授权码"),

                // 异常情况
                new TestData("TC002", "invalid_user@qq.com", "correct_auth_code",
                        false, "错误的用户名"),

                new TestData("TC003", "valid_user@qq.com", "wrong_auth_code",
                        false, "错误的授权码"),

                new TestData("TC004", "", "correct_auth_code",
                        false, "用户名为空"),

                new TestData("TC005", "valid_user@qq.com", "",
                        false, "授权码为空"),

                new TestData("TC006", "user_with_space @qq.com", "correct_auth_code",
                        false, "用户名包含空格"),

                new TestData("TC007", "invalid_email_format", "correct_auth_code",
                        false, "无效的邮箱格式"),

                new TestData("TC008", "valid_user@qq.com", "123",
                        false, "授权码过短"),

                new TestData("TC009", "valid_user@qq.com", "very_long_auth_code_that_is_too_long",
                        false, "授权码过长")
        );
    }
    /**
     * 获取正向测试数据（预期成功的）
     */
    public static List<TestData> getPositiveTestData() {
        return getAllTestData().stream()
                .filter(data -> data.isExpectedResult())
                .toList();
    }

    /**
     * 获取负向测试数据（预期失败的）
     */
    public static List<TestData> getNegativeTestData() {
        return getAllTestData().stream()
                .filter(data -> !data.isExpectedResult())
                .toList();
    }

    /**
     * 为JUnit参数化测试提供数据流
     */
    public static Stream<TestData> provideTestData() {
        return getAllTestData().stream();
    }

    /**
     * 从CSV文件加载测试数据（可选）
     */
    public static List<TestData> loadTestDataFromCSV(String filePath) {
        // 这里可以实现从CSV文件读取测试数据
        // 简化版本：返回示例数据
        return Arrays.asList(
                new TestData("CSV001", "csv_user1@qq.com", "csv_auth1", true, "CSV数据1"),
                new TestData("CSV002", "csv_user2@qq.com", "csv_auth2", true, "CSV数据2")
        );
    }
}

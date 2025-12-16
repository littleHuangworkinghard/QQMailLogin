public class TestData {
    private String testCaseName;
    private String userName;
    private String password;
    private boolean expectedResult;
    private String description;


    public TestData(String testCaseName, String userName, String password, boolean expectedResult, String description) {
        this.testCaseName = testCaseName;
        this.userName = userName;
        this.password = password;
        this.expectedResult = expectedResult;
        this.description = description;
    }

    public String getTestCaseName() {
        return testCaseName;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public boolean isExpectedResult() {
        return expectedResult;
    }

    public String getDescription() {
        return description;
    }
    @Override
    public String toString(){
        return testCaseName + " - " + description;
    }
}

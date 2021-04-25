package testUtils.Listeners;


import com.relevantcodes.extentreports.LogStatus;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import testUtils.ExtentReports.ExtentTestManager;

public class TestRetry implements IRetryAnalyzer {

    private int count = 0;
    private static int maxTry = 1;                           //Run the failed test again

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (count < maxTry) {                            //Check if maxTry count is reached
                count++;                                     //Increase the maxTry count by 1
                iTestResult.setStatus(ITestResult.FAILURE);
                FailOperation();
                return true;                                 //Tells TestNG to re-run the test
            }
        }
        else {
            iTestResult.setStatus(ITestResult.SUCCESS);      //If test passes, TestNG marks it as passed
        }
        return false;
    }

    public void FailOperation() {
        ExtentTestManager.getTest().log(LogStatus.FAIL, "Test Failed");
    }
}

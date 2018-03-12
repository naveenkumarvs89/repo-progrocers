package com.progrocers.Listeners;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.mail.MessagingException;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import com.progrocers.Base.TestBase;
import com.progrocers.Utilities.MonitoringMail;
import com.progrocers.Utilities.TestConfig;
import com.progrocers.Utilities.TestUtil;
import com.relevantcodes.extentreports.LogStatus;

public class CustomListeners extends TestBase implements ITestListener, ISuiteListener {

	public String messagebody;

	public void onStart(ISuite suite) {
		// TODO Auto-generated method stub

	}

	public void onFinish(ISuite suite) {
		// TODO Auto-generated method stub
		MonitoringMail mail = new MonitoringMail();
		try {

			messagebody = "https://" + InetAddress.getLocalHost().getHostAddress()
					+ ":8080/job/ECommerceProject/HTML_Report/";
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		try {
			mail.sendMail(TestConfig.server, TestConfig.from, TestConfig.to, TestConfig.subject, messagebody);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void onTestStart(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onTestSuccess(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onTestFailure(ITestResult result) {
		// TODO Auto-generated method stub
		System.setProperty("org.uncommons.reportng.escape-output", "false");
		try {
			TestUtil.captureScreenshot();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Reporter.log("Test failed, ");
		Reporter.log("<a \" href=" + TestUtil.Screenshotname + ">Screenshot</a>");
		Reporter.log("<br>");
		Reporter.log("<br>");

		Reporter.log("<a \"href=" + TestUtil.Screenshotname + "><img src=" + TestUtil.Screenshotname
				+ " height=200 width=200></img></a>");
		test.log(LogStatus.FAIL, "This step fails",
				result.getName().toUpperCase() + " Failed with Exception " + result.getThrowable());
		test.log(LogStatus.FAIL, test.addScreenCapture(TestUtil.Screenshotname));
		exrep.endTest(test);
		exrep.flush();

		// Below code will logout and proceed with next TC(login with second
		// credential).It will not stop execution in case of failure of first test case
		/*
		 * if(isElementPresent(By.xpath(OR.getProperty("Logout_XPATH")))) {
		 * click("Logout_XPATH"); }
		 */
	}

	public void onTestSkipped(ITestResult result) {
		// TODO Auto-generated method stub
		test.log(LogStatus.SKIP, "This step is skipped", result.getName().toUpperCase() + "Skipped");
		exrep.endTest(test);
		exrep.flush();

	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		// TODO Auto-generated method stub

	}

	public void onStart(ITestContext context) {
		// TODO Auto-generated method stub
		test = exrep.startTest(context.getName().toUpperCase());

	}

	public void onFinish(ITestContext context) {
		// TODO Auto-generated method stub
		test.log(LogStatus.PASS, context.getName().toUpperCase() + "PASS");
		exrep.endTest(test);
		exrep.flush();

	}

}

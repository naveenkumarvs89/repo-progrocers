package com.progrocers.Base;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Reporter;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.progrocers.Utilities.ExcelReader;
import com.progrocers.Utilities.ExtentManager;
import com.progrocers.Utilities.TestUtil;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class TestBase {

	public static WebDriver driver;
	public static Properties config = new Properties();
	public static Properties OR = new Properties();
	public static FileInputStream fis;
	public static Logger log = Logger.getLogger("devinploy");
	public static WebDriverWait wait;
	public static ExtentReports exrep = ExtentManager.getInstance();
	public static ExtentTest test;
	public static ExcelReader excel = new ExcelReader(
			"C:\\Users\\naveenkumar.v.s.HTSS\\git\\repo-progrocers\\online-groceries-shopping-project\\src\\test\\resources\\Excel\\TestSheet.xlsx");
	public static String browser;

	@BeforeSuite
	public void setUp() {
		if (driver == null) {
			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "//src//test//resources//Properties//Config.properties");
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
			try {
				config.load(fis);
				log.debug("Config file is loaded");
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fis = new FileInputStream(
						System.getProperty("user.dir") + "//src//test//resources//Properties//OR.properties");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			try {
				OR.load(fis);
				log.debug("OR file is loaded");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (System.getenv("browser") != null && System.getenv("browser").isEmpty()) {
			browser = System.getenv("browser");
		} else {
			browser = config.getProperty("browser");
		}
		config.setProperty("browser", browser);

		if (config.getProperty("browser").equalsIgnoreCase("chrome")) {
			System.setProperty("webdriver.chrome.driver",
					System.getProperty("user.dir") + "//src//test//resources//Executables//chromedriver.exe");
			driver = new ChromeDriver();
			log.debug("Chrome Launched Successfully");
		} else if (config.getProperty("browser").equalsIgnoreCase("firefox")) {
			System.setProperty("webdriver.gecko.marionette",
					System.getProperty("user.dir") + "//src//test//resources//Executables//geckodriver.exe");
			driver = new FirefoxDriver();
			log.debug("Firefox Launched Successfully");
		} else {
			System.setProperty("webdriver.ie.driver",
					System.getProperty("user.dir") + "//src//test//resources//Executables//IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			log.debug("IE Launched Succesfully");
		}
		driver.get(config.getProperty("baseurl"));
//		driver.manage().window().maximize();
		wait = new WebDriverWait(driver, Integer.parseInt(config.getProperty("implicit.wait")));
	}

	public void click(String locator) {
		if (locator.endsWith("_xpath")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).click();
		} else if (locator.endsWith("_css")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).click();
		}
	}

	public void type(String locator, String value) {
		if (locator.endsWith("_xpath")) {
			driver.findElement(By.xpath(OR.getProperty(locator))).sendKeys(value);
		} else if (locator.endsWith("_css")) {
			driver.findElement(By.cssSelector(OR.getProperty(locator))).sendKeys(value);
		}
	}

	public static WebElement dropDown;

	public void select(String locator, String value) {
		if (locator.endsWith("_xpath")) {
			dropDown = driver.findElement(By.xpath(locator));
		} else if (locator.endsWith("_css")) {
			dropDown = driver.findElement(By.cssSelector(locator));
		}
		Select sel = new Select(dropDown);
		sel.selectByValue(value);
	}

	public boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (ElementNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void assertEquals(String actual, String expected) throws IOException {
		try {
			assertEquals(actual, expected);
		} catch (Throwable t) {
			TestUtil.captureScreenshot();

			Reporter.log("Failed with exception " + t.getMessage());
			Reporter.log("<a target=\"blank \"href=" + TestUtil.Screenshotname + ">screenshot</a>");

			test.log(LogStatus.FAIL, "Verification fails with exception ", t.getMessage());
			test.log(LogStatus.FAIL, test.addScreenCapture(TestUtil.Screenshotname));
		}
	}

	public void assertTextConsistsOf(String actual, String expected) throws IOException {
		try {
			actual.contains(expected);
			log.debug("Search input criteria is passed ");
		} catch (Throwable t) {
			TestUtil.captureScreenshot();

			Reporter.log("Failed with exception " + t.getMessage());
			Reporter.log("<a target=\"blank \"href=" + TestUtil.Screenshotname + ">screenshot</a>");

			test.log(LogStatus.FAIL, "Verification fails with exception ", t.getMessage());
			test.log(LogStatus.FAIL, test.addScreenCapture(TestUtil.Screenshotname));
		}
	}

	@AfterSuite
	public void finish() {
		if (driver != null) {
			driver.close();
			log.debug("Test execution completed");
		}
	}

}

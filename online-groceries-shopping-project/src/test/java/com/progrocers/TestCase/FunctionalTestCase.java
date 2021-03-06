package com.progrocers.TestCase;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.progrocers.Base.TestBase;
import com.progrocers.Utilities.TestUtil;

public class FunctionalTestCase extends TestBase {
	@Test(priority = 0)
	public void brokenLinks() throws MalformedURLException, IOException {
		String URL = null;
		int respCode = 200;
		HttpURLConnection huc = null;
		String baseURL = config.getProperty("baseurl");
		// List of all links in the homepage
		List<WebElement> lstAllLinks = driver.findElements(By.tagName("a"));
		Iterator<WebElement> it = lstAllLinks.iterator();
		while (it.hasNext()) {
			URL = it.next().getAttribute("href");
			if (URL == null || URL.isEmpty()) {
				log.debug(URL + " --> " + "URL is not assigned");
				continue;
			} else if (!URL.startsWith(baseURL)) {
				log.debug(URL + " --> " + "URL belongs to some other domain");
				continue;
			} else {
				// Checking each page has a URL and whether it is connecting or not
				huc = (HttpURLConnection) new URL(URL).openConnection();
				huc.setRequestMethod("HEAD");
				huc.connect();
				respCode = huc.getResponseCode();
				if (respCode >= 400) {
					log.debug(URL + " --> " + "Page Not Found");
				} else {
					log.debug(URL + " --> " + "This is a Valid Link");
				}
			}
		}
	}

	@Test(priority = 1)
	public void searchAlgorithm() {
		String searchInput = "soap";
		type("SearchBox_xpath", searchInput);
		click("SearchSubmit_xpath");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("window.scrollBy(0,800)");
		driver.manage().timeouts().implicitlyWait(10l, TimeUnit.SECONDS);
		// List of searched Pages
		List<WebElement> lstPageContains = driver
				.findElements(By.xpath(".//*[@id='maincontent']/div[3]/div[1]/div[2]/div[3]/div[2]/ul/li"));
		// Dynamic xpath for each page
		for (int i = 1; i < lstPageContains.size(); i++) {
			driver.findElement(By.xpath("//div[@class='toolbar toolbar-products'][2]/div[2]/ul/li[" + i + "]")).click();
			driver.manage().timeouts().implicitlyWait(8l, TimeUnit.SECONDS);

			// List<WebElement> lstProduct =
			// driver.findElements(By.xpath(OR.getProperty("ListOfProducts_xpath")));

			List<WebElement> lstProductLabelLinks = driver.findElements(By.xpath("ListOfLabelLinks_xpath"));

			Iterator<WebElement> itLinksText = lstProductLabelLinks.iterator();

			while (itLinksText.hasNext()) {
				String linkInnerText = itLinksText.next().getText();
				try {
					assertTextConsistsOf(searchInput, linkInnerText);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test(dataProviderClass = TestUtil.class, dataProvider = "dp1")
	public void createNewCustomer(Hashtable<String, String> data) throws InterruptedException {
		WebElement createAccountlink = driver.findElement(By.xpath("//a[text()='Account']"));
		createAccountlink.click();
		Thread.sleep(25l);
		WebElement firstName = driver.findElement(By.name("firstname"));
		firstName.sendKeys(data.get("FirstName"));
		WebElement lastName = driver.findElement(By.name("lastname"));
		lastName.sendKeys(data.get("LastName"));
		Thread.sleep(30l);
	}
}

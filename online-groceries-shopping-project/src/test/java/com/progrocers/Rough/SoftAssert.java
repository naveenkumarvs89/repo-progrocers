package com.progrocers.Rough;

import java.io.IOException;
import java.net.MalformedURLException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import com.progrocers.Base.TestBase;

public class SoftAssert extends TestBase {

	public void brokenLinks() throws MalformedURLException, IOException {
		System.setProperty("webdriver.chrome.driver",
				System.getProperty("user.dir") + "//src//test//resources//Executables//chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.get("http://progrocers.in");
		
		type("SearchBox_xpath", "soap");
		click("SearchSubmit_xpath");
		
		driver.close();
	}
}

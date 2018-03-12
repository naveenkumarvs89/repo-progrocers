package com.progrocers.Utilities;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Hashtable;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.DataProvider;

import com.progrocers.Base.TestBase;

public class TestUtil extends TestBase {
	public static String Screenshotname;

	public static void captureScreenshot() throws IOException {
		File scrnfile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		Date d = new Date();

		Screenshotname = d.toString().replace(":", "_").replace(" ", "_") + ".jpg";

		// copying file to project directory

		FileUtils.copyFile(scrnfile,
				new File(System.getProperty("user.dir") + "\\target\\surefire-reports\\html\\" + Screenshotname));
	}
	/*
	 * @DataProvider public Object[][] custData() { String sheetname = "Customer";
	 * 
	 * int row = excel.getRowCount(sheetname); int col =
	 * excel.getColumnCount(sheetname); System.out.println(row);
	 * System.out.println(col);
	 * 
	 * Object datas[][]= new Object[row-1][col];
	 * 
	 * for(int rowcnt=2;rowcnt<=row;rowcnt++) { for(int
	 * colcnt=0;colcnt<col;colcnt++) { datas[rowcnt-2][colcnt]=
	 * excel.getCellData(sheetname, colcnt, rowcnt);
	 * System.out.println(datas[rowcnt-2][colcnt]); } }
	 * 
	 * return datas;
	 * 
	 * }
	 */

	// Implementing common data provider for all testcases by passing Method m
	@DataProvider
	public Object[][] data(Method m) {

		String sheetname = m.getName();

		int row = excel.getRowCount(sheetname);
		int col = excel.getColumnCount(sheetname);
		System.out.println(row);
		System.out.println(col);

		Object datas[][] = new Object[row - 1][col];

		for (int rowcnt = 2; rowcnt <= row; rowcnt++) {
			for (int colcnt = 0; colcnt < col; colcnt++) {
				datas[rowcnt - 2][colcnt] = excel.getCellData(sheetname, colcnt, rowcnt);
				System.out.println(datas[rowcnt - 2][colcnt]);
			}
		}

		return datas;

	}

	// Implementing common data provider using HashTable
	@DataProvider(name = "DP")
	public Object[][] getdata(Method m) {
		String sheetname = m.getName();
		int row = excel.getRowCount(sheetname);
		int col = excel.getColumnCount(sheetname);

		// Initializing 2D Object array

		Object datas[][] = new Object[row - 1][1];

		// Initializing Hashtable

		Hashtable<String, String> tab = null;

		for (int rowcnt = 2; rowcnt <= row; rowcnt++) {
			tab = new Hashtable<String, String>();
			for (int colcnt = 0; colcnt < col; colcnt++) {
				tab.put(excel.getCellData(sheetname, colcnt, 1), excel.getCellData(sheetname, colcnt, rowcnt));
				datas[rowcnt - 2][0] = tab;
			}
		}

		return datas;

	}

	public static boolean isTestRunnable(String testname, ExcelReader excel) {
		String sheetname = "TestSuite";
		int row = excel.getRowCount(sheetname);

		for (int rownum = 2; rownum <= row; rownum++) {
			String testcase = excel.getCellData(sheetname, "TCID", rownum);
			if (testcase.equalsIgnoreCase(testname)) {
				String runmode = excel.getCellData(sheetname, "Runmode", rownum);
				{
					if (runmode.equalsIgnoreCase("Y")) {
						return true;
					} else
						return false;
				}
			}
		}
		return false;

	}

}

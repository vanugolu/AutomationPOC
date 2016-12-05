package com.rbc;

import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;

import com.rbc.util.Functions;
import com.rbc.xls.ExcelOperations;

public class Keywords {
	public String version;
	public String user;
	public String password;
	public static Properties CONFIG;

	public static Properties OR;
	public static Properties APPTEXT;
	public static Properties testCONFIG;
	public Properties reportCONFIG;
	public ExcelOperations controller;
	public ExcelOperations testData;
	public boolean testCasesfileAssigned = false;
	public boolean testDatafileAssigned = false;

	public String currentTest;
	public String currentTest_Description;
	public String keyword;
	public RemoteWebDriver driver = null, driver1 = null, driver2 = null, driver3 = null, driver4 = null, driver5 = null;
	public String object;
	public String objectArr[];
	public String currentTSID;
	public String stepDescription;
	public String proceedOnFail;
	public String testStatus;
	public String data_column_name;
	public String data_column_nameArr[];
	public String data;
	public int testRepeat;
	public String testCaseDescription;
	public Logger log;
	public String userAgent = "Desktop";

	public String runTestApp;
	public String runTest;
	public String runModule;
	public String testBrowser;
	public String launchBrowser;
	public String displayBrowserVersion;

	public static String reportFolder;
	public File html;
	public String captureScreenShot = "true";
	public String screenshotFolder;
	public int browserNumber = 1;
	public boolean mkDir = false;
	public Keywords keywords;
	public String modules[] = new String[1];
	public String sanityModules[] = new String[1];
	public String browsers[] = { "Firefox", "InternetExplorer", "Chrome" };
	public int moduleFailCount;
	long WAIT1SEC = 1000, WAIT2SEC = 2000, WAIT3SEC = 3000, WAIT4SEC = 4000, WAIT5SEC = 5000, WAIT6SEC = 6000, WAIT7SEC = 7000,
			WAIT8SEC = 8000;
	// long WAIT1SEC=2000, WAIT2SEC=4000, WAIT3SEC=6000, WAIT4SEC=8000,
	// WAIT5SEC=10000, WAIT6SEC=12000, WAIT7SEC=14000, WAIT8SEC=15000;

	public Actions action;

	public Keywords() {

	}

	public By getBy(Properties objectFile, String locator) {

		By by = null;
		String value = null;

		try {
			value = objectFile.getProperty(locator);

			if (locator.endsWith("xpath"))
				by = By.xpath(value);
			else if (locator.endsWith("id"))
				by = By.id(value);
			else if (locator.endsWith("cssSelector"))
				by = By.cssSelector(value);
			else if (locator.endsWith("linkText"))
				by = By.linkText(value);
			else if (locator.endsWith("partialLinkText"))
				by = By.partialLinkText(value);
			else if (locator.endsWith("tagName"))
				by = By.tagName(value);
			else if (locator.endsWith("name"))
				by = By.name(value);
			else if (locator.endsWith("className"))
				by = By.className(value);
			else
				by = By.xpath(value); // statement added to cater to the rest
										// locator properties
		} catch (Throwable t) {
			log.debug("Exception caught while accessing the locator :" + locator);
		}
		return by;
	}

	public WebElement getWebElement(Properties objectFile, String locator) {
		WebElement element = null;

		try {

			element = driver.findElement(getBy(objectFile, locator));
			// Functions.highlighter(driver, element);

		} catch (Throwable t) {
			log.debug("Exception caught at object :" + locator);
		}
		return element;
	}

	public List<WebElement> getWebElements(Properties objectFile, String locator) {
		List<WebElement> element = null;
		try {
			element = driver.findElements(getBy(objectFile, locator));

		} catch (Throwable t) {
			log.debug("Exception caught at object :" + locator);

		}
		return element;
	}

	public String extractUser() {

		try {
			String url = CONFIG.getProperty(objectArr[1]);

			user = url.substring(0, url.indexOf(":"));

			/*
			 * //Commenting the code due for security issues
			 * log.debug("User: "+user);
			 */
		} catch (Throwable t) {
			log.debug(t.getMessage());

		}
		return user;
	}

	public String extractPassword() {

		try {
			String url = CONFIG.getProperty(objectArr[1]);

			password = url.substring(url.indexOf(":") + 1, url.length());

			/*
			 * //Commenting the code due for security issues
			 * log.debug("Password: "+password);
			 */
		} catch (Throwable t) {
			log.debug(t.getMessage());

		}
		return password;
	}

	public String navigateMultiWindow() {
		log.debug("=============================");
		log.debug("Executing navigateMultiWindow");

		try {
			int b = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/chromedriver.exe");
			log.debug("No of windows to open " + b);
			for (int i = 0; i < b; i++) {

				switch (i) {

				case 0:
					driver1 = new ChromeDriver();
					driver1.manage().window().maximize();
					log.debug("Window 1 launched");
					break;
				case 1:
					driver2 = new ChromeDriver();
					driver2.manage().window().maximize();
					log.debug("Window 2 launched");
					break;
				case 2:
					driver3 = new ChromeDriver();
					driver3.manage().window().maximize();
					log.debug("Window 3 launched");
					break;
				case 3:
					driver4 = new ChromeDriver();
					driver4.manage().window().maximize();
					log.debug("Window 4 launched");
					break;
				case 4:
					driver5 = new ChromeDriver();
					driver5.manage().window().maximize();
					log.debug("Window 5 launched");
					break;
				}
			}
			return "Pass";
		} catch (Throwable t) {
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String setDriver() {
		log.debug("=============================");
		log.debug("Executing setDriver");
		try {
			int a = Integer.parseInt(testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
			switch (a) {
			case 1:
				driver = driver1;
				System.out.println(driver.getCurrentUrl());
				break;
			case 2:
				driver = driver2;
				System.out.println(driver.getCurrentUrl());
				break;
			case 3:
				driver = driver3;
				System.out.println(driver.getCurrentUrl());
				break;
			case 4:
				driver = driver4;
				System.out.println(driver.getCurrentUrl());
				break;
			case 5:
				driver = driver5;
				System.out.println(driver.getCurrentUrl());
				break;
			}
			return "Pass";
		}

		catch (Throwable t) {
			log.debug(t.getMessage());
			return "Fail";
		}

	}

	public String clickLink() {
		log.debug("=============================");
		log.debug("Executing clickLink");
		try {

			try {
				log.debug("Content of the item clicked :" + getWebElement(OR, objectArr[0]).getText());
			} catch (Throwable t) {
				log.debug(t);
			}

			if (!testBrowser.equals("InternetExplorer"))
				getWebElement(OR, objectArr[0]).click();
			else {
				WebElement ele = getWebElement(OR, objectArr[0]);
				ele.sendKeys(Keys.CONTROL);
				ele.click();
			}

			// Handling Data Unavailable Pop Up for Specific Dev and QA
			// Environment pages
			// Functions.handleDataUnavailablePopUp(driver, log, CONFIG);
			// Functions.handleExceptionHandlingPopUp(driver, log, CONFIG);

		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0] + t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	/**
	 * Click html icon
	 * 
	 * @return
	 */
	public String clickButton() {
		log.debug("=============================");
		log.debug("Executing clickButton");
		try {

			try {
				log.debug("Content of the item clicked :" + getWebElement(OR, objectArr[0]).getText());
			} catch (Throwable t) {
				// do nothing
			}
			getWebElement(OR, objectArr[0]).click();

		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on Button -" + objectArr[0] + t.getMessage());
			return "Fail - Button Not Found";
		}
		return "Pass";
	}

	public String clickByText() {
		log.debug("=============================");
		log.debug("Executing clickByText");
		boolean flag = false;
		try {

			try {
				log.debug("Content of the item clicked :" + getWebElement(OR, objectArr[0]).getText());
			} catch (Throwable t) {
				// do nothing
			}
			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			List<WebElement> elements = getWebElements(OR, objectArr[0]);
			for (WebElement webElement : elements) {
				if (webElement.getText().toLowerCase().contains(data.toLowerCase())) {
					webElement.click();
					flag = true;
					break;
				}
			}
			if (flag == false) {
				log.debug("No buton found to click with text-" + data);
				return "Fail - Button Not Found";
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on Buttonby text-" + objectArr[0] + t.getMessage());
			return "Fail - Button Not Found";
		}
		return "Pass";
	}

	public String clickCheckBox() {
		log.debug("=============================");
		log.debug("Executing clickCheckBox Keyword");
		try {

			if (!(driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).isSelected())) {
				driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).click();
				Thread.sleep(WAIT2SEC);
			}
		} catch (Throwable t) {
			log.debug("Error while clicking on checkbox -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String clickLink_linkText() {
		log.debug("=============================");
		log.debug("Executing clickLink_linkText");
		try {
			String linktext = objectArr[0];
			driver.findElement(By.linkText(linktext.substring(5))).click();
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0] + t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	public String input() {
		log.debug("=============================");
		log.debug("Executing input Keyword");
		// extract the test data
		try {
			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("input data@@@@@ -" + data);
			WebElement element = getWebElement(OR, objectArr[0]);
			element.clear();
			element.sendKeys(data);
			log.debug("input data -" + data);
		} catch (Throwable t) {
			// report error
			log.debug("Error while writing into input -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String actionInput() {
		log.debug("=============================");
		log.debug("Executing actionInput Keyword");
		// extract the test data
		try {
			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			action.moveToElement(driver.findElement(By.xpath(OR.getProperty(objectArr[0]))));
			action.click();
			action.sendKeys(data);
			action.build().perform();
		} catch (Throwable t) {
			// report error
			log.debug("Error while writing into actionInput -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String inputClear() {
		log.debug("=============================");
		log.debug("Executing inputClear Keyword");

		try {
			getWebElement(OR, objectArr[0]).clear();

		} catch (Throwable t) {
			// report error
			log.debug("Error while clearing the textfield -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String Wait() {
		log.debug("=============================");
		log.debug("Executing wait Keyword");

		/*
		 * try{ if(!(testBrowser.equals("Safari"))) {
		 * Functions.waitForElementClickable(driver, log, objectArr[1]); }
		 * }catch(ArrayIndexOutOfBoundsException e) {
		 * 
		 * }
		 */

		try {
			String data = OR.getProperty(objectArr[0]);
			Thread.sleep(Long.parseLong(data));
		} catch (Throwable t) {

		}

		return "Pass";
	}

	public String waitForElementAndClick() {
		log.debug("=============================");
		log.debug("Executing waitForElementAndClick Keyword");

		try {
			String data = OR.getProperty(objectArr[0]);

			if (!(launchBrowser.equals("Safari")))
				Functions.waitForElementClickable(driver, log, objectArr[1]);
			else
				Thread.sleep(Long.parseLong(data));

			Thread.sleep(Long.parseLong(data));

			if (!testBrowser.equals("InternetExplorer"))
				getWebElement(OR, objectArr[1]).click();
			else {
				WebElement ele = getWebElement(OR, objectArr[1]);
				ele.sendKeys(Keys.CONTROL);
				ele.click();
			}

		} catch (Throwable t) {
			log.debug("Error while executing waitForElementAndClick-" + t.getMessage());
			return "Fail";
		}

		return "Pass";
	}

	public String rightClickToClickElement() {
		log.debug("=============================");
		log.debug("Executing rightClickToClickElement Keyword");

		try {
			String data = OR.getProperty(objectArr[0]);

			WebElement obj = driver.findElement(By.xpath(OR.getProperty(objectArr[2])));
			(new Actions(driver)).contextClick(obj).perform();
			try {
				Functions.waitForElementClickable(driver, log, objectArr[1]);
			} catch (ArrayIndexOutOfBoundsException e) {

			}

			Thread.sleep(Long.parseLong(data));

			getWebElement(OR, objectArr[1]).click();

		} catch (Throwable t) {
			log.debug("Error while executing rightClickToClickElement" + t.getMessage());
			return "Fail";
		}

		return "Pass";
	}

	public String closeBrowser() {
		log.debug("=============================");
		log.debug("Executing closeBrowser");
		try {
			if (driver != null) {

				driver.close();
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while closing the browser -" + t.getMessage());
			return "Fail - browser close issue";
		}
		return "Pass";
	}

	public String quitBrowser() {
		log.debug("=============================");
		log.debug("Executing closeBrowser");
		try {
			if (driver != null) {

				driver.quit();

				if (launchBrowser.equalsIgnoreCase("Safari") && System.getProperty("os.name").equals("Mac OS X")) {
					Thread.sleep(WAIT5SEC);
				}

			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while closing the browser -" + t.getMessage());
			return "Fail - QUIT Browser issue";
		}
		return "Pass";
	}

	public String shiftToBrowserWindow() {
		log.debug("=============================");
		log.debug("Executing shiftToBrowserWindow");
		try {
			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			int dataIntValue = Integer.parseInt(data);
			Object handle[] = driver.getWindowHandles().toArray();
			String popupWindosID = handle[dataIntValue].toString();
			driver.switchTo().window(popupWindosID);
			Thread.sleep(3000);
		} catch (Throwable t) {
			log.debug("Error while passing control to another browser window -- " + t.getMessage());
			return "Fail - Window Not Found";
		}
		return "Pass";
	}

	public String switchToWindow_UsingBrowserTitle() {
		log.debug("====================================");
		log.debug("Executing switchToWindow_UsingTitle");
		try {
			String currentWindowHandle = driver.getWindowHandle();
			Set<String> handles = driver.getWindowHandles();

			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

			for (String handle : handles) {
				if (!handle.equals(currentWindowHandle)) {
					driver.switchTo().window(handle);
					log.debug(driver.getTitle());
					if (!driver.getTitle().equalsIgnoreCase(data)) {
						continue;
					} else {
						log.debug("Switched to " + data + " Window");
					}
				}
			}
		} catch (Throwable t) {
			log.debug("Error in switchToWindow- " + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String removeIframes_Author() {
		log.debug("=============================");
		log.debug("Executing removeIframes_Author1");
		try {

			String url = driver.getCurrentUrl();
			log.debug("Current Url: " + url);

			url = url.replace("cf#/", "");
			log.debug("New Url: " + url);

			driver.get(url);

			driver.manage().window().maximize();

			try {
				Functions.handleTnCPopUp(driver, log, OR);
			} catch (Throwable t) {
				log.debug("Terms and condition did not came in Author Login");
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while Executing removeIframes_Author1 -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String navigateForward() {
		log.debug("=============================");
		log.debug("Executing NavigateForward");
		try {

			driver.navigate().forward();
		} catch (Throwable t) {
			log.debug("Error while navigating forward   -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String navigateBackward() {
		log.debug("=============================");
		log.debug("Executing NavigateBackward");
		try {

			driver.navigate().back();
		} catch (Throwable t) {
			log.debug("Error while navigating backward   -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String getAttributeValue() {
		log.debug("====================================");
		log.debug("Executing getAttributeValue");
		String data;
		try {
			String attribute = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat).trim();
			data = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute(attribute);
			log.debug("Return Value :" + data);
			testData.setCellData(currentTest, data_column_nameArr[0], 2, data);
			return "Pass";

		} catch (Throwable t) {
			log.debug("Error in getAttributeValue -- " + objectArr[0] + t);
			return "Fail";
		}
	}

	public String refreshBrowser() {
		try {
			driver.navigate().refresh();

		} catch (Throwable t) {
			log.debug("Error while deselecting the checkbox -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String isWebElementPresent() {
		log.debug("====================================");
		log.debug("Executing isWebElementPresent");

		String expected = null;
		WebElement webElement = null;
		try {
			try {
				expected = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
				log.debug("Expected:" + expected);

				if (!(expected.contains("true") || expected.contains("false"))) {
					return "Fail- Debug Required";
				}

			} catch (Throwable t) {
				log.debug("Test Data Column is not present in controller sheet .Expected variable value :" + expected);
				return "Fail- Debug Required";
			}

			try {
				webElement = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
				log.debug("WebElement: " + webElement);
			} catch (Throwable e) {
				webElement = null;
			}
			if (webElement == null) {
				if (expected.equalsIgnoreCase("true"))
					return "Fail -" + " Element not present";
				else
					return "Pass";
			} else {
				if (expected.equalsIgnoreCase("true"))
					return "Pass";
				else
					return "Fail -" + " Element should not be present";
			}
		} catch (Throwable t) {
			log.debug("Error while executing isWebElementPresent -" + t.getMessage());
			return "Fail";
		}
	}

	public String DragDrop() {
		log.debug("=============================");
		log.debug("Executing DragDrop Keyword");

		try {

			driver.getCapabilities();
			WebElement source = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			WebElement target = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			Actions act = new Actions(driver);
			act.dragAndDrop(source, target).build().perform();

			// (new Actions(driver)).dragAndDrop(source,
			// target).build().perform();

		} catch (Throwable t) {
			log.debug("Error while  dragging and dropping  -" + objectArr[0] + "." + t.getMessage());
			return "Fail";
		}
		return "Pass" + objectArr[0];
	}

	public String doubleClick() {
		log.debug("=============================");
		log.debug("Executing doubleClick Keyword");
		try {
			WebElement obj = getWebElement(OR, objectArr[0]);
			(new Actions(driver)).doubleClick(obj).perform();
		} catch (Throwable t) {
			// report error
			log.debug("Error while double clicking on Object -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String rightClick() {
		log.debug("=============================");
		log.debug("Executing rightClick Keyword");
		try {
			WebElement obj = getWebElement(OR, objectArr[0]);
			(new Actions(driver)).contextClick(obj).perform();
		} catch (Throwable t) {
			// report error
			log.debug("Error while right clicking on Object -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String editXpathAndClick() {
		log.debug("=============================");
		log.debug("Executing editXpathAndClick Keyword");
		String editXpath = OR.getProperty(objectArr[0]);
		String replaceString = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		String finalEditUSerXpath = editXpath.replaceAll("xyz", replaceString);
		try {
			driver.findElement(By.xpath(finalEditUSerXpath)).click();
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0] + t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";

	}

	public String editXpathAndVerifyText() {
		log.debug("=============================");
		log.debug("Executing editXpathAndVerifyText Keyword");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String editXpath = OR.getProperty(objectArr[0]);
		String userEmail = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		String finalEditUSerXpath = editXpath.replaceAll("xyz", userEmail);
		String actual = null;
		try {

			actual = driver.findElement(By.xpath(finalEditUSerXpath)).getText();
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// report error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";

	}

	public String verifyTextBoxText() {
		log.debug("=============================");
		log.debug("Executing verifyText");

		String expected = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		String actual = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("value");
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyImage() {
		log.debug("=============================");
		log.debug("Executing verifyImage");
		String expectedImageName = APPTEXT.getProperty(objectArr[0]);
		String actualImageName = null;
		try {
			actualImageName = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("src");
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}

		log.debug("expectedImageName  -  " + expectedImageName);
		log.debug("actualImageName  -  " + actualImageName);
		try {
			Assert.assertEquals(true, actualImageName.trim().contains(expectedImageName.trim()));
		} catch (Throwable t) {
			log.debug("Error in text - " + objectArr[0]);
			log.debug("expectedImageName " + expectedImageName);
			log.debug("actualImageName " + actualImageName);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyImageCss() {
		log.debug("=============================");
		log.debug("Executing verifyImageCss");
		String expectedImageName = APPTEXT.getProperty(objectArr[0]);
		String actualImageName = null;
		try {
			actualImageName = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("background-image");
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expectedImageName  -  " + expectedImageName);
		log.debug("actualImageName  -  " + actualImageName);
		try {
			Assert.assertEquals(true, actualImageName.trim().contains(expectedImageName.trim()));
		} catch (Throwable t) {
			log.debug("Error in text - " + objectArr[0]);
			log.debug("expectedImageName " + expectedImageName);
			log.debug("actualImageName " + actualImageName);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyText() {
		log.debug("=============================");
		log.debug("Executing verifyText");
		String expected = null, actual = null;
		try {
			expected = APPTEXT.getProperty(objectArr[0]);
			log.debug("expected Text  -  " + expected);
		} catch (Throwable e) {
			log.debug("expected Text  -  " + expected);
			log.debug("Property " + objectArr[0] + " missing from APPTEXT file or invalid property used.");
			return "Fail- Debug Required";
		}
		try {
			actual = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("actual Text  -  " + actual);
		} catch (Throwable t) {
			log.debug("actual Text  -  " + actual);
			log.debug("Property " + objectArr[0] + " missing from OR file or invalid property used.");
			return "Fail- Debug Required";
		}

		if (actual.trim().equals(expected.trim()))
			return "Pass";
		else
			return "Fail";
	}

	public String verifyTextIgnoreCase() {
		log.debug("=============================");
		log.debug("Executing verifyTextIgnoreCase");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);

		if (actual.trim().equalsIgnoreCase(expected.trim())) {
			return "Pass";
		} else {
			return "Fail";
		}

	}

	public String verifyText_linkText() {
		log.debug("=============================");
		log.debug("Executing verifyText_linkText");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = driver.findElement(By.linkText(APPTEXT.getProperty(objectArr[0]))).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyLinkText() {
		log.debug("=============================");
		log.debug("Executing verifyLinkText");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			WebElement element = driver.findElement(By.linkText("Logout"));
			actual = element.getText();
			System.out.println("******************************************");
			System.out.println("Actual values is: " + actual);
			System.out.println("******************************************");
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			Assert.assertEquals(expected.trim(), actual.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}
		return "Pass";
	}

	public String verifyPartialText() {
		log.debug("=============================");
		log.debug("Executing verifyPartialText");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			if (actual.trim().toLowerCase().trim().contains(expected.toLowerCase().trim())) {
				return "Pass";
			} else
				return "Fail";
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}

	}

	public String verifyPartialText2() {
		// the keyword checks if the expected value contains the actual value
		log.debug("=============================");
		log.debug("Executing verifyPartialText2");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			if (expected.trim().contains(actual.trim())) {
				return "Pass";
			} else
				return "Fail";
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}

	}

	public String verifyPartialText_CaseSensitive() {
		log.debug("=============================");
		log.debug("Executing verifyPartialText_CaseSensitive");
		String expected = APPTEXT.getProperty(objectArr[0]);
		String actual = null;
		try {
			actual = getWebElement(OR, objectArr[0]).getText();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Text  -  " + expected);
		log.debug("actual Text  -  " + actual);
		try {
			if (expected.contains(actual)) {
				return "Pass";
			} else
				return "Fail";
		} catch (Throwable t) {
			// error
			log.debug("Error in text - " + objectArr[0]);
			log.debug("Actual - " + actual);
			log.debug("Expected -" + expected);
			return "Fail";
		}

	}

	public String verifyTooltip() {
		log.debug("=============================");
		log.debug("Executing verifyTooltip");
		String expectedTooltip = APPTEXT.getProperty(objectArr[0]);
		String actualTooltip = null;
		try {
			actualTooltip = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("title").toString();
		} catch (Throwable t) {
			// error
			log.debug("Error while finding the objectArr[0] - " + objectArr[0]);
			log.debug("Object- " + objectArr[0] + " not found");
			return "Fail";
		}
		log.debug("expected Tooltip  -  " + expectedTooltip);
		log.debug("actual Tooltip  -  " + actualTooltip);
		try {
			Assert.assertEquals(expectedTooltip.trim(), actualTooltip.trim());
		} catch (Throwable t) {
			// error
			log.debug("Error in Tooltiptext - " + objectArr[0]);
			log.debug("Actual - " + actualTooltip);
			log.debug("Expected -" + expectedTooltip);
			return "Fail";
		}
		return "Pass";
	}

	public String selectvalidate() {
		log.debug("=============================");
		log.debug("Executing selectvalidate Keyword");
		// extract the test data
		// The expected data given in the TestData excel sheet should be
		// separated by colon( : )
		String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

		String[] setofdata = data.split(":");
		String[] dropdownListData;
		try {
			String listData = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			dropdownListData = listData.split("\n");
		} catch (Throwable t) {
			// report error
			log.debug("Error while validating for droplist list -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		if (Arrays.asList(dropdownListData).containsAll(Arrays.asList(setofdata))) {
			return "Pass";
		} else {
			return "Fail, Expected data doesnot match with the data in drop down list";
		}

	}

	public String navigateURL() {
		log.debug("=============================");
		log.debug("Executing navigateURL");

		try {
			driver.navigate().to(OR.getProperty(objectArr[0]).toString());

			log.debug("navigate completed");
		} catch (Throwable t) {
			log.debug("error while navigating to the URL" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String navigateNewEnv() {
		log.debug("=============================");
		log.debug("Executing navigateURL");

		try {

			String url = CONFIG.getProperty(objectArr[0]);
			String[] testDataUrl = url.split("/");

			String url1 = testDataUrl[0] + "//" + CONFIG.getProperty(objectArr[1]) + "@" + testDataUrl[2];

			for (int i = 3; i < testDataUrl.length; i++) {
				url1 = url1 + "/" + testDataUrl[i];
			}

			driver.get(url1);

			log.debug("Navigation completed using navigateNewEnv keyword");
		} catch (Throwable t) {
			log.debug("Error while navigating to the URL using navigateNewEnv keyword" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String inputAndClickEnterKey() {
		log.debug("=============================");
		log.debug("Executing inputAndClickEnterKey Keyword");
		// extract the test data
		String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		try {
			getWebElement(OR, objectArr[0]).sendKeys(data);
			log.debug("data inserted into the search box");

			if (!testBrowser.equals("Firefox")) {
				getWebElement(OR, objectArr[0]).sendKeys(Keys.ENTER);
			} else {
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ENTER).build().perform();
			}
			Thread.sleep(WAIT4SEC);
			log.debug("enter clicked");
			String title = driver.getTitle();
			log.debug("browser title is :" + title);
			if (!title.contains("Page Not Found")) {
				log.debug("arrived on search results page. The page title is :" + title);
				return "Pass";
			} else {
				log.debug("did not arrive on search results page. The page title is :" + title);
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while inputAndClickEnterKey -" + objectArr[0] + t.getMessage());
			return "Fail";
		}

	}

	public String isDisplayedandClick() {
		log.debug("====================================");
		log.debug("Executing isDisplayedandClick");

		try {
			// objectStatus =
			if (getWebElement(OR, objectArr[0]).isDisplayed()) {
				getWebElement(OR, objectArr[0]).click();
			}

		} catch (Throwable t) {
			log.debug("Error in isEnabled -- " + objectArr[0]);
			return "Fail";
		}
		return "Pass";
	}

	public String clickIfElementPresent() {
		log.debug("=============================");
		log.debug("Executing clickIfElementPresent");
		try {
			getWebElement(OR, objectArr[0]).click();
			return "Pass";
		} catch (Throwable t) {
			// report error
			log.debug("Element not present, hence will not trigger the click event.");
			return "Pass";
		}
	}

	public String compareTextColorOnMouseHover() {
		log.debug("==============================================");
		log.debug("Executing compareTextColorOnMouseHover Keyword");
		// this method hovers the mouse on the object specified and gets its
		// actual text color and compares it with the expected color
		// objectArr[0] is the object ; objectArr[1] is the expected color
		// before hover; objectArr[2] is the expected color after hover
		int resultCount = 0;

		try {

			String expectedColorBeforeHover = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String expectedColorAfterHover = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);

			WebElement object2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

			String actualColorBeforeHoverRGB = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("Color");
			String actualColorBeforeHoverHex = Color.fromString(actualColorBeforeHoverRGB).asHex().toUpperCase();
			log.debug(objectArr[0] + " :  expected Color BEFORE Hover : " + expectedColorBeforeHover);
			log.debug(objectArr[0] + " :  actual Color BEFORE Hover : " + actualColorBeforeHoverHex);
			System.out.println(objectArr[0] + " :  expected Color BEFORE Hover : " + expectedColorBeforeHover);
			System.out.println(objectArr[0] + " :  actual Color BEFORE Hover : " + actualColorBeforeHoverHex);
			if (actualColorBeforeHoverHex.trim().equals(expectedColorBeforeHover.trim()))
				resultCount++;
			Thread.sleep(WAIT2SEC);

			Actions builder = new Actions(driver);
			builder.moveToElement(object2).build().perform();

			String actualColorAfterHoverRGB = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("Color");
			String actualColorAfterHoverHex = Color.fromString(actualColorAfterHoverRGB).asHex().toUpperCase();
			log.debug(objectArr[0] + " :  expected Color AFTER Hover : " + expectedColorAfterHover);
			log.debug(objectArr[0] + " :  actual Color AFTER Hover : " + actualColorAfterHoverHex);
			System.out.println(objectArr[0] + " :  expected Color AFTER Hover : " + expectedColorAfterHover);
			System.out.println(objectArr[0] + " :  actual Color AFTER Hover : " + actualColorAfterHoverHex);

			if (actualColorAfterHoverHex.trim().equalsIgnoreCase(expectedColorAfterHover.trim()))
				resultCount++;

			if (resultCount == 2) {
				System.out.println(resultCount);
				return "Pass";

			}

			else {
				System.out.println(resultCount);
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing compareTextColorOnMouseHover -" + objectArr[0] + t.getMessage());
			return "Fail";
		}

	}

	public String verifyElementOrdering() {
		log.debug("=======================================");
		log.debug("Executing verifyElementOrdering Keyword");
		// this keyword checks whether the order of the links present
		// objectArr[0] is the object whose links order is to be verified

		try {
			String expectedOrder = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String order = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			List<WebElement> search = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			String actualOrder = "";
			String seperator = "";
			for (WebElement e2 : search) {
				if (!e2.getText().isEmpty()) {
					log.debug("category link : " + e2.getText());
					if (order.equalsIgnoreCase("Reverse"))
						actualOrder = e2.getText() + seperator + actualOrder;
					else if (order.equalsIgnoreCase("Forward"))
						actualOrder = actualOrder + seperator + e2.getText();
				}
				seperator = ",";
			}

			if (actualOrder.trim().equals(expectedOrder)) {
				log.debug("actual order is : " + actualOrder + "and expected order is : " + expectedOrder);
				return "Pass";
			} else {
				log.debug("actual order is : " + actualOrder + "and expected order is : " + expectedOrder);
				return "Fail";
			}
		} catch (Throwable t) {
			log.debug("Error while executing verifyElementOrdering -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String verifyFont() {
		log.debug("=============================");
		log.debug("Executing verifyFont Keyword");
		String flag = "Pass", font_expected1, font_expected2, font_expected3, font_actual, elementKey, elementLocator;
		elementKey = objectArr[0];
		elementLocator = OR.getProperty(objectArr[0]);

		try {

			WebElement element = driver.findElement(By.xpath(elementLocator));
			Functions.highlighter(driver, element);
			font_expected1 = "\'" + testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

			font_actual = element.getCssValue("font-family").substring(0, element.getCssValue("font-family").indexOf(",")) + ","
					+ element.getCssValue("font-size") + "," + element.getCssValue("color");

			if (!font_expected1.equals(font_actual)) {
				log.debug("\nFont Values for the element do not match.\nActual   Font: \t" + font_actual + "\nExpected Font: \t"
						+ font_expected1);
				log.debug("\nElement     key: \t" + elementKey + "\nElement locator: \t" + elementLocator + "\n");
				flag = "Fail";
			}

			try {
				font_expected2 = "\'" + testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);

				action.moveToElement(element).build().perform();

				font_actual = element.getCssValue("font-family").substring(0, element.getCssValue("font-family").indexOf(",")) + ","
						+ element.getCssValue("font-size") + "," + element.getCssValue("color");

				if (!font_expected2.equals(font_actual)) {
					log.debug("\nOn hovering, Font Values for the element do not match.\nActual   Font: \t" + font_actual
							+ "\nExpected Font: \t" + font_expected2);
					log.debug("\nElement     key: \t" + elementKey + "\nElement locator: \t" + elementLocator + "\n");
					flag = "Fail";
				}

			} catch (ArrayIndexOutOfBoundsException e) {
			}

			try {
				font_expected3 = "\'" + testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
				element.click();

				// Handling Data Unavailable Pop Up for Specific Dev and QA
				// Environment pages
				Functions.handleDataUnavailablePopUp(driver, log, CONFIG);

				Thread.sleep(WAIT3SEC);

				element = driver.findElement(By.xpath(elementLocator));
				font_actual = element.getCssValue("font-family").substring(0, element.getCssValue("font-family").indexOf(",")) + ","
						+ element.getCssValue("font-size") + "," + element.getCssValue("color");

				if (!font_expected3.equals(font_actual)) {
					log.debug("\nOn Clicking, Font Values for the element do not match.\nActual   Font: \t" + font_actual
							+ "\nExpected Font: \t" + font_expected3);
					log.debug("\nElement     key: \t" + elementKey + "\nElement locator: \t" + elementLocator + "\n");
					flag = "Fail";
				}

			} catch (ArrayIndexOutOfBoundsException e) {
			}

		} catch (Throwable t) {
			log.debug("Error while executing verifyFont on: \nElement     Key= " + elementKey + "\nElement Locator" + elementLocator + "\n"
					+ t.getMessage());
			return "Fail";
		}
		return flag;

	}

	public String getCount()

	{

		log.debug("=============================");
		log.debug("Executing getCount Keyword");
		try {
			List<WebElement> actualdata = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			int actualcount = actualdata.size();
			String count = String.valueOf(actualcount);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, count);
			log.debug("Actual count : " + actualcount);
			return "Pass";
		} catch (Throwable e) {
			log.debug("Error while executing getcount " + e.getMessage());
			return "Fail";
		}
	}

	public String compareTwoStrings() {
		log.debug("=======================================");
		log.debug("Executing compareTwoStrings Keyword");

		try {
			String value1, value2, getValFromTestData = null;

			try {
				getValFromTestData = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);

			} catch (Throwable e) {
				// do nothing
			}

			if (getValFromTestData.equals("default"))
				value1 = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
			else
				value1 = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

			if (value1.contains("_.-/+',()&:"))
				value1 = Functions.replaceAll(value1, "[^-_./+',()&:]", "");

			if (driver.getTitle().contains("Search Results") || driver.getTitle().contains("Home"))
				value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("value");
			else if (getValFromTestData.equals("src"))
				value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("src");
			else
				value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			if (value1.equals(value2)) {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Pass";
			} else {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Fail";
			}

		} catch (Throwable t) {
			log.debug("error while executing compareTwoStrings keyword " + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String compareText() {
		log.debug("=======================================");
		log.debug("Executing compareText Keyword");

		try {
			String value1, value2 = null;

			try {
				value1 = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
				log.debug("value1  data sheet is :" + value1);

			} catch (Throwable e) {
				log.debug("Data from sheet isn't fetched");
				value1 = null;
				// do nothing
			}

			value2 = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("value2 application value is :" + value2);
			if (value2.contains("_.-/+',()&:"))
				value2 = Functions.replaceAll(value2, "[^-_. /+',()&:]", "");
			if (value1.contains("_.-/+',()&:"))
				value1 = Functions.replaceAll(value1, "[^-_. /+',()&:]", "");
			if (value2.trim().toLowerCase().trim().contains(value1.toLowerCase().trim())) {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Pass";
			} else {
				log.debug("value1 is :" + value1);
				log.debug("value2 is :" + value2);
				return "Fail";
			}

		} catch (Throwable t) {
			log.debug("error while executing compareText keyword " + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String compareTwoDataColumns() {
		log.debug("=======================================");
		log.debug("Executing compareTwoDataColumns Keyword");

		try {
			String flag;
			String value1, value2;
			try {
				value1 = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
				value2 = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
				flag = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			} catch (Throwable t) {
				log.debug("error while executing compareTwoDataColumns keyword " + objectArr[0] + t.getMessage());
				return "Fail";
			}
			if (value1.contains("_.-/+',()&:"))
				value1 = Functions.replaceAll(value1, "[^-_./+',()&:]", "").toString();
			if (value2.contains("_.-/+',()&:"))
				value2 = Functions.replaceAll(value2, "[^-_./+',()&:]", "").toString();
			log.debug("value1 is :" + value1);
			log.debug("value2 is :" + value2);
			log.debug("flag is :" + flag);
			if (flag.equalsIgnoreCase("True")) {
				if (value1.trim().equals(value2.trim())) {
					log.debug("value1 is :" + value1);
					log.debug("value2 is :" + value2);
					return "Pass";
				} else {
					log.debug("value1 is :" + value1);
					log.debug("value2 is :" + value2);
					return "Fail";
				}
			} else {
				if (value1.trim().equals(value2.trim())) {
					log.debug("value1 is :" + value1);
					log.debug("value2 is :" + value2);
					return "Fail";
				} else {
					log.debug("value1 is :" + value1);
					log.debug("value2 is :" + value2);
					return "Pass";
				}
			}

		} catch (Throwable t) {
			log.debug("error while executing compareTwoDataColumns keyword " + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String verifyTotalCharacterLength() {
		log.debug("=============================");
		log.debug("executing keyword verifyTotalCharacterLength");
		// the keyword verifies the total character count for more than one
		// paragraph
		try {
			String length = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			int expectedCharLength = Integer.parseInt(length);
			int initialpara = 0, initialcharcount = 0;
			String initpara = String.valueOf(initialpara);
			String initcharcount = String.valueOf(initialcharcount);
			String actualChar;
			int actualCharLength = 0;

			actualChar = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			actualCharLength = actualChar.length();

			String paracount = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			int paras = Integer.parseInt(paracount);
			String totalcharcount = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			int totalcharacters = Integer.parseInt(totalcharcount);
			if (paras == 0) {
				paras++;
				paracount = String.valueOf(paras);
				testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, paracount);

				totalcharacters = totalcharacters + actualCharLength;
				totalcharcount = String.valueOf(totalcharacters);
				testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, totalcharcount);

				if (actualCharLength <= expectedCharLength) {
					log.debug("actual character length is :  " + actualCharLength);
					log.debug("expected character length is :  " + expectedCharLength);
					return "Pass";
				} else {
					log.debug("actual character length is :  " + actualCharLength);
					log.debug("expected character length is :  " + expectedCharLength);
					return "Fail";
				}
			} else {
				totalcharacters = totalcharacters + actualCharLength;
				testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, initpara);
				testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, initcharcount);

				if (totalcharacters <= expectedCharLength) {
					log.debug("total character length expected is :" + expectedCharLength);
					log.debug("total character length actual is :" + totalcharacters);
					return "Pass";
				} else {
					log.debug("total character length expected is :" + expectedCharLength);
					log.debug("total character length actual is :" + totalcharacters);
					return "Fail";
				}
			}
		} catch (Throwable t) {
			log.debug("Error while executing verifyCharacterLength " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyCharacterLength() {
		log.debug("=============================");
		log.debug("executing keyword verifyCharacterLength");

		try {
			String length = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			int expectedCharLength = Integer.parseInt(length);
			String actualChar, seeMore = "";
			int actualCharLength = 0;

			actualChar = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			try {
				seeMore = driver.findElement(By.xpath(OR.getProperty(objectArr[1]))).getText();
				actualCharLength = actualChar.length() + seeMore.length();
			} catch (Throwable e) {
				// do nothing
				actualCharLength = actualChar.length();
			}

			if (actualCharLength <= expectedCharLength) {
				log.debug("actual character lenth is :  " + actualCharLength);
				log.debug("expected character lenth is :  " + expectedCharLength);
				return "Pass";
			} else {
				log.debug("actual character lenth is :  " + actualCharLength);
				log.debug("expected character lenth is :  " + expectedCharLength);
				return "Fail";
			}
		} catch (Throwable t) {
			log.debug("Error while executing verifyCharacterLength " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyScrollbarPosition() {
		log.debug("=============================");
		log.debug("Executing verifyScrollbarPosition");
		try {
			String rowsLocator = OR.getProperty(objectArr[4]);
			String dragThumbLocator = OR.getProperty(objectArr[5]);
			String totalRowCount = driver.findElement(By.xpath(OR.getProperty(objectArr[6]))).getText();
			int totalRowInt = Integer.parseInt(totalRowCount);
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, totalRowInt, 100);
			WebElement element1;
			String val, scrollHeight;
			double topVal, topHeightVal, scrollHeightVal;
			int top = 0;
			int pixelsToClick = 0;
			boolean flag = true;
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getAttribute("style");
			String scenario = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			topHeightVal = Functions.pixelValDouble(val, "height:");
			WebElement element2 = driver.findElement(By.xpath(OR.getProperty(objectArr[1])));
			scrollHeight = element2.getAttribute("style");
			scrollHeightVal = Functions.pixelValDouble(scrollHeight, "height:");
			top = (int) -scrollHeightVal;
			Functions.dragTo(driver, element1, top);
			// cases
			if (scenario.equals("bottom")) {
				pixelsToClick = (int) (scrollHeightVal - topHeightVal);
				Functions.dragTo(driver, element1, pixelsToClick);
			} else if (scenario.equals("top"))
				pixelsToClick = 0;
			else if (scenario.equals("mid1")) {
				pixelsToClick = (int) (scrollHeightVal - topHeightVal) / 2;
				Functions.dragTo(driver, element1, pixelsToClick);
			} else if (scenario.equals("mid2")) {
				pixelsToClick = (int) (scrollHeightVal - topHeightVal) / 4;
				Functions.dragTo(driver, element1, pixelsToClick);
			}
			// fetch top value now and match it with the pixelsToClick
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getCssValue("top");
			topVal = Functions.pixelVal(val);
			if (pixelsToClick != topVal) {
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				flag = false;
			}
			// navigate to the other view and return back
			driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
			Thread.sleep(WAIT1SEC);
			driver.findElement(By.xpath(OR.getProperty(objectArr[3]))).click();
			// verify again
			element1 = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			val = element1.getCssValue("top");
			topVal = Functions.pixelVal(val);
			if (pixelsToClick != topVal) {
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				flag = false;
			}
			if (flag) {
				log.debug("pixels to Click value :" + pixelsToClick);
				log.debug("top value :" + topVal);
				return "Pass";
			} else
				return "Fail";
		} catch (Throwable e) {
			log.debug("Error while executing verifyScrollbarPosition " + e.getMessage());
			return "Fail";
		}
	}

	public String verifyIsDisabled() {
		log.debug("=============================");
		log.debug("Executing verifyIsDisabled");
		try {
			String check = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("display");
			log.debug("Display: " + check);
			if (check.equalsIgnoreCase("none"))
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing verifyIsDisabled for object " + objectArr[0] + "\n StackTrace: \n" + t.getMessage());
			return "Fail";
		}

	}

	public String verifyScrollDrag() {
		log.debug("====================================");
		log.debug("Executing verifyScrollDrag");
		// the keyword drags the scroll bar up and down
		try {
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			Actions actions = new Actions(driver);
			int pixelsToClick = 30;
			actions.dragAndDropBy(element, 0, pixelsToClick).perform();
			int pixelsToClick1 = -30;
			actions.dragAndDropBy(element, 0, pixelsToClick1).perform();
			return "Pass";
		} catch (Throwable t) {
			log.debug("Error while executing verifyScrollDrag" + t.getMessage());
			return "Fail";
		}
	}

	public String isDisplayed() {
		log.debug("====================================");
		log.debug("Executing isDisplayed");
		// the keyword checks whether an element is displayed or not
		boolean result = false;
		String expected = null;
		try {
			try {
				expected = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
				log.debug("Expected:" + expected);
			} catch (Throwable r) {
				log.debug("Test Data Column is not present in controller sheet .Expected variable value :" + expected);
				return "Fail- Debug Required";
			}

			if (expected.equals(null) || expected.isEmpty()) {
				log.debug("Test Data value is blank. Expoected variable value :" + expected);
				return "Fail- Debug Required";
			}

			try {
				result = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).isDisplayed();
				log.debug("Result: " + result);
			} catch (Throwable e) {
				result = false;
			}

			if (!result) {
				if (expected.equalsIgnoreCase("true"))
					return "Fail -" + " Element not present";
				else
					return "Pass";
			} else {
				if (expected.equalsIgnoreCase("true"))
					return "Pass";
				else
					return "Fail -" + " Element should not be present";
			}
		} catch (Throwable t) {
			log.debug("Error while executing isDisplayed for object " + objectArr[0] + "\n StackTrace: \n" + t.getMessage());
			return "Fail";
		}
	}

	public String compareInputData() {
		log.debug("====================================");
		log.debug("Executing compareInputData");
		try {
			String actualValue = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getAttribute("value");
			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

			if (actualValue.equals(expectedValue))
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error in compareInputData- " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyIsEnabled() {
		log.debug("=============================");
		log.debug("Executing verifyIsEnabled");
		try {
			String check = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getCssValue("display");
			log.debug("Display: " + check);
			if (check.equalsIgnoreCase("block"))
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error in verifyIsDisabled -- " + objectArr[0]);
			return "Fail";
		}

	}

	public String scrollDrag() {
		log.debug("====================================");
		log.debug("Executing scrollDrag");
		// the keyword drags the scroll bar up and down

		try {
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			Actions actions = new Actions(driver);
			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			double doubleData = Double.parseDouble(data);
			int pixelsToClick = (int) doubleData;
			log.debug("Pixels to move:" + pixelsToClick);
			actions.dragAndDropBy(element, 0, pixelsToClick).perform();
			return "Pass";
		} catch (Throwable t) {
			log.debug("Error in dragging scroll bar " + t.getMessage());
			return "Fail";
		}
	}

	public String setData() {
		log.debug("====================================");
		log.debug("Executing setData");
		// the keyword drags the scroll bar up and down

		try {
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String data = element.getText();
			log.debug("Data: " + data);
			testData.setCellData(currentTest, data_column_nameArr[0], 2, data);
			return "Pass";
		} catch (Throwable t) {
			log.debug("Error in setData " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyDataEquals() {
		log.debug("====================================");
		log.debug("Executing verifyDataEquals");

		try {
			WebElement element = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String actualData = element.getText();
			log.debug("Actual Data: " + actualData);
			String data1 = Functions.replaceAll(actualData, "[a-zA-Z \\( \\)]", "");
			log.debug("Actual Data: " + data1);
			int actualIntData = Integer.parseInt(data1.trim());
			log.debug("Actual Integer Data: " + actualIntData);
			String expectedData = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("Expected Data: " + expectedData);
			String data2 = Functions.replaceAll(expectedData, "[a-zA-Z \\( \\)]", "");
			log.debug("Expected Data: " + data2);
			int expectedIntData = Integer.parseInt(data2.trim());
			log.debug("Actual Integer Data: " + expectedIntData);
			if (expectedIntData == actualIntData)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing verifyDataEquals for object " + objectArr[0] + "\n Stack Trace: \n" + t.getMessage());
			return "Fail";
		}
	}

	public String verifyCount() {
		log.debug("====================================");
		log.debug("Executing verifyCount");
		try {

			String flag = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);
			String actualData = driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).getText();
			String data1 = Functions.replaceAll(actualData, "[a-zA-Z \\( \\)]", "");
			log.debug("Expected Data: " + data1);
			System.out.println("Expected Data: " + data1);
			int actualIntData = Integer.parseInt(data1.trim());
			log.debug("Actual Integer Data: " + actualIntData);
			System.out.println("Actual Integer Data: " + actualIntData);

			Functions.dragTillAllRowsLoadedWithWait(driver, log, rowsLocator, dragThumbLocator, actualIntData, 200);
			int count = driver.findElements(By.xpath(rowsLocator)).size();
			log.debug("Count after dragTillAllRowsLoadedWithWait: " + count);
			log.debug("Actual Integer Data: " + actualIntData);
			String expectedData = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("Expected Data: " + expectedData);
			String data2 = Functions.replaceAll(expectedData, "[a-zA-Z \\( \\)]", "");
			log.debug("Expected Data: " + data2);
			int expectedIntData = Integer.parseInt(data2.trim());
			log.debug("Actual Integer Data: " + expectedIntData);
			if (flag.equalsIgnoreCase("TRUE")) {
				if (expectedIntData == actualIntData && count == actualIntData)
					return "Pass";
				else
					return "Fail";
			} else {
				if (expectedIntData != actualIntData && count == actualIntData)
					return "Pass";
				else
					return "Fail";
			}
		} catch (Throwable t) {
			log.debug("Error while executing verifyCount \n Stack Trace: \n" + t.getMessage());
			return "Fail";
		}
	}

	public String setDynamicValue() {
		log.debug("=============================");
		log.debug("Executing setDynamicValue");
		try {
			String x = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("Dynamic value is" + x);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, x);
			log.debug("Value set in excel is" + testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0] + t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	public String verifyDynamicValue() {
		log.debug("=============================");
		log.debug("Executing verifyDynamicValue");
		try {

			String x = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();

			log.debug("Actual value is: " + x);
			String expectedValue = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("Expected value is: " + expectedValue);
			try {
				int x1 = Integer.parseInt(x);
				int expectedValue1 = Integer.parseInt(expectedValue);
				if (x1 == expectedValue1) {
					return "Pass";
				} else
					return "Fail";
			} catch (Throwable t) {
				if (x.equals(expectedValue)) {
					return "Pass";
				} else {
					return "Fail";
				}

			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyDynamicValue -" + objectArr[0] + t.getMessage());
			return "Fail - Link Not Found";
		}
	}

	public String browserBack() {
		log.debug("=============================");
		log.debug("Executing browserBack");
		try {
			driver.navigate().back();
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking browserBack button -" + objectArr[0] + t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	public String author_clickWorkspaceOKButton() {
		log.debug("=============================");
		log.debug("Executing author_clickWorkspaceOKButton Keyword");

		try {
			for (int i = 2; i <= 10; i++) {
				WebElement OKButton = driver
						.findElement(By
								.xpath("//div[@class=' x-window-plain x-form-label-left' or @class='x-window-plain x-form-label-left']["
										+ i
										+ "]/descendant::div[@class='x-window x-window-plain x-resizable-pinned' or @class=' x-window x-window-plain x-resizable-pinned']/descendant::button[text()='OK' and (@class=' x-btn-text' or @class='x-btn-text')]"));
				if (OKButton.isDisplayed()) {
					OKButton.click();
					break;
				}

			}

		} catch (Throwable t) {
			log.debug("Error while executing author_clickWorkspaceOKButton keyword");
			log.debug(t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String verifyTextFromDataSheet() {
		log.debug("=============================");
		log.debug("Executing verifyTextFromDataSheet");
		String expected = null, actual = null;
		try {
			expected = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("expected Text  -  " + expected);
		} catch (Throwable e) {
			log.debug("expected Text  -  " + expected);
			log.debug("Property " + objectArr[0] + " missing from data  file");
			return "Fail- Debug Required";
		}
		try {
			actual = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("actual Text  -  " + actual);
		} catch (Throwable t) {
			log.debug("actual Text  -  " + actual);
			log.debug("Property " + objectArr[0] + " missing from the web page");
			return "Fail- Debug Required";
		}

		if (expected.trim().contains(actual.trim()) || actual.trim().contains(expected.trim()))
			return "Pass";
		else {
			log.debug("expected Text  -  " + expected);
			log.debug("actual Text  -  " + actual);
			return "Fail";
		}
	}

	public String verifyCheckBoxSelected() {
		log.debug("=============================");
		log.debug("Executing verifyCheckBoxSelected Keyword");
		try {
			WebElement checkbox = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			String expected = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			boolean isChecked = checkbox.isSelected();
			log.debug("Expected Value: " + expected);
			log.debug("isChecked Value: " + isChecked);
			if (isChecked && expected.equalsIgnoreCase("TRUE")) {
				return "Pass";
			} else if (!isChecked && expected.equalsIgnoreCase("FALSE"))
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing verifyCheckBoxSelected keyword - Object: " + objectArr[0] + "\n Stacktrace: \n"
					+ t.getMessage());
			return "Fail- Debug Required in catch";
		}
	}

	public String inputAndClickEnterKeyTwice() {
		log.debug("=============================");
		log.debug("Executing inputAndClickEnterKeyTwice Keyword");
		// this keyword takes an input and press enter key twice
		String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		try {
			getWebElement(OR, objectArr[0]).sendKeys(data);
			Thread.sleep(WAIT2SEC);
			log.debug("data inserted into the search box: " + data);
			getWebElement(OR, objectArr[0]).sendKeys(Keys.ENTER);
			Thread.sleep(WAIT2SEC);
			getWebElement(OR, objectArr[0]).sendKeys(Keys.ENTER);
			Thread.sleep(WAIT2SEC);
			log.debug("Enter clicked");
			String title = driver.getTitle();
			log.debug("Browser title is :" + title);
			if (!title.contains("Page Not Found")) {
				log.debug("Arrived on crx page. The page title is :" + title);
				return "Pass";
			} else {
				log.debug("Did not arrive on crx page. The page title is :" + title);
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while inputAndClickEnterKeyTwice -" + objectArr[0] + t.getMessage());
			return "Fail";
		}

	}

	public String setAppendedDynamicValue() {
		log.debug("=============================");
		log.debug("Executing setAppendedDynamicValue");
		// this keyword appends a string to a dynamic string and sets it in test
		// data
		try {
			Functions.highlighter(driver, driver.findElement(By.xpath(OR.getProperty(objectArr[0]))));
			String x = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("Dynamic value is" + x);
			String string = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			String appendedString = x.concat(string);
			log.debug("Appended string is: " + appendedString);
			testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, "");
			testData.setCellData(currentTest, data_column_nameArr[1], testRepeat, appendedString);
			log.debug("Value set in excel is" + testData.getCellData(currentTest, data_column_nameArr[1], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing setAppendedDynamicValue -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String setAppendedURL() {
		log.debug("=============================");
		log.debug("Executing setAppendedURL");
		try {
			String string = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			System.out.println(string);
			String x = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			System.out.println(x);
			String appendedString;
			if ((string.contains("Manager_"))) {
				String[] str;
				str = string.split("/");
				int size = str.length;
				String manID = str[size - 1];
				testData.setCellData(currentTest, data_column_nameArr[3], testRepeat, "");
				testData.setCellData(currentTest, data_column_nameArr[3], testRepeat, manID);
				appendedString = x.concat(manID);
			} else {
				appendedString = x.concat(string);
			}
			log.debug(appendedString);
			testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, "");
			testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, appendedString);

			log.debug("value set in excel is" + testData.getCellData(currentTest, data_column_nameArr[2], testRepeat));
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing setAppendedURL -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String dateConverter() {
		log.debug("=============================");
		log.debug("Executing dateConverter");
		// this keyword converts date in specified format sets it in testData
		try {
			String actualDate = driver.findElement(By.xpath(OR.getProperty(objectArr[0]))).getText();
			log.debug("Date from crx is: " + actualDate);
			String array[] = actualDate.split("T");
			String dateFormat = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat df = new SimpleDateFormat(dateFormat);
			Date date = formatter.parse(array[0]);
			String convertedDate = df.format(date);
			log.debug("Converted date is: " + convertedDate);
			String flag = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			if (flag.equalsIgnoreCase("yes")) {
				String expectedFormat = " (" + convertedDate + ")";
				log.debug("Converted date in expected format is: " + expectedFormat);
				testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, expectedFormat);
			} else
				testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, convertedDate);
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing dateConverter -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String closeTab() {
		log.debug("=============================");
		log.debug("CloseTab");

		try {

			ArrayList<String> tabs2 = new ArrayList<String>(driver.getWindowHandles());
			driver.switchTo().window(tabs2.get(1));
			driver.close();
			driver.switchTo().window(tabs2.get(0));

		} catch (Throwable t) {
			log.debug("CloseTab");
			log.debug(t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String findElementAndDelete() {
		log.debug("=============================");
		log.debug("findElementAndDelete");

		WebElement webElement;

		try {
			Thread.sleep(WAIT5SEC);
			try {
				webElement = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));
			} catch (Throwable e) {
				webElement = null;
			}
			while (webElement != null) {
				Thread.sleep(WAIT5SEC);
				action.contextClick(getWebElement(OR, objectArr[0])).perform();
				// action.contextClick(driver.findElement(By.xpath(OR.getProperty(objectArr[0])))).perform();
				getWebElement(OR, objectArr[1]).click();
				Thread.sleep(WAIT4SEC);
				getWebElement(OR, objectArr[2]).click();
				Thread.sleep(WAIT5SEC);
				try {
					driver.findElement(By.xpath(OR.getProperty(objectArr[2])));
					driver.findElement(By.xpath(OR.getProperty(objectArr[2]))).click();
					Thread.sleep(WAIT5SEC);
				} catch (NoSuchElementException f) {
					log.debug("Second pop up asking delete confirmation did not appear");
				}
				try {
					webElement = driver.findElement(By.xpath(OR.getProperty(objectArr[0])));

				} catch (Throwable e) {
					webElement = null;
				}
			}
			return "Pass";
		} catch (Throwable f) {
			log.debug("Error while executing findElementAndDelete");
			log.debug(f.getMessage());
			return "Fail";
		}

	}

	public String verifyVisibility() {
		log.debug("=============================");
		log.debug("Executing verifyVisibility Keyword");
		// keyword clicks the object until its visibillity is true
		try {
			WebElement element = getWebElement(OR, objectArr[0]);
			WebElement element1 = getWebElement(OR, objectArr[1]);
			int i = 0;
			while (i < 2) {
				if (element.isDisplayed()) {
					element.click();
					log.debug("Element click at " + (i + 1) + " attempt");
					return "Pass";
				} else {
					element1.click();
					Thread.sleep(5000);
					if (element.isDisplayed()) {
						element.click();
						log.debug("Element clicked at " + (i + 1) + " attempt");
						return "Pass";
					} else
						continue;
				}
			}
			log.debug("Failed 3 attempts!");
			return "Fail";
		} catch (Throwable e) {
			log.debug("Error while executing verifyVisibility keyword" + e.getMessage());
			return "Fail";

		}
	}

	public String messageValidation_splitText() {
		log.debug("=============================");
		log.debug("Executing messageValidation_splitText");
		try {
			String actualString = getWebElement(OR, objectArr[0]).getText();
			String splitString = getWebElement(OR, objectArr[1]).getText();
			String expected = APPTEXT.getProperty(objectArr[0]);
			log.debug("actual value is: " + actualString);
			log.debug("split value is: " + splitString);
			log.debug("expected value is: " + expected);
			String array[] = actualString.split(splitString);
			log.debug("String after splitting is: " + array[0]);
			if (array[0].trim().equals(expected.trim())) {
				return "Pass";
			} else {
				return "Fail";
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing messageValidation_splitText -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String executeExeFile() {
		log.debug("=============================");
		log.debug("executeExeFile");
		try {
			String exefilename = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			// String exepath =
			// "C:/Users/dsing6/Desktop/"+exefilenameC:\Test\dependencies\workflow_Files;
			String exepath = System.getProperty("user.dir") + "/dependencies/workflow_Files/" + exefilename;
			log.debug(System.getProperty("user.dir") + "/dependencies/workflow_Files/" + exefilename);
			Runtime.getRuntime().exec(exepath);
			Thread.sleep(WAIT5SEC);
			return "Pass";
		} catch (Throwable t) {
			log.debug("executeExeFile" + data_column_nameArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String verifyFieldPresenseAndClick() {
		log.debug("=============================");
		log.debug("Verifyfieldpresense");
		try {
			int numElements1 = driver.findElements(By.xpath(OR.getProperty(objectArr[0]))).size();
			if (numElements1 == 1)
				return "Pass";
			else
				return "Fail-field not present";
		} catch (Throwable t) {
			log.debug("Error while executing hoverOverVideoOverlay");
			log.debug(t.getMessage());
			return "Fail";
		}
	}

	public String customClickLink() {
		log.debug("=============================");
		log.debug("Executing customClickLink");
		try {
			if (launchBrowser.equals("Firefox")) {
				Actions action = new Actions(driver);
				action.sendKeys(Keys.ESCAPE).build().perform();
				Thread.sleep(WAIT2SEC);
			} else {
				WebElement obj = getWebElement(OR, objectArr[0]);
				(new Actions(driver)).doubleClick(obj).perform();
			}
		} catch (Throwable t) {
			// report error
			log.debug("Error while clicking on link -" + objectArr[0] + t);
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	public String verifyDocumentAndClick() {
		log.debug("=============================");
		log.debug("Executing verifyDocumentAndClick");
		try {
			WebElement obj = getWebElement(OR, objectArr[0]);
			if (obj.getAttribute("data-doctype").equals("pdf")) {
				obj.click();
				Thread.sleep(WAIT1SEC);
				getWebElement(OR, objectArr[1]).click();
			} else
				obj.click();
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing verifyDocumentAndClick -" + objectArr[0] + t.getMessage());
			return "Fail - Link Not Found";
		}
		return "Pass";
	}

	public String sortDate() {
		log.debug("=============================");
		log.debug("Executing keyword sortDate");
		// this keyword sorts a list in asc or des order according to date
		List<Date> dates = new ArrayList<Date>();
		List<Date> datesSorted = new ArrayList<Date>();
		String ascOrDesc = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		String attribute = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
		String dateFormat = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);

		boolean result = false;
		String value = null;
		try {
			List<WebElement> search = getWebElements(OR, objectArr[0]);

			for (WebElement item : search) {
				value = item.getAttribute(attribute).trim();
				log.debug("Date is: " + value);
				SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);
				Date date = formatter.parse(value);
				if (!value.isEmpty())
					dates.add(date);
			}

			datesSorted.addAll(dates);

			log.debug(dates.size());
			log.debug(datesSorted.size());
			if (ascOrDesc.equals("asc"))
				Collections.sort(datesSorted);
			else {
				Collections.sort(datesSorted);
				Collections.reverse(datesSorted);
			}

			for (int k = 0; k < dates.size(); k++) {

				log.debug("Dates: " + dates.get(k) + " Dates sorted:  " + datesSorted.get(k));
				String unsorted = dates.get(k).toString().trim();
				String sorted = datesSorted.get(k).toString().trim();

				if (!(unsorted.equalsIgnoreCase(sorted))) {
					result = false;
					break;
				} else
					result = true;
			}

			if (result == true)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing sortDate -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String dragAndDropElement() {
		log.debug("=============================");
		log.debug("Executing dragAndDropElement");
		try {

			WebElement dragElement = getWebElement(OR, objectArr[0]);
			WebElement dropElement = getWebElement(OR, objectArr[1]);
			System.out.println(objectArr[0] + "  " + objectArr[1]);

			Actions builder = new Actions(driver);
			// builder.dragAndDrop(dragElement, dropElement).build().perform();

			builder.clickAndHold(dragElement).perform();
			Thread.sleep(2000);

			// builder.clickAndHold(dragElement).moveToElement(dropElement).release(dropElement).build().perform();

			Thread.sleep(2000);

			builder.release(dropElement).perform();

			return "Pass";

		} catch (Throwable t) {
			log.debug("Error while executing  dragAndDropElement- " + t.getMessage());
			return "Fail";

		}
	}

	public String verifySize() {
		log.debug("=============================");
		log.debug("Executing verifySize");
		try {
			@SuppressWarnings("unchecked")
			List<WebElement> playList = (List<WebElement>) getWebElement(OR, objectArr[0]);
			int size = playList.size();

			log.debug("Size : " + size);
			return "Pass";

		} catch (Throwable t) {
			log.debug("Error while executing  verifySize- " + t.getMessage());
			return "Fail";

		}
	}

	public String compareColor() {
		// this keyword verifies the actual colour with a given expected colour
		log.debug("==============================================");
		log.debug("Executing compareColor Keyword");
		try {

			String expectedColor = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("Expected Color: " + expectedColor);
			System.out.println(objectArr[0] + " :  expected Color: " + expectedColor);
			String actualColor = getWebElement(OR, objectArr[0]).getCssValue("Color");
			String actualColorHex = Color.fromString(actualColor).asHex().toUpperCase();
			log.debug("Actual Color: " + actualColorHex);
			System.out.println(objectArr[0] + " :  actual Color: " + actualColorHex);
			if (actualColorHex.trim().equalsIgnoreCase(expectedColor.trim())) {
				return "Pass";
			}

			else {
				return "Fail";
			}

		} catch (Throwable t) {
			log.debug("Error while executing compareColor -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String setDataByValue() {
		// This keyword sets the value attribute of an element into the test
		// sheet
		log.debug("====================================");
		log.debug("Executing setDataByValue");
		try {
			WebElement element = getWebElement(OR, objectArr[0]);
			String actualValue = element.getAttribute("value");

			log.debug("Value is" + actualValue);
			testData.setCellData(currentTest, data_column_nameArr[0], testRepeat, actualValue);
			log.debug("Value set in excel is" + testData.getCellData(currentTest, data_column_nameArr[0], testRepeat));
			return "Pass";

		} catch (Throwable t) {
			log.debug("Error in setDataByValue- " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyPath() {
		// This keyword verifies path of a document by using the test data sheet
		log.debug("==============================================");
		log.debug("Executing verifyPath Keyword");
		try {
			String expectedPath = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			WebElement element = getWebElement(OR, objectArr[0]);
			String actualPath = element.getAttribute("data-href");

			if (expectedPath.trim().equals(actualPath.trim()))
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing verifyPath " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyHelpImage() {
		log.debug("====================================");
		log.debug("Executing verifyHelpImage");
		String imageFileName;
		try {
			imageFileName = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			imageFileName = imageFileName + ".png";
			log.debug("Input Image File Name: " + imageFileName);
		} catch (Throwable e) {
			log.debug("Error while executing verifyHelpImage- TestData value is Null. " + "\n Stack Trace: \n" + e.getMessage());
			return "Fail";
		}
		try {
			WebElement element = getWebElement(OR, objectArr[0]);
			Functions.downloadImage(driver, log, element, imageFileName, "png");

			boolean ret1 = false;
			boolean ret2 = false;

			String image1 = System.getProperty("user.dir") + "/images/" + imageFileName;
			log.debug("Actual Image Path: " + image1);
			BufferedImage originalImage = ImageIO.read(new File(image1));
			String image2 = System.getProperty("user.dir") + "/inputImages/" + imageFileName;
			log.debug("Input Image Path: " + image2);
			BufferedImage inputImage = ImageIO.read(new File(image2));

			Raster ras1 = originalImage.getData();
			log.debug("Raster for Image1: " + ras1);
			Raster ras2 = inputImage.getData();
			log.debug("Raster for Image2: " + ras2);
			// Comparing the the two images for number of bands,width & height.
			if (ras1.getNumBands() == ras2.getNumBands() && ras1.getWidth() == ras2.getWidth() && ras1.getHeight() == ras2.getHeight()) {
				ret1 = true;
			}
			// Once the band ,width & height matches, comparing the images.
			search: for (int i = 0; i < ras1.getNumBands(); ++i) {
				for (int x = 0; x < ras1.getWidth(); ++x) {
					for (int y = 0; y < ras1.getHeight(); ++y) {
						if (ras1.getSample(x, y, i) == ras2.getSample(x, y, i)) {
							ret2 = true;
							break search;
						}
					}
				}
			}

			log.debug("Net Result Value: " + ret1);
			if (ret1 && ret2)
				return "Pass";
			else {
				log.debug("Images are not Same.");
				return "Fail";
			}
		} catch (Throwable t) {
			log.debug("Error while executing verifyHelpImage. Object:  " + objectArr[0] + "\n Stack Trace: \n" + t.getMessage());
			return "Fail";
		}
	}

	public String clickAtCoordinate() {
		log.debug("==============================================");
		log.debug("Executing clickAtCoordinate Keyword");
		try {
			WebElement toElement = getWebElement(OR, objectArr[0]);
			Actions builder = new Actions(driver);
			builder.moveToElement(toElement, 1, 1).click().perform();

			return "Pass";
		} catch (Throwable t) {
			log.debug("Error while executing clickAtCoordinate " + t.getMessage());
			return "Fail";
		}
	}

	public String pressEscape() {
		log.debug("=============================");
		log.debug("Executing pressEscape Keyword");
		// extract the test data
		try {
			Thread.sleep(WAIT2SEC);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.ESCAPE).build().perform();
			Thread.sleep(WAIT2SEC);
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing pressEscape -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String pressBackSpace() {
		log.debug("=============================");
		log.debug("Executing pressBackSpace Keyword");
		// extract the test data
		try {
			Thread.sleep(WAIT2SEC);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.BACK_SPACE).build().perform();
			Thread.sleep(WAIT2SEC);
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing pressBackSpace -" + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String pressEnd() {
		log.debug("=============================");
		log.debug("Executing pressEnd Keyword");
		// extract the test data
		try {
			Thread.sleep(WAIT2SEC);
			Actions action = new Actions(driver);
			action.sendKeys(Keys.END).build().perform();
			Thread.sleep(WAIT2SEC);
		} catch (Throwable t) {
			// report error
			log.debug("Error while executing pressEscape -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public String verifyTimeDifference() {
		log.debug("==============================================");
		log.debug("Executing verifyTimeDifference Keyword");
		String Flag = null;
		String Start = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		String Stop = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
		try {
			Flag = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
		} catch (Exception t) {
			log.debug("Error while executing verifyTimeDifference -" + t.getMessage());

		}

		String Start1 = Start.split("T")[0];
		String Start2 = (Start.split("T")[1]).split("\\.")[0];
		String dateStart = Start1 + " " + Start2;
		String Stop1 = Stop.split("T")[0];
		String Stop2 = Stop.split("T")[1].split("\\.")[0];
		String dateStop = Stop1 + " " + Stop2;

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			Date d1 = null;
			Date d2 = null;

			d1 = format.parse(dateStart);
			d2 = format.parse(dateStop);

			// in milliseconds
			long diff = d2.getTime() - d1.getTime();
			log.debug("Time Difference is: " + diff);

			if (diff > 0 && Flag == null) {
				return "Pass";
			} else if (diff == 0 && Flag.equalsIgnoreCase("FALSE")) {
				return "Pass";
			} else {
				log.debug("Time Difference is: " + diff + "  Please Debug");
				return "Fail";
			}

		} catch (Exception e) {
			log.debug("Error while executing verifyTimeDifference -" + e.getMessage());
			return "Fail";
		}

	}

	public String dragTill() {

		log.debug("=============================");
		log.debug("Executing dragTill");
		try {
			String rowsLocator = OR.getProperty(objectArr[0]);
			String dragThumbLocator = OR.getProperty(objectArr[1]);

			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			int count = Integer.parseInt(data);
			Functions.dragTillAllRowsLoaded(driver, log, rowsLocator, dragThumbLocator, count, 100);
			return "Pass";
		} catch (Throwable t) {
			log.debug("Error while executing dragTill -" + t.getMessage());
			return "Fail";
		}

	}
	public String runPdfComparison() {
		log.debug("=============================");
		log.debug("Executing runPdfComparison");
		try {
			File exepath, path;
			String cmd;
			exepath = new File(System.getProperty("user.dir") + "/dependencies/PDFComparison");
			path = new File(System.getProperty("user.dir") + "/dependencies");
			cmd = "cmd.exe /c start compare.bat " + path.toString();
			Runtime.getRuntime().exec(cmd, null, exepath);
			Thread.sleep(WAIT5SEC);
			return "Pass";
		} catch (Throwable t) {
			log.debug("Error while executing runPdfComparison" + t.getMessage());
			return "Fail";
		}
	}

	public String verifyTitle() {
		log.debug("=============================");
		log.debug("Executing verifyTitle");

		String expectedTitle, actualTitle;
		Boolean flag = true;

		try {
			expectedTitle = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			actualTitle = driver.getTitle();

			try {
				String append = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
				expectedTitle = expectedTitle + append;
			} catch (Throwable t) {
				// do nothing
			}

			log.debug("Expected Title :" + expectedTitle);
			log.debug("Actual Title :" + actualTitle);

			if (testBrowser.contains("InternetExplorer")) {
				if (actualTitle.contains(expectedTitle)) {
					log.debug("Values are equal.");
				} else {
					log.debug("Values are not equal.");
					flag = false;
				}

			} else {
				if (expectedTitle.equalsIgnoreCase(actualTitle)) {
					log.debug("Values are equal.");
				} else {
					log.debug("Values are not equal.");
					flag = false;
				}
			}
			if (flag)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error while executing verifyTitle -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String verifyPdfNameAndRename() {
		log.debug("=============================");
		log.debug("Executing verifyPdfNameAndRename");

		String fileName, replaceString = "", replaceByString = "", dir, temp1 = "", temp2 = "", concatString, newFileName, expectedFileName, dirPath, flag = "true";
		boolean result = false;
		File file;
		File[] files;
		try {

			dir = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

			try {
				temp1 = testData.getCellData(currentTest, data_column_nameArr[6], testRepeat);
				replaceString = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
				replaceByString = testData.getCellData(currentTest, data_column_nameArr[4], testRepeat);
				temp2 = testData.getCellData(currentTest, data_column_nameArr[5], testRepeat);
				flag = testData.getCellData(currentTest, data_column_nameArr[7], testRepeat);

			} catch (Throwable e) {
				log.debug("Do Nothing");
				// do nothing
			}

			concatString = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			newFileName = testData.getCellData(currentTest, data_column_nameArr[3], testRepeat);

			log.debug("Replace String: " + replaceString);
			log.debug("Concat String: " + concatString);
			log.debug("NewFileName : " + newFileName);
			dirPath = System.getProperty("user.dir") + dir;
			log.debug("Directory Path: " + dirPath);

			if (!temp2.equals("")) {
				concatString = temp2.trim().replaceAll(replaceString, replaceByString).concat(concatString);
			} else {
				concatString = temp1.replaceAll(replaceString, replaceByString).trim().concat("_")
						.concat(concatString.trim().replaceAll(" ", "+"));
			}
			Thread.sleep(WAIT2SEC);
			expectedFileName = concatString.concat(".pdf");
			log.debug("Expected File Name: " + expectedFileName);

			file = new File(dirPath);
			files = file.listFiles();
			for (File f : files) {
				if (f.exists()) {
					if (!f.getName().contains("SitePrinting") && f.getName().contains(".pdf")) {
						fileName = f.getName();

						if (fileName.equals(expectedFileName))
							result = true;

						log.debug("FileName to be renamed is: " + fileName);
						Functions.renameFile(dirPath, newFileName, f, log);
						Thread.sleep(WAIT2SEC);
					}
				}
			}
			if (flag.equalsIgnoreCase("true")) {
				if (result)
					return "Pass";
				else
					return "Fail";
			} else {
				if (result)
					return "Fail";
				else
					return "Pass";
			}

		} catch (Throwable t) {
			log.debug("Error while executing verifyPdfNameAndRename -" + t.getMessage());
			return "Fail";
		}
	}

	/*
	 * public String convertAndComparePdfFiles() {
	 * log.debug("=============================");
	 * log.debug("Executing convertAndComparePdfFiles");
	 * 
	 * String fileName = null, tempDirTest,file1 = null,file2 =
	 * null,tempDirStage,testDirectory,stageDirectory; boolean result = true;
	 * File tmpFile1, tmpFile2, fileTemp1, fileTemp2, temp1, temp2;
	 * BufferedImage tempImage1, tempImage2; File[] files1, files2;
	 * 
	 * try{
	 * 
	 * fileName = testData.getCellData(currentTest,
	 * data_column_nameArr[0],testRepeat); log.debug("FileName: "+fileName);
	 * 
	 * stageDirectory = testData.getCellData(currentTest,
	 * data_column_nameArr[1],testRepeat); testDirectory =
	 * testData.getCellData(currentTest, data_column_nameArr[2],testRepeat);
	 * 
	 * file1 = System.getProperty("user.dir")+stageDirectory+fileName; file2 =
	 * System.getProperty("user.dir")+testDirectory+fileName;
	 * 
	 * tempDirStage =
	 * System.getProperty("user.dir")+"/dependencies/tempDirStage"; tmpFile1 =
	 * new File(tempDirStage); tmpFile1.mkdir();
	 * 
	 * tempDirTest = System.getProperty("user.dir")+"/dependencies/tempDirTest";
	 * tmpFile2 = new File(tempDirTest); tmpFile2.mkdir();
	 * 
	 * Functions.convertPdfToImage(file1, log, "/dependencies/tempDirStage");
	 * Functions.convertPdfToImage(file2, log, "/dependencies/tempDirTest");
	 * 
	 * fileTemp1 = new File(tempDirStage); files1 = fileTemp1.listFiles();
	 * 
	 * fileTemp2 = new File(tempDirTest); files2 = fileTemp2.listFiles();
	 * 
	 * if(files1.length != files2.length){ result = false;
	 * log.debug("No. of pages in the pdfs are different."); } else{ for(int
	 * i=0;i<files1.length;i++){ if(result){ temp1 = files1[i]; temp2 =
	 * files2[i]; tempImage1 = ImageIO.read(temp1); tempImage2 =
	 * ImageIO.read(temp2); log.debug("ImageFile1 at "+i+" :"+temp1);
	 * log.debug("ImageFile2 at "+i+" :"+temp2); result =
	 * Functions.compareTwoImages(tempImage1, tempImage2, log); } } }
	 * FileUtils.deleteDirectory(tmpFile1); FileUtils.deleteDirectory(tmpFile2);
	 * 
	 * log.debug("ImageFiles1 Deleted from: "+tmpFile1);
	 * log.debug("ImageFiles2 Deleted from: "+tmpFile2);
	 * log.debug("Result: "+result);
	 * 
	 * if(result){ return "Pass"; } else{ return "Fail"; } }catch (Throwable t)
	 * { log.debug("Error while executing convertAndComparePdfFiles" +
	 * t.getMessage()); return "Fail"; } }
	 */public String verifyDynamicSearch() {
		log.debug("=============================");
		log.debug("Executing verifyDynamicSearch");
		List<WebElement> search;
		boolean result = false;
		String searchInput, attribute;
		try {
			search = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			searchInput = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			attribute = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			for (WebElement item : search) {
				if (!item.getAttribute(attribute).contains(searchInput)) {
					result = false;
					break;
				} else
					result = true;
			}
			if (result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error while executing  verifyDynamicSearch- " + t.getMessage());
			return "Fail";
		}
	}

	public String trimAndVerifyText() {
		log.debug("=============================");
		log.debug("Executing trimAndVerifyText");
		String actualText, expectedText;

		try {
			WebElement element = getWebElement(OR, objectArr[0]);
			expectedText = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			actualText = element.getText().replaceAll("\n", " ");

			log.debug("Actual Text Trimmed : " + actualText.trim());
			log.debug("Expected Text Trimmed : " + expectedText.trim());

			if (actualText.trim().equals(expectedText.trim()))
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing trimAndVerifyText" + t.getMessage());
			return "Fail";

		}
	}

	public String verifyDropdownSize() {
		log.debug("=============================");
		log.debug("Executing verifyDropdownSize");
		List<WebElement> search;
		boolean result = false;
		String expectedSize;
		int size, expectedIntSize;
		try {
			search = driver.findElements(By.xpath(OR.getProperty(objectArr[0])));
			size = search.size();
			expectedSize = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			expectedIntSize = Integer.parseInt(expectedSize);
			if (expectedIntSize == size)
				result = true;

			if (result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error while executing  verifyDropdownSize- " + t.getMessage());
			return "Fail";
		}
	}

	public String verifyWorkspaceExcelNameAndRename() {
		log.debug("=============================");
		log.debug("Executing verifyWorkspacePdfNameAndRename");
		String fileName, dir, name, temp, concatString, newFileName, expectedFileName, dirPath, replaceString = "", replaceByString = "";
		boolean result = false;
		File tmpFile = null;
		try {
			dir = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			temp = getWebElement(OR, objectArr[0]).getText();
			replaceString = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			replaceByString = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			name = temp.trim().replaceAll(replaceString, replaceByString);
			testData.setCellData(currentTest, data_column_nameArr[5], testRepeat, name);
			concatString = testData.getCellData(currentTest, data_column_nameArr[3], testRepeat);
			newFileName = testData.getCellData(currentTest, data_column_nameArr[4], testRepeat);
			expectedFileName = name.concat(concatString);
			log.debug("NewFileName : " + newFileName);
			log.debug("Expected File Name: " + expectedFileName);
			dirPath = System.getProperty("user.dir") + dir;
			log.debug("Directory Path: " + dirPath);
			File file = new File(dirPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.exists()) {
					if (!(f.getName().contains("ExcelDownload") || f.getName().contains("svn"))) {
						fileName = f.getName();
						if (fileName.equals(expectedFileName)) {
							result = true;
							log.debug("Name of downloaded file is as expected name");
						}
						log.debug("FileName to be renamed is: " + fileName);
						tmpFile = new File(dirPath + "/" + newFileName);
						log.debug("File Renamed at: " + tmpFile);
						f.renameTo(tmpFile);
					}
				}
			}
			if (result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error while executing verifyWorkspaceExcelNameAndRename -" + t.getMessage());
			return "Fail";
		}
	}

	public String mergeAndReplaceStrings() {
		log.debug("=============================");
		log.debug("Executing  mergeAndReplaceStrings");
		String firstString, secondString, thirdString, fourthString, finalString, fifthString = null, replacedString = null;
		try {

			firstString = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			secondString = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			thirdString = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			fourthString = testData.getCellData(currentTest, data_column_nameArr[3], testRepeat);
			try {
				fifthString = testData.getCellData(currentTest, data_column_nameArr[4], testRepeat);
			} catch (Throwable t) {
				fifthString = null;
			}
			if (!fifthString.equals(null) && !fifthString.isEmpty()) {
				replacedString = fifthString.replaceAll(" ", "_").trim();
				finalString = firstString.concat(secondString).concat(replacedString).concat(secondString).concat(thirdString)
						.concat(fourthString);
				log.debug("String after concatenation is: " + finalString);
				testData.setCellData(currentTest, data_column_nameArr[5], testRepeat, finalString);
				log.debug("Value set in excel is" + testData.getCellData(currentTest, data_column_nameArr[5], testRepeat));
			} else {
				finalString = firstString.concat(secondString).concat(thirdString).concat(fourthString);
				log.debug("String after concatenation is: " + finalString);
				testData.setCellData(currentTest, data_column_nameArr[5], testRepeat, finalString);
				log.debug("Value set in excel is" + testData.getCellData(currentTest, data_column_nameArr[5], testRepeat));
			}

		} catch (Throwable t) {
			// report error
			log.debug("Error while executing mergeAndReplaceStrings -" + t.getMessage());
			return "Fail - test Data Not Found";
		}
		return "Pass";
	}

	public String verifyWorkspaceLenseExcelName() {
		log.debug("=============================");
		log.debug("Executing verifyWorkspaceLenseExcelName");
		String fileName, expectedFileName, newFileName, dirPath, dir;
		boolean result = false;
		File tmpFile = null;
		try {
			dir = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			expectedFileName = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			newFileName = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);
			log.debug("NewFileName : " + newFileName);
			log.debug("Expected File Name: " + expectedFileName);
			dirPath = System.getProperty("user.dir") + dir;
			log.debug("Directory Path: " + dirPath);
			File file = new File(dirPath);
			File[] files = file.listFiles();
			for (File f : files) {
				if (f.exists()) {
					if (!(f.getName().contains("ExcelDownload") || f.getName().contains("svn"))) {
						fileName = f.getName();
						log.debug("File name is: " + f.getName());
						if (fileName.equals(expectedFileName)) {
							result = true;
							log.debug("Name of downloaded file is as expected name");
						}
						log.debug("FileName to be renamed is: " + fileName);
						tmpFile = new File(dirPath + "/" + newFileName);

						log.debug("File Renamed at: " + tmpFile);
						f.renameTo(tmpFile);
					}
				}
			}
			if (result)
				return "Pass";
			else
				return "Fail";

		} catch (Throwable t) {
			log.debug("Error while executing verifyWorkspaceLenseExcelName -" + t.getMessage());
			return "Fail";
		}
	}

	public String compareExcel() {
		log.debug("=============================");
		log.debug("Executing compareExcel");
		// this keyword compares two excel files
		String fileName = null, file1 = null, file2 = null, testDirectory, stageDirectory;
		File myFile, myFile2;
		FileInputStream fis, fis2;
		XSSFWorkbook myWorkBook, myWorkBook2;
		XSSFSheet mySheet, mySheet2;
		Iterator<Row> rowIterator, rowIterator2;
		boolean flag = false;
		try {

			fileName = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			log.debug("FileName: " + fileName);

			stageDirectory = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
			testDirectory = testData.getCellData(currentTest, data_column_nameArr[2], testRepeat);

			file1 = System.getProperty("user.dir") + stageDirectory + fileName;
			file2 = System.getProperty("user.dir") + testDirectory + fileName;

			myFile = new File(file1);
			fis = new FileInputStream(myFile);
			myFile2 = new File(file2);
			fis2 = new FileInputStream(myFile2);

			myWorkBook = new XSSFWorkbook(fis);
			myWorkBook2 = new XSSFWorkbook(fis2);

			mySheet = myWorkBook.getSheetAt(0);
			mySheet2 = myWorkBook2.getSheetAt(0);

			rowIterator = mySheet.iterator();
			rowIterator2 = mySheet2.iterator();

			while (rowIterator.hasNext() && rowIterator2.hasNext()) {
				Row row1 = rowIterator.next();
				Row row2 = rowIterator2.next();
				Iterator<Cell> cellIterator1 = row1.cellIterator();
				Iterator<Cell> cellIterator2 = row2.cellIterator();

				while (cellIterator1.hasNext() && cellIterator2.hasNext()) {
					Cell cell1 = cellIterator1.next();
					Cell cell2 = cellIterator2.next();

					switch (cell1.getCellType()) {

					case Cell.CELL_TYPE_STRING:
						if (cell1.getStringCellValue().equals(cell2.getStringCellValue())) {
							flag = true;
							log.debug(cell1.getStringCellValue() + "\t");
						} else {
							log.debug("Failed at: " + cell1.getColumnIndex() + " " + cell1.getRowIndex() + " Data:  "
									+ cell1.getStringCellValue() + "\t");
							return "Fail: files are not same";
						}
						break;

					case Cell.CELL_TYPE_NUMERIC:
						if (cell1.getNumericCellValue() == cell2.getNumericCellValue()) {
							flag = true;
							log.debug(cell1.getNumericCellValue() + "\t");
						} else {
							log.debug("Failed at: " + cell1.getColumnIndex() + " " + cell1.getRowIndex() + " Data:  "
									+ cell1.getNumericCellValue() + "\t");
							return "Fail: files are not same";
						}
						break;

					case Cell.CELL_TYPE_BOOLEAN:
						if (cell1.getBooleanCellValue() == cell1.getBooleanCellValue()) {
							flag = true;
							log.debug(cell1.getBooleanCellValue() + "\t");
						} else {
							log.debug("Failed at: " + cell1.getColumnIndex() + " " + cell1.getRowIndex() + " Data:  "
									+ cell1.getBooleanCellValue() + "\t");
							return "Fail: files are not same";
						}
						break;

					default:
						log.debug("In default ");
					}
				}
			}

			log.debug("Flag is " + flag);

			if (flag) {
				log.debug("Files are same");
				return "Pass";
			} else {
				log.debug("Files are not same");
				return "Fail";
			}
		} catch (Throwable t) {
			log.debug("Error while executing compareExcel -" + t.getMessage());
			return "Fail";
		}
	}

	public String verifyListText() {
		log.debug("===============================");
		log.debug("Executing verifyListText");

		String date;
		List<WebElement> searchResults;
		int actualCount;
		boolean status = false;
		try {
			date = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			searchResults = getWebElements(OR, objectArr[0]);
			actualCount = searchResults.size();

			log.debug("Search Text: " + date);
			log.debug("Expected Count: " + actualCount);

			for (int i = 0; i < actualCount; i++) {
				log.debug("Data-title: " + searchResults.get(i).getText());
				if ((searchResults.get(i).getText().contains(date)))
					status = true;
			}
			if (status)
				return "Pass";
			else
				return "Fail";
		} catch (Throwable t) {
			log.debug("Error while executing verifyListText -" + objectArr[0] + t.getMessage());
			return "Fail";
		}
	}

	public String launchWebpage() {
		log.debug("=============================");
		log.debug("executing keyword launchWebpage");

		DesiredCapabilities cap = null;
		launchBrowser = testBrowser;
		@SuppressWarnings("unused")
		String currentTitle;

		if (launchBrowser.equalsIgnoreCase("Firefox")) {

			FirefoxProfile profile = new FirefoxProfile();
			profile.setPreference("geo.prompt.testing", true);
			profile.setPreference("geo.prompt.testing.allow", true);
			// profile.setEnableNativeEvents(true);
			log.debug("inside navigate firefox");
			cap = DesiredCapabilities.firefox();
			cap.setBrowserName("firefox");
			cap.setCapability("nativeEvents", true);
			cap.setCapability(FirefoxDriver.PROFILE, profile);

		} else if (launchBrowser.equalsIgnoreCase("InternetExplorer")) {
			log.debug("webdriver.ie.driver: " + System.getProperty("user.dir") + "/drivers/IEDriverServer.exe");
			System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "/drivers/IEDriverServer.exe");
			log.debug("inside navigate IE");
			cap = DesiredCapabilities.internetExplorer();
			// cap.setBrowserName("iexplore");
			cap.setPlatform(Platform.WINDOWS);
			cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
			cap.setCapability("enablePersistentHover", false);
			// cap.setCapability("requireWindowFocus", true);
			cap.setCapability("ignoreProtectedModeSettings", true);
			cap.setCapability("ie.ensureCleanSession", true);

		} else if (launchBrowser.equalsIgnoreCase("Chrome")) {

			log.debug("inside navigate chrome");
			cap = DesiredCapabilities.chrome();
			cap.setBrowserName("chrome");

			String chromeDriver;
			if (System.getProperty("os.name").equals("Mac OS X")) {
				cap.setPlatform(Platform.MAC);
				chromeDriver = "chromedriver";
			} else {
				cap.setPlatform(Platform.WINDOWS);
				chromeDriver = "chromedriver.exe";

				log.debug("webdriver.chrome.driver: " + System.getProperty("user.dir") + "/drivers/" + chromeDriver);
				System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/" + chromeDriver);
			}

			ChromeOptions options = new ChromeOptions();

			options.addArguments("--silent");
			options.addArguments("--disable-extensions");
			options.addArguments("test-type");
			options.addArguments("start-maximized");

			cap.setCapability(ChromeOptions.CAPABILITY, options);

		} else if (launchBrowser.equalsIgnoreCase("Safari")) {
			cap = DesiredCapabilities.safari();
			cap.setBrowserName("safari");
			cap.setPlatform(Platform.MAC);
		}

		log.debug("Url: " + CONFIG.getProperty(objectArr[0]));

		try {
			if (testCONFIG.getProperty("Env").equals("LocalMachine")) {
				driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), cap);
				cap.setCapability("nativeEvents", true);
			} else {
				driver = new RemoteWebDriver(new URL("http://ggstoolsvc.sapient.com:8080/jenkins/wd/hub"), cap);
			}

			driver.navigate().to(CONFIG.getProperty(objectArr[0]));
			currentTitle = driver.getTitle();

		} catch (Throwable e) {
			log.debug(e.getMessage());
			return "Fail";
		}
		log.debug("@@@ DRIVER K1 " + driver);

		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

		if (launchBrowser.equalsIgnoreCase("Chrome") && System.getProperty("os.name").equals("Mac OS X")) {
			driver.manage().window().setSize(new Dimension(1920, 978));
		} else {
			driver.manage().window().maximize();
		}
		return "Pass";
	}

	public String removeSpecialChar() {

		log.debug("=============================");
		log.debug("executing keyword removeSpecialChar");

		String oldCount;

		try {
			oldCount = getWebElement(OR, objectArr[0]).getText();
			Functions.replaceAll(oldCount, "[^0-9]", "");
			testData.setCellData(currentTest, data_column_nameArr[0], 2, oldCount);

			log.debug("data set in excel :" + oldCount);
			return "Pass";
		} catch (Throwable r) {
			log.debug("Error while executing removeSpecialChar keyword" + r.getMessage());
			return "Fail";
		}
	}

	public String checkDateFormat() {
		log.debug("=============================");
		log.debug("Executing checkDateFormat");
		// this keyword takes a date input and checks its format
		String date;
		String[] format = { "MMM dd yyyy" };
		date = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		if (date != null) {
			for (String string : format) {
				SimpleDateFormat sdf = new SimpleDateFormat(string);
				try {
					sdf.parse(date);
					log.debug("Printing the value of " + string);
				} catch (ParseException e) {
					log.debug("Date is not in required format");
					return "Fail";
				}
			}
		}
		return "Pass";
	}

	public String splitAndSetString() {
		log.debug("=============================");
		log.debug("Executing splitAndSetString");
		// this keyword takes an input string splits it and sets it
		String inputString, setString, splitBy;
		String[] splitString;
		inputString = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		splitBy = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
		try {
			splitString = inputString.split(splitBy);
		} catch (Throwable r) {
			log.debug("Error while executing splitAndSetString keyword" + r.getMessage());
			return "Fail";
		}
		if (splitBy.contains(":"))
			setString = splitString[0];
		else
			setString = splitString[1].trim();
		testData.setCellData(currentTest, data_column_nameArr[2], testRepeat, setString);
		log.debug("Value set in excel is" + testData.getCellData(currentTest, data_column_nameArr[2], testRepeat));
		return "Pass";
	}

	public String compareInputFromExcel() {
		log.debug("=============================");
		log.debug("Executing compareInputFromExcel");
		// this keyword takes two string and compares them.
		String inputString, expectedString;
		inputString = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
		expectedString = testData.getCellData(currentTest, data_column_nameArr[1], testRepeat);
		if (inputString.equals(expectedString))
			return "Pass";
		else
			return "Fail";

	}

	public String selectFromDropdown() {

		log.debug("=============================");
		log.debug("executing keyword selectFromDropdown");

		try {
			WebElement dropdownObject = getWebElement(OR, objectArr[0]);
			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);

			log.debug("data is  :" + data);
			Select dropdown = new Select(dropdownObject);
			dropdown.selectByVisibleText(data);

		} catch (Throwable r) {
			log.debug("Error while executing selectFromDropdown keyword" + r.getMessage());
			return "Fail";
		}
		return "Pass";
	}

	public DesiredCapabilities setCapabilitiesFirefox(DesiredCapabilities cap) {

		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("geo.prompt.testing", true);
		profile.setPreference("geo.prompt.testing.allow", true);
		log.debug("inside navigate firefox");
		cap = DesiredCapabilities.firefox();
		cap.setBrowserName("firefox");
		cap.setCapability(FirefoxDriver.PROFILE, profile);
		return cap;

	}

	public DesiredCapabilities setCapabilitiesChrome(DesiredCapabilities cap) {

		log.debug("inside navigate chrome");
		cap = DesiredCapabilities.chrome();
		cap.setBrowserName("chrome");

		String chromeDriver;
		if (System.getProperty("os.name").equals("Mac OS X")) {
			cap.setPlatform(Platform.MAC);
			chromeDriver = "chromedriver";
		} else {
			cap.setPlatform(Platform.WINDOWS);
			chromeDriver = "chromedriver.exe";

			log.debug("webdriver.chrome.driver: " + System.getProperty("user.dir") + "/drivers/" + chromeDriver);
			System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/drivers/" + chromeDriver);
		}

		ChromeOptions options = new ChromeOptions();

		options.addArguments("--silent");
		options.addArguments("--disable-extensions");
		options.addArguments("test-type");
		options.addArguments("start-maximized");

		cap.setCapability(ChromeOptions.CAPABILITY, options);
		return cap;

	}

	public DesiredCapabilities setCapabilitiesInternetExplorer(DesiredCapabilities cap) {

		log.debug("webdriver.ie.driver: " + System.getProperty("user.dir") + "/drivers/IEDriverServer.exe");
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir") + "/drivers/IEDriverServer.exe");
		log.debug("inside navigate IE");
		cap = DesiredCapabilities.internetExplorer();
		cap.setPlatform(Platform.WINDOWS);
		cap.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		cap.setCapability("enablePersistentHover", false);
		// cap.setCapability("requireWindowFocus", true);
		cap.setCapability("ignoreProtectedModeSettings", true);
		cap.setCapability("ie.ensureCleanSession", true);
		return cap;

	}

	public DesiredCapabilities setCapabilitiesSafari(DesiredCapabilities cap) {
		cap = DesiredCapabilities.safari();
		cap.setBrowserName("safari");
		cap.setPlatform(Platform.MAC);
		return cap;
	}

	public String selectByDropdownText() {
		log.debug("executing selectByDropdownText ");

		try {
			WebElement selectDropdown = driver.findElement(getBy(OR, objectArr[0]));
			Select select = new Select(selectDropdown);
			String data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
			select.selectByVisibleText(data);
			return "Pass";
		} catch (Throwable e) {
			log.debug(e.getMessage());
			return "Fail";
		}
	}
}// close keywords class

package com.poc.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.poc.Keywords;
import com.poc.report.ModuleStats;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ReportsUtil extends Keywords {
	public String version;
	public Logger log;
	public static File indexHTML;
	public static String RUN_DATE;
	public static String testStartTime;
	public static String testEndTime;
	public static String ENVIRONMENT;
	public static String suite;

	public static Integer passCount;
	public static Integer failCount;
	public static Integer skipCount;
	public static Integer grandTotal;

	public static ArrayList<ModuleStats> allModulesStats;
	public static Map<String, Object> indexFileData;

	static {
		allModulesStats = new ArrayList<ModuleStats>();
		indexFileData = new HashMap<String, Object>();
	}

	public ReportsUtil() {

	}

	// returns current date and time
	public static String now(String dateFormat) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(cal.getTime());

	}

	// store screenshots
	public static void takeScreenShot(String file, RemoteWebDriver driver, String reportFolder, Logger log) {
		try {
			driver = (RemoteWebDriver) new Augmenter().augment(driver);
			File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
			FileUtils.moveFile(scrFile, new File(System.getProperty("user.dir") + File.separator + reportFolder, file));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Throwable t) {
			// TODO: handle exception
			log.debug(t.getMessage());
		}

	}

	public static void prepareWebReport(String templatePath, Map<String, Object> data, File targetFile) throws IOException {
		Configuration cfg = new Configuration();
		FileWriter filestream = null;
		BufferedWriter bw = null;
		try {
			// Load template from source folder
			Template template = cfg.getTemplate(templatePath);
			// File output
			// Create file if it doesn't exists
			if (!targetFile.exists()) {
				targetFile.createNewFile();
			}
			filestream = new FileWriter(targetFile);
			bw = new BufferedWriter(filestream);
			template.process(data, bw);
			bw.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		} finally {
			if (bw != null) {
				bw.close();
			}
		}

	}

	public static void clearTempFolder() throws IOException {

		try {
			File file = new File(System.getProperty("java.io.tmpdir"));
			FileUtils.cleanDirectory(file);
		}

		catch (IOException e) {
			// Do nothing since
		}
	}

	public static void shutDownGrid() throws IOException {
		try {
			if (testCONFIG.getProperty("Env").equals("LocalMachine")) {
				Runtime.getRuntime().exec("taskkill /IM cmd.exe");
				Runtime.getRuntime().exec("taskkill /IM java.exe");
				Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /f");
				Runtime.getRuntime().exec("taskkill /IM IEDriverServer.exe /f");
				Runtime.getRuntime().exec("taskkill /IM iexplore.exe /f");
			} else {
				Runtime.getRuntime().exec("taskkill /IM cmd.exe");
				Runtime.getRuntime().exec("taskkill /IM java.exe");
				Runtime.getRuntime().exec("taskkill /IM chromedriver.exe /f");
				Runtime.getRuntime().exec("taskkill /IM IEDriverServer.exe /f");
				Runtime.getRuntime().exec("taskkill /IM iexplore.exe /f");
				Runtime.getRuntime().exec("taskkill /IM chrome.exe /f");
				Runtime.getRuntime().exec("taskkill /IM firefox.exe /f");
			}
		} catch (Throwable t) {

		}

	}

}

package com.poc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import com.poc.util.DurationTracker;
import com.poc.util.Functions;
import com.poc.util.ReportsUtil;

public class Driver extends Controller {

	private DurationTracker durationTracker;
	private String enviroment;
	private String envLink;
	private String suiteName;
	private String browserName;

	File batchFileDir;

	static {
		try {
			testCONFIG = Functions.loadConfigFile("config", "TestConfiguration");

			CONFIG = Functions.loadConfigFile("config", "config_" + testCONFIG.getProperty("RunTestEnv"));

			OR = Functions.loadConfigFile("objectRepo", "OR");

			APPTEXT = Functions.loadConfigFile("objectRepo", "APPTEXT");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@BeforeSuite
	public void createReportFolder() throws FileNotFoundException, IOException, InterruptedException {
		if (!testCONFIG.getProperty("TestBrowser").equalsIgnoreCase("Safari")) {

			ReportsUtil.shutDownGrid();

			Thread.sleep(WAIT3SEC);
			batchFileDir = new File(System.getProperty("user.dir") + "/lib/grid");
			if (testCONFIG.getProperty("Env").equals("LocalMachine")) {
				Runtime.getRuntime().exec("cmd.exe /c start grid_LOCAL_WIN.bat", null, batchFileDir);
			} else {
				Runtime.getRuntime().exec("cmd.exe /c start grid_TEST_WIN1.bat", null, batchFileDir);
			}
			Thread.sleep(5000);
		} else {
			batchFileDir = new File(System.getProperty("user.dir") + "/lib/grid");

			String node;

			if (testCONFIG.getProperty("Env").equals("LocalMachine")) {
				node = "gridNode_LOCAL_MAC.sh";
				String[] cmd = { "/usr/bin/open", "-a", "terminal.app", batchFileDir.toString() + "/gridHub_LOCAL_MAC.sh" };
				Runtime.getRuntime().exec(cmd);
			} else {
				node = "gridNode_TEST_MAC.sh";
			}

			String[] cmd1 = { "/usr/bin/open", "-a", "terminal.app", batchFileDir.toString() + "/" + node };
			Runtime.getRuntime().exec(cmd1);
		}

		durationTracker = new DurationTracker();
		durationTracker.startTime();

		reportFolder = "WebReport-" + durationTracker.getStartTime();
		reportFolder.replaceAll(" ", "_");
		new File(reportFolder).mkdir();
		setReportVariables();
		ReportsUtil.indexFileData.put("indexFileData", ReportsUtil.allModulesStats);
		ReportsUtil.indexFileData.put("suiteDurationTracker", durationTracker);
		ReportsUtil.indexFileData.put("environment", enviroment);
		ReportsUtil.indexFileData.put("envLink", envLink);
		ReportsUtil.indexFileData.put("suiteName", suiteName);
		ReportsUtil.indexFileData.put("browserName", browserName);
	}

	@AfterSuite
	public void createMainReport() throws IOException {
		ReportsUtil.clearTempFolder();
		ReportsUtil.shutDownGrid();

		if (!testCONFIG.getProperty("Env").equals("LocalMachine")) {
			Runtime.getRuntime().exec("cmd.exe /c start grid_TEST_WIN.bat", null, batchFileDir);
		}

	}

	private void setReportVariables() throws FileNotFoundException, IOException {

		enviroment = testCONFIG.getProperty("RunTestEnv");
		envLink = CONFIG.getProperty("mainApp.url");
		browserName = testCONFIG.getProperty("TestBrowser");
		if (testCONFIG.getProperty("RunTest").equalsIgnoreCase("sanitySuite")) {
			suiteName = "Smoke Test";
		} else {
			suiteName = "Regression Test";
		}
	}

}

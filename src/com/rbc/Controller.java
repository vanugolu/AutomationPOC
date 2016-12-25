package com.rbc;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;

import com.rbc.report.ModuleStats;
import com.rbc.report.ModuleTestCasesStats;
import com.rbc.report.TestStepStats;
import com.rbc.util.DurationTracker;
import com.rbc.util.Functions;
import com.rbc.util.ReportsUtil;
import com.rbc.xls.ExcelOperations;

public class Controller extends Keywords {
	String result = "false";
	ReportsUtil reportsUtil = new ReportsUtil();
	ModuleStats moduleStats;
	boolean stepstatus;
	int testSequenceId = 1;

	public Controller() {
		super();
	}

	public void startTesting() {

		runTest = (String) testCONFIG.get("RunTest");
		testBrowser = (String) testCONFIG.get("TestBrowser");
		captureScreenShot = (String) testCONFIG.get("CaptureScreenShot");
	}

	public void testAppMain() throws IOException, InterruptedException {

		if (runTest.equalsIgnoreCase("regressionSuite") || runTest.equalsIgnoreCase("sanitySuite")) {
			System.out.println("RUN TEST = " + runTest + "  BROWSER  = " + launchBrowser);
			for (int moduleCount = 0; moduleCount < modules.length; moduleCount++) {
				launchBrowser = testBrowser;
				System.out.println("ELSE IF launchBrowser " + launchBrowser);
				System.out.println("displayBrowserVersion " + displayBrowserVersion);
				Thread.sleep(5000);
				String module = modules[moduleCount];
				initializeAndRun(module);
			}

		} else {
			launchBrowser = testBrowser;
			System.out.println("displayBrowserVersion " + displayBrowserVersion);
			Thread.sleep(5000);
			initializeAndRun(runTest);
		}

	}

	public void initializeAndRun(String runTest) throws IOException, InterruptedException {
		File testFiles = new File(System.getProperty("user.dir") + "/src/files.modules/" + runTest);
		if (testFiles.isDirectory()) {
			String[] files = testFiles.list();
			testCasesfileAssigned = false;
			testDatafileAssigned = false;
			for (String fileName : files) {
				initialize(fileName);
			}
		}
		Thread.sleep(5000);
		testApp();

	}

	public void initialize(String fileName) throws IOException {

		String srcFolder;

		srcFolder = System.getProperty("user.dir") + "/src/files.modules/";

		// initialize test module controller excel file
		if (fileName.contains("Controller") && (testCasesfileAssigned == false)) {
			System.out.println("Controller excel file is : " + fileName);
			controller = new ExcelOperations(srcFolder + modules[0] + "/" + fileName);
			log.debug("controller file : " + fileName);
			testCasesfileAssigned = true;
		}

		// initialize test data excel file
		if (fileName.contains("TestData") && testDatafileAssigned == false) {
			

			log.debug("test data  file : " + fileName);
			testData = new ExcelOperations(srcFolder + modules[0] + "/" + fileName);
			testDatafileAssigned = true;
		}
	}

	// @Test
	public void testApp() throws InterruptedException, IOException {
		// String actualTestCaseExec = "";
		moduleStats = new ModuleStats();
		DurationTracker durationTracker = new DurationTracker();
		ArrayList<ModuleTestCasesStats> moduleTestCasesStats = new ArrayList<ModuleTestCasesStats>();
		Integer passCount = 0;
		Integer failCount = 0;
		Integer skipCount = 0;
		boolean testCaseExecuted = false, flagNavigationError = false;

		Set<String> set = new HashSet<String>();
		Integer manualSize = 0;

		log.debug("");

		log.debug("===================================================================================");
		log.debug("OS : " + System.getProperty("os.name") + "  Browser : " + displayBrowserVersion + "   Module : "
				+ modules[0].toUpperCase());
		log.debug("====================================================================================");
		log.debug("");
		System.out.println("OS : " + System.getProperty("os.name") + "  Browser : " + displayBrowserVersion + "   Module : "
				+ modules[0].toUpperCase());
		boolean isSmokeTest = false;
		String smokeTest = "IsSmokeTest";
		String currentTest_Mapping, testCaseMappingDescription = null;

		if (runTest.equalsIgnoreCase("sanitySuite")) {
			isSmokeTest = true;

		}
		
		durationTracker.startTime();
		ReportsUtil.allModulesStats.add(moduleStats);
		moduleStats.setModuleName(modules[0]);
		moduleStats.setDurationTracker(durationTracker);

		DurationTracker testCaseDurationTracker = null;
		ModuleTestCasesStats testCasesStats = null;

		for (int tcid = 2; tcid <= controller.getRowCount(modules[0]); tcid++) {
			// Thread.sleep(5000);
			String fileName = null;
			String currentTest = controller.getCellData(modules[0], "TCID", tcid);
			String currentTest_Description = controller.getCellData(modules[0], "Description", tcid);
			this.currentTest = currentTest;
			this.currentTest_Description = currentTest_Description;

			currentTest_Mapping = controller.getCellData(modules[0], "Manual TC ID", tcid);

			// initialize start time of test
			if (controller.getCellData(modules[0], "Runmode", tcid).equals("Y") && !flagNavigationError) {
				int currentTestDataCount = testData.getRowCount(currentTest); // holds total rows
				// TestData sheet.
				// If sheet does not
				// exist then 2 by
				// default
				if (currentTestDataCount < 2) {
					currentTestDataCount = 2; // run at least once
				}
				int stepSequenceId = 1;
				for (testRepeat = 2; testRepeat <= currentTestDataCount; testRepeat++) {
					
					//currentTest_Description = currentTest_Description +" datarow "+ testRepeat;// for more then 1 row of data 
					log.debug("Executing the test :                                " + currentTest);
					log.debug("Test Description :    " + currentTest_Description);
					
					testCasesStats = new ModuleTestCasesStats();
					testCaseDurationTracker = new DurationTracker();
					testCaseDurationTracker.startTime();
					testCasesStats.setSequenceId(testSequenceId++);
					testCasesStats.setTestCaseDescription(currentTest + " -" + currentTest_Description+" datarow "+ testRepeat);
					System.out.println(testCasesStats.getTestCaseDescription());
					fileName = getTestCaseFileName(testCasesStats.getTestCaseDescription(), tcid);
					fileName = fileName.substring(fileName.lastIndexOf(File.separator) + 1, fileName.length());
					System.out.println(fileName);
					
					if (isSmokeTest) {
						
						if (controller.getCellData(modules[0], smokeTest, tcid).equals("Y")) {
							System.out.println("### " + modules[0] + ": Test Case: " + currentTest);
							testCaseMappingDescription = executeTest("Runmode", currentTest_Mapping, testCaseMappingDescription, tcid, stepSequenceId);
							testCaseExecuted = true;
						}
						// else {
						// testCaseMappingDescription =
						// skipTestCase(currentTest_Mapping);
						// }
					} else {
						System.out.println("### " + modules[0] + ": Test Case: " + currentTest);
						testCaseMappingDescription = executeTest("Runmode", currentTest_Mapping, testCaseMappingDescription, tcid, stepSequenceId);
						testCaseExecuted = true;
					}
					stepSequenceId++;
					if (testCaseExecuted) {
						if (!currentTest_Mapping.isEmpty() || currentTest_Mapping != null) {
							set.addAll(Arrays.asList(currentTest_Mapping.split(",")));
							manualSize = set.size();
						}
						testCasesStats.setManualTCId(currentTest_Mapping);
						if (stepstatus) {
							testCasesStats.setResult("Pass");
							passCount++;
						} else {
							testCasesStats.setResult("Fail");
							failCount++;
						}
						
						testCaseDurationTracker.endTime();
						testCasesStats.setDurationTracker(testCaseDurationTracker);
						testCasesStats.setTestCaseHyperLinkName(fileName);
						moduleTestCasesStats.add(testCasesStats);
						durationTracker.endTime();
						testCaseExecuted = false;
						
					}
					if (result.contains("Domain Page not Found")) {
						break;
					}

					if (testStatus == null) {
						testStatus = "Pass";
					}

					log.debug("****************************************************" + currentTest + " --- " + testStatus);
					testStatus = null;
					createReport(durationTracker, moduleTestCasesStats, passCount, failCount, skipCount, manualSize);
				}

			} else if (!(controller.getCellData(modules[0], "Runmode", tcid).equals("N"))) {
				testCasesStats.setResult("Skip");
				//failCount++;
				testCaseExecuted = false;
				testStatus = null;
				createReport(durationTracker, moduleTestCasesStats, passCount, failCount, skipCount, manualSize);
			}
			// else {
			// testCaseMappingDescription = skipTestCase(currentTest_Mapping);
			// }
			if (result.contains("Domain Page not Found") && !flagNavigationError) {
				flagNavigationError = true;
			}

		}
	}

	private void createReport(DurationTracker durationTracker, ArrayList<ModuleTestCasesStats> moduleTestCasesStats, Integer passCount,
			Integer failCount, Integer skipCount, Integer mSize) throws IOException {
		durationTracker.endTime();
		String templatePath = "src" + File.separator + "templates" + File.separator + "testCasesReport.ftl";
		Map<String, Object> testCaseData = new HashMap<String, Object>();
		File moduleFile = new File(System.getProperty("user.dir") + File.separator + reportFolder + File.separator + modules[0] + ".html");

		moduleStats.setTotalPassCount(passCount);
		moduleStats.setTotalFailCount(failCount);
		moduleStats.setTotalSkipCount(skipCount);

		// added
		moduleStats.setTotalManualMappingCount(mSize);

		testCaseData.put("testcases", moduleTestCasesStats);
		testCaseData.put("moduleStats", moduleStats);
		testCaseData.put("browserName", testBrowser);
		ReportsUtil.prepareWebReport(templatePath, testCaseData, moduleFile);

		// prepare index file
		createIndexFile();
		// reporting.endSuite();
	}

	private void createIndexFile() throws IOException {
		((DurationTracker) ReportsUtil.indexFileData.get("suiteDurationTracker")).endTime();
		String templatePath = "src" + File.separator + "templates" + File.separator + "index.ftl";
		File indexFile = new File(System.getProperty("user.dir") + File.separator + reportFolder + File.separator + "index" + ".html");
		ReportsUtil.prepareWebReport(templatePath, ReportsUtil.indexFileData, indexFile);
	}

	public String getTestCaseFileName(String testDescription, Integer testCaseId) {
		String testCaseName = testDescription.replaceAll("[\"/:*?<>|\\\\]", "");
		System.out.println(testCaseName);
		/*String fileName = System.getProperty("user.dir") + File.separator + reportFolder + File.separator + modules[0] + "_TC" + testCaseId
				+ "_" + testCaseName.replaceAll(" ", "_") + ".html";*/
		String fileName = System.getProperty("user.dir") + File.separator + reportFolder + File.separator + modules[0] + "_" +testSequenceId + 
				 "_" + testCaseName.replaceAll(" ", "_") + ".html";
		System.out.println(fileName);
		return fileName;
	}

	public String executeTest(String runMode, String currentTest_Mapping, String testCaseMappingDescription, int tcid, int stepSequenceId)
			throws InterruptedException, IOException {
		String isSmokeTestStep;
		ArrayList<TestStepStats> testStepStats = new ArrayList<TestStepStats>();
		int screenshotCount = 0;
		userAgent = controller.getCellData(modules[0], "UserAgent", tcid);
		stepstatus = true;
		boolean testStepExecuted = false;
		String descriptionModified = "No";
		// execute the keywords
		// loop again - rows in test data
			for (int tsid = 2; tsid <= controller.getRowCount(currentTest); tsid++) {
				String screenshot = null;
				String stepDescription = null;
				String keyword = null;
				// values from Testcase sheet in Controller.xlsx
				if (!controller.getCellData(currentTest, runMode, tsid).equals("N")) {
					TestStepStats stepStats = new TestStepStats();
					isSmokeTestStep = controller.getCellData(currentTest, "IsSmokeTestStep", tsid);

					if (!(!runTest.equals("sanitySuite") && isSmokeTestStep.equals("N"))) {
						currentTSID = controller.getCellData(currentTest, "TSID", tsid);
						stepDescription = controller.getCellData(currentTest, "Description", tsid);
						keyword = controller.getCellData(currentTest, "Keyword", tsid);
						object = controller.getCellData(currentTest, "Object", tsid);
						objectArr = object.split(",");

						for (int i = 0; i < objectArr.length; i++) {
							objectArr[i] = objectArr[i];
						}

						proceedOnFail = controller.getCellData(currentTest, "ProceedOnFail", tsid);
						data_column_name = controller.getCellData(currentTest, "Data_Column_Name", tsid);

						if (StringUtils.isNotBlank(data_column_name)) {
							data_column_nameArr = data_column_name.split(",");
							data = testData.getCellData(currentTest, data_column_nameArr[0], testRepeat);
							data = data.replaceAll("\\W+", "");
							testCaseDescription = currentTest + " -" + currentTest_Description+" datarow "+ testRepeat;
							testCaseMappingDescription = currentTest_Mapping;
							descriptionModified = "Yes";
						} else {
							if (!(descriptionModified.equalsIgnoreCase("Yes")))
								testCaseDescription = currentTest + " -" + currentTest_Description+" datarow "+ testRepeat;
							testCaseMappingDescription = currentTest_Mapping;
						}

						try {
							if (keyword.isEmpty()) {
								continue;
							}

							Method method = null;
							try {
								System.out.println("@@@ " + modules[0] + ": KEYWORD C1 " + keyword);
								method = this.getClass().getMethod(keyword);
							} catch (NoSuchMethodException nsme) {
								// method implemented in CustomeKeyword class
								// method =
								// CustomKeywords.class.getMethod(keyword);
								System.out.println("@@@ ERROR MSG NSME M1 " + nsme.getMessage());
							}

							// System.out.println("@@@ METHOD C1"+method);

							try {
								result = (String) method.invoke(this);
							} catch (Throwable t) {
								result = "Fail-Debug Required";
							}

							if (!(data_column_name.equals(""))) {
								log.debug("\nData :" + data);
							}

							log.debug("***Result of execution -- " + result);
							if (result.startsWith("Pass")) {

							} else if (result.startsWith("Fail")) {
								stepstatus = false;
								testStatus = result;
								// take screenshot for fail
								screenshot = "RBC_Portal_Module-" + modules[0] + "_TC" + tcid + "_TS" + currentTSID + "_"
										+ launchBrowser + ++screenshotCount + ".jpeg";

								if (captureScreenShot.equals("true"))
									ReportsUtil.takeScreenShot(screenshot, driver, reportFolder, log);

								if (proceedOnFail.equalsIgnoreCase("N")) {
									break;
								}

							}

						} catch (Exception t) {
							t.printStackTrace();
							log.debug("Error  " + t.getMessage());
						}
						testStepExecuted = true;
					}
					if (testStepExecuted) {
						stepStats.setTestStepId(stepSequenceId);
						stepStats.setTestStepDescription(stepDescription);
						stepStats.setTestStepKeyword(keyword);
						stepStats.setTestStepResult(result);
						stepStats.setFailureScreenShot(screenshot);
						testStepStats.add(stepStats);
						testStepExecuted = false;
					}

					if (result.contains("Domain Page not Found")) {
						break;
					}
					try {
						boolean driverNull = (driver.toString().contains("(null)")) ? true : false;
						if (!driverNull) {
							Functions.handleTnCPopUp(driver, log, OR);
							System.out.println("executing handle tnc pop up function");
						}
					} catch (Throwable t) {
						// do nothing
					}
				}
			}// keywords one loop over
				// report pass or fail
		String templatePath = "src" + File.separator + "templates" + File.separator + "testStepsReport.ftl";
		Map<String, Object> testStepData = new HashMap<String, Object>();
		File testCaseFile = new File(getTestCaseFileName(testCaseDescription, tcid));
		testCaseFile.createNewFile();
		testStepData.put("testSteps", testStepStats);
		testStepData.put("testCaseName", testCaseDescription);
		ReportsUtil.prepareWebReport(templatePath, testStepData, testCaseFile);

		return testCaseMappingDescription;
	}

	public String skipTestCase(String currentTest_Mapping) {
		String testCaseMappingDescription;
		log.debug("Skipping the test " + currentTest);
		testStatus = "Skip";
		// report skipped
		log.debug("****************************************************" + currentTest + " --- " + testStatus);
		testCaseDescription = currentTest + " -" + currentTest_Description;
		testCaseMappingDescription = currentTest_Mapping;
		return testCaseMappingDescription;
	}

	public void endScript() {
		try {
			FileUtils.copyDirectoryToDirectory(new File(System.getProperty("user.dir") + "/TestingLogs"),
					new File(System.getProperty("user.dir") + "/" + reportFolder));
			log.debug("Testing Logs copied to the latest report folder.");
			FileUtils.copyFileToDirectory(new File(System.getProperty("user.dir") + "/" + reportFolder + "/index.html"),
					new File(System.getProperty("user.dir")));
			log.debug("Latest index file copied to the root folder.");
		} catch (IOException e) {
			log.debug("Exception caught. Stack trace :" + e.getMessage());
		}

	}

}

package com.poc.report;

import com.poc.util.DurationTracker;

public class ModuleTestCasesStats {
	
	private Integer sequenceId;
	private String testCaseDescription;
	private String manualTCId;
	private String result;
	private DurationTracker durationTracker;
	private String testCaseHyperLinkName;
	
	public Integer getSequenceId() {
		return sequenceId;
	}
	public void setSequenceId(Integer sequenceId) {
		this.sequenceId = sequenceId;
	}
	public String getTestCaseDescription() {
		return testCaseDescription;
	}
	public void setTestCaseDescription(String testCaseDescription) {
		this.testCaseDescription = testCaseDescription;
	}
	public String getManualTCId() {
		return manualTCId;
	}
	public void setManualTCId(String manualTCId) {
		this.manualTCId = manualTCId;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public DurationTracker getDurationTracker() {
		return durationTracker;
	}
	public void setDurationTracker(DurationTracker durationTracker) {
		this.durationTracker = durationTracker;
	}
	public String getTestCaseHyperLinkName() {
		return testCaseHyperLinkName;
	}
	public void setTestCaseHyperLinkName(String testCaseHyperLinkName) {
		this.testCaseHyperLinkName = testCaseHyperLinkName;
	}

}

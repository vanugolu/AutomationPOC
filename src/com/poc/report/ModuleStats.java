package com.poc.report;

import com.poc.util.DurationTracker;

public class ModuleStats {

	private String moduleName;
	private Integer totalTestCaseCount = 0;
	private Integer totalPassCount = 0;
	private Integer totalFailCount = 0;
	private Integer totalSkipCount = 0;
	private DurationTracker durationTracker;
	
	private Integer totalManualMappingCount = 0;
	
	public Integer getTotalManualMappingCount() {
		return totalManualMappingCount;
	}
	public void setTotalManualMappingCount(Integer totalManualMappingCount) {
		this.totalManualMappingCount = totalManualMappingCount;
	}
	
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public Integer getTotalTestCaseCount() {
		return totalTestCaseCount;
	}
	public void setTotalTestCaseCount(Integer totalTestCaseCount) {
		this.totalTestCaseCount = totalTestCaseCount;
	}
	public Integer getTotalPassCount() {
		return totalPassCount;
	}
	public void setTotalPassCount(Integer totalPassCount) {
		this.totalPassCount = totalPassCount;
	}
	public Integer getTotalFailCount() {
		return totalFailCount;
	}
	public void setTotalFailCount(Integer totalFailCount) {
		this.totalFailCount = totalFailCount;
	}
	public Integer getTotalSkipCount() {
		return totalSkipCount;
	}
	public void setTotalSkipCount(Integer totalSkipCount) {
		this.totalSkipCount = totalSkipCount;
	}
	public DurationTracker getDurationTracker() {
		return durationTracker;
	}
	public void setDurationTracker(DurationTracker durationTracker) {
		this.durationTracker = durationTracker;
	}
}

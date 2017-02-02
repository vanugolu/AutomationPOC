package com.poc.util;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Seconds;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class DurationTracker {
	
	private String format =  "dd.MMMMM.yyyy hh.mm.ss aaa";
	private DateTime startTime;
	private DateTime endTime;
	DateTimeFormatter formatter;
	
	public DurationTracker() {
		initialiseDateFormatter();
	}

	public DurationTracker(String format) {
		this.format = format;
		initialiseDateFormatter();
	}
	
	private void initialiseDateFormatter() {
		formatter = DateTimeFormat.forPattern(this.format);
	}
	
	public void setFormat(String format) {
		this.format = format;
	}

	public void startTime() {
		startTime = new DateTime();
	}
	
	public void endTime() {
		endTime = new DateTime();
	}
	
	public String getStartTime() {
		if(startTime == null) {
			return "Start Time cannot be null";
		}
		return formatter.parseDateTime(this.startTime.toString(formatter)).toString(formatter);
	}
	
	public String getStartTime(String format) {
		this.format = format;
		initialiseDateFormatter();
		return getStartTime();
	}
	
	public String getEndTime() {
		if(endTime == null) {
			return "In Progress";
		}
		return formatter.parseDateTime(this.endTime.toString(formatter)).toString(formatter);
	}
	
	public String timeTakenToComplete() {
		StringBuilder timeFormatString = new StringBuilder();
		if(startTime != null && endTime != null) {
			int days =  Days.daysBetween(startTime, endTime).getDays();
			int hours = Hours.hoursBetween(startTime, endTime).getHours()%24;
			int minutes = Minutes.minutesBetween(startTime, endTime).getMinutes()%60;
			int seconds = Seconds.secondsBetween(startTime, endTime).getSeconds()%60;
			
			timeFormatString.append(formatClockNeedle(days, "Day"));
			timeFormatString.append(formatClockNeedle(hours, "Hour"));
			timeFormatString.append(formatClockNeedle(minutes, "Min"));
			timeFormatString.append(formatClockNeedle(seconds, "Sec"));
		}
		return timeFormatString.toString();
		
	}
	
	public String formatClockNeedle(int clockNeedle, String textToAppend) {
		if(clockNeedle > 1) {
			return clockNeedle +" "+textToAppend+"s"+" ";
		} else if(clockNeedle == 1) {
			return clockNeedle +" "+textToAppend+" ";
		} else {
			return "";
		}
	}
}

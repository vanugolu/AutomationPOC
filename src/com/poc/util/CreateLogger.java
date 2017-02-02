package com.poc.util;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class CreateLogger {

	String moduleName;
	public static final Logger log = Logger.getLogger(CreateLogger.class);

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public void createLogger() {
		log.setLevel(Level.DEBUG);
		
		RollingFileAppender rfa = new RollingFileAppender();
		PatternLayout layout = new PatternLayout("org.apache.log4j.PatternLayout");
		String conversionPattern = "%d{dd/MM/yyyy HH:mm:ss} %m%n";
		layout.setConversionPattern(conversionPattern);
		rfa.setName("FileLogger");
		rfa.setMaxBackupIndex(3);
		rfa.setMaxFileSize("5000KB");
		rfa.setFile("./TestingLogs/"+ getModuleName() +".log");
		rfa.setLayout(layout);
		rfa.setAppend(false);
		rfa.activateOptions();
		
		Logger.getLogger(getModuleName()).addAppender(rfa);	  
	}
}

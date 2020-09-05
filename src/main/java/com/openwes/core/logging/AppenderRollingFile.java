package com.openwes.core.logging;

import ch.qos.logback.core.Appender;
import ch.qos.logback.core.rolling.DefaultTimeBasedFileNamingAndTriggeringPolicy;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;
import com.openwes.core.utils.Validate;
import com.typesafe.config.Config;

/**
 * @author xuanloc0511@gmail.com
 */
public class AppenderRollingFile extends AppenderBase {

	@Override
	public Appender onConfig(Config config) {
		if (config.isEmpty()) {
			return null;
		}
		String fileName = null;
		String fileNamePattern = null;
		int maxHistory = 7;

		if (config.hasPath("file-name")) {
			fileName = config.getString("file-name");
		}

		if (config.hasPath("file-name-pattern")) {
			fileNamePattern = config.getString("file-name-pattern");
		}

		if (config.hasPath("max-history")) {
			maxHistory = config.getInt("max-history");
		}

		if (Validate.isEmpty(fileNamePattern)) {
			System.out.println("Missing file name pattern configuration");
			return null;
		}

		RollingFileAppender rollingFileAppender = new RollingFileAppender();
		rollingFileAppender.setContext(getLoggerContext());
		rollingFileAppender.setName("rollingfile");
		rollingFileAppender.setEncoder(getEncoder());
		rollingFileAppender.setAppend(true);
		if (!Validate.isEmpty(fileName)) {
			rollingFileAppender.setFile(fileName);
		}

		TimeBasedRollingPolicy timeBaseRollingPolicy = new TimeBasedRollingPolicy();
		timeBaseRollingPolicy.setContext(getLoggerContext());
		timeBaseRollingPolicy.setParent(rollingFileAppender);
		timeBaseRollingPolicy.setFileNamePattern(fileNamePattern);
		timeBaseRollingPolicy.setMaxHistory(maxHistory);
		timeBaseRollingPolicy.setCleanHistoryOnStart(false);
		timeBaseRollingPolicy.start();

		DefaultTimeBasedFileNamingAndTriggeringPolicy trigger = new DefaultTimeBasedFileNamingAndTriggeringPolicy();
		trigger.setContext(getLoggerContext());
		trigger.setTimeBasedRollingPolicy(timeBaseRollingPolicy);
		trigger.start();

		timeBaseRollingPolicy.setTimeBasedFileNamingAndTriggeringPolicy(trigger);
		rollingFileAppender.setRollingPolicy(timeBaseRollingPolicy);
		rollingFileAppender.start();
		return rollingFileAppender;
	}

}

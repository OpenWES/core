package com.openwes.core.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.FileAppender;
import com.typesafe.config.Config;

/**
 *
 * @author xuanloc0511@gmail.com
 *
 */
public class AppenderFile extends AppenderBase {

    @Override
    public Appender onConfig(Config config) {
        if (config.isEmpty()) {
            return null;
        }
        String fileName = null;

        if (config.hasPath("file")) {
            fileName = config.getString("file");
        }

        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(fileName);
        fileAppender.setName("file");
        fileAppender.setEncoder(getEncoder());
        fileAppender.setContext(getLoggerContext());
        fileAppender.start();
        return fileAppender;
    }

}

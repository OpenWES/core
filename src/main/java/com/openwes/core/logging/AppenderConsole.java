package com.openwes.core.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.ConsoleAppender;
import com.typesafe.config.Config;

/**
 *
 * @author Deadpool {@literal (locngo@fortna.com)}
 * @since Jul 25, 2019
 * @version 1.0.0
 *
 */
public class AppenderConsole extends AppenderBase {

    @Override
    public Appender onConfig(Config config) {
        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(getLoggerContext());
        appender.setName("console");
        // same as 
        ((ConsoleAppender) appender).setEncoder(getEncoder());
        appender.start();
        return appender;
    }

}

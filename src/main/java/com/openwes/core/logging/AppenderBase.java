package com.openwes.core.logging;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.Layout;
import ch.qos.logback.core.encoder.Encoder;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.typesafe.config.Config;
import java.util.Map;

/**
 * @author xuanloc0511@gmail.com
 */
public abstract class AppenderBase {

    public final static String FORMAT_PLAIN = "plain",
            FORMAT_JSON = "json";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private LoggerContext loggerContext;
    private String pattern;
    private String format = "plain";
    private boolean includeCallerData;

    public boolean isIncludeCallerData() {
        return includeCallerData;
    }

    public AppenderBase setIncludeCallerData(boolean includeCallerData) {
        this.includeCallerData = includeCallerData;
        return this;
    }

    public LoggerContext getLoggerContext() {
        return loggerContext;
    }

    public AppenderBase setFormat(String format) {
        this.format = format;
        return this;
    }

    public AppenderBase setLoggerContext(LoggerContext loggerContext) {
        this.loggerContext = loggerContext;
        return this;
    }

    public String getPattern() {
        return pattern;
    }

    public AppenderBase setPattern(String pattern) {
        this.pattern = pattern;
        return this;
    }

    public final Encoder getEncoder() {
        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        Layout layout;
        if (FORMAT_JSON.equals(format)) {
            layout = getJsonLayout();
        } else {
            layout = getPatternLayout();
        }
        layout.setContext(loggerContext);
        layout.start();
        encoder.setContext(loggerContext);
        encoder.setLayout(layout);
        return encoder;
    }

    public Layout getPatternLayout() {
        PatternLayout layout = new PatternLayout();
        layout.setPattern(pattern);
        return layout;
    }

    public Layout getJsonLayout() {
        JsonLayout layout = new JsonWithLineNumberLayout();
        layout.setIncludeContextName(false);
        layout.setIncludeMDC(false);
        layout.setIncludeThreadName(true);
        layout.setAppendLineSeparator(true);
        layout.setIncludeLoggerName(true);
        layout.setJsonFormatter((Map m) -> gson.toJson(m));
        return layout;
    }

    public abstract Appender onConfig(Config config);
}

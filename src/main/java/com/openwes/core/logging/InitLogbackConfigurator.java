package com.openwes.core.logging;

import ch.qos.logback.classic.AsyncAppender;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.spi.ContextAwareBase;
import com.openwes.core.utils.ClassLoadException;
import com.openwes.core.utils.ClassUtils;
import com.openwes.core.utils.Utils;
import com.openwes.core.utils.Validate;
import com.typesafe.config.Config;
import java.util.stream.Collectors;
import java.util.Map;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigValue;
import java.util.Set;

/**
 *
 * @author Deadpool {@literal (locngo@fortna.com)}
 * @since Jul 25, 2019
 * @version 1.0.0
 *
 */
public class InitLogbackConfigurator extends ContextAwareBase implements Configurator {

    private Config config;

    public Config getConfig() {
        return config;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public void configure(LoggerContext loggerContext) {
        if (config == null) {
            //System.out.println("There are not found any log configuration");
            return;
        }
        loggerContext.setName(config.getString("name"));
        loggerContext.reset();
        String logLevel = config.getString("level");
        Level level = Level.toLevel(logLevel, Level.INFO);

        String pattern = null;
        if (config.hasPath("pattern")) {
            pattern = config.getString("pattern");
        }
        if (Validate.isEmpty(pattern)) {
            pattern = "%d{ISO8601} [%-4level] %logger{40} [line:%L]: %msg%n";
        }

        String format = AppenderBase.FORMAT_PLAIN;
        if (config.hasPath("format")) {
            format = config.getString("format");
        }

        boolean includeCallerData = false;
        if (config.hasPath("caller-data")) {
            includeCallerData = config.getBoolean("caller-data");
        }

        Appender appender = null;
        if (config.hasPath("appender")) {
            Config appenderConfig = config.getConfig("appender");
            String type = appenderConfig.getString("type");
            AppenderBase appenderBase = AppenderFactory.create(type.toLowerCase());
            if (appenderBase == null) {
                try {
                    String cutomizationAppender = appenderConfig.getString("handler");
                    appenderBase = ClassUtils.object(cutomizationAppender);
                } catch (ClassLoadException ex) {
                    throw new RuntimeException(ex);
                }
            }
            appender = appenderBase.setIncludeCallerData(includeCallerData)
                    .setFormat(format)
                    .setLoggerContext(loggerContext)
                    .setPattern(pattern)
                    .onConfig(appenderConfig.hasPath("arguments") ? appenderConfig.getConfig("arguments") : ConfigFactory.empty());
        }

        if (appender == null) {
            System.out.println("Appender is either not configured or configured incorrectly. ConsoleAppender will be used instead");
            appender = new AppenderConsole()
                    .setPattern(pattern)
                    .onConfig(config);
        }

        if (config.hasPath("loggers")) {
            Config loggersConfig = config.getConfig("loggers");
            final Set<String> loggerNames = loggersConfig.entrySet()
                    .stream().map((Map.Entry<String, ConfigValue> entry) -> {
                        String fullPath = entry.getKey();
                        return Utils.getPrefixOfKey(fullPath, 1);
                    }).collect(Collectors.toSet());

            for (String loggerName : loggerNames) {
                Config configPerLogger = loggersConfig.getConfig(loggerName);
                if (configPerLogger.hasPath("enabled")
                        && !configPerLogger.getBoolean("enabled")) {
                    return;
                }

                String name = null;
                if (configPerLogger.hasPath("name")) {
                    name = configPerLogger.getString("name");
                }

                // it will be ignore if global level is larger than
                // it means this value of logger can not smaller than global level
                Level _level = level;
                if (configPerLogger.hasPath("level")) {
                    String _levelStr = configPerLogger.getString("level");
                    _level = Level.toLevel(_levelStr);
                }

                Logger _logger = loggerContext.getLogger(name);
                _logger.setLevel(_level);
                _logger.addAppender(appender);
            }
        }

        boolean async = false;
        if (config.hasPath("async")) {
            async = config.getBoolean("async");
        }

        int asyncQueue = 256;
        if (config.hasPath("async-queue")) {
            asyncQueue = config.getInt("async-queue");
        }

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.setLevel(level);

        if (async) {
            AsyncAppender asyncAppender = new AsyncAppender();
            asyncAppender.setName("async");
            asyncAppender.setContext(appender.getContext());
            asyncAppender.addAppender(appender);

            asyncAppender.setNeverBlock(true);
            asyncAppender.setQueueSize(asyncQueue);
            asyncAppender.setDiscardingThreshold(0);
            asyncAppender.setIncludeCallerData(includeCallerData);
            asyncAppender.start();
            rootLogger.addAppender(asyncAppender);
        } else {
            rootLogger.addAppender(appender);
        }
    }

}

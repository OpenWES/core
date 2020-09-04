package com.openwes.core;

import ch.qos.logback.classic.LoggerContext;
import com.openwes.core.logging.InitLogbackConfigurator;
import com.openwes.core.utils.UniqId;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigParseOptions;
import com.typesafe.config.ConfigRenderOptions;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Application {

    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final static Application APP = new Application();

    private Application() {

    }

    private Config config = ConfigFactory.empty();

    final static Application newApp() {
        return APP;
    }

    synchronized Application readConfig() {
        final Properties runtimeProps = System.getProperties();
        if (null != runtimeProps) {
            config = ConfigFactory.parseProperties(runtimeProps)
                    .withFallback(ConfigFactory.load())
                    .withFallback(config).resolve();
        } else {
            config = ConfigFactory.load()
                    .withFallback(config).resolve();
        }
        return this;
    }

    synchronized Application bootstrap() {
        UniqId.instance().initSnowFlakeIdGenerator(config.getInt("application.node-id"));

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        InitLogbackConfigurator initLogbackConfigurator = new InitLogbackConfigurator();
        initLogbackConfigurator.setConfig(config.getConfig("application.log"));
        initLogbackConfigurator.configure(loggerContext);

        IOC.instance().start(config.getConfig("ioc"));
        LOGGER.info("configuration -- {}", config.root().render(ConfigRenderOptions.concise()));
        return this;
    }

    synchronized void start() {
        LOGGER.info("Application is started");
    }

    public final static void run() {
        Application.newApp()
                .readConfig()
                .bootstrap()
                .start();
    }
}

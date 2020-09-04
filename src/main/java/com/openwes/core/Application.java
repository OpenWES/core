package com.openwes.core;

import ch.qos.logback.classic.LoggerContext;
import com.openwes.core.logging.InitLogbackConfigurator;
import com.openwes.core.utils.ClassUtils;
import com.openwes.core.utils.UniqId;
import com.openwes.core.utils.Utils;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.openwes.core.interfaces.Initializer;
import com.openwes.core.utils.KeystoreOpts;
import com.openwes.core.utils.KeystoreUtils;
import com.openwes.core.utils.Validate;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class Application {

    private final static Logger LOGGER = LoggerFactory.getLogger(Application.class);
    private final static Application APP = new Application();

    public final static Application app() {
        return APP;
    }

    private Application() {

    }

    private Config config = ConfigFactory.empty();
    private KeystoreOpts keystoreOpts;

    private KeystoreOpts getKeystoreOpts() {
        return keystoreOpts;
    }

    public final static KeystoreOpts keystoreOpts() {
        return app().getKeystoreOpts();
    }

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

    synchronized Application loadKeystoreIfExist() {
        String keystorePath = System.getProperty("javax.net.ssl.keyStore", null);
        String keystoreType = System.getProperty("javax.net.ssl.keyStoreType", null);
        String keystorePass = System.getProperty("javax.net.ssl.keyStorePassword", null);
        String keystoreAlias = System.getProperty("javax.net.ssl.keyStoreAlias", null);

        if (Validate.isEmpty(keystorePath)) {
            if (config.hasPath("application.keystore.path")) {
                keystorePath = config.getString("application.keystore.path");
            }
        }

        if (Validate.isEmpty(keystorePath)) {
            return this;
        }

        if (Validate.isEmpty(keystorePass)) {
            if (config.hasPath("application.keystore.password")) {
                keystorePass = config.getString("application.keystore.password");
            }
        }

        if (Validate.isEmpty(keystorePass)) {
            LOGGER.error("Keystore path is configured but keystore-password is not configured.");
            return this;
        }

        if (Validate.isEmpty(keystoreType)) {
            if (config.hasPath("application.keystore.type")) {
                keystoreType = config.getString("application.keystore.type");
            }
        }

        if (Validate.isEmpty(keystoreAlias)) {
            if (config.hasPath("application.keystore.alias")) {
                keystoreAlias = config.getString("application.keystore.alias");
            }
        }

        if (Validate.isEmpty(keystoreType)) {
            keystoreType = Utils.getExtFile(keystorePath).toLowerCase();
        }

        if (!keystoreType.equals("jks")
                && !keystoreType.equals("keystore")
                && !keystoreType.equals("p12")
                && !keystoreType.equals("pfx")
                && !keystoreType.equals("pem")) {
            LOGGER.error("Type " + keystoreType + " of certificate was not supported. Common certificate configuration will be disabled.");
            return this;
        }

        keystoreOpts = new KeystoreOpts(keystorePath, keystorePass, keystoreType, keystoreAlias);
        LOGGER.info("Found a common keystore configuration for all java application at {} with type is {} and alias is {} ", keystorePath, keystoreType, keystoreAlias);
        KeystoreUtils.validKeystore(keystorePath, keystorePass, keystoreAlias, true);
        return this;
    }

    synchronized Application bootstrap() {
        //setup node id for id generator
        UniqId.instance().initSnowFlakeIdGenerator(config.getInt("application.node-id"));

        //setup logging 
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        InitLogbackConfigurator initLogbackConfigurator = new InitLogbackConfigurator();
        initLogbackConfigurator.setConfig(config.getConfig("application.log"));
        initLogbackConfigurator.configure(loggerContext);

        try {
            //setup ioc
            IOC.instance().start(config.getConfig("ioc"));
            //run startup services
            runStartups();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return this;
    }

    private void runStartups() throws Exception {
        Config startupConfig = config.getConfig("startup");
        Set<String> keys = Utils.getSetOfKey(startupConfig, 1);
        List<Config> configs = new ArrayList<>();
        keys.forEach((key) -> {
            Config c = startupConfig.getConfig(key);
            configs.add(c);
        });

        Collections.sort(configs, (left, right) -> {
            int orderLeft = left.hasPath("order") ? left.getInt("order") : 0;
            int orderRight = right.hasPath("order") ? right.getInt("order") : 0;
            return orderLeft - orderRight;
        });

        List<Initializer> modules = new ArrayList<>();
        for (Config c : configs) {
            String handler = c.getString("handler");
            final Initializer module = ClassUtils.object(handler);
            module.onStart(config.hasPath(module.configKey()) ? config.getConfig(module.configKey()) : config);
            modules.add(module);
            LOGGER.info("start service {}", handler);
        }

        //hook on shutdown
        Runtime.getRuntime()
                .addShutdownHook(new Thread() {
                    @Override
                    public void run() {
                        modules.forEach(module -> {
                            try {
                                LOGGER.info("shutdown service {}", module.getClass().getName());
                                module.onShutdow(config.hasPath(module.configKey()) ? config.getConfig(module.configKey()) : config);
                            } catch (Exception e) {
                            }
                        });
                    }
                });
    }

    synchronized void start() {
        LOGGER.info("Application is started");
    }

    public final static void run() {
        Application.newApp()
                .readConfig()
                .loadKeystoreIfExist()
                .bootstrap()
                .start();
    }
}

package com.openwes.core;

import com.openwes.core.utils.Utils;
import com.typesafe.config.Config;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Inversion of Control
 *
 * @author xuanloc0511@gmail.com
 */
public class IOC {

    private final static Logger LOGGER = LoggerFactory.getLogger(IOC.class);
    public final static IOC INSTANCE = new IOC();

    public final static IOC instance() {
        return INSTANCE;
    }

    private final IOCRegistry registry = new IOCRegistry();

    private IOC() {

    }

    synchronized void start(Config config) throws Exception {
        Set<String> moduleNames = Utils.getSetOfKey(config, 1);
        List<Config> configs = new ArrayList<>();
        moduleNames.forEach((key) -> {
            Config c = config.getConfig(key);
            configs.add(c);
        });

        Collections.sort(configs, (left, right) -> {
            int orderLeft = left.hasPath("order") ? left.getInt("order") : 0;
            int orderRight = right.hasPath("order") ? right.getInt("order") : 0;
            return orderLeft - orderRight;
        });

        List<String> totalPackages = new ArrayList<>();
        for (Config c : configs) {
            List<String> packages = c.getStringList("packages");
            if (packages == null) {
                continue;
            }
            LOGGER.info("register packages {} for scanning", packages);
            totalPackages.addAll(packages);
        }

        for (String packageName : totalPackages) {
            registry.register(packageName);
        }
    }

    public <P extends Object, T extends P> T loadClass(Class<P> clzz) throws Exception {
        return registry.loadClass(clzz);
    }

    public final static <P extends Object, T extends P> T resolve(Class<P> clzz) {
        try {
            return instance().loadClass(clzz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

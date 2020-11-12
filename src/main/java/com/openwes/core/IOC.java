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

        List<IOCScanConfig> scanConfigs = new ArrayList<>();
        for (Config c : configs) {
            List<String> packages = c.getStringList("packages");
            if (packages == null || packages.isEmpty()) {
                continue;
            }

            List<String> includes = c.hasPath("includes") ? c.getStringList("includes") : null;
            List<String> excludes = c.hasPath("excludes") ? c.getStringList("excludes") : null;
            LOGGER.info("register packages {} for scanning ", packages);
            scanConfigs.add(new IOCScanConfig()
                    .setPackages(packages)
                    .setExcludes(excludes)
                    .setIncludes(includes));
        }

        for (IOCScanConfig scanConfig : scanConfigs) {
            registry.register(scanConfig);
        }

    }

    public <P extends Object> P loadClass(Class<P> clzz) throws Exception {
        return registry.loadClass(clzz);
    }

    public final static <P extends Object> P init(Class<P> clzz) {
        try {
            return instance().loadClass(clzz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}

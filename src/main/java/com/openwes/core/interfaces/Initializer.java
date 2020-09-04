package com.openwes.core.interfaces;

import com.typesafe.config.Config;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public interface Initializer {

    public String configKey();

    public void onStart(Config config) throws Exception;

    public void onShutdow(Config config) throws Exception;
}

package com.openwes.core.interfaces;

import com.typesafe.config.Config;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public interface Startup {

    public void onLoad(Config appConfig) throws Exception;
}

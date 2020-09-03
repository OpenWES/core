package com.openwes.core.logging;

import com.openwes.core.utils.Validate;

/**
 *
 * @author Deadpool {@literal (locngo@fortna.com)}
 * @since Jul 30, 2019
 * @version 1.0.0
 *
 */
class AppenderFactory {

    public final static AppenderBase create(String type) {
        if (Validate.isEmpty(type) || type.equals("console")) {
            return new AppenderConsole();
        }
        if (type.equals("custom")) {
            return null;
        }
        if (type.equalsIgnoreCase("file")) {
            return new AppenderFile();
        } else if (type.equalsIgnoreCase("rollingfile")) {
            return new AppenderRollingFile();
        } else if (type.equalsIgnoreCase("socket")) {
            return new AppenderSocket();
        } else {
            return new AppenderConsole();
        }
    }

}

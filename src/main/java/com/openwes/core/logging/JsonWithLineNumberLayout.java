package com.openwes.core.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import java.util.Map;

/**
 *
 * @author locngo@fortna.com
 * @since May 23, 2020
 * @version 1.0.0
 *
 */
public class JsonWithLineNumberLayout extends JsonLayout {

    @Override
    protected void addCustomDataToJsonMap(Map<String, Object> map, ILoggingEvent event) {
        StackTraceElement[] ste = event.getCallerData();
        if (ste != null && ste.length > 0) {
            map.put("line", ste[0].getLineNumber());
        }
        event.getMDCPropertyMap().forEach((key, value) -> {
            map.put(key, value);
        });
    }
;

}

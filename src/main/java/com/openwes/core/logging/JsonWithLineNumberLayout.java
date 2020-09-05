package com.openwes.core.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.contrib.json.classic.JsonLayout;
import java.util.Map;

/**
 * @author xuanloc0511@gmail.com
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

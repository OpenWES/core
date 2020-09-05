package com.openwes.core.logging;

import ch.qos.logback.classic.net.SocketAppender;
import ch.qos.logback.core.Appender;
import ch.qos.logback.core.util.Duration;
import com.openwes.core.utils.Validate;
import com.typesafe.config.Config;

/**
 * @author xuanloc0511@gmail.com
 */
public class AppenderSocket extends AppenderBase {

    @Override
    public Appender onConfig(Config config) {
        if (config.isEmpty()) {
            return null;
        }

        String remoteHost = null;
        if (config.hasPath("host")) {
            remoteHost = config.getString("host");
        }

        int port = 0;
        if (config.hasPath("port")) {
            port = config.getInt("port");
        }

        if (Validate.isEmpty(remoteHost) || port <= 0) {
            return null;
        }

        int queueSize = 10000;
        if (config.hasPath("queue-size")) {
            queueSize = config.getInt("queue-size");
        }

        SocketAppender socketAppender = new SocketAppender();
        socketAppender.setName("socket");
        socketAppender.setContext(getLoggerContext());
        socketAppender.setIncludeCallerData(isIncludeCallerData());
        socketAppender.setReconnectionDelay(Duration.buildBySeconds(30));
        socketAppender.setQueueSize(queueSize);
        socketAppender.setRemoteHost(remoteHost);
        socketAppender.setPort(port);
        socketAppender.start();
        return socketAppender;
    }

}

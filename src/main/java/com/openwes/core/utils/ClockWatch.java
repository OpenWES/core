package com.openwes.core.utils;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author xuanloc0511@gmail.com
 */
public class ClockWatch {

    private final ThreadLocal<Long> started = ThreadLocal.withInitial(() -> ClockService.nowNS());

    ClockWatch() {
        started.set(ClockService.nowNS());
    }

    ClockWatch(long nanoTimes) {
        started.set(nanoTimes);
    }

    public void reset() {
        started.set(ClockService.nowNS());
    }

    public long timeElapsedNS() {
        try {
            return ClockService.nowNS() - started.get();
        } finally {
            started.set(ClockService.nowNS());
        }
    }

    public long timeElapsedMS() {
        return TimeUnit.MILLISECONDS.convert(timeElapsedNS(), TimeUnit.NANOSECONDS);
    }

    public long timeElapsedUS() {
        return TimeUnit.MICROSECONDS.convert(timeElapsedNS(), TimeUnit.NANOSECONDS);
    }
}

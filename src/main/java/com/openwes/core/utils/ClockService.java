package com.openwes.core.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author locngo@fortna.com
 */
public final class ClockService {

    private final static ClockService INSTANCE = new ClockService();

    final static ClockService getInstance() {
        return INSTANCE;
    }

    private ClockService() {
    }

    public final static ClockWatch newClockWatch() {
        return new ClockWatch();
    }

    public final static ClockWatch newClockWatch(long nanoTimes) {
        return new ClockWatch(nanoTimes);
    }

    //  High precision timestamp.
    public final static long nowUS() {
        return TimeUnit.MICROSECONDS.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
    }

    public final static long nowS() {
        return TimeUnit.SECONDS.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    //  Low precision timestamp. In tight loops generating it can be
    //  10 to 100 times faster than the high precision timestamp.
    public final static long nowMS() {
        return System.currentTimeMillis();
    }

    public final static long nowNS() {
        return System.nanoTime();
    }

    public final static Calendar parse(String pattern, String value) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(sdf.parse(value).getTime());
        return calendar;
    }

    public final static String format(String pattern, long timeInMiliseconds) {
        return format(pattern, timeInMiliseconds, TimeUnit.MILLISECONDS);
    }

    public final static String format(String pattern, long time, TimeUnit unit) {
        long timeInMils = TimeUnit.MILLISECONDS.convert(time, unit);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timeInMils));
    }

    /**
     * return first timestamp of today
     * <pre>
     * i.e:
     *  - today is 23 August, 2019
     *  - result will be 2019-08-23 00:00:00.000
     * </pre>
     *
     * @return
     */
    public final static long beginOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return calendar.getTimeInMillis();
    }

    /**
     * return first timestamp of current moth
     * <pre>
     * i.e:
     *  - current month is August, 2019
     *  - result will be 2019-08-01 00:00:00.000
     * </pre>
     *
     * @return
     */
    public final static long beginOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTimeInMillis();
    }

    /**
     * return last timestamp of the day
     * <pre>
     * i.e:
     *  - current day is 23 August, 2019
     *  - result will be 2019-08-23 23:59:59.000
     * </pre>
     *
     * @return
     */
    public final static long endOfDay() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        return calendar.getTimeInMillis();
    }

    /**
     * return last timestamp of current month
     * <pre>
     * i.e:
     *  - current month is August, 2019
     *  - result will be 2019-08-31 23:59:59.000
     * </pre>
     *
     * @return
     */
    public final static long endOfMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        /**
         * update month and date
         */
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        return calendar.getTimeInMillis();
    }
}

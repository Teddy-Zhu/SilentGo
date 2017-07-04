package com.silentgo.utils;

import java.util.Calendar;
import java.util.Date;

/**
 * Project : SilentGo
 * Package : com.silentgo.utils
 *
 * @author <a href="mailto:teddyzhu15@gmail.com" target="_blank">teddyzhu</a>
 *         <p>
 *         Created by teddyzhu on 2016/11/2.
 */
public class DateKit {

    public static Date addYears(final Date date, final int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    public static Date addMonths(final Date date, final int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    public static Date addWeeks(final Date date, final int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    public static Date addDays(final Date date, final int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    public static Date addHours(final Date date, final int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    public static Date addMinutes(final Date date, final int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    public static Date addMilliseconds(final Date date, final int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }

    public static Date addSeconds(final Date date, final int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    private static Date add(final Date date, final int calendarField, final int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    public static Date removeTime(Date date) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
        return calendar.getTime();
    }
}

/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 *
 */

package com.xpn.xwiki.stats.impl;

import org.joda.time.DateTime;
import org.joda.time.MutableDateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Helper factory class for creating Period objects in velocity
 */
public class PeriodFactory
{
    /**
     * The minimum date considered when retrieving all-time statistics
     */
    private static final DateTime MIN_DATE = new DateTime(1000, 1, 1, 0, 0, 0, 0);

    /**
     * The maximum date considered when retrieving all-time statistics
     */
    private static final DateTime MAX_DATE = new DateTime(9999, 12, 31, 23, 59, 59, 999);

    /**
     * The period of time between {@link #MIN_DATE} and {@link #MAX_DATE}
     */
    public static final Period ALL_TIME =
        createPeriod(MIN_DATE.getMillis(), MAX_DATE.getMillis());

    private static final DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyyMMdd");

    /**
     * This factory is implemented as a singleton. This is its only instance.
     */
    private static final PeriodFactory instance = new PeriodFactory();

    private PeriodFactory()
    {
    }

    /**
     * @return The only instance of this singleton factory
     */
    public static PeriodFactory getInstance()
    {
        return instance;
    }

    /**
     * @see Period#Period(long, long)
     */
    public static Period createPeriod(long start, long end)
    {
        return new Period(start, end);
    }

    /**
     * Creates a new custom period. The start and end dates must have the following format:
     * "yyyyMMdd"
     * 
     * @param start The start date
     * @param end The end date
     * @return A new Period instance
     * @see java.text.SimpleDateFormat
     */
    public static Period createPeriod(String start, String end)
    {
        return createPeriod(formatter.parseMillis(start), formatter.parseMillis(end));
    }

    /**
     * Creates a new Period instance that matches exactly the day that includes the specified time
     * stamp.
     * 
     * @param timestamp The milliseconds from 1970-01-01T00:00:00Z
     * @return A new Period instance
     */
    public static Period createDayPeriod(long timestamp)
    {
        MutableDateTime mdt = new MutableDateTime(timestamp);
        return createPeriod(toDayStart(mdt).getMillis(), toDayEnd(mdt).getMillis());
    }

    /**
     * Creates a new Period instance that matches exactly the day that includes the specified date.
     * 
     * @param date The string representation of a date uniquely identifying a day. Use the
     *            "yyyyMMdd" format.
     * @return The corresponding Period object
     * @see java.text.SimpleDateFormat
     */
    public static Period createDayPeriod(String date)
    {
        return createDayPeriod(formatter.parseMillis(date));
    }

    /**
     * @return The period of time matching the current day
     */
    public static Period getCurrentDay()
    {
        return createDayPeriod(new DateTime().getMillis());
    }

    /**
     * Creates a new Period instance that matches exactly the week that includes the specified time
     * stamp.
     * 
     * @param timestamp The milliseconds from 1970-01-01T00:00:00Z
     * @return A new Period instance
     */
    public static Period createWeekPeriod(long timestamp)
    {
        MutableDateTime mdt = new MutableDateTime(timestamp);
        return createPeriod(toWeekStart(mdt).getMillis(), toWeekEnd(mdt).getMillis());
    }

    /**
     * Creates a new Period instance that matches exactly the week that includes the specified date.
     * 
     * @param date The string representation of a date uniquely identifying a week. Use the
     *            "yyyyMMdd" format.
     * @return The corresponding Period object
     * @see java.text.SimpleDateFormat
     */
    public static Period createWeekPeriod(String date)
    {
        return createWeekPeriod(formatter.parseMillis(date));
    }

    /**
     * @return The period of time matching the current week
     */
    public static Period getCurrentWeek()
    {
        return createWeekPeriod(new DateTime().getMillis());
    }

    /**
     * Creates a new Period instance that matches exactly the month that includes the specified time
     * stamp.
     * 
     * @param timestamp The milliseconds from 1970-01-01T00:00:00Z
     * @return A new Period instance
     */
    public static Period createMonthPeriod(long timestamp)
    {
        MutableDateTime mdt = new MutableDateTime(timestamp);
        return createPeriod(toMonthStart(mdt).getMillis(), toMonthEnd(mdt).getMillis());
    }

    /**
     * Creates a new Period instance that matches exactly the month that includes the specified
     * date.
     * 
     * @param date The string representation of a date uniquely identifying a month. Use the
     *            "yyyyMMdd" format.
     * @return The corresponding Period object
     * @see java.text.SimpleDateFormat
     */
    public static Period createMonthPeriod(String date)
    {
        return createMonthPeriod(formatter.parseMillis(date));
    }

    /**
     * @return The period of time matching the current month
     */
    public static Period getCurrentMonth()
    {
        return createMonthPeriod(new DateTime().getMillis());
    }

    /**
     * Creates a new Period instance that matches exactly the year that includes the specified time
     * stamp.
     * 
     * @param timestamp The milliseconds from 1970-01-01T00:00:00Z
     * @return A new Period instance
     */
    public static Period createYearPeriod(long timestamp)
    {
        MutableDateTime mdt = new MutableDateTime(timestamp);
        return createPeriod(toYearStart(mdt).getMillis(), toYearEnd(mdt).getMillis());
    }

    /**
     * Creates a new Period instance that matches exactly the year that includes the specified date.
     * 
     * @param date The string representation of a date uniquely identifying a year. Use the
     *            "yyyyMMdd" format.
     * @return The corresponding Period object
     * @see java.text.SimpleDateFormat
     */
    public static Period createYearPeriod(String date)
    {
        return createYearPeriod(formatter.parseMillis(date));
    }

    /**
     * @return The period of time matching the current year
     */
    public static Period getCurrentYear()
    {
        return createYearPeriod(new DateTime().getMillis());
    }

    private static MutableDateTime toDayStart(MutableDateTime mdt)
    {
        mdt.setHourOfDay(mdt.hourOfDay().getMinimumValue());
        mdt.setMinuteOfHour(mdt.minuteOfHour().getMinimumValue());
        mdt.setSecondOfMinute(mdt.secondOfMinute().getMinimumValue());
        mdt.setMillisOfSecond(mdt.millisOfSecond().getMinimumValue());
        return mdt;
    }

    private static MutableDateTime toDayEnd(MutableDateTime mdt)
    {
        mdt.addDays(1);
        return toDayStart(mdt);
    }

    private static MutableDateTime toWeekStart(MutableDateTime mdt)
    {
        mdt.setDayOfWeek(mdt.dayOfWeek().getMinimumValue());
        return toDayStart(mdt);
    }

    private static MutableDateTime toWeekEnd(MutableDateTime mdt)
    {
        mdt.setDayOfWeek(mdt.dayOfWeek().getMaximumValue());
        return toDayEnd(mdt);
    }

    private static MutableDateTime toMonthStart(MutableDateTime mdt)
    {
        mdt.setDayOfMonth(mdt.dayOfMonth().getMinimumValue());
        return toDayStart(mdt);
    }

    private static MutableDateTime toMonthEnd(MutableDateTime mdt)
    {
        mdt.setDayOfMonth(mdt.dayOfMonth().getMaximumValue());
        return toDayEnd(mdt);
    }

    private static MutableDateTime toYearStart(MutableDateTime mdt)
    {
        mdt.setDayOfYear(mdt.dayOfYear().getMinimumValue());
        return toDayStart(mdt);
    }

    private static MutableDateTime toYearEnd(MutableDateTime mdt)
    {
        mdt.setDayOfYear(mdt.dayOfYear().getMaximumValue());
        return toDayEnd(mdt);
    }

    /**
     * Helper method for accessing {@link #ALL_TIME} static field in velocity
     */
    public static Period getALL_TIME()
    {
        return ALL_TIME;
    }
}
